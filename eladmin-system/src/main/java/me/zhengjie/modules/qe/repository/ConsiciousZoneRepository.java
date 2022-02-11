package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.ConsiciousZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsiciousZoneRepository extends JpaRepository<ConsiciousZone,Integer> {

    @Query(value = "select * from consicious_zone " ,nativeQuery = true)
    Page<ConsiciousZone> findAll1(Pageable pageable);

    @Query(value = "select * from consicious_zone  where " +"zone = :zone and"+" date = :date",nativeQuery = true)
    List<ConsiciousZone> findAllByZoneAndDate(@Param("zone") String zone, @Param("date") String date);

    @Query(value = "select * from consicious_zone  where " +" date = :date",nativeQuery = true)
    List<ConsiciousZone> findAllByDate( @Param("date") String date);
}
