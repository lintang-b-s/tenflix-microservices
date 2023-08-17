package com.lintang.netflik.movieservice.api.server;

import com.lintang.netflik.movieservice.api.request.AddCategoryReq;
import com.lintang.netflik.movieservice.api.request.AddTagReq;
import com.lintang.netflik.movieservice.api.response.Category;
import com.lintang.netflik.movieservice.api.response.Tag;
import com.lintang.netflik.movieservice.command.service.CategoryCommandService;
import com.lintang.netflik.movieservice.command.service.TagCommandService;
import com.lintang.netflik.movieservice.util.entityMapper.CategoryEntityMapper;
import com.lintang.netflik.movieservice.util.entityMapper.TagEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/categories")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_default-roles-tenflix')")
public class CategoryController {
    private CategoryEntityMapper categoryEntityMapper;

    @Autowired
    private CategoryCommandService categoryCommandService;

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody AddCategoryReq newCategory) {
        return ok(categoryEntityMapper.toDto(categoryCommandService.addCategory(newCategory)));
    }

    @PutMapping("/{categoryid}")
    public ResponseEntity<Category> updateCategoryById(@PathVariable(value = "categoryid") int tagId, @RequestBody AddCategoryReq updateCategory) {
        return ok(categoryEntityMapper.toDto(categoryCommandService.updateCategory(tagId, updateCategory)));
    }
}
