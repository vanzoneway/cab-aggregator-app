package com.modsen.ratingservice.model.general;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Rating {

    public static final String SEQUENCE_GENERATOR = "seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR)
    @Column(nullable = false)
    private Long id;

    protected String comment;

    protected Integer rating;

    @Column(unique = true, nullable = false)
    protected Long rideId;

    protected Long refUserId;

    protected Boolean deleted;

}
