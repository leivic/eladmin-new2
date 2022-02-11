package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.ConsiciousGroup;
import me.zhengjie.modules.qe.repository.ConsiciousGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class ConsiciousGroupService {

    @Autowired
    private ConsiciousGroupRepository consiciousGroupRepository;

    public Page<ConsiciousGroup> findall(int page, int size, String sort){ //查看所有意识工位的方法
        Pageable pageable=PageRequest.of(page-1, size, Sort.Direction.DESC, sort);
        return consiciousGroupRepository.findAll1(pageable);
    }

    public void deletebyid(Integer id){
        consiciousGroupRepository.deleteById(id);
    }

    public void savadata(List<ConsiciousGroup> list){
        consiciousGroupRepository.saveAll(list);//存储数据的方法
    }

    public List<ConsiciousGroup> findallbyzoneanddate(String zone,String date){
        return consiciousGroupRepository.findAllByZoneAndDate(zone, date);
    }
}
