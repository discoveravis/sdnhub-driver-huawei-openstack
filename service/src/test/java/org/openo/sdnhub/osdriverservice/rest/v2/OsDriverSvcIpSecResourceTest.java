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

package org.openo.sdnhub.osdriverservice.rest.v2;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.osdriverservice.dao.model.OverlayUnderlayMapping;
import org.openo.sdnhub.osdriverservice.nbi.model.SbiIkePolicy;
import org.openo.sdnhub.osdriverservice.nbi.model.SbiIpSecPolicy;
import org.openo.sdnhub.osdriverservice.nbi.model.SbiNeIpSec;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIkePolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecPolicy;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnIpSecSiteConnection;
import org.openo.sdnhub.osdriverservice.openstack.client.model.VpnService;
import org.openo.sdnhub.osdriverservice.openstack.utils.JsonUtil;
import org.openo.sdnhub.osdriverservice.rest.CloseableHttpResponseMock;
import org.openo.sdnhub.osdriverservice.util.DaoUtil;
import org.openo.sdno.overlayvpn.brs.invdao.CommParamDao;
import org.openo.sdno.overlayvpn.brs.invdao.ControllerDao;
import org.openo.sdno.overlayvpn.brs.model.AuthInfo;
import org.openo.sdno.overlayvpn.brs.model.CommParamMO;
import org.openo.sdno.overlayvpn.brs.model.ControllerMO;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.openo.sdno.overlayvpn.util.check.ValidationUtil;
import org.openo.sdno.ssl.EncryptionUtil;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import mockit.Mock;
import mockit.MockUp;

/**
 * OsDriverSvc IpSec Resource tests.<br>
 *
 * @author
 * @version SDNHUB 0.5 September 20, 2016
 */
public class OsDriverSvcIpSecResourceTest {

    OsDriverSvcIpSecRoaResource roaSource = new OsDriverSvcIpSecRoaResource();

