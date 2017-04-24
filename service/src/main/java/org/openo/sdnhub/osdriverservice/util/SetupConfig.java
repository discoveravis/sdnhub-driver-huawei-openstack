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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openo.sdnhub.osdriverservice.openstack.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>
 * <p>
 * </p>
 *
 * @author
 * @version SDNOHUB 0.5 19-Apr-2017
 */
public class SetupConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetupConfig.class);
    private static final String FILE = "openstack_setup.json";

    /**
     *
     * <br/>
     *
     * @param osName
     * @param version
     * @return
     * @since  SDNOHUB 0.5
     */
    public static Map<String, String> getOsSetup(String osName, String version) {
        try {

            File file = new File(SetupConfig.class.getClassLoader().getResource(FILE).getFile());
            Map<String,List<Map<String,String>>> setupMap = JsonUtil.fromJson(getFileContent(file), Map.class);

            List<Map<String,String>> setups = setupMap.get("openstack_setup");
            for(Map<String,String> setup : setups) {
                if(osName.equals(setup.get("name")) && version.equals(setup.get("version"))) {
                    return setup;
                }
            }
        } catch(Exception ex) {
            LOGGER.error("Exception in getting os setup",ex);
        }
        return Collections.emptyMap();
    }

    private static String getFileContent(File file){

        StringBuffer content = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String sCurrentLine;
            while (( sCurrentLine = br.readLine()) != null) {
                content.append(sCurrentLine);
            }

        } catch (IOException e) {
            LOGGER.error("Exception in getting os setup",e);
        }
        return content.toString();
    }

}
