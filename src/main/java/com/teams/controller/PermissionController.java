package com.teams.controller;

import com.teams.exception.HotelManagementException;
import com.teams.model.Permission;
import com.teams.service.PermissionService;
import io.github.classgraph.MappableInfoList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author dgardi
 */
@RestController
@RequestMapping("/1.0/permisisons")
@Api(value = "Permission Apis",description = "Rest APIs to perform permission related actions")
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @ApiOperation(value = "Save permission",produces = "application/json")
    @ApiImplicitParam(name = "permissionName",dataType = "String",required = true,
    value ="permissionName should be valid permission name", paramType = "query")
    @PostMapping("/savePermission")
    public ResponseEntity savePermission(@RequestBody Permission permission){
        try{
            return permissionService.savePermission(permission);
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
    @ApiImplicitParam(name = "permissionId",dataType = "Long",required = true, paramType = "query",
            value = "permissionName should be valid permission")
    @DeleteMapping("/deletePermission")
    public void deletePermission(@RequestParam Long permissionId){
        try{
            permissionService.deletePermission(permissionId);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Enable permission")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "permissionId",dataType = "Long",required = true, paramType = "query",
                            value = "roleId should be valid role"),
                    @ApiImplicitParam(name = "status",dataType = "Long",required = true, paramType = "query",
                            value = "roleId should be valid role")
            })
    @PutMapping("/status")
    public ResponseEntity<String> updateRoleStatus(@RequestParam Long permissionId,
                                                   @RequestParam String status){
        try{
            return permissionService.updateRoleStatus(permissionId,status);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }
    /*
    //TODO
    1) Add isEnable functionality
    2) If we delete the permission then assciated user permission should also be deleted along with permission
    3) Add pagination support refer role get API
    4) update the save api so that we can use single api for create and update operation

     */
}
