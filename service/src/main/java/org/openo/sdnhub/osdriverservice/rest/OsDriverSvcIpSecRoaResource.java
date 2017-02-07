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

package org.openo.sdnhub.osdriverservice.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.osdriverservice.nbi.IpSecNbiService;
import org.openo.sdnhub.osdriverservice.openstack.client.model.enums.IpSecVpnStatus;
import org.openo.sdnhub.osdriverservice.sbi.model.OsIpSec;
import org.openo.sdnhub.osdriverservice.util.MigrateModelUtil;
import org.openo.sdno.overlayvpn.errorcode.ErrorCode;
import org.openo.sdno.overlayvpn.model.netmodel.ipsec.DcGwIpSecConnection;
import org.openo.sdno.overlayvpn.result.ResultRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Restful interface class for OS driver.<br>
 *
 * @author
 * @version SDNHUB 0.5 2016-6-23
 */
@Service
@Path("/sbi-ipsec/v1")
public class OsDriverSvcIpSecRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsDriverSvcIpSecRoaResource.class);

    IpSecNbiService service = new IpSecNbiService();

    /**
     * Create IpSec connection batch.<br>
     *
     * @param request HttpServletRequest Object
     * @param ctrlUuidParam Controller Id Parameter
     * @param dcGwIpSecConnList List of IpSec connections
     * @return ResultRsp object with IpSec connection list data
     * @throws ServiceException when create IpSec connection failed
     * @since SDNHUB 0.5
     */
    @POST
    @Path("/dc-gateway/batch-create-ipsecs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultRsp<List<DcGwIpSecConnection>> createIpSec(@Context HttpServletRequest request,
            @HeaderParam("X-Driver-Parameter") String ctrlUuidParam, List<DcGwIpSecConnection> dcGwIpSecConnList)
            throws ServiceException {
        long enterTime = System.currentTimeMillis();
        String ctrlUuid = ctrlUuidParam.substring(ctrlUuidParam.indexOf('=') + 1);

        for(DcGwIpSecConnection dcGwIpSecConn : dcGwIpSecConnList) {
            // ValidationUtil.validateModel(dcGwIpSecConn);
            OsIpSec osIpSec = MigrateModelUtil.convert(dcGwIpSecConn);
            osIpSec = this.service.createIpSec(ctrlUuid, osIpSec);
            dcGwIpSecConn.setOperStatus(statusMap.get(osIpSec.getVpnIpSecSiteConnection().getStatus()));
        }

        LOGGER.info("Exit create method. cost time = " + (System.currentTimeMillis() - enterTime));

        return new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS, dcGwIpSecConnList);
    }

    /**
     * Create IpSec connection.<br>
     *
     * @param request HttpServletRequest Object
     * @param ctrlUuidParam Controller Id Parameter
     * @param dcGwIpSecConnList List of IpSec connections
     * @return ResultRsp object with IpSec connection list data
     * @throws ServiceException when create IpSec connection failed
     * @since SDNHUB 0.5
     */
    @POST
    @Path("/dc-gateway/ipsec-connections")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultRsp<DcGwIpSecConnection> createIpSec(@Context HttpServletRequest request,
            @HeaderParam("X-Driver-Parameter") String ctrlUuidParam, DcGwIpSecConnection dcGwIpSecConn)
            throws ServiceException {
        List<DcGwIpSecConnection> ipsecs = new ArrayList<>();
        ipsecs.add(dcGwIpSecConn);
        ResultRsp<List<DcGwIpSecConnection>> result = this.createIpSec(
                request, ctrlUuidParam, ipsecs);

        return new ResultRsp<>(ErrorCode.OVERLAYVPN_SUCCESS, result.getData().get(0));
    }

    /**
     * Delete IpSec connection.<br>
     *
     * @param request HttpServletRequest Object
     * @param ctrlUuidParam Controller Id Parameter
     * @param ipSecConnId IpSec connection id
     * @return ResultRsp object with deleting result data
     * @throws ServiceException when delete IpSec connection failed
     * @since SDNHUB 0.5
     */
    @DELETE
    @Path("/dc-gateway/ipsecs/{ipsecconnectionid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultRsp<String> deleteIpSec(@Context HttpServletRequest request,
            @HeaderParam("X-Driver-Parameter") String ctrlUuidParam, @PathParam("ipsecconnectionid") String ipSecConnId)
            throws ServiceException {
        long enterTime = System.currentTimeMillis();
        String ctrlUuid = ctrlUuidParam.substring(ctrlUuidParam.indexOf('=') + 1);

        this.service.deleteIpSec(ctrlUuid, ipSecConnId);

        LOGGER.info("Exit delete method. cost time = " + (System.currentTimeMillis() - enterTime));

        return new ResultRsp<>();
    }

    /**
     * Delete IpSec connection.<br>
     *
     * @param request HttpServletRequest Object
     * @param ctrlUuidParam Controller Id Parameter
     * @param ipSecConnIds IpSec connection ids
     * @return ResultRsp object with deleting result data
     * @throws ServiceException when delete IpSec connection failed
     * @since SDNHUB 0.5
     */
    @DELETE
    @Path("/dc-gateway/ipsec-connections/batch-delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResultRsp<String> deleteIpSec(@Context HttpServletRequest request,
            @HeaderParam("X-Driver-Parameter") String ctrlUuidParam, @PathParam("ipsecconnectionid") List<String> ipSecConnIds)
            throws ServiceException {
        long enterTime = System.currentTimeMillis();
        String ctrlUuid = ctrlUuidParam.substring(ctrlUuidParam.indexOf('=') + 1);

        for (String id: ipSecConnIds) {
            this.service.deleteIpSec(ctrlUuid, id);
        }

        LOGGER.info("Exit delete method. cost time = " + (System.currentTimeMillis() - enterTime));

        return new ResultRsp<>();
    }

    private static Map<String, String> statusMap = new HashMap<>();
    static {
        statusMap.put(IpSecVpnStatus.ACTIVE.getName(), "up");
        statusMap.put(IpSecVpnStatus.DOWN.getName(), "down");
        statusMap.put(IpSecVpnStatus.PENDING_CREATE.getName(), "down");
        statusMap.put(IpSecVpnStatus.ERROR.getName(), "none");
    }
}
