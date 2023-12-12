package com.teams.service;

import com.teams.entity.Role;
import com.teams.exception.HotelManagementException;
import com.teams.entity.Permission;
import com.teams.repository.PermissionRepository;
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

import java.util.Optional;

import static com.teams.constant.HoteManagementConstants.DISABLE;
import static com.teams.constant.HoteManagementConstants.TOTAL_RECORD;

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
        String permissionName =  permissionDto .getPermissionName();
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

    public ResponseEntity getPermissions(Integer offset,Integer pageNumber,String order,Long permissionId) {
        try{
            HttpHeaders headers = new HttpHeaders();

            if(permissionId != -1){
                log.info("Retrieving the permission for permissionId: {}",permissionId);
                Permission permission = permissionRepository.findById(permissionId).get();
                return new ResponseEntity(permission,HttpStatus.OK);
            }
            log.info("Retrieving the permission list..");
            Sort sort = order.equals("ASC")?Sort.by("createdAt").ascending():Sort.by("createdAt").descending();
            Pageable paging = PageRequest.of(pageNumber,offset, sort);
            Page<Permission> totalRecords = permissionRepository.findAll(paging);
            headers.add(TOTAL_RECORD,String.valueOf(totalRecords.getTotalElements()));
            return new ResponseEntity(totalRecords.getContent(), headers, HttpStatus.OK);
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
