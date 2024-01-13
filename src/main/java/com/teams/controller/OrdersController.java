package com.teams.controller;

import com.teams.entity.models.CategoryModel;
import com.teams.entity.models.OrdersModel;
import com.teams.exception.HotelManagementException;
import com.teams.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author dgardi
 */

@RestController
@RequestMapping("/1.0")
@Api(value = "category Apis",description = "Rest APIs to perform orders related actions")
public class OrdersController {

    @Autowired
    OrderService orderService;

    @ApiOperation(value = "Save orders",produces = "application/json")
    @PostMapping("/order")
    public ResponseEntity saveOrderDetails(@RequestBody OrdersModel ordersModel){
        try{
            return new ResponseEntity(orderService.saveOrderDetails(ordersModel), HttpStatus.OK);
        } catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Update orders",produces = "application/json")
    @PutMapping("/order")
    public ResponseEntity updateCategory(@RequestBody OrdersModel ordersModel){
        try{
            return new ResponseEntity(orderService.updateOrderDetails(ordersModel), HttpStatus.OK);
        } catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Get current order details by order id",produces = "application/json")
    @GetMapping("/order")
    public ResponseEntity getOrder(@RequestParam(name = "orderId",required = false) UUID orderId){
        try{
            return new ResponseEntity(orderService.getOrder(orderId), HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }
}
