package com.lintang.netflik.movieQueryService.command.service;


import com.lintang.netflik.movieQueryService.broker.message.UpdateActorMessage;
import com.lintang.netflik.movieQueryService.command.action.ActorCommandAction;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActorCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(ActorCommandService.class);


    private ActorCommandAction actorCommandAction;

    @Autowired
    public ActorCommandService(ActorCommandAction action) {
        this.actorCommandAction = action;
    }

    public void updateActor(UpdateActorMessage message) {
        actorCommandAction.updateActor(message);
        return;
    }
}
