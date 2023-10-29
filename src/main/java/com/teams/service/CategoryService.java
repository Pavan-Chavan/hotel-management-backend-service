package com.teams.service;

import com.teams.entity.Category;
import com.teams.entity.FoodItem;
import com.teams.entity.models.CategoryModel;
import com.teams.entity.models.ResponseMessage;
import com.teams.exception.HotelManagementException;
import com.teams.repository.CategoryRepository;
import com.teams.repository.FoodItemRepository;
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
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getCategories(Integer offset, Integer pageNumber, String order, Long categoryId) {
        try {
            List<Category> categories = new ArrayList<>();
            if(categoryId != -1) {
                log.info("fetching food item details with id {}",categoryId);
                categories.add(categoryRepository.findById(categoryId).get());
            } else {
                log.info("fetching food item details");
                categories = categoryRepository.findAll();
            }
            return categories;
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage saveCategory(CategoryModel categoryModel) {
        try {
            Category category = new Category();

            category.setName(categoryModel.getCategoryName());
            category.setCreatedAt(new Date());

            log.info("Saving category...");
            categoryRepository.save(category);
            return new ResponseMessage("Item Saved Succefully");
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage deleteCategories(Long categoryId) {
        try {
            log.info("Deleting food item with id {}",categoryId);
            Optional<Category> category = categoryRepository.findById(categoryId);
            if(category.isPresent()) {
                categoryRepository.deleteById(categoryId);
                return new ResponseMessage(category.get().getName() + " delete succefully");
            } else {
                return new ResponseMessage("Unable to delete");
            }
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }

    public ResponseMessage updateCategory(CategoryModel categoryModel) {
        try {
            Category existingCategory = categoryRepository.findById(categoryModel.getCatergoryId()).get();
            existingCategory.setName(categoryModel.getCategoryName());
            categoryRepository.save(existingCategory);
            return new ResponseMessage(existingCategory.getName() + "updated succefully");
        } catch (Exception e) {
            throw new HotelManagementException(e.getMessage());
        }
    }
}
