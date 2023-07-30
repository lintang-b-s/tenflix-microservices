package com.lintang.netflik.movieservice.util.entityMapper;

import com.lintang.netflik.movieservice.api.request.AddMovieReq;
import com.lintang.netflik.movieservice.entity.MovieEntity;
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
}
