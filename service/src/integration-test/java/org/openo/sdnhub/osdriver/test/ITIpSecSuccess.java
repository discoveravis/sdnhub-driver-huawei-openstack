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

package org.openo.sdnhub.osdriver.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.osdriver.mockserver.GenericMockServer;
import org.openo.sdno.testframework.checker.IChecker;
import org.openo.sdno.testframework.http.model.HttpModelUtils;
import org.openo.sdno.testframework.http.model.HttpRequest;
import org.openo.sdno.testframework.http.model.HttpResponse;
import org.openo.sdno.testframework.http.model.HttpRquestResponse;
import org.openo.sdno.testframework.testmanager.TestManager;

public class ITIpSecSuccess extends TestManager {

    private static final String CREATE_IPSEC_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase_jsons/createIpSecV2.json";
    private static final String DELETE_IPSEC_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase_jsons/deleteIpSecV2.json";
    private static final String UPDATE_IPSEC_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase_jsons/updateIpSecV2.json";


    private static String[] mockJsonsOpenStack =
            new String[]
                    {
                    "src/integration-test/resources/openstack_mock_jsons/project_create.json",
                    "src/integration-test/resources/openstack_mock_jsons/project_list.json",
                    "src/integration-test/resources/openstack_mock_jsons/project_delete.json",
                    "src/integration-test/resources/openstack_mock_jsons/project_create.json",
                    "src/integration-test/resources/openstack_mock_jsons/network_list.json",
                    "src/integration-test/resources/openstack_mock_jsons/network_delete.json",
                    "src/integration-test/resources/openstack_mock_jsons/network_create.json",
                    "src/integration-test/resources/openstack_mock_jsons/router_list.json",
                    "src/integration-test/resources/openstack_mock_jsons/router_delete.json",
                    "src/integration-test/resources/openstack_mock_jsons/router_create.json",
                    "src/integration-test/resources/openstack_mock_jsons/subnet_delete.json",
                    "src/integration-test/resources/openstack_mock_jsons/subnet_create.json",
                    "src/integration-test/resources/openstack_mock_jsons/subnet_attach.json",
                    "src/integration-test/resources/openstack_mock_jsons/subnet_detach.json",
                    "src/integration-test/resources/openstack_mock_jsons/createikepolicy.json",
                    "src/integration-test/resources/openstack_mock_jsons/createipsecpolicy.json",
                    "src/integration-test/resources/openstack_mock_jsons/createvpnservice.json",
                    "src/integration-test/resources/openstack_mock_jsons/createipsecconn.json",
                    "src/integration-test/resources/openstack_mock_jsons/createikepolicy.json",
                    "src/integration-test/resources/openstack_mock_jsons/deleteipsecpolicy.json",
                    "src/integration-test/resources/openstack_mock_jsons/deleteikepolicy.json",
                    "src/integration-test/resources/openstack_mock_jsons/deletevpnservice.json",
                    "src/integration-test/resources/openstack_mock_jsons/deleteipsecconn.json",
                    "src/integration-test/resources/openstack_mock_jsons/auth_token.json",
                    "src/integration-test/resources/openstack_mock_jsons/auth_projects.json",
                    "src/test/resources/openstack_mock_jsons/floatingip_create.json",
                    "src/integration-test/resources/openstack_mock_jsons/subnet_list2.json",
                    "src/integration-test/resources/openstack_mock_jsons/updateipsecconn.json"

    };

    private static String[] mockJsonsEsrs =
            new String[] {"src/integration-test/resources/esr_mock_jsons/esr_controller.json"};

    private static String[] mockJsonsBrs =
            new String[] {"src/integration-test/resources/brs_mock_jsons/create_object.json",
                            "src/integration-test/resources/brs_mock_jsons/delete_object.json",
                            "src/integration-test/resources/brs_mock_jsons/query_object.json"};

    private static GenericMockServer mocoServer = new GenericMockServer();

    @BeforeClass
    public static void setup() throws ServiceException {
        mocoServer.addMockJsons(mockJsonsOpenStack);
        mocoServer.addMockJsons(mockJsonsEsrs);
        mocoServer.addMockJsons(mockJsonsBrs);
        mocoServer.start();
    }

    @AfterClass
    public static void tearDown() throws ServiceException {
        mocoServer.stop();
    }

    @Test
    public void checkIpsecCreate() throws ServiceException {
        HttpRquestResponse httpCreateObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_IPSEC_SUCCESS_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();
        execTestCase(createRequest, new SuccessChecker());
    }

    @Test
    public void checkIpsecDelete() throws ServiceException {
        HttpRquestResponse httpCreateObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_IPSEC_SUCCESS_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();
        execTestCase(createRequest, new SuccessChecker());
    }

    @Test
    public void checkIpsecUpdate() throws ServiceException {
        HttpRquestResponse httpCreateObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(UPDATE_IPSEC_SUCCESS_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();
        execTestCase(createRequest, new SuccessChecker());
    }

    private class SuccessChecker implements IChecker {

        @Override
        public boolean check(HttpResponse response) {
            if(response.getStatus() >= 200 && response.getStatus() <= 204) {
                return true;
            }
            return false;
        }
    }
}
