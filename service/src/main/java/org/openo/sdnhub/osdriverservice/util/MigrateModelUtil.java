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

package org.openo.sdnhub.osdriverservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.osdriverservice.dao.model.OverlayUnderlayMapping;
import org.openo.sdnhub.osdriverservice.nbi.model.SbiIkePolicy;
import org.openo.sdnhub.osdriverservice.nbi.model.SbiIpSecPolicy;
import org.openo.sdnhub.osdriverservice.nbi.model.SbiNeIpSec;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnDpd;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIkePolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecPolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecSiteConnection;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnPolicyLifeTime;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnService;
import org.openo.sdnhub.osdriverservice.openstack.client.model.enums.DpdAction;
import org.openo.sdnhub.osdriverservice.sbi.model.OsIpSec;
import org.openo.sdnhub.osdriverservice.sbi.model.OsSubnet;
import org.openo.sdnhub.osdriverservice.sbi.model.OsVpc;
import org.openo.sdno.overlayvpn.model.ipsec.IkePolicy;
import org.openo.sdno.overlayvpn.model.ipsec.IpSecPolicy;
import org.openo.sdno.overlayvpn.model.netmodel.ipsec.DcGwIpSecConnection;
import org.openo.sdno.overlayvpn.model.netmodel.vpc.Subnet;
import org.openo.sdno.overlayvpn.model.netmodel.vpc.Vpc;
import org.openo.sdno.ssl.EncryptionUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Model converting class, converting SDNO model to adapter model.<br>
 *
 * @author
 * @version SDNHUB 0.5 2016-6-16
 */
public class MigrateModelUtil {

    private static final String LIFE_TIME_UINTS = "seconds";

    private static final String PHASE_NEGOTIATION_MODE = "main";

    private static final String INITIATOR = "bi-directional";

    private static final String PEER_CIDRS = "peer_cidrs";

    private static final int DPD_INTERNVAL = 30;

    private static final int DPD_TIMEOUT = 120;

    private static final int MTU = 1800;

    private MigrateModelUtil() {

    }

    /**
     * convert
     * <br>
     *
     * @param dcGwIpSecConnection
     * @return
     * @since SDNHUB 0.5
     */
    public static OsIpSec convert(DcGwIpSecConnection dcGwIpSecConnection) {

        OsIpSec ipsec = new OsIpSec();
        ipsec.setOverlayId(dcGwIpSecConnection.getUuid());

        ipsec.setVpnIkePolicy(convert(dcGwIpSecConnection.getIkePolicy()));
        ipsec.setVpnIpSecPolicy(convert(dcGwIpSecConnection.getIpSecPolicy()));
        ipsec.setVpnService(convert1(dcGwIpSecConnection));
        ipsec.setVpnIpSecSiteConnection(convert2(dcGwIpSecConnection));

        return ipsec;
    }

    /**
     * convert
     * <br>
     *
     * @param ikePolicy
     * @return
     * @since SDNHUB 0.5
     */
    private static VpnIkePolicy convert(IkePolicy ikePolicy) {

        VpnPolicyLifeTime lifeTime = new VpnPolicyLifeTime();
        lifeTime.setUnits(LIFE_TIME_UINTS);
        lifeTime.setValue(Integer.parseInt(ikePolicy.getLifeTime()));

        VpnIkePolicy ike = new VpnIkePolicy();
        ike.setTenantId(ikePolicy.getTenantId());
        ike.setName(ikePolicy.getName());
        ike.setDescription(ikePolicy.getDescription());
        ike.setAuthAlgorithm(ikePolicy.getAuthAlgorithm());
        ike.setEncryptionAlgorithm(ikePolicy.getEncryptionAlgorithm());
        ike.setPhase1NegotiationMode(PHASE_NEGOTIATION_MODE);
        ike.setPfs(ikePolicy.getPfs());
        ike.setIkeVersion(ikePolicy.getIkeVersion());
        ike.setLifetime(lifeTime);

        return ike;
    }

