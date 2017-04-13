/*
 * Copyright 2016-17 Huawei Technologies Co., Ltd.
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

import java.util.List;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.osdriverservice.openstack.client.OpenStackClient;
import org.openo.sdnhub.osdriverservice.openstack.client.exception.OpenStackException;
import org.openo.sdnhub.osdriverservice.openstack.client.model.Subnet;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIkePolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecPolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecSiteConnection;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnService;
import org.openo.sdnhub.osdriverservice.sbi.model.OsIpSec;


/**
 * Class for creating VPC service.<br>
 *
 * @author
 * @version SDNHUB 0.5 2016-6-20
 */
public class IpSecSbiService {

    private OpenStackClient client = null;

    /**
     * Constructor<br>
     * <p>
     * </p>
     *
     * @param client OpenStackClient to be used for initializing the IpSec Service.
     * @since SDNHUB 0.5
     */
    public IpSecSbiService(OpenStackClient client) {
        this.client = client;
    }

     /**
     * <br>
     *
     * @param ipsec IPSec service object to be created.
     * @return
     * @throws OpenStackException
     * @throws ServiceException
     * @since SDNHUB 0.5
     */
    public OsIpSec createIpSec(OsIpSec ipsec) throws OpenStackException {
        this.client.login();
        OsIpSec.Underlays underlays = ipsec.getAttributes();

        if (ipsec.getVpcId() != null) {
            //set all tenant id
            if (ipsec.getVpnIpSecPolicy() != null) {
                ipsec.getVpnIpSecPolicy().setTenantId(underlays.getProjectId());
            }
            if (ipsec.getVpnIkePolicy() != null) {
                ipsec.getVpnIkePolicy().setTenantId(underlays.getProjectId());
            }
            ipsec.getVpnService().setTenantId(underlays.getProjectId());
            ipsec.getVpnService().setRouterId(underlays.getRouterId());
            ipsec.getVpnIpSecSiteConnection().setTenantId(underlays.getProjectId());

            if (ipsec.getVpnService().getSubnetId() == null) {
                List<Subnet> subnets = client.listSubnetForNetowrkId(underlays.getPublicNetworkId());

                //update the vpn subnet id
                for (Subnet subnet: subnets) {
                    if(underlays.getSourceLanCidrs().contains(subnet.getCidr()))
                    {
                        ipsec.getVpnService().setSubnetId(subnet.getId());
                        break;
                    }
                }

                if(ipsec.getVpnService().getSubnetId() == null)
                {
                    throw new OpenStackException("No subnet found");
                }
            }
        }

        if(underlays.getVpnIkePolicyId() == null) {
            VpnIkePolicy ike = this.client.createVpnIkePolicy(ipsec.getVpnIkePolicy());
            underlays.setVpnIkePolicyId(ike.getId(), "c");
            ipsec.setVpnIkePolicy(ike);
        }

        if(underlays.getVpnIpSecPolicyId() == null) {
            VpnIpSecPolicy is = this.client.createVpnIpSecPolicy(ipsec.getVpnIpSecPolicy());
            underlays.setVpnIpSecPolicyId(is.getId(), "c");
            ipsec.setVpnIpSecPolicy(is);
        }

        if(underlays.getVpnServiceId() == null) {
            VpnService svc = this.client.createVpnService(ipsec.getVpnService());
            underlays.setVpnServiceId(svc.getId(), "c");
            ipsec.setVpnService(svc);
        }

        if(underlays.getVpnIpSecSiteConnectionId() == null) {

            VpnIpSecSiteConnection conn = ipsec.getVpnIpSecSiteConnection();
            conn.setIkepolicyId(ipsec.getVpnIkePolicy().getId());
            conn.setIpsecpolicyId(ipsec.getVpnIpSecPolicy().getId());
            conn.setVpnserviceId(ipsec.getVpnService().getId());
            conn = this.client.createVpnIpSecSiteConnection(ipsec.getVpnIpSecSiteConnection());
            underlays.setVpnIpSecSiteConnectionId(conn.getId(), "c");
            ipsec.setVpnIpSecSiteConnection(conn);
        }

        this.client.logout();

        return ipsec;
    }

    /**
     * Update Ipsec peer cidrs<br>
     *
     * @param ipsec IPSec service object to be created.
     * @return
     * @throws OpenStackException
     * @since SDNHUB 0.5
     */
    public OsIpSec updateIpSec(String ipSecId, OsIpSec ipsec) throws OpenStackException {
        this.client.login();

        VpnIpSecSiteConnection conn = ipsec.getVpnIpSecSiteConnection();
        conn = this.client.updateVpnIpSecSiteConnection(ipsec.getAttributes().getVpnIpSecSiteConnectionId(), ipsec.getVpnIpSecSiteConnection());
        ipsec.setVpnIpSecSiteConnection(conn);

        this.client.logout();

        return ipsec;
    }
    /**
     * Delete IpSec
     * <br>
     *
     * @param underlays
     * @throws OpenStackException
     * @since  SDNHUB 0.5
     */
    public void deleteIpSec(OsIpSec.Underlays underlays) throws OpenStackException {
        this.client.login();

        try {
            //TODO(mrkanag) IpSec connection takes time to delete completely, so track it
            this.client.deleteVpnIpSecSiteConnection(underlays.getVpnIpSecSiteConnectionId());
            underlays.setVpnIpSecSiteConnectionId(underlays.getVpnIpSecSiteConnectionId(), "d");

            //TODO(mrkanag) VpnService takes time to delete completely, so track it
            this.client.deleteVpnService(underlays.getVpnServiceId());
            underlays.setVpnServiceId(underlays.getVpnServiceId(), "d");

            this.client.deleteVpnIkePolicy(underlays.getVpnIkePolicyId());
            underlays.setVpnIkePolicyId(underlays.getVpnIkePolicyId(), "d");

            this.client.deleteVpnIpSecPolicy(underlays.getVpnIpSecPolicyId());
            underlays.setVpnIpSecPolicyId(underlays.getVpnIpSecPolicyId(), "d");
        } catch(OpenStackException e) {
            if(e.getHttpCode() != 404) {
                throw e;
            }
        }

        this.client.logout();
    }
}
