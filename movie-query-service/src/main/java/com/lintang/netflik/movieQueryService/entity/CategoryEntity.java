package com.lintang.netflik.movieQueryService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {
    @Id
    private int id;

    @NotNull(message = "category name is required")
    private String name;

    public int getId() {
        return id;
    }

    public CategoryEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryEntity setName(String name) {
        this.name = name;
        return this;
    }
}
