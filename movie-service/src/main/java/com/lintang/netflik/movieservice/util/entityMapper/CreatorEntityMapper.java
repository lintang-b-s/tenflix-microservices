package com.lintang.netflik.movieservice.util.entityMapper;

import com.lintang.netflik.movieservice.api.request.AddCreatorReq;
import com.lintang.netflik.movieservice.api.response.Creator;
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

    public CreatorEntity toEntity(String m) {
        CreatorEntity entity = new CreatorEntity();
        return entity.setName(m);
    }
}
