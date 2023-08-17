package com.lintang.netflik.movieservice.command.action;

import com.lintang.netflik.movieservice.api.request.AddCategoryReq;
import com.lintang.netflik.movieservice.entity.CategoryEntity;
import com.lintang.netflik.movieservice.exception.ResourceNotFoundException;
import com.lintang.netflik.movieservice.repository.CategoryRepository;
import com.lintang.netflik.movieservice.util.entityMapper.CategoryEntityMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class CategoryCommandAction {

    private CategoryRepository categoryRepository;
    private CategoryEntityMapper categoryEntityMapper;

    public CategoryEntity save(AddCategoryReq newCategory) {
        CategoryEntity category= categoryRepository.save(categoryEntityMapper.toEntity(newCategory.getName()));
        return category;
    }


    public CategoryEntity update(int categoryId,AddCategoryReq newCategory) {

        Optional<CategoryEntity > category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new ResourceNotFoundException("category with id: " + categoryId + " not found " );
        }
        CategoryEntity categoryEntity = category.get();
        categoryEntity.setName(newCategory.getName());
        CategoryEntity updateCategory= categoryRepository.save(categoryEntity);
        return updateCategory;
    }

    public CategoryEntity findById(int categoryId) {
        return categoryRepository.findById(categoryId).get();
    }
}