    @Test
    public void testCreateIpSec() throws ServiceException {
        new MockUp<ValidationUtil>() {

            @Mock
            public void validateModel(Object obj) throws ServiceException {

            }
        };
        new MockUp<ControllerDao>() {

            @Mock
            public ControllerMO getController(String uuid) {
                ControllerMO mo = new ControllerMO();
                mo.setHostName("abc.xyz");
                mo.setDescription("test123");

                return mo;
            }
        };
        new MockUp<CommParamDao>() {

            @Mock
            public List<CommParamMO> getCommParam(String uuid) {
                List<CommParamMO> list = new ArrayList<CommParamMO>();
                CommParamMO mo = new CommParamMO();
                mo.setHostName("test");
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                mo.setAuthInfo(info);
                list.add(mo);
                return list;
            }
        };

        new MockUp<EntityUtils>() {

            @Mock
            public String toString(final HttpEntity entity, final Charset defaultCharset)
                    throws IOException, ParseException {
                return "{\"token\": {\"catalog\": [{\"type\":\"network\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}, {\"type\":\"type\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]},{\"type\":\"network\",\"endpoints\":[{\"interface\":\"publics\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}]}}";
            }
        };
        new MockUp<CloseableHttpClient>() {

            @Mock
            public CloseableHttpResponse execute(final HttpUriRequest request)
                    throws IOException, ClientProtocolException {
                CloseableHttpResponseMock resp = new CloseableHttpResponseMock();
                org.apache.http.message.BasicStatusLine bs = new org.apache.http.message.BasicStatusLine(
                        new ProtocolVersion("bgp", 2, 1), 200, "success");
                resp.setStatusLine(bs);
                return (CloseableHttpResponse) resp;
            }
        };

        new MockUp<ObjectMapper>() {

            @Mock
            public <T> T readValue(String content, Class<T> valueType)
                    throws IOException, JsonParseException, JsonMappingException {
                if (valueType.getName().contains("VpnIkePolicy")) {
                    VpnIkePolicy vpn = new VpnIkePolicy();
                    vpn.setId("id");
                    return (T) vpn;
                } else if (valueType.getName().contains("VpnIpSecPolicy")) {
                    VpnIpSecPolicy vpn = new VpnIpSecPolicy();
                    vpn.setId("id");
                    return (T) vpn;
                } else if (valueType.getName().contains("VpnService")) {
                    VpnService vpn = new VpnService();
                    vpn.setId("id");
                    return (T) vpn;
                } else if (valueType.getName().contains("VpnIpSecSiteConnection")) {
                    VpnIpSecSiteConnection vpn = new VpnIpSecSiteConnection();
                    vpn.setId("id");
                    return (T) vpn;
                }
                return null;
            }
        };

        new MockUp<DaoUtil<T>>() {

            @Mock
            public <T> T insert(T data) throws ServiceException {
                return null;
            }
        };
        new MockUp<org.openo.sdno.framework.container.util.JsonUtil>() {

            @Mock
            public AuthInfo fromJson(String arg0, Class<AuthInfo> arg1) {
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                return info;
            }
        };

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        SbiNeIpSec dcGwIpSecConnection = new SbiNeIpSec();
        dcGwIpSecConnection.setUuid("uuid!23");
        SbiIkePolicy ikePolicy = new SbiIkePolicy();
        ikePolicy.setLifeTime("7200");
        ikePolicy.setAuthAlgorithm("authAlgorithm");
        ikePolicy.setEncryptionAlgorithm("encryptionAlgorith");
        ikePolicy.setIkeVersion("ikeVersion");
        ikePolicy.setPfs("pfs");
        ikePolicy.setPsk("psk");
        dcGwIpSecConnection.setIkePolicy(ikePolicy);

        SbiIpSecPolicy ipSecPolicy = new SbiIpSecPolicy();
        ipSecPolicy.setLifeTime("7200");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        ipSecPolicy.setPfs("pfs");
        dcGwIpSecConnection.setIpSecPolicy(ipSecPolicy);

        dcGwIpSecConnection.setTenantId("tenantId");
        dcGwIpSecConnection.setActiveStatus("None");
        dcGwIpSecConnection.setPeerLanCidrs("[{\"ipv4\":\"10.21.3.0\",\"ipMask\":\"24\"}]");
        dcGwIpSecConnection.setPeerAddress("peerAddress");
        List<SbiNeIpSec> ipSecConnList = new ArrayList<SbiNeIpSec>();
        ipSecConnList.add(dcGwIpSecConnection);
        System.out.println(JsonUtil.toJson(ipSecConnList));
        ResultRsp<SbiNeIpSec> result = roaSource.createIpSec(request, "ctrlUUIT", ipSecConnList);
        assertTrue(result.getHttpCode() == 200);
    }

