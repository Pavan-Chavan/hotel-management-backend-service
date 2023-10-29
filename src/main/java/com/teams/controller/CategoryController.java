package com.teams.controller;

import com.teams.entity.Category;
import com.teams.entity.FoodItem;
import com.teams.entity.models.CategoryModel;
import com.teams.exception.HotelManagementException;
import com.teams.service.CategoryService;
import com.teams.service.FoodItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/1.0/category")
@Api(value = "category Apis",description = "Rest APIs to perform category related actions")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @ApiOperation(value = "Save category",produces = "application/json")
    @PostMapping("/saveCategory")
    public ResponseEntity saveCategory(@RequestBody CategoryModel categoryModel){
        try{
            return new ResponseEntity(categoryService.saveCategory(categoryModel),HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Get category list",produces = "application/json")
    @GetMapping("/getCategories")
    public ResponseEntity getCategories(@RequestParam(name = "offset",defaultValue = "5") Integer offset,
                                   @RequestParam(name = "pageNo",defaultValue = "0") Integer pageNumber,
                                   @RequestParam(name = "order", defaultValue = "ASC") String order,
                                   @RequestParam(name = "categoryId",required = false,defaultValue = "-1") Long categoryId){
        try{
            return new ResponseEntity(categoryService.getCategories(offset,pageNumber,order,categoryId), HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Delete Category")
    @ApiImplicitParam(name = "categoryId",dataType = "Long",required = true, paramType = "query",
            value = "categoryId should be valid categoryId")
    @DeleteMapping("/deleteCategory")
    public ResponseEntity<String> deleteRole(@RequestParam Long categoryId){
        try{
            return new ResponseEntity(categoryService.deleteCategories(categoryId),HttpStatus.OK);
        }catch (Exception he){
            throw new HotelManagementException(he.getMessage());
        }
    }

    @ApiOperation(value = "Update category")
    @PutMapping("/updateCategory")
    public ResponseEntity updateFoodItem(@RequestBody CategoryModel categoryModel) {
        try {
            return new ResponseEntity(categoryService.updateCategory(categoryModel),HttpStatus.OK);
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
