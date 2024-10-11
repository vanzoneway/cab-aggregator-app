package com.modsen.ratingservice.model;

import com.modsen.ratingservice.model.general.Rating;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@SequenceGenerator(name = Rating.SEQUENCE_GENERATOR, sequenceName = "driver_rating_seq", allocationSize = 1)
public class DriverRating extends Rating implements Serializable {
}