    @Test
    public void testUpdateIpSec() throws ServiceException {
        new MockUp<ValidationUtil>() {

            @Mock
            public void validateModel(Object obj) throws ServiceException {

            }
        };
        new MockUp<ControllerDao>() {

            @Mock
            public ControllerMO getController(String uuid) {
                ControllerMO mo = new ControllerMO();
                mo.setHostName("abc.xyz");
                mo.setDescription("test123");

                return mo;
            }
        };
        new MockUp<CommParamDao>() {

            @Mock
            public List<CommParamMO> getCommParam(String uuid) {
                List<CommParamMO> list = new ArrayList<CommParamMO>();
                CommParamMO mo = new CommParamMO();
                mo.setHostName("test");
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                mo.setAuthInfo(info);
                list.add(mo);
                return list;
            }
        };

        new MockUp<EntityUtils>() {

            @Mock
            public String toString(final HttpEntity entity, final Charset defaultCharset)
                    throws IOException, ParseException {
                return "{\"token\": {\"catalog\": [{\"type\":\"network\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}, {\"type\":\"type\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]},{\"type\":\"network\",\"endpoints\":[{\"interface\":\"publics\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}]}}";
            }
        };
        new MockUp<CloseableHttpClient>() {

            @Mock
            public CloseableHttpResponse execute(final HttpUriRequest request)
                    throws IOException, ClientProtocolException {
                CloseableHttpResponseMock resp = new CloseableHttpResponseMock();
                org.apache.http.message.BasicStatusLine bs = new org.apache.http.message.BasicStatusLine(
                        new ProtocolVersion("bgp", 2, 1), 200, "success");
                resp.setStatusLine(bs);
                return (CloseableHttpResponse) resp;
            }
        };

        new MockUp<ObjectMapper>() {

            @Mock
            public <T> T readValue(String content, Class<T> valueType)
                    throws IOException, JsonParseException, JsonMappingException {
                if (valueType.getName().contains("VpnIkePolicy")) {
                    VpnIkePolicy vpn = new VpnIkePolicy();
                    vpn.setId("id");
                    return (T) vpn;
                } else if (valueType.getName().contains("VpnIpSecPolicy")) {
                    VpnIpSecPolicy vpn = new VpnIpSecPolicy();
                    vpn.setId("id");
                    return (T) vpn;
                } else if (valueType.getName().contains("VpnService")) {
                    VpnService vpn = new VpnService();
                    vpn.setId("id");
                    return (T) vpn;
                } else if (valueType.getName().contains("VpnIpSecSiteConnection")) {
                    VpnIpSecSiteConnection vpn = new VpnIpSecSiteConnection();
                    vpn.setId("id");
                    return (T) vpn;
                }
                return null;
            }
        };

        new MockUp<DaoUtil<T>>() {

            @Mock
            public <T> T insert(T data) throws ServiceException {
                return null;
            }
        };
        new MockUp<org.openo.sdno.framework.container.util.JsonUtil>() {

            @Mock
            public AuthInfo fromJson(String arg0, Class<AuthInfo> arg1) {
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                return info;
            }
        };

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        SbiNeIpSec dcGwIpSecConnection = new SbiNeIpSec();
        dcGwIpSecConnection.setUuid("uuid!23");
        SbiIkePolicy ikePolicy = new SbiIkePolicy();
        ikePolicy.setLifeTime("7200");
        ikePolicy.setAuthAlgorithm("authAlgorithm");
        ikePolicy.setEncryptionAlgorithm("encryptionAlgorith");
        ikePolicy.setIkeVersion("ikeVersion");
        ikePolicy.setPsk("psk");
        ikePolicy.setPfs("pfs");
        dcGwIpSecConnection.setIkePolicy(ikePolicy);

        SbiIpSecPolicy ipSecPolicy = new SbiIpSecPolicy();
        ipSecPolicy.setLifeTime("7200");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        ipSecPolicy.setPfs("pfs");
        dcGwIpSecConnection.setIpSecPolicy(ipSecPolicy);

        dcGwIpSecConnection.setTenantId("tenantId");
        dcGwIpSecConnection.setActiveStatus("None");
        dcGwIpSecConnection.setPeerLanCidrs("[{\"ipv4\":\"10.21.3.0\",\"ipMask\":\"24\"}]");
        dcGwIpSecConnection.setPeerAddress("peerAddress");
        List<SbiNeIpSec> ipSecConnList = new ArrayList<SbiNeIpSec>();
        ipSecConnList.add(dcGwIpSecConnection);
        System.out.println(JsonUtil.toJson(ipSecConnList));
        ResultRsp<SbiNeIpSec> r1 = roaSource.updateIpSec(request, "ctrlUUIT", "123", dcGwIpSecConnection);
        assertTrue(r1.getHttpCode() == 200);
    }

    @Test
    public void testUpdateIpSecException() throws ServiceException {
        new MockUp<ValidationUtil>() {

            @Mock
            public void validateModel(Object obj) throws ServiceException {

            }
        };

        SbiNeIpSec dcGwIpSecConnection = new SbiNeIpSec();
        dcGwIpSecConnection.setUuid("uuid!23");
        SbiIkePolicy ikePolicy = new SbiIkePolicy();
        ikePolicy.setLifeTime("7200");
        ikePolicy.setAuthAlgorithm("authAlgorithm");
        ikePolicy.setEncryptionAlgorithm("encryptionAlgorith");
        ikePolicy.setIkeVersion("ikeVersion");
        ikePolicy.setPsk("psk");
        ikePolicy.setPfs("pfs");
        dcGwIpSecConnection.setIkePolicy(ikePolicy);

        SbiIpSecPolicy ipSecPolicy = new SbiIpSecPolicy();
        ipSecPolicy.setLifeTime("7200");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        ipSecPolicy.setPfs("pfs");
        dcGwIpSecConnection.setIpSecPolicy(ipSecPolicy);

        dcGwIpSecConnection.setTenantId("tenantId");
        dcGwIpSecConnection.setActiveStatus("None");
        dcGwIpSecConnection.setPeerLanCidrs("[{\"ipv4\":\"10.21.3.0\",\"ipMask\":\"24\"}]");
        dcGwIpSecConnection.setPeerAddress("peerAddress");
        List<SbiNeIpSec> ipSecConnList = new ArrayList<SbiNeIpSec>();
        ipSecConnList.add(dcGwIpSecConnection);
        System.out.println(JsonUtil.toJson(ipSecConnList));
        HttpServletRequest request = new MockHttpServletRequest();
        ResultRsp<SbiNeIpSec> r1 = roaSource.updateIpSec(request, "ctrlUUIT", "123", dcGwIpSecConnection);
        assertTrue(r1.getHttpCode() == 200);
    }




