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

package org.openo.sdnhub.osdriverservice.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenStack Network Configuration Operation Class.<br>
 *
 * @author
 * @version SDNHUB 0.5 August 9, 2016
 */
public class OSNetworkConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(OSDriverConfig.class);

    private static final String OS_NETWORK_CONFIG_FILE = "etc/OsNetworkConfig.properties";

    private Properties properties = new Properties();

    /**
     * Constructor.<br>
     *
     * @since SDNHUB 0.5
     */
    public OSNetworkConfig() {
        loadProperties();
    }

    /**
     * Get network type.<br>
     *
     * @return network type
     * @since SDNHUB 0.5
     */
    public String getNetworkType() {
        return properties.getProperty("networktype");
    }

    /**
     * Get physical network name.<br>
     *
     * @return physical network name
     * @since SDNHUB 0.5
     */
    public String getPhysicalNetwork() {
        return properties.getProperty("physicalnetwork");
    }

    /**
     * Get segment id.<br>
     *
     * @return segment id
     * @since SDNHUB 0.5
     */
    public String getSegmentId() {
        return properties.getProperty("segmentid");
    }

    /**
     * Get region Id
     */
    public String getRegionId() {
        return properties.getProperty("regionId");
    }

    private void loadProperties() {
        try {
            FileInputStream fin = new FileInputStream(OS_NETWORK_CONFIG_FILE);
            properties.load(fin);
        } catch(IOException e) {
            LOGGER.error("Load Os Network Property File failed!", e);
        }
    }
}
