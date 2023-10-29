package com.teams.controller;

import com.teams.exception.HotelManagementException;
import com.teams.entity.Role;
import com.teams.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiImplicitParam(name = "roleName",dataType = "String",required = true,
    value ="rolename should be valid role name", paramType = "query")
    @PostMapping("/saveRole")
    public ResponseEntity saveRole(@RequestBody Role role){
        try{
            return roleService.saveRole(role);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Get roles list",produces = "application/json")
    @GetMapping("/getRoles")
    @RolesAllowed("user")
    public ResponseEntity getRoles(@RequestParam(name = "offset",defaultValue = "5") Integer offset,
                                   @RequestParam(name = "pageNo",defaultValue = "0") Integer pageNumber,
                                   @RequestParam(name = "order", defaultValue = "ASC") String order,
                                   @RequestParam(name = "roleId",required = false,defaultValue = "-1") Long roleId){
        try{
            return roleService.getRoles(offset,pageNumber,order,roleId);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Delete role from list")
    @ApiImplicitParam(name = "roleId",dataType = "Long",required = true, paramType = "query",
            value = "roleId should be valid role")
    @DeleteMapping("/deleteRole")
    public ResponseEntity<String> deleteRole(@RequestParam Long roleId){
        try{
            return roleService.deleteRole(roleId);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
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
    public ResponseEntity<String> updateRoleStatus(@RequestParam Long roleId,
                                             @RequestParam String status){
        try{
            return roleService.updateRoleStatus(roleId,status);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }
}