    @Test
    public void testCreateIpSecBranch() throws ServiceException {
        new MockUp<ValidationUtil>() {

            @Mock
            public void validateModel(Object obj) throws ServiceException {

            }
        };
        new MockUp<ControllerDao>() {

            @Mock
            public ControllerMO getController(String uuid) {
                ControllerMO mo = new ControllerMO();
                mo.setHostName("abc.xyz");
                mo.setDescription("test123");

                return mo;
            }
        };
        new MockUp<CommParamDao>() {

            @Mock
            public List<CommParamMO> getCommParam(String uuid) {
                List<CommParamMO> list = new ArrayList<CommParamMO>();
                CommParamMO mo = new CommParamMO();
                mo.setHostName("test");
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                mo.setAuthInfo(info);
                list.add(mo);
                return list;
            }
        };

        new MockUp<EntityUtils>() {

            @Mock
            public String toString(final HttpEntity entity, final Charset defaultCharset)
                    throws IOException, ParseException {
                return "{\"token\": {\"catalog\": [{\"type\":\"network\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}, {\"type\":\"type\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]},{\"type\":\"network\",\"endpoints\":[{\"interface\":\"publics\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}]}}";
            }
        };
        new MockUp<CloseableHttpClient>() {

            @Mock
            public CloseableHttpResponse execute(final HttpUriRequest request)
                    throws IOException, ClientProtocolException {
                CloseableHttpResponseMock resp = new CloseableHttpResponseMock();
                org.apache.http.message.BasicStatusLine bs = new org.apache.http.message.BasicStatusLine(
                        new ProtocolVersion("bgp", 2, 1), 201, "success");
                resp.setStatusLine(bs);
                return (CloseableHttpResponse) resp;
            }
        };

        new MockUp<ObjectMapper>() {

            @Mock
            public <T> T readValue(String content, Class<T> valueType)
                    throws IOException, JsonParseException, JsonMappingException {
                if (valueType.getName().contains("VpnIkePolicy")) {
                    VpnIkePolicy vpn = new VpnIkePolicy();
                    vpn.setId("id");
                    return (T) vpn;
                } else if (valueType.getName().contains("VpnIpSecPolicy")) {
                    VpnIpSecPolicy vpn = new VpnIpSecPolicy();
                    vpn.setId("id");
                    return (T) vpn;
                } else if (valueType.getName().contains("VpnService")) {
                    VpnService vpn = new VpnService();
                    vpn.setId("id");
                    return (T) vpn;
                } else if (valueType.getName().contains("VpnIpSecSiteConnection")) {
                    VpnIpSecSiteConnection vpn = new VpnIpSecSiteConnection();
                    vpn.setId("id");
                    return (T) vpn;
                }
                return null;
            }
        };

        new MockUp<DaoUtil<T>>() {

            @Mock
            public <T> T insert(T data) throws ServiceException {
                return null;
            }
        };
        new MockUp<org.openo.sdno.framework.container.util.JsonUtil>() {

            @Mock
            public AuthInfo fromJson(String arg0, Class<AuthInfo> arg1) {
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                return info;
            }
        };

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        List<SbiNeIpSec> dcGwIpSecConnectionList = new ArrayList<>();
        SbiNeIpSec dcGwIpSecConnection = new SbiNeIpSec();
        dcGwIpSecConnection.setUuid("uuid!23");
        SbiIkePolicy ikePolicy = new SbiIkePolicy();
        ikePolicy.setLifeTime("7200");
        ikePolicy.setAuthAlgorithm("authAlgorithm");
        ikePolicy.setEncryptionAlgorithm("encryptionAlgorith");
        ikePolicy.setIkeVersion("ikeVersion");
        ikePolicy.setPsk("psk");
        ikePolicy.setPfs("pfs");
        dcGwIpSecConnection.setIkePolicy(ikePolicy);

        SbiIpSecPolicy ipSecPolicy = new SbiIpSecPolicy();
        ipSecPolicy.setLifeTime("7200");
        ipSecPolicy.setAuthAlgorithm("authAlgorithm");
        ipSecPolicy.setEncryptionAlgorithm("encryptionAlgorith");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        ipSecPolicy.setPfs("pfs");
        dcGwIpSecConnection.setIpSecPolicy(ipSecPolicy);

        dcGwIpSecConnection.setTenantId("tenantId");
        dcGwIpSecConnection.setActiveStatus("adminStatus");
        dcGwIpSecConnection.setPeerLanCidrs("[{\"ipv4\":\"10.21.3.0\",\"ipMask\":\"24\"}]");
        dcGwIpSecConnection.setPeerAddress("peerAddress");

        ResultRsp<SbiNeIpSec> result = roaSource.createIpSec(request, "ctrlUUIT", dcGwIpSecConnectionList);
        assertTrue(result.getHttpCode() == 200);

        List<SbiNeIpSec> ipSecConnList = new ArrayList<>();
        ipSecConnList.add(dcGwIpSecConnection);
        ResultRsp<SbiNeIpSec> resultList = roaSource.createIpSec(request, "ctrlUUIT", ipSecConnList);
        assertTrue(resultList.getHttpCode() == 200);
    }

