package com.teams.controller;

import com.teams.constant.HoteManagementConstants;
import com.teams.constant.Response;
import com.teams.exception.HotelManagementException;
import com.teams.entity.Role;
import com.teams.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;


@RestController
@RequestMapping("/1.0/roles")
@Api(value = "Role Apis",description = "Rest APIs to perform role related actions")
public class RoleController {

    @Autowired
    RoleService roleService;

    @ApiOperation(value = "Save role",produces = "application/json")
    @PostMapping("/saveRole")
    public ResponseEntity saveRoleDetails(@RequestBody Role role){
        return new ResponseEntity(roleService.saveRole(role), HttpStatus.OK);
    }

    @ApiOperation(value = "Get roles list",produces = "application/json")
    @GetMapping("/getRoles")
    @RolesAllowed("user")
    public ResponseEntity getRoles(@RequestParam(name = "offset",defaultValue = "5") Integer offset,
                                   @RequestParam(name = "pageNo",defaultValue = "0") Integer pageNumber,
                                   @RequestParam(name = "order", defaultValue = "ASC") String order,
                                   @RequestParam(name = "roleId",required = false,defaultValue = "-1") Long roleId){

        return new ResponseEntity(roleService.getRoles(offset,pageNumber,order,roleId),HttpStatus.OK);

    }

    @ApiOperation(value = "Delete role from list")
    @ApiImplicitParam(name = "roleId",dataType = "Long",required = true, paramType = "query",
            value = "roleId should be valid role")
    @DeleteMapping("/deleteRole")
    public ResponseEntity<String> deleteRole(@RequestParam Long roleId){

        roleService.deleteRole(roleId);
        return new ResponseEntity<>("Deleted the role details for roleId "+roleId,HttpStatus.OK);
    }

    @ApiOperation(value = "Enable role")
    @ApiImplicitParams(
            value = {
                @ApiImplicitParam(name = "roleId",dataType = "Long",required = true, paramType = "query",
                value = "roleId should be valid role"),
                @ApiImplicitParam(name = "status",dataType = "Long",required = true, paramType = "query",
                value = "roleId should be valid role")
            })
    @PutMapping("/status")
    public ResponseEntity updateRoleStatus(@RequestParam Long roleId,
                                             @RequestParam HoteManagementConstants.Status status){

        return new ResponseEntity<>(roleService.updateRoleStatus(roleId,status),HttpStatus.OK);
    }
}
