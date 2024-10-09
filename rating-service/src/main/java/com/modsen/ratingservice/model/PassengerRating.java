package com.modsen.ratingservice.model;

import com.modsen.ratingservice.model.general.Rating;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
public class PassengerRating extends Rating implements Serializable {
}