    @Test
    public void testCreateIpSecException() throws ServiceException {
        new MockUp<ValidationUtil>() {

            @Mock
            public void validateModel(Object obj) throws ServiceException {

            }
        };
        new MockUp<ControllerDao>() {

            @Mock
            public ControllerMO getController(String uuid) {
                ControllerMO mo = new ControllerMO();
                mo.setHostName("abc.xyz");
                mo.setDescription("test123");

                return mo;
            }
        };
        new MockUp<CommParamDao>() {

            @Mock
            public List<CommParamMO> getCommParam(String uuid) {
                List<CommParamMO> list = new ArrayList<CommParamMO>();
                CommParamMO mo = new CommParamMO();
                mo.setHostName("test");
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                mo.setAuthInfo(info);
                list.add(mo);
                return list;
            }
        };

        new MockUp<EntityUtils>() {

            @Mock
            public String toString(final HttpEntity entity, final Charset defaultCharset)
                    throws IOException, ParseException {
                return "{\"token\": {\"catalog\": [{\"type\":\"network\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}, {\"type\":\"type\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]},{\"type\":\"network\",\"endpoints\":[{\"interface\":\"publics\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}]}}";
            }
        };
        new MockUp<CloseableHttpClient>() {

            @Mock
            public CloseableHttpResponse execute(final HttpUriRequest request)
                    throws IOException, ClientProtocolException {
                CloseableHttpResponseMock resp = new CloseableHttpResponseMock();
                org.apache.http.message.BasicStatusLine bs = new org.apache.http.message.BasicStatusLine(
                        new ProtocolVersion("bgp", 2, 1), 500, "success");
                resp.setStatusLine(bs);
                return (CloseableHttpResponse) resp;
            }
        };

        new MockUp<org.openo.sdno.framework.container.util.JsonUtil>() {

            @Mock
            public AuthInfo fromJson(String arg0, Class<AuthInfo> arg1) {
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                return info;
            }
        };

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        SbiNeIpSec dcGwIpSecConnection = new SbiNeIpSec();
        dcGwIpSecConnection.setUuid("uuid!23");
        SbiIkePolicy ikePolicy = new SbiIkePolicy();
        ikePolicy.setLifeTime("7200");
        ikePolicy.setAuthAlgorithm("authAlgorithm");
        ikePolicy.setIkeVersion("ikeVersion");
        ikePolicy.setPsk("psk");
        ikePolicy.setPfs("pfs");
        dcGwIpSecConnection.setIkePolicy(ikePolicy);

        SbiIpSecPolicy ipSecPolicy = new SbiIpSecPolicy();
        ipSecPolicy.setLifeTime("7200");
        ipSecPolicy.setAuthAlgorithm("authAlgorithm");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        ipSecPolicy.setPfs("pfs");
        dcGwIpSecConnection.setIpSecPolicy(ipSecPolicy);

        dcGwIpSecConnection.setTenantId("tenantId");
        dcGwIpSecConnection.setActiveStatus("adminStatus");
        dcGwIpSecConnection.setPeerLanCidrs("[{\"ipv4\":\"10.21.3.0\",\"ipMask\":\"24\"}]");
        dcGwIpSecConnection.setPeerAddress("peerAddress");
        List<SbiNeIpSec> ipSecConnList = new ArrayList<SbiNeIpSec>();
        ipSecConnList.add(dcGwIpSecConnection);
        ResultRsp<SbiNeIpSec> rsp = roaSource.createIpSec(request, "ctrlUUIT", ipSecConnList);

        assertTrue(rsp.getErrorCode()==ErrorCode.OVERLAYVPN_FAILED);

    }

