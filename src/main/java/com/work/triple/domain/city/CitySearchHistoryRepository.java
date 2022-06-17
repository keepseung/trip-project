package com.work.triple.domain.city;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CitySearchHistoryRepository extends JpaRepository<CitySearchHistory, Long> {
    @Query("select sh from CitySearchHistory sh where sh.createDate >= :createDate order by sh.createDate desc")
    List<CitySearchHistory> findHistory(LocalDateTime createDate, Pageable pageable);
}
