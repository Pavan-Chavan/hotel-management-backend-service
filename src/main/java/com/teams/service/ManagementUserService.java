package com.teams.service;

import com.teams.exception.HotelManagementException;
import com.teams.model.Login;
import com.teams.model.Permission;
import com.teams.model.Role;
import com.teams.model.SubUser;
import com.teams.repository.LoginRepository;
import com.teams.repository.ManagementUserRepository;
import com.teams.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dgardi
 */
@Service
@Slf4j
public class ManagementUserService {

    @Autowired
    private ManagementUserRepository managementUserRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LoginRepository loginRepository;

    /**
     *
     * @param roleName
     * @param permissionNames
     * @param username
     * @return
     */
    public ResponseEntity<SubUser> saveUser(String roleName, Set<String> permissionNames, String username) {
        try{
            SubUser subUser = new SubUser();
            UUID uuid = UUID.randomUUID();
            Role role = roleRepository.findById(roleName).get();
            if(Objects.isNull(role)){
                throw new HotelManagementException("RoleName is not available");
            }
            log.info("RoleName is validated");
            subUser.setRole(role);
            Login login = loginRepository.findById(username).get();
            if(Objects.isNull(login)){
                throw new HotelManagementException("username is not available");
            }
            log.info("Login details are validated ");
            subUser.setLogin(login);
            subUser.setSubUserId(uuid);
            //Iterating over the set of permissions and assigning it to the user set
            permissionNames.stream().map(permissions1->{
                Permission permission1 = new Permission();
                permission1.setPermissionName(permissions1);
                permission1.getUser().add(subUser);
                subUser.getPermissionList().add(permission1);
                return permission1;
            }).collect(Collectors.toSet());
            log.info("Saving the sub-user details for userId: {}",uuid);
            SubUser subUser2 = managementUserRepository.save(subUser);
            return new ResponseEntity(subUser2, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred while saving the user details for {} {} ",username,e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    /**
     *
     * @return list of user along with its permissions
     */
    public ResponseEntity getUsers() {
        try{
            List<SubUser> subUserList = managementUserRepository.findAll();
            return new ResponseEntity(subUserList,HttpStatus.OK);
        }catch(Exception e){
            log.error("Error occurred while retrieving the data for users",e);
            throw new HotelManagementException(e.getMessage());
        }
    }
}
