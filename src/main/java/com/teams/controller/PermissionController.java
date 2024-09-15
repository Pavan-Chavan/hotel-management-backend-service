package com.teams.controller;

import com.teams.constant.HoteManagementConstants;
import com.teams.entity.Permission;
import com.teams.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author dgardi
 */
@RestController
@RequestMapping("/1.0/permissions")
@Api(value = "Permission Apis",description = "Rest APIs to perform permission related actions")
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @ApiOperation(value = "Save permission details",produces = "application/json")
    @PostMapping("/savePermission")
    public ResponseEntity savePermissionDetails(@RequestBody Permission permission){

        return new ResponseEntity(permissionService.savePermission(permission), HttpStatus.OK);
    }

    @ApiOperation(value = "Get permissions list",produces = "application/json")
    @GetMapping("/getPermissions")
    public ResponseEntity getPermissions(@RequestParam(name = "offset",defaultValue = "5") Integer offset,
                                         @RequestParam(name = "pageNo",defaultValue = "0") Integer pageNumber,
                                         @RequestParam(name = "order", defaultValue = "ASC") String order,
                                         @RequestParam(name = "permissionId",required = false,defaultValue = "-1") Long permissionId){

        return new ResponseEntity(permissionService.getPermissions(offset,pageNumber,order,permissionId),HttpStatus.OK);
    }

    @ApiOperation(value = "Delete permission from list")
    @ApiImplicitParam(name = "permissionId",dataType = "Long",required = true, paramType = "query",
            value = "permissionName should be valid permission")
    @DeleteMapping("/deletePermission")
    public ResponseEntity deletePermission(@RequestParam Long permissionId){

        permissionService.deletePermission(permissionId);
        return new ResponseEntity("Permission Details deleted successfully",HttpStatus.OK);
    }

    @ApiOperation(value = "Enable permission")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "permissionId",dataType = "Long",required = true, paramType = "query",
                            value = "permissionId should be valid permissionId"),
                    @ApiImplicitParam(name = "status",dataType = "Long",required = true, paramType = "query",
                            value = "status should be valid status")
            })
    @PutMapping("/status")
    public ResponseEntity updateRoleStatus(@RequestParam Long permissionId,
                                                   @RequestParam HoteManagementConstants.Status status){

        return new ResponseEntity<>(permissionService.updatePermissionStatus(permissionId,status),HttpStatus.OK);
    }
    /*
    //TODO
    1) Add isEnable functionality
    2) If we delete the permission then assciated user permission should also be deleted along with permission
    3) Add pagination support refer role get API
    4) update the save api so that we can use single api for create and update operation

     */
}
