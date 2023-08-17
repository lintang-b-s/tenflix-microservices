package com.lintang.netflik.movieQueryService.command.service;


import com.lintang.netflik.movieQueryService.broker.message.UpdateTagMessage;
import com.lintang.netflik.movieQueryService.command.action.TagCommandAction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class TagCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(TagCommandService.class);

    @Autowired
    private TagCommandAction tagCommandAction;

    public void updateTag(UpdateTagMessage tagMessage) {
        tagCommandAction.updateTag(tagMessage);
        return ;
    }

}
