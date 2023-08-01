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
     * @return
     */
    public ResponseEntity savePermission(String permissionName) {
        Permission permission1 = new Permission();
        permission1.setPermissionName(permissionName);
        try{
            permission = permissionRepository.save(permission1);
            log.info("Data saved successfully for permissionName {}",permissionName);
        }catch(Exception e){
            log.error("Error occurred while saving data ",e);
            throw new HotelManagementException(e.getMessage());
        }
        return new ResponseEntity(permission,HttpStatus.OK);
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
     * @param permissionName
     */
    @Transactional
    public void deletePermission(String permissionName) {
        try{
            log.info("Deleting the role having permissionName {}",permissionName);
            permissionRepository.deleteByPermissionName(permissionName);
        }catch(Exception e){
            log.error("Error occurred while deleting permission ",e);
            throw new HotelManagementException(e.getMessage());
        }
    }
}
