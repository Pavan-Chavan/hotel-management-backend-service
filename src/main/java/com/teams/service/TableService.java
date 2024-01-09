package com.teams.service;

import com.teams.entity.Category;
import com.teams.entity.SubUser;
import com.teams.entity.Table;
import com.teams.entity.models.CategoryModel;
import com.teams.entity.models.ResponseMessage;
import com.teams.exception.HotelManagementException;
import com.teams.repository.CategoryRepository;
import com.teams.repository.ManagementUserRepository;
import com.teams.repository.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author pachavan
 */
@Service
@Slf4j
public class TableService {

    @Autowired
    TableRepository tableRepository;

    @Autowired
    ManagementUserRepository managementUserRepository;

    public List<Table> getTable(Integer offset, Integer pageNumber, String order, Long tableId) {
        try {
            List<Table> tables = new ArrayList<>();
            if(tableId != -1) {
                log.info("fetching food item details with id {}",tableId);
                tables.add(tableRepository.findById(tableId).get());
            } else {
                log.info("fetching food item details");
                tables = tableRepository.findAll();
            }
            return tables;
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    //create table
    public ResponseMessage saveTable(Table table) {
        try {
            log.info("Saving table...");
            table.setCreatedAt(new Date());
            tableRepository.save(table);
            return new ResponseMessage("table created Succefully");
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage deleteTable(Long tableId) {
        try {
            log.info("Deleting food item with id {}",tableId);
            Optional<Table> table = tableRepository.findById(tableId);
            if(table.isPresent()) {
                tableRepository.deleteById(tableId);
                return new ResponseMessage(table.get().getTableName() + " delete succefully");
            } else {
                return new ResponseMessage("Table not present");
            }
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage updateTable(Long tableId, String status, UUID subUserId) {
        try {
            Table existingTable = tableRepository.findById(tableId).get();
            log.info("Updating table name " + existingTable.getTableName());
            SubUser subUser = managementUserRepository.findById(subUserId).get();
            existingTable.setSubUser(subUser);
            existingTable.setStatus(status);
            tableRepository.save(existingTable);
            log.info("Updating table name " + existingTable.getTableName());
            return new ResponseMessage(subUser.getSubUserId() + " assings to " + existingTable.getTableName() );
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
