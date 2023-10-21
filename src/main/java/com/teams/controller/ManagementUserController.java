package com.teams.controller;

import com.teams.entity.models.SubUserRequestModel;
import com.teams.exception.HotelManagementException;
import com.teams.service.ManagementUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

/**
 * @author dgardi
 */
@RestController
@RequestMapping("/1.0/users")
public class ManagementUserController {
    @Autowired
    private ManagementUserService managementUserService;

    @ApiOperation(value = "Save user",produces = "application/json")
    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody SubUserRequestModel subUserRequestModel){
        return managementUserService.createUser(subUserRequestModel);
    }

    @PutMapping("/updateUser")
    public ResponseEntity updateUser(@RequestBody SubUserRequestModel subUserRequestModel){
        try{
            return new ResponseEntity(managementUserService.updateUser(subUserRequestModel), HttpStatus.OK);
        } catch (Exception e){
            throw new HotelManagementException("Error occurred while updating the sub-user");
        }
    }

    @ApiOperation(value = "Get user list",produces = "application/json")
    @GetMapping("/getUsers")
    public ResponseEntity getUsers(@RequestParam(name = "subUserId",required = false) UUID subUserId){
        return managementUserService.getUsers(subUserId);
    }

    @ApiOperation(value = "Enable Permission")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "subUserId",dataType = "UUID",required = true, paramType = "query",
                            value = "subUserId should be valid role"),
                    @ApiImplicitParam(name = "status",dataType = "Long",required = true, paramType = "query",
                            value = "subUserId should be valid role")
            })
    @PutMapping("/status")
    public ResponseEntity<String> updateRoleStatus(@RequestParam UUID subUserId,
                                                   @RequestParam String status){
        try{
            return managementUserService.updateRoleStatus(subUserId,status);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Delete sub user from list")
    @ApiImplicitParam(name = "subUserId",dataType = "UUID",required = true, paramType = "query",
            value = "subUserId should be valid UUID")
    @DeleteMapping("/deleteSubUser")
    public ResponseEntity<String> deleteRole(@RequestParam UUID subUserId){
        try{
            return managementUserService.deleteRole(subUserId);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }
}
