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

package org.openo.sdnhub.osdriverservice.sbi.model;

import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIkePolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecPolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecSiteConnection;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnService;

/**
 * Model class for IpSec.<br>
 *
 * @author
 * @version SDNHUB 0.5 2016-6-15
 */
public class OsIpSec {

    private String vpcId;

    private String overlayId;

    private VpnIkePolicy vpnIkePolicy;

    private VpnIpSecPolicy vpnIpSecPolicy;

    private VpnIpSecSiteConnection vpnIpSecSiteConnection;

    private VpnService vpnService;

    private OsIpSec.Underlays attributes = new OsIpSec.Underlays();

    public VpnIkePolicy getVpnIkePolicy() {
        return this.vpnIkePolicy;
    }

    public void setVpnIkePolicy(VpnIkePolicy vpnIkePolicy) {
        this.vpnIkePolicy = vpnIkePolicy;
    }

    public VpnIpSecPolicy getVpnIpSecPolicy() {
        return this.vpnIpSecPolicy;
    }

    public void setVpnIpSecPolicy(VpnIpSecPolicy vpnIpSecPolicy) {
        this.vpnIpSecPolicy = vpnIpSecPolicy;
    }

    public VpnService getVpnService() {
        return this.vpnService;
    }

    public void setVpnService(VpnService vpnService) {
        this.vpnService = vpnService;
    }

    public VpnIpSecSiteConnection getVpnIpSecSiteConnection() {
        return this.vpnIpSecSiteConnection;
    }

    public void setVpnIpSecSiteConnection(VpnIpSecSiteConnection vpnIpSecSiteConnection) {
        this.vpnIpSecSiteConnection = vpnIpSecSiteConnection;
    }


    public OsIpSec.Underlays getAttributes() {
        return this.attributes;
    }

    public void setAttributes(OsIpSec.Underlays attributes) {
        this.attributes = attributes;
    }


    public String getOverlayId() {
        return this.overlayId;
    }

    public void setOverlayId(String overlayId) {
        this.overlayId = overlayId;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }


    /**
     * Underlays class
     * <br>
     * <p>
     * </p>
     *
     * @author
     * @version SDNHUB 0.5 August 8, 2016
     */
    public static class Underlays extends BaseUnderlays{

        private String vpnServiceId = null;

        private String vpnIkePolicyId = null;

        private String vpnIpSecPolicyId = null;

        private String vpnIpSecSiteConnectionId = null;

        private String sourceLanCidrs = null;

        private String projectId = null;

        private String publicNetworkId = null;

        private String routerId = null;

        private String publicSubnetId = null;

        public String getProjectId() {
            return this.projectId;
        }

        /**
         * Set project ID
         * <br>
         *
         * @param projectId
         * @param action

         */
        public void setProjectId(String projectId, String action) {
            this.put("projectId", projectId, action);
            this.projectId = projectId;
        }


        public String getPublicNetworkId() {
            return this.publicNetworkId;
        }

        /**
         * Set public network ID
         * <br>
         *
         * @param publicNetworkId
         * @param action

         */
        public void setPublicNetworkId(String publicNetworkId, String action) {
            this.publicNetworkId = publicNetworkId;
            this.put("publicNetworkId", publicNetworkId, action);
        }


        public String getRouterId() {
            return this.routerId;
        }

        /**
         * Set router ID
         * <br>
         *
         * @param routerId
         * @param action

         */
        public void setRouterId(String routerId, String action) {
            this.routerId = routerId;
            this.put("routerId", routerId, action);
        }

        public String getVpnServiceId() {
            return this.vpnServiceId;
        }

        /**
         * Set VpnService ID
         * <br>
         *
         * @param vpnServiceId
         * @param action

         */
        public void setVpnServiceId(String vpnServiceId, String action) {
            this.vpnServiceId = vpnServiceId;
            this.put("vpnServiceId", vpnServiceId, action);
        }


        public String getVpnIkePolicyId() {
            return this.vpnIkePolicyId;
        }

        /**
         * Set VpnIkePolicy ID
         * <br>
         *
         * @param vpnIkePolicyId
         * @param action

         */
        public void setVpnIkePolicyId(String vpnIkePolicyId, String action) {
            this.vpnIkePolicyId = vpnIkePolicyId;
            this.put("vpnIkePolicyId", vpnIkePolicyId, action);
        }


        public String getVpnIpSecPolicyId() {
            return this.vpnIpSecPolicyId;
        }

        /**
         * Set VpnIpsecPolicy ID
         * <br>
         *
         * @param vpnIpSecPolicyId
         * @param action

         */
        public void setVpnIpSecPolicyId(String vpnIpSecPolicyId, String action) {
            this.vpnIpSecPolicyId = vpnIpSecPolicyId;
            this.put("vpnIpSecPolicyId", vpnIpSecPolicyId, action);
        }


        public String getVpnIpSecSiteConnectionId() {
            return this.vpnIpSecSiteConnectionId;
        }

        /**
         * Set VpnIpSecSite connection ID
         * <br>
         *
         * @param vpnIpSecSiteConnectionId
         * @param action

         */
        public void setVpnIpSecSiteConnectionId(String vpnIpSecSiteConnectionId, String action) {
            this.vpnIpSecSiteConnectionId = vpnIpSecSiteConnectionId;
            this.put("vpnIpSecSiteConnectionId", vpnIpSecSiteConnectionId, action);
        }

        public String getSourceLanCidrs() {
            return sourceLanCidrs;
        }

        public void setSourceLanCidrs(String sourceLanCidrs, String action) {
            this.sourceLanCidrs = sourceLanCidrs;
            this.put("sourceLanCidrs", sourceLanCidrs, action);
        }

        public String getPublicSubnetId() {
            return publicSubnetId;
        }

        public void setPublicSubnetId(String publicSubnetId, String action) {
            this.publicSubnetId = publicSubnetId;
            this.put("publicSubnetId", publicSubnetId, action);
        }


    }
}
