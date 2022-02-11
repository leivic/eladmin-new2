package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.ResponsibilityDatasource1;
import me.zhengjie.modules.qe.domain.ResponsibilityDatasource2;
import me.zhengjie.modules.qe.repository.ResponsibilityDatasource2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponsibilityDatasource2Service {
    @Autowired
    private ResponsibilityDatasource2Repository responsibilityDatasource2Repository;

    public void saveData(ResponsibilityDatasource2 responsibilityDatasource2){//实参输入一个responsibilityDatasource2类的数据 然后存入数据库
        responsibilityDatasource2Repository.save(responsibilityDatasource2);
    };

    public Page<ResponsibilityDatasource2> findAll(Pageable pageable){
        return responsibilityDatasource2Repository.findAll(pageable);
    };

    public List<ResponsibilityDatasource2> findAllByLevel(String level,String date){
        return responsibilityDatasource2Repository.findAllByLevelanddate(level,date);
    }

    public List<ResponsibilityDatasource2> findByLevelanddateandzone(String level,String date,String zone){
        return responsibilityDatasource2Repository.findByLevelanddateandzone(level, date, zone);
    }

    public List<ResponsibilityDatasource2> findByLevelanddateandzoneandfiletype(String level,String date,String zone,String file_type){
        return responsibilityDatasource2Repository.findByLevelanddateandzoneAndFile_type(level, date, zone, file_type);
    }

    public List<ResponsibilityDatasource2> findByLevelanddateandzone2(String level,String date,String zone2){
        return responsibilityDatasource2Repository.findByLevelanddateandzone2(level, date, zone2);
    }

    public List<ResponsibilityDatasource2> findByLevelanddateandzone2andfiletype(String level,String date,String zone2,String file_type){
        return responsibilityDatasource2Repository.findByLevelanddateandzone2AndFile_type(level, date, zone2, file_type);
    }

    public void deletedatasource2byid(Integer id){ //
        responsibilityDatasource2Repository.deleteById(id);}

    public List<ResponsibilityDatasource2> findzoneByzone2(String zone2){
        return responsibilityDatasource2Repository.findzoneByzone2(zone2);
    }

}