    /**
     * convert
     * <br>
     *
     * @param ipSecPolicy
     * @return
     * @since SDNHUB 0.5
     */
    private static VpnIpSecPolicy convert(IpSecPolicy ipSecPolicy) {

        VpnPolicyLifeTime lifeTime = new VpnPolicyLifeTime();
        lifeTime.setUnits(LIFE_TIME_UINTS);
        lifeTime.setValue(Integer.parseInt(ipSecPolicy.getLifeTime()));

        VpnIpSecPolicy ipsec = new VpnIpSecPolicy();
        ipsec.setAuthAlgorithm(ipSecPolicy.getAuthAlgorithm());
        ipsec.setDescription(ipSecPolicy.getDescription());
        ipsec.setEncapsulationMode(ipSecPolicy.getEncapsulationMode());
        ipsec.setEncryptionAlgorithm(ipSecPolicy.getEncryptionAlgorithm());
        ipsec.setLifetime(lifeTime);
        ipsec.setName(ipSecPolicy.getName());
        ipsec.setPfs(ipSecPolicy.getPfs());
        ipsec.setTenantId(ipSecPolicy.getTenantId());
        ipsec.setTransformProtocol(ipSecPolicy.getTransformProtocol().toLowerCase());

        return ipsec;
    }

    /**
     * convert1
     * <br>
     *
     * @param dcGwIpSecConnection
     * @return
     * @since SDNHUB 0.5
     */
    private static VpnService convert1(DcGwIpSecConnection dcGwIpSecConnection) {

        VpnService vpn = new VpnService();
        vpn.setTenantId(dcGwIpSecConnection.getTenantId());
        vpn.setRouterId(dcGwIpSecConnection.getRouterId());
        vpn.setSubnetId(dcGwIpSecConnection.getSubnetId());
        vpn.setAdminStateUp(true);
        vpn.setName(dcGwIpSecConnection.getName());
        vpn.setDescription(dcGwIpSecConnection.getName());

        return vpn;
    }

    /**
     * convert2
     * <br>
     *
     * @param dcGwIpSecConnection
     * @return
     * @since SDNHUB 0.5
     */
    private static VpnIpSecSiteConnection convert2(DcGwIpSecConnection dcGwIpSecConnection) {
        String[] peerCidrArr = dcGwIpSecConnection.getPeerSubnetCidrs().split(",");
        List<String> peerCidrList = new ArrayList<>();
        for(String peerCidr : peerCidrArr) {
            peerCidrList.add(peerCidr);
        }

        VpnDpd dpd = new VpnDpd();
        dpd.setAction(DpdAction.HOLD.getName());
        dpd.setInterval(DPD_INTERNVAL);
        dpd.setTimeout(DPD_TIMEOUT);

        VpnIpSecSiteConnection conn = new VpnIpSecSiteConnection();
        conn.setTenantId(dcGwIpSecConnection.getVpcId());
        conn.setName(dcGwIpSecConnection.getName());
        conn.setDescription(dcGwIpSecConnection.getName());
        conn.setPeerAddress(dcGwIpSecConnection.getPeerAddress());
        conn.setPeerCidrs(peerCidrList);
        conn.setPeerId(dcGwIpSecConnection.getPeerAddress());
        conn.setMtu(MTU);
        conn.setPsk(dcGwIpSecConnection.getPsk());
        conn.setInitiator(INITIATOR);
        conn.setAdminStateUp(true);
        conn.setDpd(dpd);
        conn.setSubnets(null);

        return conn;
    }

