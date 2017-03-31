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

package org.openo.sdnhub.osdriverservice.dao;

import static org.junit.Assert.assertEquals;

import mockit.Mock;
import mockit.MockUp;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.sdnhub.osdriverservice.openstack.client.OpenStackCredentials;
import org.openo.sdnhub.osdriverservice.openstack.utils.JsonUtil;
import org.openo.sdnhub.osdriverservice.util.OSDriverConfig;
import org.openo.sdno.framework.container.resthelper.RestfulProxy;
import org.openo.sdno.overlayvpn.brs.invdao.CommParamDao;
import org.openo.sdno.overlayvpn.brs.invdao.ControllerDao;
import org.openo.sdno.overlayvpn.brs.model.AuthInfo;
import org.openo.sdno.overlayvpn.brs.model.CommParamMO;
import org.openo.sdno.overlayvpn.brs.model.ControllerMO;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DaoBrsTest {

    DaoBrs<T> dbs = new DaoBrs<>();

    @Test(expected = ServiceException.class)
    public void testGetOpenStackCredentials() throws ServiceException {

        new MockUp<RestfulProxy>() {

            @Mock
            RestfulResponse get(String url, RestfulParametes restParametes)
                    throws ServiceException, MalformedURLException {
                RestfulResponse res = new RestfulResponse();
                res.setStatus(200);
                Map<String, Object> driverInfoMap = new HashMap<>();
                driverInfoMap.put("instanceID", "usb12345");
                driverInfoMap.put("domain", "java");
                driverInfoMap.put("ip", "12.32.30");
                driverInfoMap.put("url", "https://www.google.com:8082");
                driverInfoMap.put("userName", "Raju");
                driverInfoMap.put("password", "raju");
                res.setResponseJson(JsonUtil.toJson(driverInfoMap));
                return res;
            }
        };

        dbs.getOpenStackCredentials("ihuihi15454");

    }

    @Test
    public void testGetOpenStackCredentialsNull() throws ServiceException {

        new MockUp<RestfulProxy>() {

            @Mock
            RestfulResponse get(String url, RestfulParametes restParametes)
                    throws ServiceException, MalformedURLException {
                RestfulResponse res = new RestfulResponse();
                res.setStatus(200);
                Map<String, Object> driverInfoMap = new HashMap<>();
                driverInfoMap.put("instanceID", "usb12345");
                driverInfoMap.put("domain", "java");
                driverInfoMap.put("ip", "12.32.30");
                driverInfoMap.put("url", "https://www.google.com:8082");
                driverInfoMap.put("userName", "Raju");
                driverInfoMap.put("password", "raju");
                res.setResponseJson(JsonUtil.toJson(driverInfoMap));
                return res;
            }
        };

        new MockUp<OSDriverConfig>() {

            @Mock
            public boolean isEsrEnabled() {

                return false;
            }

        };
        new MockUp<ControllerDao>() {

            @Mock
            public ControllerMO getController(String uuid) throws ServiceException {

                ControllerMO cmo = new ControllerMO();
                cmo.setDescription("description");
                cmo.setObjectId("1646objectId");
                cmo.setHostName("hostName");
                return cmo;
            }

        };
        new MockUp<CommParamDao>() {

            @Mock
            public List<CommParamMO> getCommParam (String controllerID)
                    throws ServiceException {

                CommParamMO cmo = new CommParamMO();
                cmo.setDescription("description");
                cmo.setObjectId("1646objectId");
                AuthInfo info = new AuthInfo();
                info.setPassword("password");
                info.setPort("1220");
                Map<String, String> extra = new HashMap<>();
                extra.put("domain", "java");
                info.setExtra(extra);
                cmo.setAuthInfo(info);
                cmo.setHostName("hostName");
                List<CommParamMO> l1 = new LinkedList<>();
                l1.add(cmo);
                return l1;
            }

        };

        OpenStackCredentials response = dbs.getOpenStackCredentials("ihuihi15454");
        assertEquals(response.getPassword(), "password");

    }

    @Test
    public void testGetOpenStackRegion() throws ServiceException {

        new MockUp<RestfulProxy>() {

            @Mock
            RestfulResponse get(String url, RestfulParametes restParametes)
                    throws ServiceException, MalformedURLException {
                RestfulResponse res = new RestfulResponse();
                res.setStatus(200);
                Map<String, Object> driverInfoMap = new HashMap<>();
                driverInfoMap.put("instanceID", "usb12345");
                driverInfoMap.put("ip", "12.32.30");
                driverInfoMap.put("url", "http://www.google.com:8082");
                driverInfoMap.put("userName", "Raju");
                driverInfoMap.put("password", "raju");
                driverInfoMap.put("region", "bngl");
                res.setResponseJson(JsonUtil.toJson(driverInfoMap));
                return res;
            }
        };

        String response = dbs.getOpenStackRegion("jhsdjfhdji544");
        assertEquals(response, null);
    }

    @Test
    public void testGetOpenStackRegionNotNull() throws ServiceException {

        new MockUp<RestfulProxy>() {

            @Mock
            RestfulResponse get(String url, RestfulParametes restParametes)
                    throws ServiceException, MalformedURLException {

                RestfulResponse res = new RestfulResponse();
                res.setStatus(200);
                Map<String, Object> driverInfoMap = new HashMap<>();
                driverInfoMap.put("instanceID", "usb12345");
                driverInfoMap.put("ip", "12.32.30");
                driverInfoMap.put("url", "http://www.google.com:8082");
                driverInfoMap.put("userName", "Raju");
                driverInfoMap.put("password", "raju");
                res.setResponseJson(JsonUtil.toJson(driverInfoMap));
                return res;
            }
        };

        new MockUp<OSDriverConfig>() {

            @Mock
            public boolean isEsrEnabled() {

                return false;
            }

        };

        new MockUp<CommParamDao>() {

            @Mock
            public List<CommParamMO> getCommParam(String controllerID)
                    throws ServiceException {

                CommParamMO cmo = new CommParamMO();
                cmo.setDescription("description");
                cmo.setObjectId("1646objectId");
                AuthInfo info = new AuthInfo();
                info.setPassword("password");
                info.setPort("1220");
                Map<String, String> extra = new HashMap<>();
                extra.put("domain", "java");
                info.setExtra(extra);
                cmo.setAuthInfo(info);
                cmo.setHostName("hostName");
                List<CommParamMO> l1 = new LinkedList<>();
                l1.add(cmo);
                return l1;
            }

        };

        String response = dbs.getOpenStackRegion("kjdjer6454");
        assertEquals(response, null);
    }

}
