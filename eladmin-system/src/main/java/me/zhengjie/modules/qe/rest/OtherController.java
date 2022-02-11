package me.zhengjie.modules.qe.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.qe.domain.InputIndexdata1;
import me.zhengjie.modules.qe.domain.InputIndexdata2;
import me.zhengjie.modules.qe.domain.InputIndexdata3;
import me.zhengjie.modules.qe.domain.InputIndexdata4;
import me.zhengjie.modules.qe.service.InputIndexdata1Service;
import me.zhengjie.modules.qe.service.InputIndexdata2Service;
import me.zhengjie.modules.qe.service.InputIndexdata3Service;
import me.zhengjie.modules.qe.service.InputIndexdata4Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "质量：辅助API")
@RequestMapping("/qe/other")
public class OtherController {

    @Autowired
    private InputIndexdata1Service inputIndexdata1Service;

    @Autowired
    private InputIndexdata2Service inputIndexdata2Service;

    @Autowired
    private InputIndexdata3Service inputIndexdata3Service;

    @Autowired
    private InputIndexdata4Service inputIndexdata4Service;

    private EnvironmentController environmentController=new EnvironmentController();
    private ConsiciousController consiciousController=new ConsiciousController();
    private ResponsiilityController responsiilityController=new ResponsiilityController();
    private ContinueController continueController=new ContinueController();
    @ApiOperation("质量: 更新首页车间数据")
    @PostMapping(value = "/updateIndexdata1")
    public void updateIndexdata1(int id,double yiyue,double eryue,double sanyue,double siyue, double wuyue,double liuyue,double qiyue,double bayue, double jiuyue, double shiyue, double shiyiyue,double shieryue){
        inputIndexdata1Service.updateIndexdata1(id, yiyue, eryue, sanyue, siyue, wuyue, liuyue, qiyue, bayue, jiuyue, shiyue, shiyiyue, shieryue);
    }

    @ApiOperation("质量: 更新首页工段数据")
    @PostMapping(value = "/updateIndexdata2")
    public void updateIndexdata2(int id,double yiyue,double eryue,double sanyue,double siyue, double wuyue,double liuyue,double qiyue,double bayue, double jiuyue, double shiyue, double shiyiyue,double shieryue){
        inputIndexdata2Service.updateIndexdata2(id,  yiyue, eryue, sanyue, siyue, wuyue, liuyue, qiyue, bayue, jiuyue, shiyue, shiyiyue, shieryue);
    }

    @ApiOperation("质量: 更新首页班组数据")
    @PostMapping(value = "/updateIndexdata3")
    public void updateIndexdata3(int id,double chongyachejian,double cheshenchejian,double tuzhuangchejian,double zongzhuangchejian,double jijiachejian,double zhuangpeichejian){
        inputIndexdata3Service.updateIndexdata3(id, chongyachejian, cheshenchejian, tuzhuangchejian, zongzhuangchejian, jijiachejian, zhuangpeichejian);
    }

    @ApiOperation("质量: 更新首页工位数据")
    @PostMapping(value = "/updateIndexdata4")
    public void updateIndexdata4(int id,double chongyachejian,double cheshenchejian,double tuzhuangchejian,double zongzhuangchejian,double jijiachejian,double zhuangpeichejian){
        inputIndexdata4Service.updateIndexdata4(id, chongyachejian, cheshenchejian, tuzhuangchejian, zongzhuangchejian, jijiachejian, zhuangpeichejian);
    }

    @ApiOperation("质量: 查找首页车间数据")
    @GetMapping(value = "/findAllIndexdata1")
    public List<InputIndexdata1> findAllIndexdata1(){
       return inputIndexdata1Service.findAll();
    }

    @ApiOperation("质量: 查找首页工段数据")
    @GetMapping(value = "/findAllIndexdata2")
    public List<InputIndexdata2> findAllIndexdata2(){
        return inputIndexdata2Service.findAll();
    }

    @ApiOperation("质量: 查找首页班组数据")
    @GetMapping(value = "/findAllIndexdata3")
    public List<InputIndexdata3> findAllIndexdata3(){
        return inputIndexdata3Service.findAll();
    }

    @ApiOperation("质量: 查找首页工位数据")
    @GetMapping(value = "/findAllIndexdata4")
    public List<InputIndexdata4> findAllIndexdata4(){
        return inputIndexdata4Service.findAll();
    }

    @ApiOperation("质量: 更新首页车间数据") //不同车间 不同月份 月份方法传入参数  车间六个车间分别计算
    @GetMapping(value = "/InputIndexdata1save")
    public void InputIndexdata1save(String date){ //循环每个车间和十二个月份..每个车间和月份进行一次计算




        //冲压车间


        //质量生态环境

        double chongyahuanjinx11=environmentController.getZoneX11("冲压车间",date);
        double chongyahuanjinx12=environmentController.getZoneX12("冲压车间",date);
        double chongyahuanjinx13=environmentController.getZoneX13("冲压车间",date);
        double chongyahuanjinx14=environmentController.getZoneX14("冲压车间",date);

        double chongyaEE=1.3*chongyahuanjinx11+1.2*chongyahuanjinx12+0.29*chongyahuanjinx13+0.98*chongyahuanjinx14; //
        //质量生态意识

        double chongyazhishix13=consiciousController.getzonezhiliangzhishi("冲压车间",date);
        double chongyarenzhix14=consiciousController.getzonezhiliangrenzhi("冲压车间",date);
        double chongyaxinnianx15=consiciousController.getzonezhiliangxinnian("冲压车间",date);
        double chongyaxingweix16=consiciousController.getzonezhiliangxingwei("冲压车间",date);
        double chongyaEC=1.2*chongyazhishix13+1.7*chongyarenzhix14+1.2*chongyaxinnianx15+1.16*chongyaxingweix16;
        //质量生态责任
        double chongyazerenx8=responsiilityController.getx8("冲压车间",date);
        double chongyazerenx9=responsiilityController.getx9("冲压车间",date);
        double chongyazerenx10=responsiilityController.getx10("冲压车间",date);
        double chongyaER=0.9*chongyazerenx8+1.72*chongyazerenx9+0.98*chongyazerenx10;
        //质量生态持续
        double chongyachixux1=continueController.getx1("冲压车间",date);
        double chongyachixux2=continueController.getx1("冲压车间",date);
        double chongyachixux3=continueController.getx1("冲压车间",date);
        double chongyachixux4=continueController.getx1("冲压车间",date);
        double chongyachixux5=continueController.getx1("冲压车间",date);
        double chongyachixux6=continueController.getx1("冲压车间",date);
        double chongyachixux7=continueController.getx1("冲压车间",date);
        double chongyachixux8=continueController.getx1("冲压车间",date);
        double chongyachixux9=continueController.getx1("冲压车间",date);
        double chongyachixux10=continueController.getx1("冲压车间",date);
        double chongyachixux11=continueController.getx1("冲压车间",date);
        double chongyachixux12=continueController.getx1("冲压车间",date);
        double chongyaES=2*chongyachixux1+6*chongyachixux2+0.8*chongyachixux3+1.2*chongyachixux4+2*chongyachixux5+0.3*chongyachixux6+0.3*chongyachixux7+0.25*chongyachixux8+0.25*chongyachixux9+0.2*chongyachixux10+0.36*chongyachixux11+0.35*chongyachixux12;
        double chongyaQE=(chongyaER/142.1)*30+(chongyaES/430.6)*30+(chongyaEC/464.86)*20+(chongyaEE/582.3)*20;

    }
}
