package com.lintang.netflik.movieQueryService.command.service;


import com.lintang.netflik.movieQueryService.broker.message.UpdateCreatorMessage;
import com.lintang.netflik.movieQueryService.command.action.CreatorCommandAction;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatorCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(CreatorCommandService.class);

    private CreatorCommandAction creatorCommandAction;

    @Autowired
    public CreatorCommandService(
        CreatorCommandAction creatorCommandAction
    ) {
        this.creatorCommandAction = creatorCommandAction;
    }

    public void updateCreator(UpdateCreatorMessage message) {
        creatorCommandAction.updateCreator(message);
        return;
    }

}
