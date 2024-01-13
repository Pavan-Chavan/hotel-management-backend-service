package com.teams.service;

import com.teams.entity.DinningTable;
import com.teams.entity.SubUser;
import com.teams.entity.models.DinningTableModel;
import com.teams.entity.models.ResponseMessage;
import com.teams.exception.HotelManagementException;
import com.teams.repository.ManagementUserRepository;
import com.teams.repository.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.teams.entity.models.DinningTableModel.TableEnum.AVAILABLE;

/**
 * @author pachavan
 */
@Service
@Slf4j
public class DinningTableService {

    @Autowired
    TableRepository tableRepository;

    @Autowired
    ManagementUserRepository managementUserRepository;

    public List<DinningTable> getTable(Integer offset, Integer pageNumber, String order, Long tableId) {
        try {
            if(tableId != -1) {
                log.info("fetching food item details with id {}",tableId);
                return Collections.singletonList(tableRepository.findById(tableId).get());
            } else {
                log.info("fetching food item details");
                return tableRepository.findAll();
            }
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    //create table
    public ResponseMessage saveTable(DinningTableModel dinningTableModel) {
        try {
            DinningTable dinningTable = new DinningTable();
            dinningTable.setStatus(AVAILABLE.toString());
            dinningTable.setTableName(dinningTableModel.getTableName());
            dinningTable.setCreatedAt(new Date());
            dinningTable = tableRepository.save(dinningTable);
            return new ResponseMessage("Dinning Table Created with tableId "+dinningTable.getTableId(),dinningTable.getTableId());
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage deleteTable(Long tableId) {
        try {
            log.info("Deleting food item with id {}",tableId);
            Optional<DinningTable> table = tableRepository.findById(tableId);
            if(table.isPresent()) {
                tableRepository.deleteById(tableId);
                return new ResponseMessage(table.get().getTableName() + " delete successfully");
            } else {
                return new ResponseMessage("Table not present");
            }
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage updateTable(DinningTableModel dinningTableModel) {
        try {
            DinningTable existingDinningTable = tableRepository.findById(dinningTableModel.getTableId()).get();
            log.info("Updating table name " + existingDinningTable.getTableName());
            SubUser subUser = managementUserRepository.findById(dinningTableModel.getSubUserId()).get();
            existingDinningTable.setSubUser(subUser);
            existingDinningTable.setStatus(dinningTableModel.getStatus());
            tableRepository.save(existingDinningTable);
            log.info("Updating table name " + existingDinningTable.getTableName());
            return new ResponseMessage(subUser.getSubUserId() + " assigns to " + existingDinningTable.getTableName());
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
