package com.lintang.netflik.movieservice.helper.entityMapper;

import com.lintang.netflik.movieservice.dto.AddActorReq;
import com.lintang.netflik.movieservice.dto.AddCreatorReq;
import com.lintang.netflik.movieservice.dto.Creator;
import com.lintang.netflik.movieservice.entity.CreatorEntity;
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
