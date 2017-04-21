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

package org.openo.sdnhub.osdriverservice.openstack.client.model.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class DpdActionTest {

    @Test
    public void testGetName() {
        String value = DpdAction.CLEAR.getName();
        assertEquals(value, "clear");
    }

    @Test
    public void testGetNameForFour() {
        String value = DpdAction.RESTART_BY_PEER.getName();
        assertEquals(value, "restart-by-peer");
    }

    /*@Test
    public void testGetNameForNull() {
        for (DpdAction plugins : DpdAction.values()) {
            String value = plugins.getName();
            assertEquals(value , "clear");
        }
    }*/
}
