package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.ConsiciousWorkshop;
import me.zhengjie.modules.qe.domain.ConsiciousZone;
import me.zhengjie.modules.qe.repository.ConsiciousZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsiciousZoneService {

    @Autowired
    private ConsiciousZoneRepository consiciousZoneRepository;

    public void savedata(List<ConsiciousZone> list){ //存储数据
        consiciousZoneRepository.saveAll(list);
    }

    public void deletevyid(Integer id){ //删除数据
        consiciousZoneRepository.deleteById(id);
    }

    public Page<ConsiciousZone> findall(int page, int size, String sort){ //查看所有意识工位的方法
        Pageable pageable=PageRequest.of(page-1, size, Sort.Direction.DESC, sort);
        return consiciousZoneRepository.findAll1(pageable);
    }

    public List<ConsiciousZone> findallbyzoneanddate(String zone,String date){
        return consiciousZoneRepository.findAllByZoneAndDate(zone, date);
    }

    public List<ConsiciousZone> findallbydate(String date){
        return consiciousZoneRepository.findAllByDate(date);
    };
}
