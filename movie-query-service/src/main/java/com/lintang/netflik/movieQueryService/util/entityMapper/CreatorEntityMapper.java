package com.lintang.netflik.movieQueryService.util.entityMapper;

import com.lintang.netflik.movieQueryService.dto.AddCreatorReq;
import com.lintang.netflik.movieQueryService.entity.CreatorEntity;
import org.springframework.stereotype.Component;

@Component
public class CreatorEntityMapper {
    public CreatorEntity toEntity(AddCreatorReq m) {
        CreatorEntity entity = new CreatorEntity();
        return entity.setName(m.getName());
    }

    public CreatorEntity creatorDtotoCreatorEntity(Creator m) {
        CreatorEntity entity = new CreatorEntity();
        return entity.setName(m.getName()).setId(m.getId());
    }
}
