package com.teams.service;

import com.teams.entity.FoodItem;
import com.teams.entity.models.ResponseMessage;
import com.teams.exception.HotelManagementException;
import com.teams.repository.FoodItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author pachavan
 */
@Service
@Slf4j
public class FoodItemService {

    @Autowired
    FoodItemRepository foodItemRepository;

    public List<FoodItem> getFoodItems(Integer offset, Integer pageNumber, String order, Long foodItemId) {
        try {
            List<FoodItem> foodItems = new ArrayList<>();
            if(foodItemId != -1) {
                log.info("fetching food item details with id {}",foodItemId);
                foodItems.add(foodItemRepository.findById(foodItemId).get());
            } else {
                log.info("fetching food item details");
                foodItems = foodItemRepository.findAll();
            }
            return foodItems;
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage saveFoodItem(FoodItem foodItem) {
        try {
            foodItem.setCreatedAt(new Date());
            log.info("Saving food item");
            foodItemRepository.save(foodItem);
            return new ResponseMessage("Item Saved Succefully");
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage deleteFoodItem(Long foodItemId) {
        try {
            log.info("Deleting food item with id {}",foodItemId);
            Optional<FoodItem> foodItem = foodItemRepository.findById(foodItemId);
            if(foodItem.isPresent()) {
                foodItemRepository.deleteById(foodItemId);
                return new ResponseMessage("Food Item " + foodItem.get().getName() + "delete succefully");
            } else {
                return new ResponseMessage("Unable to delete");
            }
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage updateFoodItem(FoodItem foodItem) {
        try {
            FoodItem existingFoodItem = foodItemRepository.findById(foodItem.getFoodItemId()).get();
            existingFoodItem.setName(foodItem.getName());
            existingFoodItem.setPrice(foodItem.getPrice());
            foodItemRepository.save(existingFoodItem);
            return new ResponseMessage(existingFoodItem.getName() + "updated succefully");
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
