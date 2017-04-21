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

package org.openo.sdnhub.osdriverservice.openstack.client.http;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openo.sdnhub.osdriverservice.openstack.client.http.HttpInput;
import org.openo.sdnhub.osdriverservice.openstack.client.http.HttpResult;

/**
 * HTTP test.<br>
 *
 * @author
 * @version SDNHUB 0.5 September 20, 2016
 */
public class HttpTest {

    @Test
    public void HttpInputTest() {
        HttpInput obj = new HttpInput();
        Map<String, String> reqHeaders = new HashMap<String, String>();
        reqHeaders.put("key1", "10");

        obj.setReqHeaders(reqHeaders);
        assertTrue(reqHeaders.equals(obj.getReqHeaders()));

    }

    @Test
    public void HttpResultTest() {
        HttpResult obj = new HttpResult();
        Map<String, String> respHeaders = new HashMap<String, String>();
        respHeaders.put("key1", "10");

        obj.setRespHeaders(respHeaders);
        assertTrue(respHeaders.equals(obj.getRespHeaders()));

        obj.toString();

    }
}
