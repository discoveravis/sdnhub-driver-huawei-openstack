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

package org.openo.sdnhub.osdriverservice.openstack.utils;

import static org.junit.Assert.assertTrue;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;
import org.openo.sdnhub.osdriverservice.openstack.client.exception.OpenStackException;
import org.openo.sdnhub.osdriverservice.openstack.utils.JsonUtil;

import mockit.Mock;
import mockit.MockUp;

/**
 * JSON Utility tests.<br>
 *
 * @author
 * @version SDNHUB 0.5 September 20, 2016
 */
public class JsonUtilTest {

    @Test
    public void toJsonTest() {
        Object obj = new Object();

        new MockUp<JsonUtil>() {

            @Mock
            public String toJson(Object obj, boolean considerRootName) throws OpenStackException {
                return "Success";
            }
        };
        String str = JsonUtil.toJson(obj);
        assertTrue(str != null);
    }

    @Test
    public void toJsonTestTrueCase() {
        Object obj = new Object();

        new MockUp<ObjectMapper>() {

            @Mock
            public String writeValueAsString(Object value) throws OpenStackException {
                return null;
            }
        };

        boolean val;
        val = true;
        String str = JsonUtil.toJson(obj, val);
        assertTrue(str == null);
    }

    @Test(expected = Exception.class)
    public void toJsonTestFalseCase() {
        Object obj = new Object();

        new MockUp<ObjectMapper>() {

            @Mock
            public ObjectMapper disable(SerializationConfig.Feature... f) throws OpenStackException {
                return null;
            }
        };

        boolean val;
        val = false;
        JsonUtil.toJson(obj, val);
    }
}