    // ************************************v2 ********************************************
    public static OsIpSec convert(SbiNeIpSec dcGwIpSecConnection) throws ServiceException {

        OsIpSec ipsec = new OsIpSec();
        ipsec.setOverlayId(dcGwIpSecConnection.getUuid());
        ipsec.setVpcId(dcGwIpSecConnection.getNeId());
        ipsec.setVpnIkePolicy(convertIke(dcGwIpSecConnection));
        ipsec.setVpnIpSecPolicy(convertIpSec(dcGwIpSecConnection));
        ipsec.setVpnService(convertVpn(dcGwIpSecConnection));
        ipsec.setVpnIpSecSiteConnection(convertConn(dcGwIpSecConnection));

        ipsec.getAttributes().setSourceLanCidrs(dcGwIpSecConnection.getSourceLanCidrs(), "u");

        return ipsec;
    }

    /**
     * convert
     * <br>
     *
     * @param ikePolicy
     * @return
     * @since SDNHUB 0.5
     */
    private static VpnIkePolicy convertIke(SbiNeIpSec dcGwIpSecConnection) {
        SbiIkePolicy ikePolicy = dcGwIpSecConnection.getIkePolicy();
        if(ikePolicy == null) {
            return null;
        }
        VpnIkePolicy ike = new VpnIkePolicy();
        ike.setDescription(dcGwIpSecConnection.getName());
        ike.setTenantId(dcGwIpSecConnection.getTenantId());
        ike.setName(dcGwIpSecConnection.getName());
        ike.setAuthAlgorithm(ikePolicy.getAuthAlgorithm());
        ike.setEncryptionAlgorithm(ikePolicy.getEncryptionAlgorithm());
        ike.setPfs(ikePolicy.getPfs().toLowerCase());
        ike.setIkeVersion(ikePolicy.getIkeVersion());

        return ike;
    }

    /**
     * convert
     * <br>
     *
     * @param ipSecPolicy
     * @return
     * @since SDNHUB 0.5
     */
    private static VpnIpSecPolicy convertIpSec(SbiNeIpSec dcGwIpSecConnection) {
        SbiIpSecPolicy ipSecPolicy = dcGwIpSecConnection.getIpSecPolicy();

        if(ipSecPolicy == null) {
            return null;
        }

        VpnIpSecPolicy ipsec = new VpnIpSecPolicy();
        ipsec.setDescription(dcGwIpSecConnection.getName());
        ipsec.setName(dcGwIpSecConnection.getName());
        ipsec.setTransformProtocol(ipSecPolicy.getTransformProtocol().toLowerCase());
        ipsec.setAuthAlgorithm(ipSecPolicy.getAuthAlgorithm());
        ipsec.setEncapsulationMode(ipSecPolicy.getEncapsulationMode());
        ipsec.setEncryptionAlgorithm(ipSecPolicy.getEncryptionAlgorithm());

        ipsec.setPfs(ipSecPolicy.getPfs().toLowerCase());
        ipsec.setTenantId(dcGwIpSecConnection.getTenantId());

        return ipsec;
    }

    /**
     * convert1
     * <br>
     *
     * @param dcGwIpSecConnection
     * @return
     * @since SDNHUB 0.5
     */
    private static VpnService convertVpn(SbiNeIpSec dcGwIpSecConnection) {
        VpnService vpn = new VpnService();
        vpn.setDescription(dcGwIpSecConnection.getName());
        vpn.setTenantId(dcGwIpSecConnection.getTenantId());
        // it will get set at sbi layer
        vpn.setRouterId(null);
        // it will get set at sbi layer
        vpn.setSubnetId(null);
        vpn.setAdminStateUp(true);
        vpn.setName(dcGwIpSecConnection.getName());
        vpn.setDescription(dcGwIpSecConnection.getName());

        return vpn;
    }

