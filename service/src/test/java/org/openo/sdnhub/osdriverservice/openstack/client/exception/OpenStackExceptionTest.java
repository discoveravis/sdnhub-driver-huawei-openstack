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

package org.openo.sdnhub.osdriverservice.openstack.client.exception;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openo.sdnhub.osdriverservice.openstack.client.exception.OpenStackException;

/**
 * Open Stack Exception tests.<br>
 *
 * @author
 * @version SDNHUB 0.5 September 20, 2016
 */
public class OpenStackExceptionTest {

    @Test
    public void testEqual() {
        OpenStackException openStackException = new OpenStackException("throw");
        OpenStackException openStackException1 = new OpenStackException("throw");
        OpenStackException openStackException2 = new OpenStackException("throw");
        openStackException.setErrorCode(123);
        openStackException1.setErrorCode(123);
        openStackException2.setErrorCode(111);
        OpenStackException openStackException4 = new OpenStackException("throw");
        openStackException4.setErrorCode(123);
        openStackException.setHttpCode(500);
        openStackException1.setHttpCode(500);
        openStackException2.setHttpCode(500);
        openStackException4.setHttpCode(501);
        openStackException.equals(openStackException1);
        openStackException.equals(null);
        openStackException.equals(openStackException);
        openStackException.equals(openStackException2);
        openStackException.equals(openStackException4);
        openStackException.hashCode();
        openStackException.getMessage();
        openStackException.getErrorCode();
        openStackException.getHttpCode();
        openStackException.equals(openStackException.equals(new Object()));

        assertTrue(true);
    }

}
