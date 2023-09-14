package com.teams.controller;

import com.teams.exception.HotelManagementException;
import com.teams.model.Role;
import com.teams.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
    public ResponseEntity getRoles(){
        try{
            return roleService.getRoles();
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Delete role from list")
    @ApiImplicitParam(name = "roleName",dataType = "String",required = true, paramType = "query",
            value = "roleName should be valid role")
    @DeleteMapping("/deleteRole")
    public void deleteRole(@RequestParam String roleName){
        try{
            roleService.deleteRole(roleName);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }
}
