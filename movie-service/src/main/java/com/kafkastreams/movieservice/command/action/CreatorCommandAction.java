package com.kafkastreams.movieservice.command.action;


import com.kafkastreams.movieservice.api.request.AddCreatorReq;
import com.kafkastreams.movieservice.entity.CreatorEntity;
import com.kafkastreams.movieservice.exception.BadRequestException;
import com.kafkastreams.movieservice.exception.ResourceNotFoundException;
import com.kafkastreams.movieservice.repository.CreatorRepository;
import com.kafkastreams.movieservice.util.entityMapper.CreatorEntityMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@NoArgsConstructor
public class CreatorCommandAction {

    private CreatorRepository repository;

    private CreatorEntityMapper mapper;


    private CreatorEntityMapper creatorEntityMapper;

    @Autowired
    public CreatorCommandAction(CreatorRepository repository, CreatorEntityMapper mapper, CreatorEntityMapper creatorEntityMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.creatorEntityMapper = creatorEntityMapper;
    }

    /** this method is for save director to db
     * @param newCreator new director
     * @return
     *
     */
    public CreatorEntity addCreator(AddCreatorReq newCreator) {
        Optional<CreatorEntity> opCreator = repository.findByName(newCreator.getName());
        if (!opCreator.isEmpty()){
            throw new BadRequestException("Creator with name: " + newCreator.getName() + " already in database");
        }

       CreatorEntity cre =  repository.save(mapper.toEntity(newCreator));
        return cre;
    }

    public CreatorEntity saveCreator(String name){
        CreatorEntity creator = repository.save(creatorEntityMapper.toEntity(name));
        return creator;
    }

    public CreatorEntity getCreatorById(int creatorId) {
        Optional<CreatorEntity> creator = repository.findById(creatorId);

        return creator.get();
    }

    public Optional<CreatorEntity> updateCreator(int creatorId , AddCreatorReq newCreator) {
        Optional<CreatorEntity> creator = repository.findById(creatorId);
        if (!creator.isPresent()) {
           throw new  ResourceNotFoundException("creator with id : " + creatorId +  " not found" );
        }
        CreatorEntity updatedCreator = creator.get();
        updatedCreator.setName(newCreator.getName());
        return Optional.of(repository.save(updatedCreator));
    }
}
