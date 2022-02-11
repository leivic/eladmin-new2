package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.ConsiciousWorkshop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsiciousWorkshopRepository extends JpaRepository<ConsiciousWorkshop,Integer> {

    @Query(value = "select * from consicious_workshop " ,nativeQuery = true)
    Page<ConsiciousWorkshop> findAll1(Pageable pageable);

    @Query(value = "select * from consicious_workshop  where " +"zone = :zone and"+" date = :date",nativeQuery = true)
    List<ConsiciousWorkshop> findAllByZoneAndDate(@Param("zone") String zone, @Param("date") String date);
}
