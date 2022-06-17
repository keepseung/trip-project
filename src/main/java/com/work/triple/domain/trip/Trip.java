package com.work.triple.domain.trip;

import com.work.triple.domain.BaseTimeEntity;
import com.work.triple.domain.city.City;
import com.work.triple.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @NoArgsConstructor
@Builder @AllArgsConstructor
@Table(name = "TRIP")
@Entity
public class Trip extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "START_DATE")
    private LocalDate startDate;

    @Column(nullable = false, name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToOne
    @JoinColumn(name = "CITY_ID")
    private City city;
}
