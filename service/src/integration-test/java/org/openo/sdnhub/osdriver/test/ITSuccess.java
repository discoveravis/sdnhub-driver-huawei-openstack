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

public class ITSuccess extends TestManager {

    private static final String CREATE_VPC_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase_jsons/createvpcsuccess.json";

    private static final String CREATE_SUBNET_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase_jsons/createsubnetsuccess.json";

    private static final String DELETE_VPC_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase_jsons/deletevpcsuccess.json";

    private static final String DELETE_SUBNET_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase_jsons/deletesubnetsuccess.json";

    private static final String CREATE_IPSEC_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase_jsons/createipsecsuccess.json";

    private static final String DELETE_IPSEC_SUCCESS_TESTCASE =
            "src/integration-test/resources/testcase_jsons/deleteipsecsuccess.json";

    private static String[] mockJsonsOpenStack =
            new String[] {"src/integration-test/resources/openstack_mock_jsons/auth_token.json",
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
                    "src/integration-test/resources/openstack_mock_jsons/subnet_list.json",
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
                    "src/integration-test/resources/openstack_mock_jsons/auth_projects.json"};

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
    public void checkVpcCreate() throws ServiceException {
        HttpRquestResponse httpCreateObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_VPC_SUCCESS_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();
        execTestCase(createRequest, new SuccessChecker());
    }

    @Test
    public void checkVpcDelete() throws ServiceException {
        HttpRquestResponse deleteHttpObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_VPC_SUCCESS_TESTCASE);
        HttpRequest deleteRequest = deleteHttpObject.getRequest();
        execTestCase(deleteRequest, new SuccessChecker());
    }

    @Test
    public void checkSubnetCreate() throws ServiceException {
        HttpRquestResponse httpCreateObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_SUBNET_SUCCESS_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();
        execTestCase(createRequest, new SuccessChecker());
    }

    @Test
    public void checkSubnetDelete() throws ServiceException {
        HttpRquestResponse deleteHttpObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_SUBNET_SUCCESS_TESTCASE);
        HttpRequest deleteRequest = deleteHttpObject.getRequest();
        execTestCase(deleteRequest, new SuccessChecker());
    }

    @Test
    public void checkIpSecCreate() throws ServiceException {
        HttpRquestResponse httpCreateObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(CREATE_IPSEC_SUCCESS_TESTCASE);
        HttpRequest createRequest = httpCreateObject.getRequest();
        execTestCase(createRequest, new SuccessChecker());
    }

    @Test
    public void checkIpSecDelete() throws ServiceException {
        HttpRquestResponse deleteHttpObject =
                HttpModelUtils.praseHttpRquestResponseFromFile(DELETE_IPSEC_SUCCESS_TESTCASE);
        HttpRequest deleteRequest = deleteHttpObject.getRequest();
        execTestCase(deleteRequest, new SuccessChecker());
    }
    /*@Test
    public void testOsdriverSuccessOperations() throws ServiceException {

        try {
            //this.checkVpcCreate();
            this.checkSubnetCreate();
            //this.checkIpSecCreate();
            //this.checkIpSecDelete();
            //this.checkSubnetDelete();
            //this.checkVpcDelete();
        } finally {
        //NOSONAR
        }
    }*/

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
