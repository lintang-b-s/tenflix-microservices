package com.lintang.netflik.movieQueryService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagEntity {
    @Id
    private int id;

    @NotNull(message = "tag name is required")

    private String name;

    public int getId() {
        return id;
    }

    public TagEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TagEntity setName(String name) {
        this.name = name;
        return this;
    }


    @Override
    public String toString() {
        return "TagEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
