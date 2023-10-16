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

    @PostMapping("/updateUser")
    public ResponseEntity updateUser(@RequestBody SubUserRequestModel subUserRequestModel){
        try{
            return new ResponseEntity(managementUserService.updateUser(subUserRequestModel), HttpStatus.OK);
        } catch (Exception e){
            throw new HotelManagementException("Error occurred while updating the sub-user");
        }
    }

    @ApiOperation(value = "Get user list",produces = "application/json")
    @GetMapping("/getUsers")
    public ResponseEntity getUsers(){
        return managementUserService.getUsers();
    }
}
