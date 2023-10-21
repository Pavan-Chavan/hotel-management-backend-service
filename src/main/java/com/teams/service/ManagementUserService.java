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
import io.swagger.v3.oas.models.media.UUIDSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.teams.constant.HoteManagementConstants.DISABLE;

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
    public ResponseEntity createUser(SubUserRequestModel subUserRequestModel) {

        UUID uuid = UUID.randomUUID();
        try{
            Optional<SubUser> subUserOptional = managementUserRepository.findSubUserByLoginUsername(subUserRequestModel.getUsername());
            if(subUserOptional.isPresent()){
                log.info("SubUser is already present for username {}",subUserRequestModel.getUsername());
                return new ResponseEntity<>("SubUser already existed",HttpStatus.FOUND);
            }
            log.info("Creating the new sub-user record");
            subUser = new SubUser();
            subUser.setSubUserId(uuid);
            subUser.setIsDisable(subUserRequestModel.getIsDisable());
            Role role = roleRepository.findById(subUserRequestModel.getRoleId()).get();
            if(Objects.isNull(role)){
                throw new HotelManagementException("RoleName is not available");
            }
            log.info("RoleName is validated");
            subUser.setRole(role);

            Login login = new Login();
            login.setUsername(subUserRequestModel.getUsername());
            login.setPassword(DEFAULT_PASSWORD);
            login = loginRepository.save(login);
            log.info("Login details are validated ");
            subUser.setLogin(login);

            //Iterating over the set of permissions and assigning it to the user set
            subUserRequestModel.getPermissionsIds().forEach(permissionId ->{
                Permission permission = permissionRepository.findById(permissionId).get();
                permission.getUser().add(subUser);
                subUser.getPermissionSet().add(permission);
            });

            log.info("Saving the sub-user details for userId: {}",uuid);
            SubUser subUser2 = managementUserRepository.save(subUser);
            return new ResponseEntity(subUser2, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred while saving the user details for {} {} ",subUserRequestModel.getUsername(),e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    /**
     *
     * @return list of user along with its permissions
     */
    public ResponseEntity getUsers(UUID subUserId) {
        try{
            if(subUserId != null) {
                return new ResponseEntity(managementUserRepository.findById(subUserId),HttpStatus.OK);
            }
            List<SubUser> subUserList = managementUserRepository.findAll();
            return new ResponseEntity(subUserList,HttpStatus.OK);
        }catch(Exception e){
            log.error("Error occurred while retrieving the data for users",e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    public SubUser updateUser(SubUserRequestModel subUserRequestModel) {
        UUID uuid = UUID.randomUUID();
        Role role = null;
        Login login = null;
        try{
            Optional<SubUser> subUserOptional = managementUserRepository.findById(subUserRequestModel.getSubUserId());
            if(subUserOptional.isPresent()){
                log.info("Updating the sub-user record");
                subUser = subUserOptional.get();
            }

            //assign the roleName from the role table
            role = roleRepository.findById(subUserRequestModel.getRoleId()).get();
            if(role.getRoleId() != subUserRequestModel.getRoleId()){
                subUser.setRole(role);
            }
            subUser.setPermissionSet(new HashSet<>());
            if((CollectionUtils.isNotEmpty(subUserRequestModel.getPermissionsIds()))) {
                subUserRequestModel.getPermissionsIds().forEach(permissionId ->{
                    Permission permission = permissionRepository.findById(permissionId).get();
                    permission.getUser().add(subUser);
                    subUser.getPermissionSet().add(permission);
                });
            }
            subUser.setIsDisable(subUserRequestModel.getIsDisable());
            log.info("Saving the sub-user details for userId: {}",uuid);
            subUser = managementUserRepository.save(subUser);
            return subUser;
        } catch (Exception e){
            log.error("Error occurred while saving the user details for {} {} ",subUserRequestModel.getUsername(),e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseEntity<String> updateRoleStatus(UUID subUserId, String status) {
        try {
            boolean value = status.equals(DISABLE);
            SubUser subUser = managementUserRepository.findById(subUserId).get();
            subUser.setIsDisable(value);
            managementUserRepository.save(subUser);
            return new ResponseEntity<>("sub user update successfully",HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while updating role {} ",status,e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseEntity<String> deleteRole(UUID subUserId) {
        try {
            managementUserRepository.deleteById(subUserId);
            return new ResponseEntity<>("Delete succefully",HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred while updating role {} ",subUserId,e);
            throw new HotelManagementException(e.getMessage());
        }
    }
}
