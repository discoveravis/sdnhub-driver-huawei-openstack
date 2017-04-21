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

package org.openo.sdnhub.osdriverservice.openstack.utils;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.osdriverservice.openstack.client.http.HttpInput;
import org.openo.sdnhub.osdriverservice.openstack.client.http.HttpResult;
import org.openo.sdnhub.osdriverservice.sbi.endtoend.EndToEndSbiTest;

public class HttpGateKeeperTest {

    @Test
    public void test() throws ServiceException {
        assertTrue(true);
    }

    @Test
    public void generateMockFilesTest3()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        try {
            HttpGateKeeper.setEnable(true);
            HttpGateKeeper.setTargetDir(
                    "D:\\workspace\\OSDriver\\service\\src\\integration-test\\resources\\openstack_api_jsons");
            Map<String, String> map = new HashMap<>();
            map.put("172.241.0.101", "localhost");
            HttpGateKeeper.clean();
            HttpGateKeeper.generateMockFiles(map);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception is thrown");
        }
    }

    @Test(expected = Exception.class)
    public void generateMockFilesTest()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = HttpGateKeeper.class.getDeclaredField("records");
        field.setAccessible(true);
        Map records = new HashMap<>();
        HttpInput ip = new HttpInput();
        ip.setUri("http://localhost:8080");
        ip.setBody("{\"id\":123}");
        ip.setMethod("post");

        HttpResult res = new HttpResult();
        res.setBody("123");
        res.setStatus(200);
        records.put(ip, res);
        field.set(null, records);
        HttpGateKeeper.setEnable(true);
        HttpGateKeeper.setTargetDir(
                "D:\\workspace\\OSDriver\\service\\src\\integration-test\\resources\\openstack_api_jsons");
        Map<String, String> map = new HashMap<>();
        map.put("172.241.0.101", "localhost");
        HttpGateKeeper.generateMockFiles(map);
    }

    @Test(expected = Exception.class)
    public void generateMockFilesTest1()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = HttpGateKeeper.class.getDeclaredField("records");
        field.setAccessible(true);
        Map records = new HashMap<>();
        HttpInput ip = new HttpInput();
        ip.setUri("//");
        ip.setBody("{\"id\":123}");
        ip.setMethod("post");

        HttpResult res = new HttpResult();
        res.setBody("123");
        res.setStatus(200);
        records.put(ip, res);
        field.set(null, records);
        HttpGateKeeper.setEnable(true);
        HttpGateKeeper.setTargetDir(
                "D:\\workspace\\OSDriver\\service\\src\\integration-test\\resources\\openstack_api_jsons");
        Map<String, String> map = new HashMap<>();
        map.put("172.241.0.101", "localhost");
        HttpGateKeeper.generateMockFiles(map);
    }

    @Test
    public void checkcleanupMockFiles() {
        try {
            HttpGateKeeper.cleanupMockFiles();
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception is thrown");
        }
    }
}
