package com.teams.service;

import com.teams.entity.models.SubUserRequestModel;
import com.teams.exception.HotelManagementDataNotFoundException;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private static final String DEFAULT_ROLE_NAME = "waiter";
    private static final String DEFAULT_PASSWORD = "Pass@123";


    public SubUser createUser(SubUserRequestModel subUserRequestModel) {

        UUID subUserId = UUID.randomUUID();
        SubUser subUser = new SubUser();
        Login login = new Login();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        try{
            validateSubUserRequestModelData(subUserRequestModel);
            SubUser subUserOptional = managementUserRepository.findSubUserByLoginUsername(subUserRequestModel.getUsername());
            if(subUserOptional != null){  // Pavan : I have updated this check, Please check this condition once
                log.info("SubUser is already existed for username {}",subUserRequestModel.getUsername());
                throw new HotelManagementException("Data Already Existed");
            }
            log.info("Creating the new subUser record for subUserId {}",subUserId);
            subUser.setSubUserId(subUserId);
            subUser.setIsDisable(subUserRequestModel.getIsDisable());

            // setting role data
            Optional<Role> roleOptional = roleRepository.findById(subUserRequestModel.getRoleId());
            if(roleOptional.isPresent()){
                log.info("SubUserId {} is creating with roleName {}",subUserId,roleOptional.get().getRoleName());
                subUser.setRole(roleOptional.get());
            } else {
                //TODO assign one default role to subUser
            }

            // setting data in user data
            log.info("Creating login details for subUserId {}",subUserId);
            login.setUsername(subUserRequestModel.getUsername());
            String encodedPassword = encoder.encode(StringUtils.isEmpty(subUserRequestModel.getPassword())?DEFAULT_PASSWORD:subUserRequestModel.getPassword());
            login.setPassword(encodedPassword);
            loginRepository.save(login);
            subUser.setLogin(login);
            log.info("Login details are created for subUserId {} successfully",subUserId);

            //Iterating over the set of permissions and assigning it to the user set
            if(CollectionUtils.isNotEmpty(subUserRequestModel.getPermissionsIds())) {
                subUserRequestModel.getPermissionsIds().forEach(permissionId ->{
                    Optional<Permission> permissionOptional = permissionRepository.findById(permissionId);
                    if(permissionOptional.isPresent()) {
                        Permission permission = permissionOptional.get();
                        permission.getUser().add(subUser);
                        subUser.getPermissionSet().add(permission);
                    } else {
                        log.info("skipping permissionId {} for subUserId {} as it is not found",permissionId,subUserId);
                    }
                });
            } else {
                //TODO assign one default permission if the permissionIds are not available
            }

            log.info("Saving the subUser details for subUserId: {}",subUserId);
            return managementUserRepository.save(subUser);

        } catch(IllegalArgumentException iae){
            log.error("Invalid details provided ",iae);
            throw iae;
        } catch (Exception e){
            log.error("Error occurred while saving the sub-user details for {} {} ",subUserRequestModel.getUsername(),e);
            throw new HotelManagementException("Error occurred while saving the subUser details",e);
        }
    }

    private void validateSubUserRequestModelData(SubUserRequestModel subUserRequestModel) {

        if(StringUtils.isEmpty(subUserRequestModel.getUsername()) || StringUtils.isEmpty(subUserRequestModel.getPassword())) {
            throw new IllegalArgumentException("Invalid data provided");
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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        try{
            validateSubUserId(subUserRequestModel.getSubUserId());
            Optional<SubUser> subUserOptional = managementUserRepository.findById(subUserRequestModel.getSubUserId());
            if(!subUserOptional.isPresent()) {
                throw new HotelManagementDataNotFoundException("Data not found for subUserId " + subUserRequestModel.getSubUserId());
            }
            log.info("Updating the subUser details for subUserId {}",subUserRequestModel.getSubUserId());
            SubUser subUser = subUserOptional.get();

            //assign the roleName from the role table
            validateRoleId(subUserRequestModel.getRoleId());
            if(subUser.getRole()!= null && !subUser.getRole().getRoleId().equals(subUserRequestModel.getRoleId())) {
                Optional<Role> roleOptional = roleRepository.findById(subUserRequestModel.getRoleId());
                if(roleOptional.isPresent()) {
                    log.info("Updating the new role details {} for subUserId {}",subUserRequestModel.getRoleId(),subUser.getSubUserId());
                    subUser.setRole(roleOptional.get());
                } else {
                    log.info("Keeping the older role as it is as roleId {} details not found",subUserRequestModel.getRoleId());
                }
            } else {
                log.info("Skipping the role as existing role details are same for roleId {}",subUserRequestModel.getRoleId());
            }

            //remove already present permissionId list
            if(CollectionUtils.isNotEmpty(subUser.getPermissionSet())){
                List<Long> existingPermissionIdList = subUser.getPermissionSet().stream().map(Permission::getPermissionId)
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(subUserRequestModel.getPermissionsIds())) {
                    existingPermissionIdList.forEach(subUserRequestModel.getPermissionsIds()::remove);
                }
            }
            if((CollectionUtils.isNotEmpty(subUserRequestModel.getPermissionsIds()))) {
                for (Long permissionId : subUserRequestModel.getPermissionsIds()) {
                    Optional<Permission> permissionOptional = permissionRepository.findById(permissionId);
                    if(permissionOptional.isPresent()) {
                        Permission permission = permissionOptional.get();
                        permission.getUser().add(subUser);
                        subUser.getPermissionSet().add(permission);
                    } else {
                        log.info("Skipping permissionId {} for subUserId {} as it is not found",permissionId, subUser.getSubUserId());
                    }
                }
            } else {
                log.info("No new permissionId found for subUserId {}",subUser.getSubUserId());
            }

            if(StringUtils.isNotEmpty(subUserRequestModel.getPassword())) {
                Optional<Login> optionalLogin = loginRepository.findById(subUser.getLogin().getUsername());
                if(optionalLogin.isPresent()) {
                    log.info("Updating the login password details for subUserId {}",subUser.getSubUserId());
                    Login login = optionalLogin.get();
                    login.setPassword(encoder.encode(subUserRequestModel.getPassword()));
                    subUser.setLogin(login);
                }
            }
            subUser.setIsDisable(subUserRequestModel.getIsDisable());

            log.info("Saving the subUser details for userId: {}",subUser.getSubUserId());
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
            SubUser subUser  = managementUserRepository.findById(subUserId).get();
            subUser.getPermissionSet().stream().forEach(
                    permission -> {
                        permission.getUser().remove(subUser);
                    }
            );
            subUser.getPermissionSet().removeAll(subUser.getPermissionSet());
            managementUserRepository.deleteById(subUserId);
            return new ResponseEntity<>("Delete successfully",HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred while deleting sub-user {} ",subUserId,e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    public void validateRoleId (Long roleId){
        if(roleId == null || roleId < 0) {
            throw new IllegalArgumentException("Invalid Data provided");
        }
    }

    private void validateSubUserId(UUID subUserId) {
        try {
            UUID.fromString(subUserId.toString());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid SubUserId provided");
        }
    }
}
