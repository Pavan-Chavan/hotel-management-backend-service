package com.teams.service;

import com.teams.exception.HotelManagementException;
import com.teams.model.Role;
import com.teams.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleService {

    @Autowired
    RoleRepository roleRepository;
    public ResponseEntity saveRole(Role role) {
        Role role1 = null;
        try{
            role1 = roleRepository.save(role);
            log.info("Data saved successfully for roleName {}",role.getRoleName());
        }catch(Exception e){
            log.error("Error occurred while saving data ",e);
            throw new HotelManagementException(e.getMessage());
        }
        return new ResponseEntity(role1,HttpStatus.OK);
    }

    public ResponseEntity getRoles() {
        try{
            log.info("Retrieving the role list..");
            return new ResponseEntity(roleRepository.findAll(), HttpStatus.OK);
        }catch(Exception e){
            log.error("Error occurred while retrieving role data",e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    public void deleteRole(String roleId) {
        try{
            roleRepository.deleteById(roleId);
        }catch(Exception e){
            log.error("Error occurred while deleting role ",e);
            throw new HotelManagementException(e.getMessage());
        }
    }
}
