package com.lintang.netflik.movieservice.command.action;

import com.lintang.netflik.movieservice.api.request.AddTagReq;
import com.lintang.netflik.movieservice.entity.CategoryEntity;
import com.lintang.netflik.movieservice.entity.TagEntity;
import com.lintang.netflik.movieservice.exception.ResourceNotFoundException;
import com.lintang.netflik.movieservice.repository.TagRepository;
import com.lintang.netflik.movieservice.util.entityMapper.TagEntityMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class TagCommandAction {

    private TagRepository tagRepository;
    private TagEntityMapper tagEntityMapper;

    public TagEntity save(AddTagReq newTag) {
        TagEntity tag = tagRepository.save(tagEntityMapper.toEntity(newTag.getName()));
        return tag;
    }

    public TagEntity update(int tagId, AddTagReq newTag) {
        Optional<TagEntity> tag = tagRepository.findById(tagId);
        if (!tag.isPresent()) {
            throw new ResourceNotFoundException("tag with id: " + tagId + " not found " );
        }
        TagEntity tagEntity = tag.get();
        tagEntity.setName(newTag.getName());
        TagEntity updateTag= tagRepository.save(tagEntity);
        return updateTag;
    }


    public TagEntity findById(int tagId) {
        return tagRepository.findById(tagId).get();
    }
}
