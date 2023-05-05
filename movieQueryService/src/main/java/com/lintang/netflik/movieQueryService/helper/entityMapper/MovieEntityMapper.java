package com.lintang.netflik.movieQueryService.helper.entityMapper;

import com.lintang.netflik.movieQueryService.dto.AddMovieReq;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.event.AddMovieEvent;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class MovieEntityMapper {


    public MovieEntity toEntity(AddMovieReq model) {
        MovieEntity entity = new MovieEntity();
        Timestamp modelTimestamp = Timestamp.valueOf(model.getrYear().atStartOfDay());
        MovieEntity newEntity= entity.setName(model.getName()).setType(model.getType())
                .setSynopsis(model.getSynopsis()).setMpaRating(model.getMpaRating())
                .setrYear(modelTimestamp).setIdmbRating(model.getIdmbRating())
                .setImage(model.getImage());
        return newEntity;
    }


    public MovieEntity addMovieEventToEntity(AddMovieEvent movieEvent) {
        MovieEntity entity = new MovieEntity();
        Timestamp modelTimestamp = Timestamp.valueOf(movieEvent.getrYear().atStartOfDay());
        MovieEntity newEntity = entity.setId(movieEvent.getId())
                .setName(movieEvent.getName())
                .setType(movieEvent.getType())
                .setSynopsis(movieEvent.getSynopsis())
                .setMpaRating(movieEvent.getMpaRating())
                .setrYear(modelTimestamp)
                .setIdmbRating(movieEvent.getIdmbRating())
                .setImage(movieEvent.getImage());
        return newEntity;
    }
}
