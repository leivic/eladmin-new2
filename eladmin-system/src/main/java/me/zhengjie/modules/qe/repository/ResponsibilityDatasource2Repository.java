package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.ResponsibilityDatasource1;
import me.zhengjie.modules.qe.domain.ResponsibilityDatasource2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResponsibilityDatasource2Repository extends JpaRepository<ResponsibilityDatasource2,Integer> {

    @Query(value = "select * from responsibility_datasource2 " ,nativeQuery = true)
    Page<ResponsibilityDatasource2> findAll(Pageable pageable);

    @Query(value = "select * from responsibility_datasource2  where " +" level = :level and"  + " date = :date",nativeQuery = true)
    List<ResponsibilityDatasource2> findAllByLevelanddate(@Param("level") String level,@Param("date") String date);

    @Query(value = "select * from responsibility_datasource2  where " +" level = :level and"  + " date = :date and"+" zone = :zone",nativeQuery = true)
    List<ResponsibilityDatasource2> findByLevelanddateandzone(@Param("level") String level,@Param("date") String date,@Param("zone") String zone);

    @Query(value = "select * from responsibility_datasource2  where " +" level = :level and"  + " date = :date and"+" zone = :zone and"+" file_type = :file_type ",nativeQuery = true)
    List<ResponsibilityDatasource2> findByLevelanddateandzoneAndFile_type(@Param("level") String level,@Param("date") String date,@Param("zone") String zone,@Param("file_type") String file_type);

    @Query(value = "select * from responsibility_datasource2  where " +" level = :level and"  + " date = :date and"+" zone2 = :zone2",nativeQuery = true)
    List<ResponsibilityDatasource2> findByLevelanddateandzone2(@Param("level") String level,@Param("date") String date,@Param("zone2") String zone2);

    @Query(value = "select * from responsibility_datasource2  where " +" level = :level and"  + " date = :date and"+" zone2 = :zone2 and"+" file_type = :file_type ",nativeQuery = true)
    List<ResponsibilityDatasource2> findByLevelanddateandzone2AndFile_type(@Param("level") String level,@Param("date") String date,@Param("zone2") String zone2,@Param("file_type") String file_type);


    @Query(value = "select * from responsibility_datasource2  where " +" zone2 = :zone2 " ,nativeQuery = true)
    List<ResponsibilityDatasource2> findzoneByzone2(@Param("zone2") String zone2);
}