    /**
     * convert2
     * <br>
     *
     * @param dcGwIpSecConnection
     * @return
     * @throws ServiceException
     * @since SDNHUB 0.5
     */
    private static VpnIpSecSiteConnection convertConn(SbiNeIpSec dcGwIpSecConnection) throws ServiceException {

        List<String> peerCidrList = new ArrayList<>();
        if(!dcGwIpSecConnection.getPeerLanCidrs().isEmpty()) {
            JSONArray results = JSONArray.fromObject(dcGwIpSecConnection.getPeerLanCidrs());
            for(int i = 0; i < results.size(); i++) {
                JSONObject jsonObj = results.getJSONObject(i);
                peerCidrList.add(jsonObj.get("ipv4") + "/" + jsonObj.get("ipMask"));
            }
        }

        VpnIpSecSiteConnection conn = new VpnIpSecSiteConnection();
        conn.setDescription(dcGwIpSecConnection.getName());
        conn.setTenantId(dcGwIpSecConnection.getTenantId());
        conn.setAdminStateUp(true);
        conn.setName(dcGwIpSecConnection.getName());

        String nbiPeerAddress = dcGwIpSecConnection.getPeerAddress();
        if(nbiPeerAddress != null && !nbiPeerAddress.isEmpty()) {
            JSONObject jsonObj = JSONObject.fromObject(nbiPeerAddress);
            conn.setPeerAddress(jsonObj.getString("ipv4"));
            conn.setPeerId(jsonObj.getString("ipv4"));
        }

        conn.setPeerCidrs(peerCidrList);

        if(dcGwIpSecConnection.getIkePolicy() != null && dcGwIpSecConnection.getIkePolicy().getPsk() != null) {
            char[] psk = null;
            try {
                psk = EncryptionUtil.decode(dcGwIpSecConnection.getIkePolicy().getPsk().toCharArray());
            } catch(Exception e) {
                throw new ServiceException("Error while decoding psk", e);
            }
            conn.setPsk(String.valueOf(psk));
        }
        conn.setSubnets(null);

        VpnDpd dpd = new VpnDpd();
        dpd.setAction(DpdAction.HOLD.getName());
        dpd.setInterval(DPD_INTERNVAL);
        dpd.setTimeout(DPD_TIMEOUT);

        conn.setMtu(MTU);
        conn.setInitiator(INITIATOR);
        conn.setAdminStateUp(true);
        conn.setDpd(dpd);

        return conn;
    }

    // ************************************v2 ********************************************

    /**
     * convert
     * <br>
     *
     * @param vpc
     * @return
     * @since SDNHUB 0.5
     */
    public static OsVpc convert(Vpc vpc) {
        OsVpc osVpc = new OsVpc();
        osVpc.setProjectName(vpc.getProjectName());
        osVpc.setDomainName(vpc.getDomainName());
        osVpc.setRouterName(vpc.getRouterName());
        osVpc.setOverlayId(vpc.getUuid());
        return osVpc;
    }

    /**
     * convert
     * <br>
     *
     * @param subnet
     * @return
     * @since SDNHUB 0.5
     */
    public static OsSubnet convert(Subnet subnet) {
        OsSubnet osSubnet = new OsSubnet();
        osSubnet.setCidr(subnet.getCidr());
        osSubnet.setOverlayId(subnet.getUuid());
        osSubnet.setVni(subnet.getVni());
        osSubnet.setName(subnet.getName());
        return osSubnet;
    }

    /**
     * convert
     * <br>
     *
     * @param mappings
     * @return
     * @since SDNHUB 0.5
     */
    public static OsVpc.Underlays convert(List<OverlayUnderlayMapping> mappings) {
        OsVpc.Underlays underlays = new OsVpc.Underlays();
        for(OverlayUnderlayMapping mapping : mappings) {
            if("routerId".equals(mapping.getUnderlayType())) {
                underlays.setRouterId(mapping.getUnderlayId(), mapping.getAction());
            } else if("projectId".equals(mapping.getUnderlayType())) {
                underlays.setProjectId(mapping.getUnderlayId(), mapping.getAction());
            } else if("publicNetworkId".equals(mapping.getUnderlayType())) {
                underlays.setPublicNetworkId(mapping.getUnderlayId(), mapping.getAction());
            }
        }

        return underlays;
    }

