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
}
