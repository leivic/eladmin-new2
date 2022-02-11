package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.InputIndexdata3;
import me.zhengjie.modules.qe.repository.InputIndexdata3Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class InputIndexdata3Service {

    @Autowired
    private InputIndexdata3Repository inputIndexdata3Repository;

    public void updateIndexdata3(int id,double chongyachejian,double cheshenchejian,double tuzhuangchejian,double zongzhuangchejian,double jijiachejian,double zhuangpeichejian){
        inputIndexdata3Repository.updateIndexdata3(id, chongyachejian, cheshenchejian, tuzhuangchejian, zongzhuangchejian, jijiachejian, zhuangpeichejian);
    };

    public List<InputIndexdata3> findAll(){
        return inputIndexdata3Repository.findAll();
    }

    public void InputIndexdata3save(InputIndexdata3 inputIndexdata3){
        inputIndexdata3Repository.save(inputIndexdata3); //传入一个inputindexdata3类别的对象
    }
}
