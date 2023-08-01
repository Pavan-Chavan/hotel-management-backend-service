package com.teams.controller;

import com.teams.service.LoginService;
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
@RequestMapping("/1.0/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @ApiOperation(value = "Save user",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password",dataType = "String",required = true,
                    value ="password should contain \n"+
                    "one lowercase character \n"+"one uppercase character \n"+"one special case symbol", paramType = "query"),
            @ApiImplicitParam(name = "username",dataType = "String",required = true,
                    value ="username should be valid username", paramType = "query"),
    })
    @PostMapping("/saveLoginDetails")
    public ResponseEntity saveLoginCredentials(@RequestParam("username") String username,
                                               @RequestParam("password") String password){
        return loginService.saveLoginCredentials(username,password);
    }
}
