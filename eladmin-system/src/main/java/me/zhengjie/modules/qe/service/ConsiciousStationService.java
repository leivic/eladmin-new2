package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.ConsiciousGroup;
import me.zhengjie.modules.qe.domain.ConsiciousStation;
import me.zhengjie.modules.qe.repository.ConsiciousStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsiciousStationService {

    @Autowired
    private ConsiciousStationRepository consiciousStationRepository;

    public void savedata(List<ConsiciousStation> list){//存储数据的方法
        consiciousStationRepository.saveAll(list);
    }

    public Page<ConsiciousStation> findall(int page, int size, String sort){ //查看所有意识工位的方法
        Pageable pageable=PageRequest.of(page-1, size, Sort.Direction.DESC, sort);
        return consiciousStationRepository.findAll1(pageable);
    }

    public void deletebyid(Integer id){ //根据ID删除数据的方法
        consiciousStationRepository.deleteById(id);
    }

    public List<ConsiciousStation> findallbyzoneanddate(String zone,String date){
        return consiciousStationRepository.findAllByZoneAndDate(zone, date);
    }
}
