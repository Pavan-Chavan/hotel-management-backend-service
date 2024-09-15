package com.teams.service;

import com.teams.entity.models.HotelManagementListResponse;
import com.teams.exception.HotelManagementDataNotFoundException;
import com.teams.exception.HotelManagementException;
import com.teams.entity.Permission;
import com.teams.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static com.teams.constant.HoteManagementConstants.*;

/**
 * @author dgardi
 */
@Service
@Slf4j
public class PermissionService {


    @Autowired
    PermissionRepository permissionRepository;


    public Permission savePermission(Permission permissionDto) {
        String permissionName =  permissionDto.getPermissionName();
        Boolean isDisable = permissionDto.getIsDisable();
        Permission permission = new Permission();
        try{
            if(permissionDto.getPermissionId() != null) {
                Long permissionId = permissionDto.getPermissionId();
                validatePermissionId(permissionId);
                log.info("Fetching permission details for permissionId {}",permissionId);
                Optional<Permission> existingPermission = permissionRepository.findById(permissionId);

                if(existingPermission.isPresent()) {
                    log.info("Data found for permissionId {} in the database",permissionId);
                    permission = existingPermission.get();
                }
                permission.setIsDisable(isDisable);
                permission.setPermissionName(permissionName);
                permission.setCreatedAt(new Date());
                log.info("Saving Permission details for permissionName {}",permissionName);
                return permissionRepository.save(permission);
            } else {
                permissionDto.setCreatedAt(new Date());
                return permissionRepository.save(permissionDto);
            }
        } catch (IllegalArgumentException iae) {
            log.error("Invalid data provided either permissionId is null or invalid ",iae);
            throw iae;
        } catch(Exception e){
            log.error("Error occurred while saving data ",e);
            throw new HotelManagementException("Error occurred while saving permissions data");
        }
    }

    public HotelManagementListResponse getPermissions(Integer offset, Integer pageNumber, String order, Long permissionId) {
        try{
            Optional<Permission> permissionOptional = null;
            if(permissionId != -1){
                log.info("Retrieving the permission for permissionId: {}",permissionId);
                permissionOptional = permissionRepository.findById(permissionId);
            }

            if(permissionOptional != null && permissionOptional.isPresent()) {
                return HotelManagementListResponse.getResponse(permissionOptional.get(),pageNumber,pageNumber);
            } else {
                log.info("Retrieving the permission list..");
                Sort sort = order.equals(ASC)?Sort.by(CREATED_AT).ascending():Sort.by(CREATED_AT).descending();
                Pageable paging = PageRequest.of(pageNumber,offset, sort);
                Page<Permission> totalRecords = permissionRepository.findAll(paging);
                return HotelManagementListResponse.getResponse(totalRecords,pageNumber,offset);
            }

        } catch(Exception e){
            log.error("Error occurred while retrieving permission data ",e);
            throw new HotelManagementException("Error occurred while retrieving the data for permissions",e);
        }
    }




    @Transactional
    public void deletePermission(Long permissionId) {
        try{
            validatePermissionId(permissionId);
            Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);
            if(optionalPermission.isPresent()) {
                log.info("Deleting the permission having permissionId {}",permissionId);
                permissionRepository.deleteById(permissionId);
            } else {
                throw new HotelManagementDataNotFoundException("Data Not Found");
            }

        } catch (IllegalArgumentException iae) {
            log.error("Invalid data provided either permissionId is null or invalid ",iae);
            throw iae;
        } catch (HotelManagementDataNotFoundException dnfe) {
            log.error("Data not found for permissionId {} in db",permissionId);
            throw dnfe;
        } catch(Exception e){
            log.error("Error occurred while deleting permission details for permissionId {} ",permissionId,e);
            throw new HotelManagementException("Error occurred while deleting the permissionId: "+permissionId,e);
        }
    }

    public Permission updatePermissionStatus(Long permissionId, Status status) {
        try {
            validatePermissionId(permissionId);
            Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);
            if(optionalPermission.isPresent()) {
                boolean value = status.toString().equalsIgnoreCase(DISABLE);
                log.info("Updating the permission details for permissionId {} ",permissionId);
                Permission permission = optionalPermission.get();
                permission.setIsDisable(value);
                return permissionRepository.save(permission);
            } else {
                throw new HotelManagementDataNotFoundException("Data Not Found");
            }

        } catch (IllegalArgumentException iae) {
            log.error("Invalid data provided either permissionId is null or invalid ",iae);
            throw iae;
        } catch (HotelManagementDataNotFoundException dnfe) {
            log.error("Data not found for permissionId {} in db",permissionId);
            throw dnfe;
        } catch(Exception e){
            log.error("Error occurred while updating permission details for permissionId {} ",permissionId,e);
            throw new HotelManagementException("Error occurred while updating the permissionId: "+permissionId,e);
        }
    }

    public void validatePermissionId (Long permissionId){
        if(permissionId == null || permissionId < 0) {
            throw new IllegalArgumentException("Invalid Data provided");
        }
    }
}
