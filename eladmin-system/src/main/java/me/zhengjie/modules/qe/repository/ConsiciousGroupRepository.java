package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.ConsiciousGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsiciousGroupRepository extends JpaRepository<ConsiciousGroup,Integer> {

    @Query(value = "select * from consicious_group " ,nativeQuery = true)
    Page<ConsiciousGroup> findAll1(Pageable pageable);

    @Query(value = "select * from consicious_group  where " +"zone = :zone and"+" date = :date",nativeQuery = true)
    List<ConsiciousGroup> findAllByZoneAndDate(@Param("zone") String zone, @Param("date") String date);
}
