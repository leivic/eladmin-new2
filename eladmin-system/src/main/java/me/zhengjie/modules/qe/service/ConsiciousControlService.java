package me.zhengjie.modules.qe.service;

import me.zhengjie.modules.qe.domain.ConsiciousControl;
import me.zhengjie.modules.qe.repository.ConsiciousControlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
@Service
public class ConsiciousControlService {

    @Autowired
    private ConsiciousControlRepository consiciousControlRepository;

    public void changeConsiciousControlbyid(ConsiciousControl consiciousControl){ //所有月份data是个数据
        consiciousControlRepository.changeConsiciousControlbyid(consiciousControl.getId(),consiciousControl.getYear(),consiciousControl.getType(),consiciousControl.getTarget(),consiciousControl.getTargettype(),consiciousControl.getTargetstandard(),consiciousControl.getDepartment(),consiciousControl.getZoneperson(),consiciousControl.getPerson_in_charge(),consiciousControl.getGoal(),consiciousControl.getLashengoal(),consiciousControl.getZhibiaofankuiren(),consiciousControl.getYiyuegoal(),consiciousControl.getYiyueshiji(),consiciousControl.getEryuegoal(),consiciousControl.getEryueshiji(),consiciousControl.getSanyuegoal(),consiciousControl.getSanyueshiji(),consiciousControl.getSiyuegoal(),consiciousControl.getSiyueshiji(),consiciousControl.getWuyuegoal(),consiciousControl.getWuyueshiji(),consiciousControl.getLiuyuegoal(),consiciousControl.getLiuyueshiji(),consiciousControl.getQiyuegoal(),consiciousControl.getQiyueshiji(),consiciousControl.getBayuegoal(),consiciousControl.getBayueshiji(),consiciousControl.getJiuyuegoal(),consiciousControl.getJiuyueshiji(),consiciousControl.getShiyuegoal(),consiciousControl.getShiyueshiji(),consiciousControl.getShiyiyuegoal(),consiciousControl.getShiyiyueshiji(),consiciousControl.getShieryuegoal(),consiciousControl.getShieryueshiji() );
    }

    public void savedata(List<ConsiciousControl> list){ //传入泛型列表
        consiciousControlRepository.saveAll(list);
    }

    public List<ConsiciousControl> findAllByYear(String year){
        return consiciousControlRepository.findAllByYear(year);
    }

    public void changezhuangtai(int id,int zhaungtai,int yuefen){
        switch (yuefen){
            case 1:
                consiciousControlRepository.changeyiyuezhuangtaibyid(id,zhaungtai);
                break;
            case 2:
                consiciousControlRepository.changeeryuezhuangtaibyid(id,zhaungtai);
                break;
            case 3:
                consiciousControlRepository.changesanyuezhuangtaibyid(id,zhaungtai);
                break;
            case 4:
                consiciousControlRepository.changesiyuezhuangtaibyid(id,zhaungtai);
                break;
            case 5:
                consiciousControlRepository.changewuyuezhuangtaibyid(id,zhaungtai);
                break;
            case 6:
                consiciousControlRepository.changeliuyuezhuangtaibyid(id,zhaungtai);
                break;
            case 7:
                consiciousControlRepository.changeqiyuezhuangtaibyid(id,zhaungtai);
                break;
            case 8:
                consiciousControlRepository.changebayuezhuangtaibyid(id,zhaungtai);
                break;
            case 9:
                consiciousControlRepository.changejiuyuezhuangtaibyid(id,zhaungtai);
                break;
            case 10:
                consiciousControlRepository.changeshiyuezhuangtaibyid(id,zhaungtai);
                break;
            case 11:
                consiciousControlRepository.changeshiyiyuezhuangtaibyid(id,zhaungtai);
                break;
            case 12:
                consiciousControlRepository.changeshieryuezhuangtaibyid(id,zhaungtai);
                break;
        }
    }


    public List finddepartmentbyyear( String year){
        return consiciousControlRepository.finddepartmentbyyear(year);
    };

    public List findtargetnumberbyyear( String year){
        return consiciousControlRepository.findtargetnumberbyyear(year);
    };

    public List finddepartmenthegezhiliang (String year,int zhuangtai,String jiyue){
        List list=new ArrayList();
        switch (jiyue){
            case "01":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang1(year,zhuangtai));
                break;
            case "02":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang2(year,zhuangtai));
                break;
            case "03":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang3(year,zhuangtai));
                break;
            case "04":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang4(year,zhuangtai));
                break;
            case "05":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang5(year,zhuangtai));
                break;
            case "06":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang6(year,zhuangtai));
                break;
            case "07":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang7(year,zhuangtai));
                break;
            case "08":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang8(year,zhuangtai));
                break;
            case "09":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang9(year,zhuangtai));
                break;
            case "10":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang10(year,zhuangtai));
                break;
            case "11":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang11(year,zhuangtai));
                break;
            case "12":
                list.add(consiciousControlRepository.finddepartmenthegezhiliang12(year,zhuangtai));
                break;
        }

        return list;
    }
}
