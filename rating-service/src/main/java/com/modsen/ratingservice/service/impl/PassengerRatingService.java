package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.dto.UserType;
import com.modsen.ratingservice.mapper.ListContainerMapper;
import com.modsen.ratingservice.mapper.impl.PassengerRatingMapper;
import com.modsen.ratingservice.model.PassengerRating;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import com.modsen.ratingservice.service.general.AbstractRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class PassengerRatingService extends AbstractRatingService<PassengerRating, PassengerRatingRepository> {

    @Autowired
    public PassengerRatingService(PassengerRatingRepository repository,
                                  PassengerRatingMapper ratingMapper,
                                  ListContainerMapper listContainerMapper,
                                  MessageSource messageSource) {
        super(repository, ratingMapper, listContainerMapper, messageSource, String.valueOf(UserType.PASSENGER));
    }

}
