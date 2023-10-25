package com.kafkastreams.movieservice.api.server;


import com.kafkastreams.movieservice.api.request.AddTagReq;
import com.kafkastreams.movieservice.api.response.Tag;
import com.kafkastreams.movieservice.command.service.TagCommandService;
import com.kafkastreams.movieservice.util.entityMapper.TagEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/tags")
public class TagController {
    private TagEntityMapper tagEntityMapper;


    private TagCommandService tagCommandService;


    @Autowired
    public TagController(TagEntityMapper tagEntityMapper, TagCommandService tagCommandService) {
        this.tagEntityMapper = tagEntityMapper;
        this.tagCommandService = tagCommandService;
    }

    @PostMapping
    public ResponseEntity<Tag>  addTag(@RequestBody AddTagReq newTag) {
        return ok(tagEntityMapper.toDto(tagCommandService.addTag(newTag)));
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<Tag> updateTagById(@PathVariable(value = "tagId") int tagId, @RequestBody AddTagReq updateTag) {
        return ok(tagEntityMapper.toDto(tagCommandService.updateTag(tagId, updateTag)));
    }



}