    @Test
    public void testDeleteIpSec() throws ServiceException {
        new MockUp<DaoUtil<T>>() {

            @Mock
            public <T> List<OverlayUnderlayMapping> getChildren(Class<T> clazz, String overlayId)
                    throws ServiceException {
                List<OverlayUnderlayMapping> list = new ArrayList<>();
                OverlayUnderlayMapping obj1 = new OverlayUnderlayMapping();
                OverlayUnderlayMapping obj2 = new OverlayUnderlayMapping();
                OverlayUnderlayMapping obj3 = new OverlayUnderlayMapping();
                OverlayUnderlayMapping obj4 = new OverlayUnderlayMapping();
                OverlayUnderlayMapping obj5 = new OverlayUnderlayMapping();
                obj1.setUnderlayType("vpnIkePolicyId");
                obj2.setUnderlayType("vpnIpSecPolicyId");
                obj3.setUnderlayType("vpnServiceId");
                obj4.setUnderlayType("vpnIpSecSiteConnectionId");
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
                return list;
            }
        };
        new MockUp<ControllerDao>() {

            @Mock
            public ControllerMO getController(String uuid) {
                ControllerMO mo = new ControllerMO();
                mo.setHostName("abc.xyz");
                mo.setDescription("test123");

                return mo;
            }
        };
        new MockUp<CommParamDao>() {

            @Mock
            public List<CommParamMO> getCommParam(String uuid) {
                List<CommParamMO> list = new ArrayList<CommParamMO>();
                CommParamMO mo = new CommParamMO();
                mo.setHostName("test");
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                mo.setAuthInfo(info);
                list.add(mo);
                return list;
            }
        };
        new MockUp<org.openo.sdno.framework.container.util.JsonUtil>() {

            @Mock
            public AuthInfo fromJson(String arg0, Class<AuthInfo> arg1) {
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                return info;
            }
        };
        new MockUp<EntityUtils>() {

            @Mock
            public String toString(final HttpEntity entity, final Charset defaultCharset)
                    throws IOException, ParseException {
                return "{\"token\": {\"catalog\": [{\"type\":\"network\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}, {\"type\":\"type\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]},{\"type\":\"network\",\"endpoints\":[{\"interface\":\"publics\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}]}}";
            }
        };
        new MockUp<CloseableHttpClient>() {

            @Mock
            public CloseableHttpResponse execute(final HttpUriRequest request)
                    throws IOException, ClientProtocolException {
                CloseableHttpResponseMock resp = new CloseableHttpResponseMock();
                org.apache.http.message.BasicStatusLine bs = new org.apache.http.message.BasicStatusLine(
                        new ProtocolVersion("bgp", 2, 1), 200, "success");
                resp.setStatusLine(bs);
                return (CloseableHttpResponse) resp;
            }
        };
        new MockUp<DaoUtil<T>>() {

            @Mock
            public <T> void delete(Class<T> clazz, String uuid) throws ServiceException {
            }
        };

        SbiNeIpSec dcGwIpSecConnection = new SbiNeIpSec();
        dcGwIpSecConnection.setUuid("uuid!23");
        SbiIkePolicy ikePolicy = new SbiIkePolicy();
        ikePolicy.setLifeTime("7200");
        ikePolicy.setAuthAlgorithm("authAlgorithm");
        ikePolicy.setIkeVersion("ikeVersion");
        ikePolicy.setPfs("pfs");
        ikePolicy.setPsk("psk");
        dcGwIpSecConnection.setIkePolicy(ikePolicy);

        SbiIpSecPolicy ipSecPolicy = new SbiIpSecPolicy();
        ipSecPolicy.setLifeTime("7200");
        ipSecPolicy.setAuthAlgorithm("authAlgorithm");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        ipSecPolicy.setPfs("pfs");
        dcGwIpSecConnection.setIpSecPolicy(ipSecPolicy);

        dcGwIpSecConnection.setTenantId("tenantId");
        dcGwIpSecConnection.setActiveStatus("adminStatus");
        dcGwIpSecConnection.setPeerLanCidrs("123,123,342,34343");
        dcGwIpSecConnection.setPeerAddress("peerAddress");
        List<SbiNeIpSec> ipSecConnList = new ArrayList<SbiNeIpSec>();
        ipSecConnList.add(dcGwIpSecConnection);

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        ResultRsp<SbiNeIpSec> result = roaSource.deleteIpSec(request, "ctrlUuidParam", ipSecConnList);
        assertTrue(result.getHttpCode() == 200);

        ResultRsp<SbiNeIpSec> result1 = roaSource.deleteIpSec(request, "ctrlUuidParam", ipSecConnList);
        assertTrue(result1.getHttpCode() == 200);
    }

