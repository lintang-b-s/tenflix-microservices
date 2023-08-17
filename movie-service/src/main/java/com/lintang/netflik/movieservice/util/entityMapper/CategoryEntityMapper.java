package com.lintang.netflik.movieservice.util.entityMapper;


import com.lintang.netflik.movieservice.api.response.Category;
import com.lintang.netflik.movieservice.api.response.Tag;
import com.lintang.netflik.movieservice.entity.CategoryEntity;
import com.lintang.netflik.movieservice.entity.TagEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityMapper {

    public CategoryEntity toEntity(Category m) {
        CategoryEntity category = CategoryEntity.builder()
                .id(m.getId()).name(m.getName())
                .build();
        return category;
    }

    public Category toDto(CategoryEntity m) {
        Category category = Category.builder()
                .id(m.getId()).name(m.getName())
                .build();
        return category;
    }

    public CategoryEntity toEntity(String m) {
        CategoryEntity category = CategoryEntity.builder()
               .name(m)
                .build();
        return category;
    }

}
