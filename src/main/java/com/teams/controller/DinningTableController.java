package com.teams.controller;

import com.teams.entity.DinningTable;
import com.teams.entity.models.DinningTableModel;
import com.teams.exception.HotelManagementException;
import com.teams.service.DinningTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;




@RestController
@RequestMapping("/1.0")
@Api(value = "table Apis",description = "Rest APIs to perform table related actions")
public class DinningTableController {
    @Autowired
    DinningTableService dinningTableService;

    @ApiOperation(value = "Save table",produces = "application/json")
    @PostMapping("/table")
    public ResponseEntity saveTable(@RequestBody DinningTableModel dinningTableModel){
        try{
            return new ResponseEntity(dinningTableService.saveTable(dinningTableModel),HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Get table list",produces = "application/json")
    @GetMapping("/tables")
    public ResponseEntity getCategories(@RequestParam(name = "offset",defaultValue = "5") Integer offset,
                                   @RequestParam(name = "pageNo",defaultValue = "0") Integer pageNumber,
                                   @RequestParam(name = "order", defaultValue = "ASC") String order,
                                   @RequestParam(name = "tableId",required = false,defaultValue = "-1") Long tableId){
        try{
            return new ResponseEntity(dinningTableService.getTable(offset,pageNumber,order,tableId), HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Delete table")
    @ApiImplicitParam(name = "tableId",dataType = "Long",required = true, paramType = "query",
            value = "tableId should be valid table ID")
    @DeleteMapping("/table")
    public ResponseEntity<String> deleteRole(@RequestParam Long tableId){
        try{
            return new ResponseEntity(dinningTableService.deleteTable(tableId),HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Update table and can be use to assing waiter")
    @PutMapping("/table")
    public ResponseEntity updateFoodItem(@RequestBody DinningTableModel dinningTableModel) {
        try {
            return new ResponseEntity(dinningTableService.updateTable(dinningTableModel),HttpStatus.OK);
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
