package com.teams.controller;

import com.teams.exception.HotelManagementException;
import com.teams.model.Role;
import com.teams.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/1.0/roles")
@Api(value = "Role Apis",description = "Rest APIs to perform role related actions")
public class RoleController {
    @Autowired
    RoleService roleService;

    @ApiOperation(value = "Save role",produces = "application/json")
    @ApiImplicitParam(name = "role",dataType = "object",required = true,paramType = "body",
    value = "RoleId should be valid UUID for eg: 123e4567-e89b-12d3-a456-426655440000")
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
    public ResponseEntity getRoles(){
        try{
            return roleService.getRoles();
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Delete role from list")
    @ApiImplicitParam(name = "rule",dataType = "String",required = true, paramType = "query",
            value = "RoleId should be valid UUID for eg: 123e4567-e89b-12d3-a456-426655440000")
    @DeleteMapping("/deleteRole")
    public void deleteRole(String roleId){
        try{
            roleService.deleteRole(roleId);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }
}
