package com.teams.controller;

import com.teams.exception.HotelManagementException;
import com.teams.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author dgardi
 */
@RestController
@RequestMapping("/1.0/roles")
@Api(value = "Permission Apis",description = "Rest APIs to perform permission related actions")
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @ApiOperation(value = "Save permission",produces = "application/json")
    @ApiImplicitParam(name = "permissionName",dataType = "String",required = true,
    value ="permissionName should be valid permission name", paramType = "query")
    @PostMapping("/savePermission")
    public ResponseEntity savePermission(@RequestParam String permissionName,@RequestParam Boolean isDisable){
        try{
            return permissionService.savePermission(permissionName,isDisable);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Get permissions list",produces = "application/json")
    @GetMapping("/getPermissions")
    public ResponseEntity getPermissions(){
        try{
            return permissionService.getPermissions();
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Delete permission from list")
    @ApiImplicitParam(name = "permissionName",dataType = "String",required = true, paramType = "query",
            value = "permissionName should be valid permission")
    @DeleteMapping("/deletePermission")
    public void deletePermission(@RequestParam String permissionName){
        try{
            permissionService.deletePermission(permissionName);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }
}
