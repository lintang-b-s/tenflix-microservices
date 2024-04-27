package com.kafkastreams.movieservice.api.server;

import com.kafkastreams.movieservice.api.response.Creator;
import com.kafkastreams.movieservice.command.service.CreatorCommandService;
import com.kafkastreams.movieservice.util.DtoMapper.CreatorDtoMapper;
import com.kafkastreams.movieservice.api.request.AddCreatorReq;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 *
 * @author lintang birda s
 */
@RestController
@PreAuthorize("hasAuthority('ROLE_user')")
@RequestMapping("/api/v1/movie-service/creators")
public class CreatorController {
    private static final Logger log = LoggerFactory.getLogger(CreatorController.class);
    private CreatorCommandService creatorService;


    private CreatorDtoMapper mapper;

    @Autowired
    public CreatorController(CreatorCommandService creatorService, CreatorDtoMapper mapper) {
        this.creatorService = creatorService;
        this.mapper = mapper;
    }


    /**  this method is for add director
     * @param newCreator movie director name
     * @return the director
     * @author lintang birda s
     */
    @PostMapping
    public ResponseEntity<Creator> addCreator(@RequestBody AddCreatorReq newCreator) {
        return ok(mapper.creatorEntityToCreatorDto(creatorService.addCreator(newCreator)));
    }


    /** thid method for updating director
     * @param creatorId director id
     * @param newCreator new director
     * @return
     * @author lintang birda s
     */
    @PutMapping("/{creatorId}")
    public ResponseEntity<Creator> updateCreator(@PathVariable int creatorId,@RequestBody AddCreatorReq newCreator ) {
        return creatorService.updateCreator(creatorId, newCreator).map(mapper::creatorEntityToCreatorDto).map(ResponseEntity::ok)
                .orElse(notFound().build());
    }
}
