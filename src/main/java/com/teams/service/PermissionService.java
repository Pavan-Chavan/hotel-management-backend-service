package com.teams.service;

import com.teams.exception.HotelManagementException;
import com.teams.model.Permission;
import com.teams.model.Role;
import com.teams.repository.PermissionRepository;
import com.teams.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.teams.constant.HoteManagementConstants.DISABLE;

/**
 * @author dgardi
 */
@Service
@Slf4j
public class PermissionService {

    Permission permission;
    @Autowired
    PermissionRepository permissionRepository;

    /**
     *
     * @param permissionName
     * @param isDisable
     * @return
     */
    public ResponseEntity savePermission(Permission permissionDto) {
        String permissionName =  permissionDto.getPermissionName();
        Long permissionId = permissionDto.getPermissionId();
        Boolean isDisable = permissionDto.getIsDisable();
        Permission permission = null;
        try{
            if(permissionId != null) {
                Optional<Permission> existingPermission = permissionRepository.findById(permissionId);

                if(existingPermission.isPresent()){
                    permission = existingPermission.get();
                    permission.setIsDisable(isDisable);
                    permission.setPermissionName(permissionName);
                }
            }
            else {
                permission = new Permission();
                permission.setPermissionName(permissionName);
                permission.setIsDisable(isDisable);
            }
            permission = permissionRepository.save(permission);
            log.info("Data saved successfully for permissionName {}",permissionName);
        }catch(Exception e){
            log.error("Error occurred while saving data ",e);
            throw new HotelManagementException(e.getMessage());
        }
        return new ResponseEntity(permission,HttpStatus.OK);  // TODO : update response with statdard respose dto
    }

    public ResponseEntity getPermissions() {
        try{
            log.info("Retrieving the permissions list..");
            return new ResponseEntity(permissionRepository.findAll(), HttpStatus.OK);
        }catch(Exception e){
            log.error("Error occurred while retrieving permission data",e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    /**
     *
     * @param permissionId
     */
    @Transactional
    public void deletePermission(Long permissionId) {
        try{
            log.info("Deleting the role having permissionName {}",permissionId);
            permissionRepository.deleteById(permissionId);
        }catch(Exception e){
            log.error("Error occurred while deleting permission ",e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseEntity<String> updateRoleStatus(Long permissionId, String status) {
        try {
            boolean value = status.equals(DISABLE) ? true : false ;
            Permission permission = permissionRepository.findById(permissionId).get();
            permission.setIsDisable(value);
            permissionRepository.save(permission);
            return new ResponseEntity<>("permission update successfully",HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while updating role {} ",status,e);
            throw new HotelManagementException(e.getMessage());
        }
    }
}
