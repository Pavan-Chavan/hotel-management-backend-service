package com.teams.service;

import com.teams.entity.Category;
import com.teams.entity.Table;
import com.teams.entity.models.CategoryModel;
import com.teams.entity.models.ResponseMessage;
import com.teams.exception.HotelManagementException;
import com.teams.repository.CategoryRepository;
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

    public ResponseMessage updateTable(Table table) {
        try {
            Table existingTable = tableRepository.findById(table.getTableId()).get();
            log.info("Updating table name " + existingTable.getTableName());
            existingTable.setTableName(table.getTableName());
            tableRepository.save(existingTable);
            log.info("Updating table name " + existingTable.getTableName());
            return new ResponseMessage(existingTable.getTableName() + " updated succefully");
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
