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

package org.openo.sdnhub.osdriverservice.nbi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.osdriverservice.dao.model.OverlayUnderlayMapping;
import org.openo.sdnhub.osdriverservice.openstack.client.OpenStackClient;
import org.openo.sdnhub.osdriverservice.openstack.client.exception.OpenStackException;
import org.openo.sdnhub.osdriverservice.openstack.client.http.OpenStackHttpConnection;
import org.openo.sdnhub.osdriverservice.openstack.client.model.FloatingIp;
import org.openo.sdnhub.osdriverservice.openstack.client.model.Subnet;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIkePolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecPolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecSiteConnection;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnService;
import org.openo.sdnhub.osdriverservice.sbi.model.OsIpSec;
import org.openo.sdnhub.osdriverservice.sbi.model.OsIpSec.Underlays;

import mockit.Mock;
import mockit.MockUp;

/**
 * VPC NBI service tests.<br>
 *
 * @author
 * @version SDNHUB 0.5 September 20, 2016
 */
public class IpSecNbiServiceTest {

    IpSecNbiService service = new IpSecNbiService();
    OsIpSec osIpSec = new OsIpSec();
    private static final String CTRL_UUID = "uid123";

    @Before
    public void setUp() throws Exception {

        osIpSec.setOverlayId("overlayId123");
        osIpSec.setVpcId("vpcId123");

        VpnIkePolicy vpnIkePolicy = new VpnIkePolicy();
        vpnIkePolicy.setName("name");
        vpnIkePolicy.setTenantId("tenantId");
        vpnIkePolicy.setAuthAlgorithm("authAlgorithm");
        vpnIkePolicy.setDescription("description");
        vpnIkePolicy.setEncryptionAlgorithm("encryptionAlgorith");
        vpnIkePolicy.setIkeVersion("ikeVersion");
        vpnIkePolicy.setPfs("pfs");
        vpnIkePolicy.setId("policy1");
        osIpSec.setVpnIkePolicy(vpnIkePolicy);

        VpnIpSecPolicy vpnIpSecPolicy = new VpnIpSecPolicy();
        vpnIpSecPolicy.setName("name");
        vpnIpSecPolicy.setTenantId("tenantId");
        vpnIpSecPolicy.setAuthAlgorithm("authAlgorithm");
        vpnIpSecPolicy.setDescription("description");
        vpnIpSecPolicy.setEncryptionAlgorithm("encryptionAlgorith");
        vpnIpSecPolicy.setEncapsulationMode("encapsulationMode");
        vpnIpSecPolicy.setTransformProtocol("transformProtocol");
        vpnIpSecPolicy.setPfs("pfs");
        osIpSec.setVpnIpSecPolicy(vpnIpSecPolicy);

        VpnIpSecSiteConnection vpnIpSecSiteConnection = new VpnIpSecSiteConnection();
        vpnIpSecSiteConnection.setTenantId("tenantId");
        vpnIpSecSiteConnection.setAuthMode("authMode");
        vpnIpSecSiteConnection.setPsk("psk");
        vpnIpSecSiteConnection.setPeerAddress("peerAddress");
        osIpSec.setVpnIpSecSiteConnection(vpnIpSecSiteConnection);

        VpnService vpn = new VpnService();
        vpn.setId("id1234");
        // vpn.setSubnetId("subnet1234");
        osIpSec.setVpnService(vpn);

        Underlays attributes = new Underlays();
        attributes.setPublicSubnetId("subnet12", "add");
        attributes.setRouterId("router12", "add");
        attributes.setProjectId("projet123", "add");
        osIpSec.setAttributes(attributes);

        new MockUp<OpenStackHttpConnection>() {

            @Mock
            public void login() throws OpenStackException {
                return;
            }
        };
    }

    @Test(expected = ServiceException.class)
    public void testCreateIpSec() throws ServiceException {

        new MockUp<OpenStackClient>() {

            @Mock
            public FloatingIp createFloatingIp(FloatingIp floatingIp) throws OpenStackException {
                return floatingIp;
            }

            @Mock
            public List<Subnet> listSubnetForNetowrkId(String networkId) throws OpenStackException {
                List<Subnet> subnetList = new LinkedList<>();
                Subnet subnet = new Subnet();
                subnet.setId("id123");
                subnet.setIpVersion("4");
                subnet.setName("subnet1");
                subnet.setNetworkId("network123");
                subnet.setProjectId("project123");
                subnet.setGatewayIp("192.1.0.0");
                subnet.setCidr("10.172.12.13/24");
                subnetList.add(subnet);
                return subnetList;
            }

            @Mock
             public VpnIkePolicy createVpnIkePolicy(VpnIkePolicy policy) throws OpenStackException {
                return policy;
            }
        };

        VpnService vpn = new VpnService();
        vpn.setId("id1234");
        vpn.setSubnetId("subnet1234");
        osIpSec.setVpnService(vpn);
        service.createIpSec(CTRL_UUID, osIpSec);
    }

    @Test(expected = ServiceException.class)
    public void testCreateIpSec_EmptySubnet() throws ServiceException {

        service.createIpSec(CTRL_UUID, osIpSec);
    }

    @Test(expected = ServiceException.class)
    public void testUpdateIpSec_EmptySubnet() throws ServiceException {
        VpnService vpn = new VpnService();
        vpn.setId("id1234");
        vpn.setSubnetId("subnet1234");
        osIpSec.setVpnService(vpn);
        service.updateIpSec(CTRL_UUID, "ipsec123",osIpSec);
    }

    @Test(expected = ServiceException.class)
    public void testDeleteIpSec_EmptySubnet() throws ServiceException {
        VpnService vpn = new VpnService();
        vpn.setId("id1234");
        vpn.setSubnetId("subnet1234");
        osIpSec.setVpnService(vpn);
        service.deleteIpSec(CTRL_UUID, "ipsec123");
    }

}
