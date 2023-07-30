package com.lintang.netflik.movieQueryService.command.service;

import com.lintang.netflik.movieQueryService.broker.message.UpdateCategoryMessage;
import com.lintang.netflik.movieQueryService.command.action.CategoryCommandAction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class CategoryCommandService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryCommandService.class);

    @Autowired
    private CategoryCommandAction categoryCommandAction;
    public void updateCategory(UpdateCategoryMessage message) {
        categoryCommandAction.updateCategory(message);
        return ;

    }
}
