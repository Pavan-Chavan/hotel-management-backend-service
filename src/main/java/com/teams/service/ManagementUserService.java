package com.teams.service;

import com.teams.entity.models.SubUserRequestModel;
import com.teams.exception.HotelManagementException;
import com.teams.entity.Login;
import com.teams.entity.Permission;
import com.teams.entity.Role;
import com.teams.entity.SubUser;
import com.teams.repository.LoginRepository;
import com.teams.repository.ManagementUserRepository;
import com.teams.repository.PermissionRepository;
import com.teams.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private PermissionRepository permissionRepository;
    private static SubUser subUser;
    private static final String DEFAULT_ROLE_NAME = "waiter";
    private static final String DEFAULT_PASSWORD = "Pass@123";

    /**
     *
     * @param subUserRequestModel
     * @return
     */
    public ResponseEntity<SubUser> createUser(SubUserRequestModel subUserRequestModel) {

        UUID uuid = UUID.randomUUID();
        Role role = null;
        Login login = null;
        try{
            Optional<SubUser> subUserOptional = managementUserRepository.findSubUserByLoginUsername(subUserRequestModel.getUsername());
            if(subUserOptional.isPresent()){
                log.info("Updating the sub-user record");
                subUser = subUserOptional.get();
            } else {
                log.info("Creating the new sub-user record");
                subUser = new SubUser();
                subUser.setSubUserId(uuid);
            }
            //assign the roleName from the role table
            role = roleRepository.findRoleByRoleName(subUserRequestModel.getRoleName()).get();
            log.info("RoleName is added {}",role.getRoleName());
            subUser.setRole(role);

            Optional<Login> loginOptional = loginRepository.findById(subUserRequestModel.getUsername());
            if(loginOptional.isPresent()){
                log.info("update the password for username:{}",subUserRequestModel.getUsername());
                login = loginOptional.get();
                login.setPassword(subUserRequestModel.getPassword());

            } else {
                if(subUserRequestModel.getPassword() == null)   {
                     login = new Login();
                     login.setUsername(subUserRequestModel.getUsername());
                     login.setPassword(DEFAULT_PASSWORD);
                }
            }
            login = loginRepository.save(login);
            log.info("Login details are validated ");
            subUser.setLogin(login);

            //Iterating over the set of permissions and assigning it to the user set
            if(subUserRequestModel.getPermissions() != null){
                subUserRequestModel.getPermissions().forEach(permissionName ->{
                    Permission permission = permissionRepository.findPermissionByPermissionName(permissionName).get();
                    permission.getUser().add(subUser);
                    subUser.getPermissionSet().add(permission);
                });
            }
            log.info("Saving the sub-user details for userId: {}",uuid);
            subUser = managementUserRepository.save(subUser);
            return new ResponseEntity(subUser, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred while saving the user details for {} {} ",subUserRequestModel.getUsername(),e);
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
