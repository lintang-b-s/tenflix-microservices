package com.lintang.netflik.movieservice.api.server;


import com.lintang.netflik.movieservice.api.request.AddTagReq;
import com.lintang.netflik.movieservice.api.response.Tag;
import com.lintang.netflik.movieservice.command.service.TagCommandService;
import com.lintang.netflik.movieservice.util.entityMapper.TagEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/movie-service/tags")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_default-roles-tenflix')")
public class TagController {
    private TagEntityMapper tagEntityMapper;

    @Autowired
    private TagCommandService tagCommandService;

    @PostMapping
    public ResponseEntity<Tag>  addTag(@RequestBody AddTagReq newTag) {
        return ok(tagEntityMapper.toDto(tagCommandService.addTag(newTag)));
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<Tag> updateTagById(@PathVariable(value = "tagId") int tagId, @RequestBody AddTagReq updateTag) {
        return ok(tagEntityMapper.toDto(tagCommandService.updateTag(tagId, updateTag)));
    }



}