    @Test
    public void testDeleteIpSecException() throws ServiceException {

        SbiNeIpSec dcGwIpSecConnection = new SbiNeIpSec();
        dcGwIpSecConnection.setUuid("uuid!23");
        SbiIkePolicy ikePolicy = new SbiIkePolicy();
        ikePolicy.setLifeTime("7200");
        ikePolicy.setAuthAlgorithm("authAlgorithm");
        ikePolicy.setIkeVersion("ikeVersion");
        ikePolicy.setPfs("pfs");
        dcGwIpSecConnection.setIkePolicy(ikePolicy);

        SbiIpSecPolicy ipSecPolicy = new SbiIpSecPolicy();
        ipSecPolicy.setLifeTime("7200");
        ipSecPolicy.setAuthAlgorithm("authAlgorithm");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        ipSecPolicy.setPfs("pfs");
        ikePolicy.setPsk("psk");
        dcGwIpSecConnection.setIpSecPolicy(ipSecPolicy);

        dcGwIpSecConnection.setTenantId("tenantId");
        dcGwIpSecConnection.setActiveStatus("adminStatus");
        dcGwIpSecConnection.setPeerLanCidrs("123,123,342,34343");
        dcGwIpSecConnection.setPeerAddress("peerAddress");
        List<SbiNeIpSec> ipSecConnList = new ArrayList<SbiNeIpSec>();
        ipSecConnList.add(dcGwIpSecConnection);

        HttpServletRequest request = new MockHttpServletRequest();
        ResultRsp<SbiNeIpSec> result = roaSource.deleteIpSec(request, "ctrlUuidParam", ipSecConnList);
        assertTrue(result.getHttpCode() == 200);

        ResultRsp<SbiNeIpSec> result1 = roaSource.deleteIpSec(request, "ctrlUuidParam", ipSecConnList);
        assertTrue(result1.getHttpCode() == 200);
    }

