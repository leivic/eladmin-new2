package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.Responsibilitychejianjichufen;
import me.zhengjie.modules.qe.repository.ResponsibilitychejianjichufenRepository;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service //mvc模式的模型层 模型层中的业务层service 前面Reponsibility/mapper只处理数据库相关的事情 controller则是负责和前后端交互
public class ResponsibilitychejianjichufenService {
    @Autowired
    private ResponsibilitychejianjichufenRepository responsibilitychejianjichufenRepository;

    public List<Responsibilitychejianjichufen> findAll(){ //查看所有数据
        return responsibilitychejianjichufenRepository.findAll();
    }

    public List<Responsibilitychejianjichufen> findAllBychejian(String chejian){
        return responsibilitychejianjichufenRepository.findAllBychejian(chejian);
    }

    public void updatejichufen(int id,int fenshu){
        responsibilitychejianjichufenRepository.updatejichufen(id, fenshu);
    };

    public List<Responsibilitychejianjichufen> findAlljichufen(){
       return responsibilitychejianjichufenRepository.findAll();
    }
}
