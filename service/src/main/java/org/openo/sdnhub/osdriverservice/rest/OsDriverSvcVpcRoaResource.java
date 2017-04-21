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

package org.openo.sdnhub.osdriverservice.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.sdnhub.osdriverservice.nbi.VpcNbiService;
import org.openo.sdnhub.osdriverservice.sbi.model.OsSubnet;
import org.openo.sdnhub.osdriverservice.sbi.model.OsVpc;
import org.openo.sdnhub.osdriverservice.util.MigrateModelUtil;
import org.openo.sdno.overlayvpn.model.netmodel.vpc.Subnet;
import org.openo.sdno.overlayvpn.model.netmodel.vpc.Vpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.swagger.annotations.*;

/**
 * Restful interface class for OS driver VPC.<br>
 *
 * @author
 * @version SDNHUB 0.5 2016-6-23
 */
@Service
@Path("/sbi-vpc/v1")
@Api(value = "/vpcs")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJAXRSSpecServerCodegen")
public class OsDriverSvcVpcRoaResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsDriverSvcVpcRoaResource.class);

    private static final String STR = "Start. ";

    private static final String EXIT_LOG = "Exit. cost time = ";

    VpcNbiService service = new VpcNbiService();

    /**
     * Create VPC.<br>
     *
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param ctrlUuidParam Controller Id Parameter
     * @param vpc VPC Object
     * @return VPC Object created
     * @throws ServiceException when create VPC failed
     * @since SDNHUB 0.5
     */
    @POST
    @Path("/vpcs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create VPC .", notes = "Create VPC.", response = void.class, tags = {})
    @ApiResponses(value = {
                    @ApiResponse(code = 201, message = "VPC creation success.", response = Vpc.class),
                    @ApiResponse(code = 400, message = "Vpc resource provided in body is missing with required properties.", response = void.class),
                    @ApiResponse(code = 401, message = "Unauthorized.", response = void.class),
                    @ApiResponse(code = 404, message = "Not found.", response = void.class),
                    @ApiResponse(code = 500, message = "internal server error.", response = void.class)})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "vpc", value = "VPC to be created.", required = true,  paramType = "body", dataType = "org.openo.sdno.overlayvpn.model.netmodel.vpc.Vpc")})
    public Vpc createVpc(@Context HttpServletRequest request, @Context HttpServletResponse response,
            @HeaderParam("X-Driver-Parameter") String ctrlUuidParam, Vpc vpc) throws ServiceException {

        LOGGER.debug(STR);

        long infterEnterTime = System.currentTimeMillis();
        String ctrlUuid = ctrlUuidParam.substring(ctrlUuidParam.indexOf('=') + 1);

        OsVpc osVpc = MigrateModelUtil.convert(vpc);
        osVpc = this.service.createVpc(ctrlUuid, osVpc);
        vpc.setExternalIp(osVpc.getGatewayIp());
        vpc.setAttributes(new Vpc.UnderlayResources());
        vpc.getAttributes().setRouterId(osVpc.getAttributes().getRouterId());
        vpc.getAttributes().setProjectId(osVpc.getAttributes().getProjectId());
        if(response != null) {
            response.setStatus(HttpStatus.SC_CREATED);
        }

        LOGGER.debug(EXIT_LOG + (System.currentTimeMillis() - infterEnterTime));

        return vpc;
    }

    /**
     * Delete VPC.<br>
     *
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param ctrlUuidParam Controller Id Parameter
     * @param vpcId VPC Id
     * @throws ServiceException when delete VPC failed
     * @since SDNHUB 0.5
     */
    @DELETE
    @Path("/vpcs/{vpc_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete VPC .", notes = "Delete VPC.", tags = {})
    @ApiResponses(value = {
                    @ApiResponse(code = 204, message = "VPC deleted successfully.", response = void.class),
                    @ApiResponse(code = 500, message = "internal server error.", response = void.class)})
    public void deleteVpc(@Context HttpServletRequest request, @Context HttpServletResponse response,
            @HeaderParam("X-Driver-Parameter") String ctrlUuidParam, @PathParam("vpc_id") String vpcId)
            throws ServiceException {

        LOGGER.debug(STR);

        long infterEnterTime = System.currentTimeMillis();
        String ctrlUuid = ctrlUuidParam.substring(ctrlUuidParam.indexOf('=') + 1);

        this.service.deleteVpc(ctrlUuid, vpcId);

        if(response != null) {
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        }
        LOGGER.info(EXIT_LOG + (System.currentTimeMillis() - infterEnterTime));
    }

    /**
     * Create Subnet.<br>
     *
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param ctrlUuidParam Controller Id Parameter
     * @param subnet Subnet Object
     * @return Subnet Object created
     * @throws ServiceException when create Subnet failed
     * @since SDNHUB 0.5
     */
    @POST
    @Path("/subnets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create Subnet .", notes = "Create Subnet.", response = void.class, tags = {})
    @ApiResponses(value = {
                    @ApiResponse(code = 201, message = "Subnet creation success.", response = Subnet.class),
                    @ApiResponse(code = 400, message = "Subnet resource provided in body is missing with required properties.", response = void.class),
                    @ApiResponse(code = 401, message = "Unauthorized.", response = void.class),
                    @ApiResponse(code = 404, message = "Not found.", response = void.class),
                    @ApiResponse(code = 500, message = "internal server error.", response = void.class)})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "subnet", value = "Subnet to be created.", required = true,  paramType = "body", dataType = "org.openo.sdno.overlayvpn.model.netmodel.vpc.Subnet")})
    public Subnet createSubnet(@Context HttpServletRequest request, @Context HttpServletResponse response,
            @HeaderParam("X-Driver-Parameter") String ctrlUuidParam, Subnet subnet) throws ServiceException {

        LOGGER.debug(STR);

        long infterEnterTime = System.currentTimeMillis();
        String ctrlUuid = ctrlUuidParam.substring(ctrlUuidParam.indexOf('=') + 1);

        OsSubnet osSubnet = MigrateModelUtil.convert(subnet);
        osSubnet = this.service.createSubnet(ctrlUuid, subnet.getVpcId(), osSubnet);
        subnet.setAdminStatus(osSubnet.getAdminStatus());
        subnet.setGatewayIp(osSubnet.getGatewayIp());
        subnet.setAttributes(new Subnet.UnderlayResources());
        subnet.getAttributes().setNetworkId(osSubnet.getAttributes().getVpcNetworkId());
        subnet.getAttributes().setSubnetId(osSubnet.getAttributes().getVpcSubnetId());
        if(response != null) {
            response.setStatus(HttpStatus.SC_CREATED);
        }

        LOGGER.debug(EXIT_LOG + (System.currentTimeMillis() - infterEnterTime));

        return subnet;
    }

    /**
     * Delete Subnet<br>
     *
     * @param request HttpServletRequest Object
     * @param response HttpServletResponse Object
     * @param ctrlUuidParam Controller Id Parameter
     * @param subnetId Subnet Id
     * @throws ServiceException when delete Subnet failed
     * @since SDNHUB 0.5
     */
    @DELETE
    @Path("/subnets/{subnet_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete VPC .", notes = "Delete VPC.", tags = {})
    @ApiResponses(value = {
                    @ApiResponse(code = 204, message = "VPC creation success.", response = void.class),
                    @ApiResponse(code = 500, message = "internal server error.", response = void.class)})
    public void deleteSubnet(@Context HttpServletRequest request, @Context HttpServletResponse response,
            @HeaderParam("X-Driver-Parameter") String ctrlUuidParam, @PathParam("subnet_id") String subnetId)
            throws ServiceException {

        LOGGER.debug(STR);

        long infterEnterTime = System.currentTimeMillis();
        String ctrlUuid = ctrlUuidParam.substring(ctrlUuidParam.indexOf('=') + 1);

        this.service.deleteSubnet(ctrlUuid, subnetId);

        if(response != null) {
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        }

        LOGGER.debug(EXIT_LOG + (System.currentTimeMillis() - infterEnterTime));
    }
}
