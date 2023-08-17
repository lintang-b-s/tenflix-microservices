package com.lintang.netflik.movieservice.util.entityMapper;


import com.lintang.netflik.movieservice.api.request.AddMovieReq;
import com.lintang.netflik.movieservice.api.response.Tag;
import com.lintang.netflik.movieservice.entity.TagEntity;
import org.springframework.stereotype.Component;

@Component
public class TagEntityMapper {

    public TagEntity toEntity(Tag m) {
        TagEntity tag = TagEntity.builder()
                .id(m.getId()).name(m.getName())
                .build();
        return tag;
    }

    public Tag toDto(TagEntity m) {
        Tag tag = Tag.builder()
                .id(m.getId()).name(m.getName())
                .build();
        return tag;
    }


    public TagEntity toEntity(String m) {
        TagEntity tag = TagEntity.builder()
                .name(m)
                .build();
        return tag;
    }
}
