package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.ResponsibilityDatasource1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface ResponsibilityDatasource1Repository extends JpaRepository<ResponsibilityDatasource1,Integer> { //继承JpaRepository 这个类，就有了一些这个类默认的方法


    @Query(value = "select * from responsibility_datasource1 " ,nativeQuery = true)
    Page<ResponsibilityDatasource1> findAll(Pageable pageable);//传入一个pageable类型的数据，查询Page类型，ResponsibilityDatasource1泛型的所有数据


    @Query(value = "select * from responsibility_datasource1  where " +" level = :level and" + " date = :date" ,nativeQuery = true)
    List<ResponsibilityDatasource1> findAllByLevelanddate(@Param("level") String level,@Param("date") String date); //根据不同的级别查找数据 是车间数据 工位数据 还是工段数据 班组数据

    @Query(value = "select * from responsibility_datasource1  where " +" level = :level and" + " date = :date and" + " zone = :zone" ,nativeQuery = true)
    List<ResponsibilityDatasource1> findAllByLevelanddateaAndZone(@Param("level") String level,@Param("date") String date,@Param("zone") String zone);

    @Query(value = "select * from responsibility_datasource1  where " +" level = :level and" + " date = :date and" + " zone2 = :zone2" ,nativeQuery = true)
    List<ResponsibilityDatasource1> findAllByLevelanddateaAndZone2(@Param("level") String level,@Param("date") String date,@Param("zone2") String zone2);

    @Query(value = "select * from responsibility_datasource1  where " +" zone2 = :zone2 " ,nativeQuery = true)
    List<ResponsibilityDatasource1> findzoneByzone2(@Param("zone2") String zone2);
}