    /**
     * convert1
     * <br>
     *
     * @param mappings
     * @return
     * @since SDNHUB 0.5
     */
    public static OsSubnet.Underlays convert1(List<OverlayUnderlayMapping> mappings) {
        OsSubnet.Underlays underlays = new OsSubnet.Underlays();
        for(OverlayUnderlayMapping mapping : mappings) {
            if("routerId".equals(mapping.getUnderlayType())) {
                underlays.setRouterId(mapping.getUnderlayId(), mapping.getAction());
            } else if("networkId".equals(mapping.getUnderlayType())) {
                underlays.setVpcNetworkId(mapping.getUnderlayId(), mapping.getAction());
            } else if("subnetId".equals(mapping.getUnderlayType())) {
                underlays.setVpcSubnetId(mapping.getUnderlayId(), mapping.getAction());
            } else if("projectId".equals(mapping.getUnderlayType())) {
                underlays.setProjectId(mapping.getUnderlayId(), mapping.getAction());
            }
        }

        return underlays;
    }

    /**
     * convert2
     * <br>
     *
     * @param mappings
     * @return
     * @since SDNHUB 0.5
     */
    public static OsIpSec.Underlays convert2(List<OverlayUnderlayMapping> mappings) {
        OsIpSec.Underlays underlays = new OsIpSec.Underlays();
        for(OverlayUnderlayMapping mapping : mappings) {
            if("vpnIkePolicyId".equals(mapping.getUnderlayType())) {
                underlays.setVpnIkePolicyId(mapping.getUnderlayId(), mapping.getAction());
            } else if("vpnIpSecPolicyId".equals(mapping.getUnderlayType())) {
                underlays.setVpnIpSecPolicyId(mapping.getUnderlayId(), mapping.getAction());
            } else if("vpnServiceId".equals(mapping.getUnderlayType())) {
                underlays.setVpnServiceId(mapping.getUnderlayId(), mapping.getAction());
            } else if("vpnIpSecSiteConnectionId".equals(mapping.getUnderlayType())) {
                underlays.setVpnIpSecSiteConnectionId(mapping.getUnderlayId(), mapping.getAction());
            } else if("sourceLanCidrs".equals(mapping.getUnderlayType())) {
                underlays.setSourceLanCidrs(mapping.getUnderlayId(), mapping.getAction());
            } else if("projectId".equals(mapping.getUnderlayType())) {
                underlays.setProjectId(mapping.getUnderlayId(), mapping.getAction());
            } else if("publicNetworkId".equals(mapping.getUnderlayType())) {
                underlays.setPublicNetworkId(mapping.getUnderlayId(), mapping.getAction());
            } else if("routerId".equals(mapping.getUnderlayType())) {
                underlays.setRouterId(mapping.getUnderlayId(), mapping.getAction());
            } else if("publicSubnetId".equals(mapping.getUnderlayType())) {
                underlays.setPublicSubnetId(mapping.getUnderlayId(), mapping.getAction());
            }
        }

        return underlays;
    }

    /**
     * convert
     * <br>
     *
     * @param resources
     * @param actions
     * @param tenantId
     * @param overlayType
     * @param controllerId
     * @param overlayId
     * @return
     * @throws ServiceException
     * @since SDNHUB 0.5
     */
    public static List<OverlayUnderlayMapping> convert(Map<String, String> resources, Map<String, String> actions,
            String tenantId, String overlayType, String controllerId, String overlayId) throws ServiceException {
        List<OverlayUnderlayMapping> mappings = new ArrayList<>();

        for(Entry<String, String> entry : actions.entrySet()) {
            OverlayUnderlayMapping mapping = new OverlayUnderlayMapping();
            mapping.setUuid(UUID.randomUUID().toString());
            mapping.setControllerId(controllerId);
            mapping.setOverlayId(overlayId);
            mapping.setOverlayType(overlayType);
            mapping.setUnderlayTenantId(tenantId);
            mapping.setUnderlayId(entry.getKey());
            mapping.setAction(entry.getValue());
            mapping.setUnderlayType(resources.get(entry.getKey()));
            mappings.add(mapping);
        }
        return mappings;
    }
}
