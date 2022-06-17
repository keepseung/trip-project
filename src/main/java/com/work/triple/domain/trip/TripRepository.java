package com.work.triple.domain.trip;

import com.work.triple.domain.city.City;
import com.work.triple.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByUserAndCity(User user, City city);
    Optional<Trip> findByUserAndId(User user, Long id);


    @Query("select t from Trip t where t.user = :user and t.startDate <= :now and t.endDate >= :now order by t.startDate asc ")
    List<Trip> findValidTrip(User user, LocalDate now, Pageable pageable);

    @Query("select t from Trip t where t.user = :user and t.startDate > :now and t.endDate > :now order by t.startDate asc ")
    List<Trip> findNotStartTrip(User user, LocalDate now, Pageable pageable);
}
