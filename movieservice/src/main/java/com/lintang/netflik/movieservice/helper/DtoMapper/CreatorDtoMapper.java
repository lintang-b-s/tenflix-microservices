package com.lintang.netflik.movieservice.helper.DtoMapper;

import com.lintang.netflik.movieservice.dto.Actor;
import com.lintang.netflik.movieservice.dto.Creator;
import com.lintang.netflik.movieservice.entity.ActorEntity;
import com.lintang.netflik.movieservice.entity.CreatorEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Component
public class CreatorDtoMapper {
    public Creator creatorEntityToCreatorDto(CreatorEntity entity) {
        Creator creator = new Creator();
        creator.setId(entity.getId());creator.setName(entity.getName());
        return creator;
    }

    public List<Creator> toListModel(Iterable<CreatorEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(entities.spliterator(), false).map(e -> creatorEntityToCreatorDto(e))
                .collect(toList());
    }


}
