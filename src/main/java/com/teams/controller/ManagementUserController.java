package com.teams.controller;

import com.teams.service.ManagementUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName",dataType = "String",required = true,
                    value ="rolename should be valid role name", paramType = "query"),
            @ApiImplicitParam(name = "username",dataType = "String",required = true,
                    value ="username should be valid username", paramType = "path"),
            @ApiImplicitParam(name = "permissions",dataType = "Set<String>",required = true,
                    value ="permissions to give user", paramType = "query")
    })
    @PostMapping("/saveUser/{username}")
    public ResponseEntity saveUsers(@RequestParam(name = "roleName") String roleName,
                                    @RequestParam(name ="permissions") Set<String> permissions,
                                    @PathVariable(name = "username") String username){
        return managementUserService.saveUser(roleName,permissions,username);
    }

    @ApiOperation(value = "Get user list",produces = "application/json")
    @GetMapping("/getUsers")
    public ResponseEntity getUsers(){
        return managementUserService.getUsers();
    }
}