    @Test
    public void testDeleteIpSecBranch() throws ServiceException {
        new MockUp<DaoUtil<T>>() {

            @Mock
            public <T> List<OverlayUnderlayMapping> getChildren(Class<T> clazz, String overlayId)
                    throws ServiceException {
                List<OverlayUnderlayMapping> list = new ArrayList<>();
                OverlayUnderlayMapping obj1 = new OverlayUnderlayMapping();
                OverlayUnderlayMapping obj2 = new OverlayUnderlayMapping();
                OverlayUnderlayMapping obj3 = new OverlayUnderlayMapping();
                OverlayUnderlayMapping obj4 = new OverlayUnderlayMapping();
                OverlayUnderlayMapping obj5 = new OverlayUnderlayMapping();
                obj1.setUnderlayType("vpnIkePolicyId");
                obj2.setUnderlayType("vpnIpSecPolicyId");
                obj3.setUnderlayType("vpnServiceId");
                obj4.setUnderlayType("vpnIpSecSiteConnectionId");
                obj5.setUnderlayType("extra");
                obj1.setAction("action");
                obj2.setAction("action");
                obj3.setAction("action");
                obj4.setAction("action");
                obj5.setAction("action");
                return list;
            }
        };
        new MockUp<ControllerDao>() {

            @Mock
            public ControllerMO getController(String uuid) {
                ControllerMO mo = new ControllerMO();
                mo.setHostName("abc.xyz");
                mo.setDescription("test123");

                return mo;
            }
        };
        new MockUp<CommParamDao>() {

            @Mock
            public List<CommParamMO> getCommParam(String uuid) {
                List<CommParamMO> list = new ArrayList<CommParamMO>();
                CommParamMO mo = new CommParamMO();
                mo.setHostName("test");
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                mo.setAuthInfo(info);
                list.add(mo);
                return list;
            }
        };
        new MockUp<org.openo.sdno.framework.container.util.JsonUtil>() {

            @Mock
            public AuthInfo fromJson(String arg0, Class<AuthInfo> arg1) {
                AuthInfo info = new AuthInfo();
                info.setPassword("pwd");
                info.setPort("123");
                info.setUserName("uname");
                return info;
            }
        };
        new MockUp<EntityUtils>() {

            @Mock
            public String toString(final HttpEntity entity, final Charset defaultCharset)
                    throws IOException, ParseException {
                return "{\"token\": {\"catalog\": [{\"type\":\"network\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}, {\"type\":\"type\",\"endpoints\":[{\"interface\":\"public\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]},{\"type\":\"network\",\"endpoints\":[{\"interface\":\"publics\", \"url\":\"huawai.com\", \"region_id\":\"123\"}]}]}}";
            }
        };
        new MockUp<CloseableHttpClient>() {

            @Mock
            public CloseableHttpResponse execute(final HttpUriRequest request)
                    throws IOException, ClientProtocolException {
                CloseableHttpResponseMock resp = new CloseableHttpResponseMock();
                org.apache.http.message.BasicStatusLine bs = new org.apache.http.message.BasicStatusLine(
                        new ProtocolVersion("bgp", 2, 1), 200, "success");
                resp.setStatusLine(bs);
                return (CloseableHttpResponse) resp;
            }
        };
        new MockUp<DaoUtil<T>>() {

            @Mock
            public <T> void delete(Class<T> clazz, String uuid) throws ServiceException {
            }
        };

        SbiNeIpSec dcGwIpSecConnection = new SbiNeIpSec();
        dcGwIpSecConnection.setUuid("uuid!23");
        SbiIkePolicy ikePolicy = new SbiIkePolicy();
        ikePolicy.setLifeTime("7200");
        ikePolicy.setAuthAlgorithm("authAlgorithm");
        ikePolicy.setIkeVersion("ikeVersion");
        ikePolicy.setPfs("pfs");
        ikePolicy.setPsk("psk");
        dcGwIpSecConnection.setIkePolicy(ikePolicy);

        SbiIpSecPolicy ipSecPolicy = new SbiIpSecPolicy();
        ipSecPolicy.setLifeTime("7200");
        ipSecPolicy.setAuthAlgorithm("authAlgorithm");
        ipSecPolicy.setEncapsulationMode("encapsulationMode");
        ipSecPolicy.setTransformProtocol("transformProtocol");
        ipSecPolicy.setPfs("pfs");
        dcGwIpSecConnection.setIpSecPolicy(ipSecPolicy);

        dcGwIpSecConnection.setTenantId("tenantId");
        dcGwIpSecConnection.setActiveStatus("adminStatus");
        dcGwIpSecConnection.setPeerLanCidrs("123,123,342,34343");
        dcGwIpSecConnection.setPeerAddress("peerAddress");
        List<SbiNeIpSec> ipSecConnList = new ArrayList<SbiNeIpSec>();
        ipSecConnList.add(dcGwIpSecConnection);

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        ResultRsp<SbiNeIpSec> result = roaSource.deleteIpSec(request, "ctrlUuidParam", ipSecConnList);
        assertTrue(result.getHttpCode() == 200);
    }

}
