package com.teams.service;

import com.teams.entity.DiningTable;
import com.teams.entity.models.ResponseMessage;
import com.teams.exception.HotelManagementException;
import com.teams.repository.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author pachavan
 */
@Service
@Slf4j
public class TableService {

    @Autowired
    TableRepository tableRepository;

    public List<DiningTable> getTable(Integer offset, Integer pageNumber, String order, Long tableId) {
        try {
            List<DiningTable> diningTables = new ArrayList<>();
            if(tableId != -1) {
                log.info("fetching food item details with id {}",tableId);
                diningTables.add(tableRepository.findById(tableId).get());
            } else {
                log.info("fetching food item details");
                diningTables = tableRepository.findAll();
            }
            return diningTables;
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage saveTable(DiningTable diningTable) {
        try {
            log.info("Saving diningTable...");
            diningTable.setCreatedAt(new Date());
            tableRepository.save(diningTable);
            return new ResponseMessage("diningTable created Succefully");
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage deleteTable(Long tableId) {
        try {
            log.info("Deleting food item with id {}",tableId);
            Optional<DiningTable> table = tableRepository.findById(tableId);
            if(table.isPresent()) {
                tableRepository.deleteById(tableId);
                return new ResponseMessage(table.get().getTableName() + " delete succefully");
            } else {
                return new ResponseMessage("DiningTable not present");
            }
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage updateTable(DiningTable diningTable) {
        try {
            DiningTable existingTable = tableRepository.findById(diningTable.getTableId()).get();
            log.info("Updating diningTable name " + existingTable.getTableName());
            existingTable.setTableName(diningTable.getTableName());
            tableRepository.save(existingTable);
            log.info("Updating diningTable name " + existingTable.getTableName());
            return new ResponseMessage(existingTable.getTableName() + " updated succefully");
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
