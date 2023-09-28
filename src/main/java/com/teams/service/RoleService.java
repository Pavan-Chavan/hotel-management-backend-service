package com.teams.service;

import com.teams.exception.HotelManagementException;
import com.teams.model.Role;
import com.teams.model.SubUser;
import com.teams.repository.ManagementUserRepository;
import com.teams.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

import static com.teams.constant.HoteManagementConstants.*;

/**
 * @author dgardi
 */
@Service
@Slf4j
public class RoleService {

    Role role1 = null;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ManagementUserRepository managementUserRepository;

    /**
     *
     * @param role
     * @return
     */
    public ResponseEntity saveRole(Role role) {
        String roleName = role.getRoleName();
        Long roleId = role.getRoleId();
        Boolean isDisable = role.getIsDisable();
        Role roles = null;
        try{
            if(roleId != null) {
                Optional<Role> existingRole = roleRepository.findById(roleId);
                if(existingRole.isPresent()){
                    roles = existingRole.get();
                    roles.setIsDisable(isDisable);
                    roles.setRoleName((roleName));
                }
            }
            else {
                roles = new Role();
                roles.setRoleName(roleName);
                roles.setIsDisable(isDisable);
                roles.setCreatedAt(new Date());
            }
            roleRepository.save(roles);
            log.info("Data saved successfully for roleName {}",roleName);
        }catch(Exception e){
            log.error("Error occurred while saving data ",e);
            throw new HotelManagementException(e.getMessage());
        }
        return new ResponseEntity(roles,HttpStatus.OK);
    }

    /**
     *
     * @param offset
     * @param pageNumber
     * @param order
     * @return
     */
    public ResponseEntity getRoles(Integer offset,Integer pageNumber,String order){
        try{
            HttpHeaders headers = new HttpHeaders();
            log.info("Retrieving the role list..");
            Sort sort = order.equals("ASC")?Sort.by("createdAt").ascending():Sort.by("createdAt").descending();
            Pageable paging = PageRequest.of(pageNumber,offset, sort);
            Page<Role> totalRecords = roleRepository.findAll(paging);
            headers.add(TOTAL_RECORD,String.valueOf(totalRecords.getTotalElements()));
            return new ResponseEntity(totalRecords.getContent(),headers, HttpStatus.OK);
        }catch(Exception e){
            log.error("Error occurred while retrieving role data",e);
            throw new HotelManagementException(e.getMessage());
        }
    }

    /**
     *
     * @param roleId
     * @return
     */
    @Transactional
    public ResponseEntity deleteRole(Long roleId) {
        try{
            log.info("Deleting the role having roleName {}",roleId);

            Optional<Role> defaultRole = roleRepository.findById(1L);
            List<SubUser> subUserList = managementUserRepository.findByRoleRoleId(roleId);
            if(Objects.nonNull(subUserList)) {
                log.info("Sub user list have subusers");
                List<SubUser> modifiedSubUserList = subUserList.stream().map(subUser -> {
                    subUser.setIsDisable(true);
                    subUser.setRole(defaultRole.get()); // TODO : insert ideal role when it created
                    return subUser;
                }).collect(Collectors.toList());
                managementUserRepository.saveAll(modifiedSubUserList);
            }
            log.info("Deleting record for role id {}",roleId);

            roleRepository.deleteById(roleId);

        }catch(Exception e){
            log.error("Error occurred while deleting role ",e);
            throw new HotelManagementException(e.getMessage());
        }
        return new ResponseEntity("Deleted record successfully",HttpStatus.OK); // TODO : write a sepracte response DTO
    }

    public ResponseEntity<String> updateRoleStatus(Long roleId, String status) {
        try {
            boolean value = status.equals(DISABLE) ? true : false ;
            Role role = roleRepository.findById(roleId).get();
            role.setIsDisable(value);
            roleRepository.save(role);
            return new ResponseEntity<>("role update successfully",HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while updating role {} ",status,e);
            throw new HotelManagementException(e.getMessage());
        }
    }
}
