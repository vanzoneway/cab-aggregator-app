package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.client.RideFeignClient;
import com.modsen.ratingservice.dto.UserType;
import com.modsen.ratingservice.mapper.impl.DriverRatingMapper;
import com.modsen.ratingservice.mapper.ListContainerMapper;
import com.modsen.ratingservice.model.DriverRating;

import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.service.general.AbstractRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class DriverRatingService extends AbstractRatingService<DriverRating, DriverRatingRepository> {

    @Autowired
    public DriverRatingService(DriverRatingRepository repository,
                               DriverRatingMapper ratingMapper,
                               ListContainerMapper listContainerMapper,
                               MessageSource messageSource,
                               RideFeignClient rideFeignClient) {
        super(repository, ratingMapper, listContainerMapper,
                messageSource,rideFeignClient, String.valueOf(UserType.DRIVER));
    }
}
