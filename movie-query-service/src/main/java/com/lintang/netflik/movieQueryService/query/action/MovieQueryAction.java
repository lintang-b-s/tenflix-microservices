package com.lintang.netflik.movieQueryService.query.action;


import com.lintang.netflik.models.GetActiveSubscriptionDto;
import com.lintang.netflik.models.GetUserCurrentSubscriptionRequest;
import com.lintang.netflik.models.SubscriptionServiceGrpc;
import com.lintang.netflik.movieQueryService.entity.MovieEntity;
import com.lintang.netflik.movieQueryService.exception.ResourceNotFoundException;
import com.lintang.netflik.movieQueryService.repository.MovieRepository;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.NoArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Component
public class MovieQueryAction {


    private MovieRepository repository;

    @GrpcClient("subscriptionService")
    private SubscriptionServiceGrpc.SubscriptionServiceBlockingStub subscriptionStub;

    @Autowired
    public MovieQueryAction(
         MovieRepository repository
    ) {
        this.repository = repository;
    }

    public MovieEntity getMovieById(int movieId, String userId) {
        GetUserCurrentSubscriptionRequest req =
                GetUserCurrentSubscriptionRequest.newBuilder().setGetSubscriptionRequestDto(
                                GetActiveSubscriptionDto.newBuilder().setUserId(userId)
                                        .build()
                        )
                        .build();
        try{
            this.subscriptionStub.getUserCurrentSubscription(req);

        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.FAILED_PRECONDITION){
                throw new ResourceNotFoundException(e.getMessage());
            }
        }
        Optional<MovieEntity> entity = repository.findById(movieId);
        if (!entity.isPresent()) {
            throw new ResourceNotFoundException("movie with id : " + movieId + " not found!") ;
        }
        MovieEntity movie = entity.get();
        return movie;
    }

    public List<MovieEntity> getAllMovie(String userId) {
        GetUserCurrentSubscriptionRequest req =
                GetUserCurrentSubscriptionRequest.newBuilder().setGetSubscriptionRequestDto(
                                GetActiveSubscriptionDto.newBuilder().setUserId(userId)
                                        .build()
                        )
                        .build();
        try{
            this.subscriptionStub.getUserCurrentSubscription(req);

        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.FAILED_PRECONDITION){
                throw new ResourceNotFoundException(e.getMessage());
            }
        }
        List<MovieEntity> movies = repository.findAll();

        return movies;
    }
}
