package com.teams.service;

import com.teams.entity.Permission;
import com.teams.entity.models.HotelManagementListResponse;
import com.teams.exception.HotelManagementDataNotFoundException;
import com.teams.exception.HotelManagementException;
import com.teams.entity.Role;
import com.teams.entity.SubUser;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.teams.constant.HoteManagementConstants.*;

/**
 * @author dgardi
 */
@Service
@Slf4j
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ManagementUserRepository managementUserRepository;


    public Role saveRole(Role role) {

        String roleName = role.getRoleName();
        Long roleId = role.getRoleId();
        Boolean isDisable = role.getIsDisable();
        Role existingRole = new Role();
        try{
            validateRoleId(roleId);
            log.info("Fetching role details for roleId {}",roleId);
            Optional<Role> roleOptional = roleRepository.findById(roleId);

            if(roleOptional.isPresent()) {
                log.info("Data found for roleId {} in the database",roleId);
                existingRole = roleOptional.get();
            }
            existingRole.setIsDisable(isDisable);
            existingRole.setRoleName(roleName);
            existingRole.setCreatedAt(new Date());

            log.info("Saving role details for roleName {}",roleName);
            return roleRepository.save(existingRole);

        } catch (IllegalArgumentException iae) {
            log.error("Invalid data provided either roleId is null or invalid ",iae);
            throw iae;
        } catch(Exception e){
            log.error("Error occurred while saving data ",e);
            throw new HotelManagementException(e.getMessage());
        }
    }


    public HotelManagementListResponse getRoles(Integer offset,Integer pageNumber,String order,Long roleId){
        try{

            Optional<Role> roleOptional = null;
            if(roleId != -1){
                log.info("Retrieving the role for roleId: {}",roleId);
                roleOptional = roleRepository.findById(roleId);
            }

            if(roleOptional != null && roleOptional.isPresent()) {
                return HotelManagementListResponse.getResponse(roleOptional.get(),pageNumber,pageNumber);
            } else {
                log.info("Retrieving the roles list..");
                Sort sort = order.equals(ASC)?Sort.by(CREATED_AT).ascending():Sort.by(CREATED_AT).descending();
                Pageable paging = PageRequest.of(pageNumber,offset, sort);
                Page<Role> totalRecords = roleRepository.findAll(paging);
                return HotelManagementListResponse.getResponse(totalRecords,pageNumber,offset);
            }

        } catch(Exception e){
            log.error("Error occurred while retrieving role data ",e);
            throw new HotelManagementException("Error occurred while retrieving the data for roles",e);
        }
    }


    @Transactional
    public void deleteRole(Long roleId) {
        try{

            validateRoleId(roleId);
            Optional<Role> optionalRole = roleRepository.findById(roleId);
            if(optionalRole.isPresent()) {
                //TODO insert the default role details through liquibase script
                List<SubUser> subUserList = managementUserRepository.findByRoleRoleId(roleId);
                if(Objects.isNull(subUserList)) {
                    Role defaultRole = roleRepository.findById(1L).get();
                    log.info("Updating the default role to subUsers list which has roleId {}",roleId);
                    List<SubUser> modifiedSubUserList = subUserList.stream().peek(subUser -> {
                        subUser.setIsDisable(true);
                        subUser.setRole(defaultRole);
                    }).collect(Collectors.toList());
                    managementUserRepository.saveAll(modifiedSubUserList);
                }

                log.info("Deleting the role details for roleId {}",roleId);
                roleRepository.deleteById(roleId);
            } else{
                throw new HotelManagementDataNotFoundException("Data Not Found");
            }

        } catch (IllegalArgumentException iae) {
            log.error("Invalid data provided either roleId is null or invalid ",iae);
            throw iae;
        } catch (HotelManagementDataNotFoundException dnfe) {
            log.error("Data not found for roleId {} in db",roleId);
            throw dnfe;
        } catch(Exception e){
            log.error("Error occurred while deleting role details ",e);
            throw new HotelManagementException("Error occurred while deleting the roleDetails",e);
        }
    }

    public Role updateRoleStatus(Long roleId, Status status) {

        try {
            validateRoleId(roleId);
            Optional<Role> optionalRole = roleRepository.findById(roleId);
            if(optionalRole.isPresent()) {
                boolean value = status.toString().equalsIgnoreCase(DISABLE);
                log.info("Updating the role details for roleId {} ",roleId);
                Role role = optionalRole.get();
                role.setIsDisable(value);
                return roleRepository.save(role);
            } else {
                throw new HotelManagementDataNotFoundException("Data Not Found");
            }

        } catch (IllegalArgumentException iae) {
            log.error("Invalid data provided either roleId is null or invalid ",iae);
            throw iae;
        } catch (HotelManagementDataNotFoundException dnfe) {
            log.error("Data not found for roleId {} in db",roleId);
            throw dnfe;
        } catch(Exception e){
            log.error("Error occurred while updating role details for roleId {} ",roleId,e);
            throw new HotelManagementException("Error occurred while updating the roleId: "+roleId,e);
        }
    }

    public void validateRoleId (Long roleId){
        if(roleId == null || roleId < 0) {
            throw new IllegalArgumentException("Invalid Data provided");
        }
    }
}
