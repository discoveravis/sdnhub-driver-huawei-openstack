/*
 * Copyright 2016-2017 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdnhub.osdriverservice.sbi;

import org.openo.sdnhub.osdriverservice.openstack.client.OpenStackClient;
import org.openo.sdnhub.osdriverservice.openstack.client.exception.NotFoundException;
import org.openo.sdnhub.osdriverservice.openstack.client.exception.OpenStackException;
import org.openo.sdnhub.osdriverservice.openstack.client.model.Network;
import org.openo.sdnhub.osdriverservice.openstack.client.model.Project;
import org.openo.sdnhub.osdriverservice.openstack.client.model.Router;
import org.openo.sdnhub.osdriverservice.openstack.client.model.Subnet;
import org.openo.sdnhub.osdriverservice.sbi.model.OsSubnet;
import org.openo.sdnhub.osdriverservice.sbi.model.OsVpc;
import org.openo.sdnhub.osdriverservice.util.OSNetworkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for creating VPC service.<br>
 *
 * @author
 * @version SDNHUB 0.5 2016-6-20
 */
public class VpcSbiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VpcSbiService.class);

    private OpenStackClient client = null;

    /**
     * Constructor<br>
     *
     * @param client OpenStackClient object to be used by the VPC service.
     * @since SDNHUB 0.5
     */
    public VpcSbiService(OpenStackClient client) {
        this.client = client;
    }

    /**
     * Create VPC.
     * <br>
     *
     * @param vpc
     * @return
     * @throws OpenStackException
     * @since SDNHUB 0.5
     */
    public OsVpc createVpc(OsVpc vpc) throws OpenStackException {
        this.client.login();

        Project project = null;
        try {
            project = this.client.getProject(vpc.getProjectName());
            vpc.getAttributes().setProjectId(project.getId(), "u");
        } catch(NotFoundException e) {
            LOGGER.warn("Project " + vpc.getProjectName() + " not exists!!");
            project = new Project();
            project.setName(vpc.getProjectName());
            project.setDomainId(vpc.getDomainName());
            project = this.client.createProject(project);
            vpc.getAttributes().setProjectId(project.getId(), "c");
        }

        Network publicNetwork = this.client.getPublicNetwork();
        vpc.getAttributes().setPublicNetworkId(publicNetwork.getId(), "u");

        Router router = null;
        String routerName = vpc.getRouterName();
        if (routerName == null || routerName.isEmpty()) {
            routerName = Utils.getName(vpc.getOverlayId());
        }

        try {
            router = this.client.getRouter(routerName);
            vpc.getAttributes().setRouterId(router.getId(), "u");
        } catch(NotFoundException e) {
            LOGGER.error("Router " + routerName + "does not exist");
            router = new Router();
            router.setName(routerName);
            router.setProjectId(project.getId());
            Router.ExternalGatewayInfo externalGwInfo = new Router.ExternalGatewayInfo();
            externalGwInfo.setNetworkId(publicNetwork.getId());
            externalGwInfo.setEnableSnat(false);
            router.setExternalGatewayInfo(externalGwInfo);
            router = this.client.createRouter(router);
            vpc.getAttributes().setRouterId(router.getId(), "c");
        }

        String ip = router.getExternalGatewayInfo().getExternalFixedIps().get(0).get("ip_address");
        vpc.setGatewayIp(ip);

        this.client.logout();

        return vpc;
    }

    /**
     * <br>
     *
     * @param subnet
     * @return
     * @throws OpenStackException
     * @since SDNHUB 0.5
     */
    public OsSubnet createSubnet(OsSubnet subnet) throws OpenStackException {
        this.client.login();
        Network network = null;
        String networkName = subnet.getName();
        if (networkName == null || networkName.isEmpty()) {
            networkName = Utils.getName(subnet.getOverlayId());
        }
        try {
            network = this.client.getNetwork(networkName);
            subnet.getAttributes().setVpcNetworkId(network.getId(), "u");
        } catch(NotFoundException e) {
            LOGGER.warn("Network " + networkName + " does not exist");
            network = new Network();
            network.setName(networkName);
            network.setAdminStateUp(true);
            network.setProjectId(subnet.getAttributes().getProjectId());
            OSNetworkConfig networkConfig = new OSNetworkConfig();
            network.setNetworkType(networkConfig.getNetworkType());
            network.setPhysicalNetwork(this.client.getProviderNetwork());
            network.setSegmentationId(String.valueOf(subnet.getVni()));
            network = this.client.createNetwork(network);
            subnet.getAttributes().setVpcNetworkId(network.getId(), "c");
        }

        if(network.isAdminStateUp()) {
            subnet.setAdminStatus("up");
        } else {
            subnet.setAdminStatus("down");
        }
        Subnet osSubnet = null;
        String subnetName = networkName;
        try {
            osSubnet = this.client.getSubnet(subnetName);
            subnet.getAttributes().setVpcSubnetId(osSubnet.getId(), "u");
        } catch(NotFoundException e) {
            LOGGER.warn("Subnet " + subnetName + " does not exist");
            osSubnet = new org.openo.sdnhub.osdriverservice.openstack.client.model.Subnet();
            osSubnet.setName(subnetName);
            osSubnet.setNetworkId(network.getId());
            osSubnet.setCidr(subnet.getCidr());
            osSubnet.setProjectId(subnet.getAttributes().getProjectId());
            osSubnet = this.client.createSubnet(osSubnet);
            subnet.getAttributes().setVpcSubnetId(osSubnet.getId(), "c");
        }

        subnet.setGatewayIp(osSubnet.getGatewayIp());

        Router router = new Router();
        router.setId(subnet.getAttributes().getRouterId());
        try {
            // TODO(mrkanag) before attaching. Check the attachment in
            // router->external_gateway_info.external_fixed_ips
            this.client.attachSubnetToRouter(router, osSubnet);
        } catch(OpenStackException e) {
            LOGGER.error("OpenStackException ", e);
            // Already attached
            if(e.getHttpCode() != 400) {
                throw e;
            }
        }

        this.client.logout();

        return subnet;
    }

    /**
     * Delete Subnet
     * <br>
     *
     * @param underlays
     * @throws OpenStackException
     * @since SDNHUB 0.5
     */
    public void deleteSubnet(OsSubnet.Underlays underlays) throws OpenStackException {
        this.client.login();

        Subnet subnet = new Subnet();
        subnet.setId(underlays.getVpcSubnetId());

        Router router = new Router();
        router.setId(underlays.getRouterId());
        try {
            // TODO(mrkanag) before attaching. Check the attachment in
            // router->external_gateway_info.external_fixed_ips
            this.client.detachSubnetFromRouter(router, subnet);
            underlays.setRouterId(router.getId(), "u");
        } catch(OpenStackException e) {

            // Already detached
            if(e.getHttpCode() != 400) {
                throw e;
            }
        }

        try {
            this.client.deleteSubnet(underlays.getVpcSubnetId());
            underlays.setVpcSubnetId(underlays.getVpcSubnetId(), "d");
        } catch(OpenStackException e) {
            if(e.getHttpCode() != 404) {
                throw e;
            }
        }

        try {
            this.client.deleteNetwork(underlays.getVpcNetworkId());
            underlays.setVpcNetworkId(underlays.getVpcNetworkId(), "d");
        } catch(OpenStackException e) {
            if(e.getHttpCode() != 404) {
                throw e;
            }
        }

        this.client.logout();
    }

    /**
     * Delete VPC.
     * <br>
     *
     * @param underlays
     * @throws OpenStackException
     * @since SDNHUB 0.5
     */
    public void deleteVpc(OsVpc.Underlays underlays) throws OpenStackException {
        this.client.login();

        try {
            this.client.deleteRouter(underlays.getRouterId());
            underlays.setRouterId(underlays.getRouterId(), "d");
        } catch(OpenStackException e) {
            if(e.getHttpCode() != 404) {
                throw e;
            }
        }

        try {
            if("c".equals(underlays.getResourceActions().get(underlays.getProjectId()))) {
                this.client.deleteProject(underlays.getProjectId());
                underlays.setProjectId(underlays.getProjectId(), "d");
            }
        } catch(OpenStackException e) {
            if(e.getHttpCode() != 404) {
                throw e;
            }
        }

        this.client.logout();
    }

    static class Utils {

        /**
         * Constructor<br>
         *
         * @since SDNHUB 0.5
         */
        private Utils() {
            // Default private constructor
        }

        static String getName(String uuid) {
            return "sdno_" + uuid;
        }
    }
}
