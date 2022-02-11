package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.ConsiciousStation;
import me.zhengjie.modules.qe.domain.ConsiciousWorkshop;
import me.zhengjie.modules.qe.repository.ConsiciousWorkshopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsiciousWorkshopService {

    @Autowired
    private ConsiciousWorkshopRepository consiciousWorkshopRepository;

    public void savedata(List<ConsiciousWorkshop> list){
        consiciousWorkshopRepository.saveAll(list); //存储数据
    }

    public void deletebyid(Integer id){ //删除数据
        consiciousWorkshopRepository.deleteById(id);
    }

    public Page<ConsiciousWorkshop> findall(int page, int size, String sort){ //查看所有意识工位的方法
        Pageable pageable=PageRequest.of(page-1, size, Sort.Direction.DESC, sort);
        return consiciousWorkshopRepository.findAll1(pageable);
    }

    public List<ConsiciousWorkshop> findallbyzoneanddate(String zone,String date){
        return consiciousWorkshopRepository.findAllByZoneAndDate(zone, date);
    }
}
