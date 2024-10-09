package com.modsen.ratingservice.dto.request;

import com.modsen.ratingservice.dto.Marker;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;


import java.io.Serializable;

public record RatingRequestDto(
        @NotBlank(groups = Marker.OnCreate.class, message = "{comment.blank}")
        String comment,

        @NotNull(groups = Marker.OnCreate.class, message = "{rating.null}")
        @Min(value = 1, groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{rating.size}")
        @Max(value = 5, groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "{rating.size}")
        Integer rating,

        @NotNull(groups = Marker.OnCreate.class, message = "{ride.id.null}")
        @Null(groups = Marker.OnUpdate.class, message = "{ride.id.not.null}")
        Long rideId) implements Serializable {
}
