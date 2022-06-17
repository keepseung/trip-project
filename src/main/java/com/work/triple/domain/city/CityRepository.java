package com.work.triple.domain.city;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);

    @Query("select c from City c where c.createDate >= :createDate order by c.createDate desc ")
    List<City> findCity(LocalDateTime createDate, Pageable pageable);


    @Query("select c from City c where c.id in :idList")
    List<City> findCityRandom(List<Long> idList);
}
