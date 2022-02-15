package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.InputIndexdata2;
import me.zhengjie.modules.qe.repository.InputIndexdata2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InputIndexdata2Service {

    @Autowired
    private InputIndexdata2Repository inputIndexdata2Repository;

    //更新数据的方法
    public void updateIndexdata2( int id,double yiyue,double eryue,double sanyue,double siyue, double wuyue,double liuyue,double qiyue,double bayue, double jiuyue, double shiyue, double shiyiyue,double shieryue){
        inputIndexdata2Repository.updateIndexdata2(id, yiyue, eryue, sanyue, siyue, wuyue, liuyue, qiyue, bayue, jiuyue, shiyue, shiyiyue, shieryue);
    };

    public List<InputIndexdata2> findAll(){ //查找所有数据
        return inputIndexdata2Repository.findAll();
    }

    public void InputIndexdata2save(InputIndexdata2 inputIndexdata2){
        inputIndexdata2Repository.save(inputIndexdata2);
    }

    public void Inputdatayiyue(int id,double yiyue){
        inputIndexdata2Repository.updateIndexdata2yiyue(id,yiyue);
    }

    public void Inputdata1eryue(int id,double eryue){
        inputIndexdata2Repository.updateIndexdata2eryue(id,eryue);
    }

    public void Inputdata1sanyue(int id,double sanyue){
        inputIndexdata2Repository.updateIndexdata2sanyue(id,sanyue);
    }

    public void Inputdata1ysiyue(int id,double siyue){
        inputIndexdata2Repository.updateIndexdata2siyue(id,siyue);
    }

    public void Inputdata1wuyue(int id,double wuyue){
        inputIndexdata2Repository.updateIndexdata2wuyue(id,wuyue);
    }

    public void Inputdata1liuyue(int id,double liuyue){
        inputIndexdata2Repository.updateIndexdata2liuyue(id,liuyue);
    }

    public void Inputdata1qiyue(int id,double qiyue){
        inputIndexdata2Repository.updateIndexdata2qiyue(id,qiyue);
    }

    public void Inputdata1bayue(int id,double bayue){
        inputIndexdata2Repository.updateIndexdata2bayue(id,bayue);
    }

    public void Inputdata1jiuyue(int id,double jiuyue){
        inputIndexdata2Repository.updateIndexdata2jiuyue(id,jiuyue);
    }

    public void Inputdata1shiyue(int id,double shiyue){
        inputIndexdata2Repository.updateIndexdata2shiyue(id,shiyue);
    }

    public void Inputdata1shiyiyue(int id,double shiyiyue){
        inputIndexdata2Repository.updateIndexdata2shiyiyue(id,shiyiyue);
    }

    public void Inputdata1shieryue(int id,double shieryue){
        inputIndexdata2Repository.updateIndexdata2shieryue(id,shieryue);
    }
}
