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

package org.openo.sdnhub.osdriverservice.util;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.osdriverservice.dao.model.OverlayUnderlayMapping;
import org.openo.sdnhub.osdriverservice.nbi.model.SbiIkePolicy;
import org.openo.sdnhub.osdriverservice.nbi.model.SbiIpSecPolicy;
import org.openo.sdnhub.osdriverservice.nbi.model.SbiNeIpSec;
import org.openo.sdnhub.osdriverservice.sbi.model.OsIpSec;
import org.openo.sdnhub.osdriverservice.sbi.model.OsSubnet;
import org.openo.sdnhub.osdriverservice.sbi.model.OsVpc;
import org.openo.sdno.overlayvpn.model.ipsec.IkePolicy;
import org.openo.sdno.overlayvpn.model.ipsec.IpSecPolicy;
import org.openo.sdno.overlayvpn.model.netmodel.ipsec.DcGwIpSecConnection;
import org.openo.sdno.overlayvpn.model.netmodel.vpc.Subnet;
import org.openo.sdno.overlayvpn.model.netmodel.vpc.Vpc;

/**
 * MigrateModel Utility tests.<br>
 *
 * @author
 * @version SDNHUB 0.5 September 20, 2016
 */
public class MigrateModelUtilTest {
    @Test
    public void testConvert() {
        DcGwIpSecConnection dcGwIpSecConnection = new DcGwIpSecConnection();
        dcGwIpSecConnection.setUuid("uuid!23");
        IkePolicy ikePolicy = new IkePolicy();
        ikePolicy.setLifeTime("7200");
        ikePolicy.setName("name");
        ikePolicy.setTenantId("tenantId");
        ikePolicy.setAuthAlgorithm("authAlgorithm");
        ikePolicy.setDescription("description");
        ikePolicy.setEncryptionAlgorithm("encryptionAlgorith");
        ikePolicy.setIkeVersion("ikeVersion");
        ikePolicy.setPfs("pfs");
        dcGwIpSecConnection.setIkePolicy(ikePolicy);

        IpSecPolicy ipSecPolicy = new IpSecPolicy();
        ipSecPolicy.setLifeTime("7200");
        ipSecPolicy.setName("name");
        ipSecPolicy.setTenantId("tenantId");
        ipSecPolicy.setAuthAlgorithm("authAlgorithm");
        ipSecPolicy.setDescription("description");
        ipSecPolicy.setEncryptionAlgorithm("encryptionAlgorith");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        ipSecPolicy.setPfs("pfs");
        dcGwIpSecConnection.setIpSecPolicy(ipSecPolicy);

        dcGwIpSecConnection.setRouterId("routerId");
        dcGwIpSecConnection.setVpcId("vpcId");
        dcGwIpSecConnection.setTenantId("tenantId");
        dcGwIpSecConnection.setAdminStatus("adminStatus");
        dcGwIpSecConnection.setPeerSubnetCidrs("123,123,342,34343");dcGwIpSecConnection.setAuthMode("authMode");
        dcGwIpSecConnection.setPsk("psk");
        dcGwIpSecConnection.setPeerAddress("peerAddress");
        org.openo.sdnhub.osdriverservice.sbi.model.OsIpSec osIpSec = MigrateModelUtil.convert(dcGwIpSecConnection);
        assertTrue(osIpSec != null);
    }

    @Test
    public void testConvertVpc(){

        Vpc vpc = new Vpc();
        vpc.setName("name/name");
        vpc.setUuid("uuid");

        OsVpc osVpc =  MigrateModelUtil.convert(vpc);
        assertTrue(osVpc != null);
    }

    @Test
    public void testConvertSubnet(){

        Subnet subnet = new Subnet();
        subnet.setCidr("cidr");

        OsSubnet osSubnet =  MigrateModelUtil.convert(subnet);
        assertTrue(osSubnet != null);
    }

    @Test
    public void testConvertUnderlay(){

        List<OverlayUnderlayMapping> list = new ArrayList<>();
        OverlayUnderlayMapping obj1 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj2 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj3 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj4 = new OverlayUnderlayMapping();
        obj1.setUnderlayType("routerId");
        obj2.setUnderlayType("projectId");
        obj3.setUnderlayType("publicNetworkId");
        obj4.setUnderlayType("extra");
        obj1.setAction("action");
        obj2.setAction("action");
        obj3.setAction("action");
        obj4.setAction("action");
        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        list.add(obj4);

        OsVpc.Underlays underlays =  MigrateModelUtil.convert(list);
        assertTrue(underlays != null);
    }

