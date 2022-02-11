package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.ResponsibilityDatasource1;
import me.zhengjie.modules.qe.repository.ResponsibilityDatasource1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponsibilityDatasource1Service {
    @Autowired
    private ResponsibilityDatasource1Repository responsibilityDatasource1Repository;

    public void saveData(ResponsibilityDatasource1 responsibilityDatasource1){//实参输入一个responsibilityDatasource1类的数据 然后存入数据库
        responsibilityDatasource1Repository.save(responsibilityDatasource1);
    }

    public Page<ResponsibilityDatasource1> findAll(Pageable pageable){
        return responsibilityDatasource1Repository.findAll(pageable);
    };

    public List<ResponsibilityDatasource1> findAllByLevel(String level,String date){
        return responsibilityDatasource1Repository.findAllByLevelanddate(level,date);
    };

    public List<ResponsibilityDatasource1> findAllByLevelanddateaAndZone(String level,String date,String zone){
        return responsibilityDatasource1Repository.findAllByLevelanddateaAndZone(level, date, zone);
    }

    public List<ResponsibilityDatasource1> findAllByLevelanddateaAndZone2(String level,String date,String zone2){
        return responsibilityDatasource1Repository.findAllByLevelanddateaAndZone2(level, date, zone2);
    }

    public List<ResponsibilityDatasource1> findzoneByzone2(String zone2){
        return responsibilityDatasource1Repository.findzoneByzone2(zone2);
    }

    public void deletedatasource1byid(Integer id){
        responsibilityDatasource1Repository.deleteById(id);//根据ID删除数据
    }
}
