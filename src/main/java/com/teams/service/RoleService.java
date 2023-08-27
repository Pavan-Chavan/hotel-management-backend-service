package com.teams.service;

import com.teams.exception.HotelManagementException;
import com.teams.model.Role;
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
public class RoleService {

    Role role1 = null;
    @Autowired
    RoleRepository roleRepository;

    /**
     *
     * @param roleName
     * @return
     */
    public ResponseEntity saveRole(String roleName, Boolean isDisable) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setIsDisable(isDisable);
        try{
            role1 = roleRepository.save(role);
            log.info("Data saved successfully for roleName {}",roleName);
        }catch(Exception e){
            log.error("Error occurred while saving data ",e);
            throw new HotelManagementException(e.getMessage());
        }
        return new ResponseEntity(role1,HttpStatus.OK);
    }

    /**
     *
     * @return
     */
    public ResponseEntity getRoles() {
        try{
            log.info("Retrieving the role list..");
            return new ResponseEntity(roleRepository.findAll(), HttpStatus.OK);
        }catch(Exception e){
            log.error("Error occurred while retrieving role data",e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    /**
     *
     * @param roleName
     */
    @Transactional
    public void deleteRole(String roleName) {
        try{
            log.info("Deleting the role having roleName {}",roleName);
            roleRepository.deleteByRoleName(roleName);
        }catch(Exception e){
            log.error("Error occurred while deleting role ",e);
            throw new HotelManagementException(e.getMessage());
        }
    }
}
