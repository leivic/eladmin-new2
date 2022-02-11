package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.ConsiciousStation;
import me.zhengjie.modules.qe.domain.EnvironmentBaseZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsiciousStationRepository extends JpaRepository<ConsiciousStation,Integer> {

    @Query(value = "select * from consicious_station " ,nativeQuery = true)
    Page<ConsiciousStation> findAll1(Pageable pageable);

    @Query(value = "select * from consicious_station  where " +"zone = :zone and"+" date = :date",nativeQuery = true)
    List<ConsiciousStation> findAllByZoneAndDate(@Param("zone") String zone, @Param("date") String date);

}