    @Test
    public void testConvertUnderlay1(){

        List<OverlayUnderlayMapping> list = new ArrayList<>();
        OverlayUnderlayMapping obj1 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj2 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj3 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj4 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj5 = new OverlayUnderlayMapping();
        obj1.setUnderlayType("routerId");
        obj2.setUnderlayType("projectId");
        obj3.setUnderlayType("networkId");
        obj4.setUnderlayType("subnetId");
        obj5.setUnderlayType("extra");
        obj1.setAction("action");
        obj2.setAction("action");
        obj3.setAction("action");
        obj4.setAction("action");
        obj5.setAction("action");
        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        list.add(obj4);
        list.add(obj5);

        OsSubnet.Underlays underlays =  MigrateModelUtil.convert1(list);
        assertTrue(underlays != null);
    }


    @Test
    public void testConvertIpSecUnderlays(){

        List<OverlayUnderlayMapping> list = new ArrayList<>();
        OverlayUnderlayMapping obj1 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj2 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj3 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj4 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj5 = new OverlayUnderlayMapping();

        OverlayUnderlayMapping obj6 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj7 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj8 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj9 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj10 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj11 = new OverlayUnderlayMapping();
        OverlayUnderlayMapping obj12 = new OverlayUnderlayMapping();

        obj1.setUnderlayType("vpnIkePolicyId");
        obj2.setUnderlayType("vpnIpSecPolicyId");
        obj3.setUnderlayType("vpnServiceId");
        obj4.setUnderlayType("vpnIpSecSiteConnectionId");
        obj5.setUnderlayType("extra");
        obj6.setUnderlayType("floatingIpId");
        obj7.setUnderlayType("sourceAddress");
        obj8.setUnderlayType("sourceLanCidrs");
        obj9.setUnderlayType("projectId");
        obj10.setUnderlayType("publicNetworkId");
        obj11.setUnderlayType("routerId");
        obj12.setUnderlayType("publicSubnetId");

        obj1.setAction("action");
        obj2.setAction("action");
        obj3.setAction("action");
        obj4.setAction("action");
        obj5.setAction("action");
        obj6.setAction("action");
        obj7.setAction("action");
        obj8.setAction("action");
        obj9.setAction("action");
        obj10.setAction("action");
        obj11.setAction("action");
        obj12.setAction("action");

        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        list.add(obj4);
        list.add(obj5);
        list.add(obj6);
        list.add(obj7);
        list.add(obj8);
        list.add(obj9);
        list.add(obj10);
        list.add(obj11);
        list.add(obj12);

        OsIpSec.Underlays underlays =  MigrateModelUtil.convert2(list);
        assertTrue(underlays != null);
    }

    @Test
    public void testConvertList() throws ServiceException{
        Map<String, String> resources = new HashMap<>();
        resources.put("key", "value");
        Map<String, String> actions = new HashMap<>();
        actions.put("key", "value");
        MigrateModelUtil.convert(resources, actions, "tenantID", "overlayType", "controllerId", "overlayId");
    }

    @Test
    public void testConvertNullIpSec() throws ServiceException {

        SbiNeIpSec dcGwIpSecConnection = new SbiNeIpSec();
        SbiIkePolicy sbiIkePolicy = new SbiIkePolicy();
        dcGwIpSecConnection.setIkePolicy(sbiIkePolicy);
        dcGwIpSecConnection.setPeerLanCidrs("[{\"ipv4\":\"10.21.3.0\",\"ipMask\":\"24\"}]");
        org.openo.sdnhub.osdriverservice.sbi.model.OsIpSec osIpSec = MigrateModelUtil.convert(dcGwIpSecConnection);
        assertTrue(osIpSec != null);
    }
    @Test (expected = NullPointerException.class)
    public void testConvertNull() throws ServiceException {
        SbiNeIpSec snips = new SbiNeIpSec();
        MigrateModelUtil.convert(snips);
    }

    @Test (expected = NullPointerException.class)
    public void testConvertNotNullForIpSec() throws ServiceException {
        SbiNeIpSec snips = new SbiNeIpSec();
        SbiIpSecPolicy ipSecPolicy = new SbiIpSecPolicy();
        ipSecPolicy.setAuthAlgorithm("jhc");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setEncryptionAlgorithm("encryptionAlgorithm");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        snips.setIpSecPolicy(ipSecPolicy);
        MigrateModelUtil.convert(snips);
    }
}
