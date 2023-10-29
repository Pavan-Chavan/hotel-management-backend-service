package com.teams.controller;

import com.teams.entity.Table;
import com.teams.entity.models.CategoryModel;
import com.teams.exception.HotelManagementException;
import com.teams.service.CategoryService;
import com.teams.service.TableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/1.0/table")
@Api(value = "table Apis",description = "Rest APIs to perform table related actions")
public class TableController {
    @Autowired
    TableService tableService;

    @ApiOperation(value = "Save table",produces = "application/json")
    @PostMapping("/saveTable")
    public ResponseEntity saveTable(@RequestBody Table table){
        try{
            return new ResponseEntity(tableService.saveTable(table),HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Get table list",produces = "application/json")
    @GetMapping("/getTables")
    public ResponseEntity getCategories(@RequestParam(name = "offset",defaultValue = "5") Integer offset,
                                   @RequestParam(name = "pageNo",defaultValue = "0") Integer pageNumber,
                                   @RequestParam(name = "order", defaultValue = "ASC") String order,
                                   @RequestParam(name = "tableId",required = false,defaultValue = "-1") Long tableId){
        try{
            return new ResponseEntity(tableService.getTable(offset,pageNumber,order,tableId), HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Delete table")
    @ApiImplicitParam(name = "tableId",dataType = "Long",required = true, paramType = "query",
            value = "tableId should be valid table ID")
    @DeleteMapping("/deleteTableId")
    public ResponseEntity<String> deleteRole(@RequestParam Long tableId){
        try{
            return new ResponseEntity(tableService.deleteTable(tableId),HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Update table")
    @PutMapping("/updateTable")
    public ResponseEntity updateFoodItem(@RequestBody Table table) {
        try {
            return new ResponseEntity(tableService.updateTable(table),HttpStatus.OK);
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
