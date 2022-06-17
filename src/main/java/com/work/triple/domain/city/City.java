package com.work.triple.domain.city;

import com.work.triple.domain.BaseTimeEntity;
import com.work.triple.domain.trip.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter @NoArgsConstructor
@Builder @AllArgsConstructor
@Table(name = "CITY")
@Entity
public class City extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "city")
    private Trip trip;
}
