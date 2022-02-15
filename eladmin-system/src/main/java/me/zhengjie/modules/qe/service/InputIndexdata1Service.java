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

    public void Inputdata1yiyue(int id,double yiyue){
    inputIndexdata1Repository.updateIndexdata1yiyue(id,yiyue);
}

    public void Inputdata1eryue(int id,double eryue){
        inputIndexdata1Repository.updateIndexdata1eryue(id,eryue);
    }

    public void Inputdata1sanyue(int id,double sanyue){
        inputIndexdata1Repository.updateIndexdata1sanyue(id,sanyue);
    }

    public void Inputdata1siyue(int id,double siyue){
        inputIndexdata1Repository.updateIndexdata1siyue(id,siyue);
    }

    public void Inputdata1wuyue(int id,double wuyue){
        inputIndexdata1Repository.updateIndexdata1wuyue(id,wuyue);
    }

    public void Inputdata1liuyue(int id,double liuyue){
        inputIndexdata1Repository.updateIndexdata1liuyue(id,liuyue);
    }

    public void Inputdata1qiyue(int id,double qiyue){
        inputIndexdata1Repository.updateIndexdata1qiyue(id,qiyue);
    }

    public void Inputdata1bayue(int id,double bayue){
        inputIndexdata1Repository.updateIndexdata1bayue(id,bayue);
    }

    public void Inputdata1jiuyue(int id,double jiuyue){
        inputIndexdata1Repository.updateIndexdata1jiuyue(id,jiuyue);
    }

    public void Inputdata1shiyue(int id,double shiyue){
        inputIndexdata1Repository.updateIndexdata1shiyue(id,shiyue);
    }

    public void Inputdata1shiyiyue(int id,double shiyiyue){
        inputIndexdata1Repository.updateIndexdata1shiyiyue(id,shiyiyue);
    }

    public void Inputdata1shieryue(int id,double shieryue){
        inputIndexdata1Repository.updateIndexdata1shieryue(id,shieryue);
    }
}
