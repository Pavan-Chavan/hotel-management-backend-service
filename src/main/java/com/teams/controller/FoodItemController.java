package com.teams.controller;

import com.teams.entity.FoodItem;
import com.teams.entity.Role;
import com.teams.entity.models.FoodItemModel;
import com.teams.exception.HotelManagementException;
import com.teams.service.FoodItemService;
import com.teams.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;


@RestController
@RequestMapping("/1.0/foodItem")
@Api(value = "Food Item Apis",description = "Rest APIs to perform food item related actions")
public class FoodItemController {
    @Autowired
    FoodItemService foodItemService;

    @ApiOperation(value = "Save food item",produces = "application/json")
    @PostMapping("/saveFoodItem")
    public ResponseEntity saveRole(@RequestBody FoodItemModel foodItemModel){
        try{
            return new ResponseEntity(foodItemService.saveFoodItem(foodItemModel),HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Get food item list",produces = "application/json")
    @GetMapping("/getFoodItems")
    public ResponseEntity getFoodItems(@RequestParam(name = "offset",defaultValue = "5") Integer offset,
                                   @RequestParam(name = "pageNo",defaultValue = "0") Integer pageNumber,
                                   @RequestParam(name = "order", defaultValue = "ASC") String order,
                                   @RequestParam(name = "foodItemId",required = false,defaultValue = "-1") Long foodItemId){
        try{
            return new ResponseEntity(foodItemService.getFoodItems(offset,pageNumber,order,foodItemId), HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Delete food item from list")
    @ApiImplicitParam(name = "foodItemId",dataType = "Long",required = true, paramType = "query",
            value = "foodItemId should be valid food item id")
    @DeleteMapping("/deleteFoodItemId")
    public ResponseEntity<String> deleteRole(@RequestParam Long foodItemId){
        try{
            return new ResponseEntity(foodItemService.deleteFoodItem(foodItemId),HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Upate food item")
    @PutMapping("/updateFoodItem")
    public ResponseEntity updateFoodItem(@RequestBody FoodItemModel foodItemModel) {
        try {
            return new ResponseEntity(foodItemService.updateFoodItem(foodItemModel),HttpStatus.OK);
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
