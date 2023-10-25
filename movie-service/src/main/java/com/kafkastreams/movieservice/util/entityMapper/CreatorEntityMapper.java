package com.kafkastreams.movieservice.util.entityMapper;

import com.kafkastreams.movieservice.api.response.Creator;
import com.kafkastreams.movieservice.api.request.AddCreatorReq;
import com.kafkastreams.movieservice.entity.CreatorEntity;
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
