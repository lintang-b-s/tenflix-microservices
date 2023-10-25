package com.kafkastreams.movieservice.util.entityMapper;

import com.kafkastreams.movieservice.api.request.AddMovieReq;
import com.kafkastreams.movieservice.entity.MovieEntity;
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
                .setImage(model.getImage()).setNotification(model.getNotification());
        return newEntity;
    }
}
