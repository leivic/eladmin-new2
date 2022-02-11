package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.InputIndexdata4;
import me.zhengjie.modules.qe.repository.InputIndexdata4Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InputIndexdata4Service {

    @Autowired
    private InputIndexdata4Repository inputIndexdata4Repository;

    public void updateIndexdata4(int id,double chongyachejian,double cheshenchejian,double tuzhuangchejian,double zongzhuangchejian,double jijiachejian,double zhuangpeichejian){
        inputIndexdata4Repository.updateIndexdata4(id, chongyachejian, cheshenchejian, tuzhuangchejian, zongzhuangchejian, jijiachejian, zhuangpeichejian);
    };

    public List<InputIndexdata4> findAll(){
        return inputIndexdata4Repository.findAll();
    }

    public void InputIndexdata4save(InputIndexdata4 inputIndexdata4){
        inputIndexdata4Repository.save(inputIndexdata4);
    }
}
