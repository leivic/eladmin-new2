package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.InputIndexdata1;
import me.zhengjie.modules.qe.repository.InputIndexdata1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InputIndexdata1Service {
    @Autowired
    private InputIndexdata1Repository inputIndexdata1Repository;

    public void updateIndexdata1( int id,double yiyue,double eryue,double sanyue,double siyue, double wuyue,double liuyue,double qiyue,double bayue, double jiuyue, double shiyue, double shiyiyue,double shieryue){
        inputIndexdata1Repository.updateIndexdata1(id, yiyue, eryue, sanyue, siyue, wuyue, liuyue, qiyue, bayue, jiuyue, shiyue, shiyiyue, shieryue);
    };//

    public List<InputIndexdata1> findAll(){ //查找所有的方法 这个查找方法不用分页
        return inputIndexdata1Repository.findAll();
    }

    public void InputIndexdata1save(InputIndexdata1 inputIndexdata1){
        inputIndexdata1Repository.save(inputIndexdata1);
    }
}
