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

import java.text.DecimalFormat;
import java.util.ArrayList;
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

    @Autowired
    private EnvironmentController environmentController; //自动注入才不会空指针异常
    @Autowired
    private ConsiciousController consiciousController;
    @Autowired
    private ResponsiilityController responsiilityController;
    @Autowired
    private ContinueController continueController;
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
    public int InputIndexdata1save(String date){ //循环每个车间和十二个月份..每个车间和月份进行一次计算
        DecimalFormat df = new DecimalFormat("#.##");
        try {

            //冲压车间


            //质量生态环境


            double chongyahuanjinx11 = environmentController.getZoneX11("冲压车间", date);
            double chongyahuanjinx12 = environmentController.getZoneX12("冲压车间", date);
            double chongyahuanjinx13 = environmentController.getZoneX13("冲压车间", date);
            double chongyahuanjinx14 = environmentController.getZoneX14("冲压车间", date);

            double chongyaEE = 1.3 * chongyahuanjinx11 + 1.2 * chongyahuanjinx12 + 0.29 * chongyahuanjinx13 + 0.98 * chongyahuanjinx14; //
            //质量生态意识

            double chongyazhishix13 = consiciousController.getzonezhiliangzhishi("冲压车间", date);
            double chongyarenzhix14 = consiciousController.getzonezhiliangrenzhi("冲压车间", date);
            double chongyaxinnianx15 = consiciousController.getzonezhiliangxinnian("冲压车间", date);
            double chongyaxingweix16 = consiciousController.getzonezhiliangxingwei("冲压车间", date);
            double chongyaEC = 1.2 * chongyazhishix13 + 1.7 * chongyarenzhix14 + 1.2 * chongyaxinnianx15 + 1.14 * chongyaxingweix16;
            //质量生态责任
            double chongyazerenx8 = responsiilityController.getx8("冲压车间", date);
            double chongyazerenx9 = responsiilityController.getx9("冲压车间", date);
            double chongyazerenx10 = responsiilityController.getx10("冲压车间", date);
            double chongyaER = 0.9 * chongyazerenx8 + 1.72 * chongyazerenx9 + 0.98 * chongyazerenx10;
            //质量生态持续
            double chongyachixux1 = continueController.getx1("冲压车间", date);
            double chongyachixux2 = continueController.getx2("冲压车间", date);
            double chongyachixux3 = continueController.getx3("冲压车间", date);
            double chongyachixux4 = continueController.getx4("冲压车间", date);
            double chongyachixux5 = continueController.getx5("冲压车间", date);
            double chongyachixux6 = continueController.getx6("冲压车间", date);
            double chongyachixux7 = continueController.getx7("冲压车间", date);
            double chongyachixux8 = continueController.getx8("冲压车间", date);
            double chongyachixux9 = continueController.getx9("冲压车间", date);
            double chongyachixux10 = continueController.getx10("冲压车间", date);
            double chongyachixux11 = continueController.getx11("冲压车间", date);
            double chongyachixux12 = continueController.getx12("冲压车间", date);
            double chongyaES = 2 * chongyachixux1 + 6 * chongyachixux2 + 0.8 * chongyachixux3 + 1.2 * chongyachixux4 + 2 * chongyachixux5 + 0.3 * chongyachixux6 + 0.3 * chongyachixux7 + 0.25 * chongyachixux8 + 0.25 * chongyachixux9 + 0.2 * chongyachixux10 + 0.36 * chongyachixux11 + 0.35 * chongyachixux12;
            double chongyaQE = (chongyaER / 142.1) * 30 + (chongyaES / 430.6) * 30 + (chongyaEC / 464.86) * 20 + (chongyaEE / 582.3) * 20;


            //车身车间


            //质量生态环境

            double cheshenhuanjinx11 = environmentController.getZoneX11("车身车间", date);
            double cheshenhuanjinx12 = environmentController.getZoneX12("车身车间", date);
            double cheshenhuanjinx13 = environmentController.getZoneX13("车身车间", date);
            double cheshenhuanjinx14 = environmentController.getZoneX14("车身车间", date);

            double cheshenEE = 1.3 * cheshenhuanjinx11 + 1.2 * cheshenhuanjinx12 + 0.29 * cheshenhuanjinx13 + 0.98 * cheshenhuanjinx14; //
            //质量生态意识

            double cheshenzhishix13 = consiciousController.getzonezhiliangzhishi("车身车间", date);
            double cheshenrenzhix14 = consiciousController.getzonezhiliangrenzhi("车身车间", date);
            double cheshenxinnianx15 = consiciousController.getzonezhiliangxinnian("车身车间", date);
            double cheshenxingweix16 = consiciousController.getzonezhiliangxingwei("车身车间", date);
            double cheshenEC = 1.2 * cheshenzhishix13 + 1.7 * cheshenrenzhix14 + 1.2 * cheshenxinnianx15 + 1.14 * cheshenxingweix16;
            //质量生态责任
            double cheshenzerenx8 = responsiilityController.getx8("车身车间", date);
            double cheshenzerenx9 = responsiilityController.getx9("车身车间", date);
            double cheshenzerenx10 = responsiilityController.getx10("车身车间", date);
            double cheshenER = 0.9 * cheshenzerenx8 + 1.72 * cheshenzerenx9 + 0.98 * cheshenzerenx10;
            //质量生态持续
            double cheshenchixux1 = continueController.getx1("车身车间", date);
            double cheshenchixux2 = continueController.getx2("车身车间", date);
            double cheshenchixux3 = continueController.getx3("车身车间", date);
            double cheshenchixux4 = continueController.getx4("车身车间", date);
            double cheshenchixux5 = continueController.getx5("车身车间", date);
            double cheshenchixux6 = continueController.getx6("车身车间", date);
            double cheshenchixux7 = continueController.getx7("车身车间", date);
            double cheshenchixux8 = continueController.getx8("车身车间", date);
            double cheshenchixux9 = continueController.getx9("车身车间", date);
            double cheshenchixux10 = continueController.getx10("车身车间", date);
            double cheshenchixux11 = continueController.getx11("车身车间", date);
            double cheshenchixux12 = continueController.getx12("车身车间", date);
            double cheshenES = 2 * cheshenchixux1 + 6 * cheshenchixux2 + 0.8 * cheshenchixux3 + 1.2 * cheshenchixux4 + 2 * cheshenchixux5 + 0.3 * cheshenchixux6 + 0.3 * cheshenchixux7 + 0.25 * cheshenchixux8 + 0.25 * cheshenchixux9 + 0.2 * cheshenchixux10 + 0.36 * cheshenchixux11 + 0.35 * cheshenchixux12;
            double cheshenQE = (cheshenER / 142.1) * 30 + (cheshenES / 430.6) * 30 + (cheshenEC / 464.86) * 20 + (cheshenEE / 582.3) * 20;

            //涂装车间


            //质量生态环境

            double tuzhuanghuanjinx11 = environmentController.getZoneX11("涂装车间", date);
            double tuzhuanghuanjinx12 = environmentController.getZoneX12("涂装车间", date);
            double tuzhuanghuanjinx13 = environmentController.getZoneX13("涂装车间", date);
            double tuzhuanghuanjinx14 = environmentController.getZoneX14("涂装车间", date);

            double tuzhuangEE = 1.3 * tuzhuanghuanjinx11 + 1.2 * tuzhuanghuanjinx12 + 0.29 * tuzhuanghuanjinx13 + 0.98 * tuzhuanghuanjinx14; //
            //质量生态意识

            double tuzhuangzhishix13 = consiciousController.getzonezhiliangzhishi("涂装车间", date);
            double tuzhuangrenzhix14 = consiciousController.getzonezhiliangrenzhi("涂装车间", date);
            double tuzhuangxinnianx15 = consiciousController.getzonezhiliangxinnian("涂装车间", date);
            double tuzhuangxingweix16 = consiciousController.getzonezhiliangxingwei("涂装车间", date);
            double tuzhuangEC = 1.2 * tuzhuangzhishix13 + 1.7 * tuzhuangrenzhix14 + 1.2 * tuzhuangxinnianx15 + 1.14 * tuzhuangxingweix16;
            //质量生态责任
            double tuzhuangzerenx8 = responsiilityController.getx8("涂装车间", date);
            double tuzhuangzerenx9 = responsiilityController.getx9("涂装车间", date);
            double tuzhuangzerenx10 = responsiilityController.getx10("涂装车间", date);
            double tuzhuangER = 0.9 * tuzhuangzerenx8 + 1.72 * tuzhuangzerenx9 + 0.98 * tuzhuangzerenx10;
            //质量生态持续
            double tuzhuangchixux1 = continueController.getx1("涂装车间", date);
            double tuzhuangchixux2 = continueController.getx2("涂装车间", date);
            double tuzhuangchixux3 = continueController.getx3("涂装车间", date);
            double tuzhuangchixux4 = continueController.getx4("涂装车间", date);
            double tuzhuangchixux5 = continueController.getx5("涂装车间", date);
            double tuzhuangchixux6 = continueController.getx6("涂装车间", date);
            double tuzhuangchixux7 = continueController.getx7("涂装车间", date);
            double tuzhuangchixux8 = continueController.getx8("涂装车间", date);
            double tuzhuangchixux9 = continueController.getx9("涂装车间", date);
            double tuzhuangchixux10 = continueController.getx10("涂装车间", date);
            double tuzhuangchixux11 = continueController.getx11("涂装车间", date);
            double tuzhuangchixux12 = continueController.getx12("涂装车间", date);
            double tuzhuangES = 2 * tuzhuangchixux1 + 6 * tuzhuangchixux2 + 0.8 * tuzhuangchixux3 + 1.2 * tuzhuangchixux4 + 2 * tuzhuangchixux5 + 0.3 * tuzhuangchixux6 + 0.3 * tuzhuangchixux7 + 0.25 * tuzhuangchixux8 + 0.25 * tuzhuangchixux9 + 0.2 * tuzhuangchixux10 + 0.36 * tuzhuangchixux11 + 0.35 * tuzhuangchixux12;
            double tuzhuangQE = (tuzhuangER / 142.1) * 30 + (tuzhuangES / 430.6) * 30 + (tuzhuangEC / 464.86) * 20 + (tuzhuangEE / 582.3) * 20;

            //总装车间


            //质量生态环境

            double zongzhuanghuanjinx11 = environmentController.getZoneX11("总装车间", date);
            double zongzhuanghuanjinx12 = environmentController.getZoneX12("总装车间", date);
            double zongzhuanghuanjinx13 = environmentController.getZoneX13("总装车间", date);
            double zongzhuanghuanjinx14 = environmentController.getZoneX14("总装车间", date);

            double zongzhuangEE = 1.3 * zongzhuanghuanjinx11 + 1.2 * zongzhuanghuanjinx12 + 0.29 * zongzhuanghuanjinx13 + 0.98 * zongzhuanghuanjinx14; //
            //质量生态意识

            double zongzhuangzhishix13 = consiciousController.getzonezhiliangzhishi("总装车间", date);
            double zongzhuangrenzhix14 = consiciousController.getzonezhiliangrenzhi("总装车间", date);
            double zongzhuangxinnianx15 = consiciousController.getzonezhiliangxinnian("总装车间", date);
            double zongzhuangxingweix16 = consiciousController.getzonezhiliangxingwei("总装车间", date);
            double zongzhuangEC = 1.2 * zongzhuangzhishix13 + 1.7 * zongzhuangrenzhix14 + 1.2 * zongzhuangxinnianx15 + 1.16 * zongzhuangxingweix16;
            //质量生态责任
            double zongzhuangzerenx8 = responsiilityController.getx8("总装车间", date);
            double zongzhuangzerenx9 = responsiilityController.getx9("总装车间", date);
            double zongzhuangzerenx10 = responsiilityController.getx10("总装车间", date);
            double zongzhuangER = 0.9 * zongzhuangzerenx8 + 1.72 * zongzhuangzerenx9 + 0.98 * zongzhuangzerenx10;
            //质量生态持续
            double zongzhuangchixux1 = continueController.getx1("总装车间", date);
            double zongzhuangchixux2 = continueController.getx2("总装车间", date);
            double zongzhuangchixux3 = continueController.getx3("总装车间", date);
            double zongzhuangchixux4 = continueController.getx4("总装车间", date);
            double zongzhuangchixux5 = continueController.getx5("总装车间", date);
            double zongzhuangchixux6 = continueController.getx6("总装车间", date);
            double zongzhuangchixux7 = continueController.getx7("总装车间", date);
            double zongzhuangchixux8 = continueController.getx8("总装车间", date);
            double zongzhuangchixux9 = continueController.getx9("总装车间", date);
            double zongzhuangchixux10 = continueController.getx10("总装车间", date);
            double zongzhuangchixux11 = continueController.getx11("总装车间", date);
            double zongzhuangchixux12 = continueController.getx12("总装车间", date);
            double zongzhuangES = 2 * zongzhuangchixux1 + 6 * zongzhuangchixux2 + 0.8 * zongzhuangchixux3 + 1.2 * zongzhuangchixux4 + 2 * zongzhuangchixux5 + 0.3 * zongzhuangchixux6 + 0.3 * zongzhuangchixux7 + 0.25 * zongzhuangchixux8 + 0.25 * zongzhuangchixux9 + 0.2 * zongzhuangchixux10 + 0.36 * zongzhuangchixux11 + 0.35 * zongzhuangchixux12;
            double zongzhuangQE = (zongzhuangER / 142.1) * 30 + (zongzhuangES / 430.6) * 30 + (zongzhuangEC / 464.86) * 20 + (zongzhuangEE / 582.3) * 20;


            //机加车间


            //质量生态环境

            double jijiahuanjinx11 = environmentController.getZoneX11("机加车间", date);
            double jijiahuanjinx12 = environmentController.getZoneX12("机加车间", date);
            double jijiahuanjinx13 = environmentController.getZoneX13("机加车间", date);
            double jijiahuanjinx14 = environmentController.getZoneX14("机加车间", date);

            double jijiaEE = 1.3 * jijiahuanjinx11 + 1.2 * jijiahuanjinx12 + 0.29 * jijiahuanjinx13 + 0.98 * jijiahuanjinx14; //
            //质量生态意识

            double jijiazhishix13 = consiciousController.getzonezhiliangzhishi("机加车间", date);
            double jijiarenzhix14 = consiciousController.getzonezhiliangrenzhi("机加车间", date);
            double jijiaxinnianx15 = consiciousController.getzonezhiliangxinnian("机加车间", date);
            double jijiaxingweix16 = consiciousController.getzonezhiliangxingwei("机加车间", date);
            double jijiaEC = 1.2 * jijiazhishix13 + 1.7 * jijiarenzhix14 + 1.2 * jijiaxinnianx15 + 1.14 * jijiaxingweix16;
            //质量生态责任
            double jijiazerenx8 = responsiilityController.getx8("机加车间", date);
            double jijiazerenx9 = responsiilityController.getx9("机加车间", date);
            double jijiazerenx10 = responsiilityController.getx10("机加车间", date);
            double jijiaER = 0.9 * jijiazerenx8 + 1.72 * jijiazerenx9 + 0.98 * jijiazerenx10;
            //质量生态持续
            double jijiachixux1 = continueController.getx1("发动机工厂", date);
            double jijiachixux2 = continueController.getx2("发动机工厂", date);
            double jijiachixux3 = continueController.getx3("发动机工厂", date);
            double jijiachixux4 = continueController.getx4("发动机工厂", date);
            double jijiachixux5 = continueController.getx5("发动机工厂", date);
            double jijiachixux6 = continueController.getx6("发动机工厂", date);
            double jijiachixux7 = continueController.getx7("发动机工厂", date);
            double jijiachixux8 = continueController.getx8("发动机工厂", date);
            double jijiachixux9 = continueController.getx9("发动机工厂", date);
            double jijiachixux10 = continueController.getx10("发动机工厂", date);
            double jijiachixux11 = continueController.getx11("发动机工厂", date);
            double jijiachixux12 = continueController.getx12("发动机工厂", date);
            double jijiaES = 2 * jijiachixux1 + 6 * jijiachixux2 + 0.8 * jijiachixux3 + 1.2 * jijiachixux4 + 2 * jijiachixux5 + 0.3 * jijiachixux6 + 0.3 * jijiachixux7 + 0.25 * jijiachixux8 + 0.25 * jijiachixux9 + 0.2 * jijiachixux10 + 0.36 * jijiachixux11 + 0.35 * jijiachixux12;
            double jijiaQE = (jijiaER / 142.1) * 30 + (jijiaES / 430.6) * 30 + (jijiaEC / 464.86) * 20 + (jijiaEE / 582.3) * 20;

            //装配车间


            //质量生态环境

            double zhuangpeihuanjinx11 = environmentController.getZoneX11("装配车间", date);
            double zhuangpeihuanjinx12 = environmentController.getZoneX12("装配车间", date);
            double zhuangpeihuanjinx13 = environmentController.getZoneX13("装配车间", date);
            double zhuangpeihuanjinx14 = environmentController.getZoneX14("装配车间", date);

            double zhuangpeiEE = 1.3 * zhuangpeihuanjinx11 + 1.2 * zhuangpeihuanjinx12 + 0.29 * zhuangpeihuanjinx13 + 0.98 * zhuangpeihuanjinx14; //
            //质量生态意识

            double zhuangpeizhishix13 = consiciousController.getzonezhiliangzhishi("装配车间", date);
            double zhuangpeirenzhix14 = consiciousController.getzonezhiliangrenzhi("装配车间", date);
            double zhuangpeixinnianx15 = consiciousController.getzonezhiliangxinnian("装配车间", date);
            double zhuangpeixingweix16 = consiciousController.getzonezhiliangxingwei("装配车间", date);
            double zhuangpeiEC = 1.2 * zhuangpeizhishix13 + 1.7 * zhuangpeirenzhix14 + 1.2 * zhuangpeixinnianx15 + 1.14 * zhuangpeixingweix16;
            //质量生态责任
            double zhuangpeizerenx8 = responsiilityController.getx8("装配车间", date);
            double zhuangpeizerenx9 = responsiilityController.getx9("装配车间", date);
            double zhuangpeizerenx10 = responsiilityController.getx10("装配车间", date);
            double zhuangpeiER = 0.9 * zhuangpeizerenx8 + 1.72 * zhuangpeizerenx9 + 0.98 * zhuangpeizerenx10;
            //质量生态持续
            double zhuangpeichixux1 = continueController.getx1("发动机工厂", date);
            double zhuangpeichixux2 = continueController.getx2("发动机工厂", date);
            double zhuangpeichixux3 = continueController.getx3("发动机工厂", date);
            double zhuangpeichixux4 = continueController.getx4("发动机工厂", date);
            double zhuangpeichixux5 = continueController.getx5("发动机工厂", date);
            double zhuangpeichixux6 = continueController.getx6("发动机工厂", date);
            double zhuangpeichixux7 = continueController.getx7("发动机工厂", date);
            double zhuangpeichixux8 = continueController.getx8("发动机工厂", date);
            double zhuangpeichixux9 = continueController.getx9("发动机工厂", date);
            double zhuangpeichixux10 = continueController.getx10("发动机工厂", date);
            double zhuangpeichixux11 = continueController.getx11("发动机工厂", date);
            double zhuangpeichixux12 = continueController.getx12("发动机工厂", date);
            double zhuangpeiES = 2 * zhuangpeichixux1 + 6 * zhuangpeichixux2 + 0.8 * zhuangpeichixux3 + 1.2 * zhuangpeichixux4 + 2 * zhuangpeichixux5 + 0.3 * zhuangpeichixux6 + 0.3 * zhuangpeichixux7 + 0.25 * zhuangpeichixux8 + 0.25 * zhuangpeichixux9 + 0.2 * zhuangpeichixux10 + 0.36 * zhuangpeichixux11 + 0.35 * zhuangpeichixux12;
            double zhuangpeiQE = (zhuangpeiER / 142.1) * 30 + (zhuangpeiES / 430.6) * 30 + (zhuangpeiEC / 464.86) * 20 + (zhuangpeiEE / 582.3) * 20;

            switch (date.substring(5, 7)) {
                case "01":
                    inputIndexdata1Service.Inputdata1yiyue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1yiyue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1yiyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1yiyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1yiyue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1yiyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "02":
                    inputIndexdata1Service.Inputdata1eryue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1eryue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1eryue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1eryue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1eryue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1eryue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "03":
                    inputIndexdata1Service.Inputdata1sanyue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1sanyue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1sanyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1sanyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1sanyue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1sanyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "04":
                    inputIndexdata1Service.Inputdata1siyue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1siyue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1siyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1siyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1siyue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1siyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "05":
                    inputIndexdata1Service.Inputdata1wuyue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1wuyue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1wuyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1wuyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1wuyue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1wuyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "06":
                    inputIndexdata1Service.Inputdata1liuyue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1liuyue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1liuyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1liuyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1liuyue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1liuyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "07":
                    inputIndexdata1Service.Inputdata1qiyue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1qiyue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1qiyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1qiyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1qiyue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1qiyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "08":
                    inputIndexdata1Service.Inputdata1bayue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1bayue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1bayue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1bayue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1bayue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1bayue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "09":
                    inputIndexdata1Service.Inputdata1jiuyue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1jiuyue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1jiuyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1jiuyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1jiuyue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1jiuyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "10":
                    inputIndexdata1Service.Inputdata1shiyue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1shiyue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1shiyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1shiyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1shiyue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1shiyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "11":
                    inputIndexdata1Service.Inputdata1shiyiyue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1shiyiyue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1shiyiyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1shiyiyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1shiyiyue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1shiyiyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "12":
                    inputIndexdata1Service.Inputdata1shieryue(0, Double.parseDouble(df.format(chongyaQE)));
                    inputIndexdata1Service.Inputdata1shieryue(1, Double.parseDouble(df.format(cheshenQE)));
                    inputIndexdata1Service.Inputdata1shieryue(2, Double.parseDouble(df.format(tuzhuangQE)));
                    inputIndexdata1Service.Inputdata1shieryue(3, Double.parseDouble(df.format(zongzhuangQE)));
                    inputIndexdata1Service.Inputdata1shieryue(4, Double.parseDouble(df.format(jijiaQE)));
                    inputIndexdata1Service.Inputdata1shieryue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
            }}catch (Exception e){
            return 0;
        }
        return 1;

    }

    @ApiOperation("质量: 更新首页工段数据") //不同车间 不同月份 月份方法传入参数  车间六个车间分别计算
    @GetMapping(value = "/InputIndexdata2save")
    public int InputIndexdata2save(String date){
        DecimalFormat df = new DecimalFormat("#.##");
            //冲压车间
            try {

                //质量生态环境
                double chongyahuanjinx6 = environmentController.getWorkshopX6("冲压车间", date);
                double chongyahuanjinx7 = environmentController.getWorkshopX7("冲压车间", date);
                double chongyahuanjinx8 = environmentController.getWorkshopX8("冲压车间", date);
                double chongyahuanjinx9 = environmentController.getWorkshopX9("冲压车间", date);
                double chongyahuanjinx10 = environmentController.getWorkshopX10("冲压车间", date);

                double chongyaEE = 1.4 * chongyahuanjinx6 + 1.2 * chongyahuanjinx7 + 1.1 * chongyahuanjinx8 + 0.3 * chongyahuanjinx9 + 0.28 * chongyahuanjinx10; //
                //质量生态意识

                double chongyazhishix9 = consiciousController.getworkshopzhiliangzhishi("冲压车间", date);
                double chongyarenzhix10 = consiciousController.getworkshopzhiliangrenzhi("冲压车间", date);
                double chongyaxinnianx11 = consiciousController.getworkshopzhiliangxinnian("冲压车间", date);
                double chongyaxingweix12 = consiciousController.getworkshopzhiliangxingwei("冲压车间", date);
                double chongyaEC = 1.38 * chongyazhishix9 + 1.5 * chongyarenzhix10 + 1.12 * chongyaxinnianx11 + 1.16 * chongyaxingweix12;
                //质量生态责任
                double chongyazerenx5 = responsiilityController.getx5("冲压车间", date);
                double chongyazerenx6 = responsiilityController.getx6("冲压车间", date);
                double chongyazerenx7 = responsiilityController.getx7("冲压车间", date);
                double chongyaER = 1.1 * chongyazerenx5 + 1.53 * chongyazerenx6 + 1.6 * chongyazerenx7;
                //质量生态持续
                double chongyachixux3 = continueController.getx3("冲压车间", date);
                double chongyachixux4 = continueController.getx4("冲压车间", date);
                double chongyachixux5 = continueController.getx5("冲压车间", date);

                double chongyaES = chongyachixux3 + chongyachixux4 + chongyachixux5;
                double chongyaQE = (chongyaER / 142.1) * 30 + (chongyaES / 430.6) * 30 + (chongyaEC / 464.86) * 20 + (chongyaEE / 582.3) * 20;


                //车身车间


                //质量生态环境
                double cheshenhuanjinx6 = environmentController.getWorkshopX6("车身车间", date);
                double cheshenhuanjinx7 = environmentController.getWorkshopX7("车身车间", date);
                double cheshenhuanjinx8 = environmentController.getWorkshopX8("车身车间", date);
                double cheshenhuanjinx9 = environmentController.getWorkshopX9("车身车间", date);
                double cheshenhuanjinx10 = environmentController.getWorkshopX10("车身车间", date);

                double cheshenEE = 1.4 * cheshenhuanjinx6 + 1.2 * cheshenhuanjinx7 + 1.1 * cheshenhuanjinx8 + 0.3 * cheshenhuanjinx9 + 0.28 * cheshenhuanjinx10; //
                //质量生态意识

                double cheshenzhishix9 = consiciousController.getworkshopzhiliangzhishi("车身车间", date);
                double cheshenrenzhix10 = consiciousController.getworkshopzhiliangrenzhi("车身车间", date);
                double cheshenxinnianx11 = consiciousController.getworkshopzhiliangxinnian("车身车间", date);
                double cheshenxingweix12 = consiciousController.getworkshopzhiliangxingwei("车身车间", date);
                double cheshenEC = 1.38 * cheshenzhishix9 + 1.5 * cheshenrenzhix10 + 1.12 * cheshenxinnianx11 + 1.16 * cheshenxingweix12;
                //质量生态责任
                double cheshenzerenx5 = responsiilityController.getx5("车身车间", date);
                double cheshenzerenx6 = responsiilityController.getx6("车身车间", date);
                double cheshenzerenx7 = responsiilityController.getx7("车身车间", date);
                double cheshenER = 1.1 * cheshenzerenx5 + 1.53 * cheshenzerenx6 + 1.6 * cheshenzerenx7;
                //质量生态持续
                double cheshenchixux3 = continueController.getx3("车身车间", date);
                double cheshenchixux4 = continueController.getx4("车身车间", date);
                double cheshenchixux5 = continueController.getx5("车身车间", date);

                double cheshenES = cheshenchixux3 + cheshenchixux4 + cheshenchixux5;
                double cheshenQE = (cheshenER / 142.1) * 30 + (cheshenES / 430.6) * 30 + (cheshenEC / 464.86) * 20 + (cheshenEE / 582.3) * 20;


                //涂装车间


                //质量生态环境
                double tuzhuanghuanjinx6 = environmentController.getWorkshopX6("涂装车间", date);
                double tuzhuanghuanjinx7 = environmentController.getWorkshopX7("涂装车间", date);
                double tuzhuanghuanjinx8 = environmentController.getWorkshopX8("涂装车间", date);
                double tuzhuanghuanjinx9 = environmentController.getWorkshopX9("涂装车间", date);
                double tuzhuanghuanjinx10 = environmentController.getWorkshopX10("涂装车间", date);

                double tuzhuangEE = 1.4 * tuzhuanghuanjinx6 + 1.2 * tuzhuanghuanjinx7 + 1.1 * tuzhuanghuanjinx8 + 0.3 * tuzhuanghuanjinx9 + 0.28 * tuzhuanghuanjinx10; //
                //质量生态意识

                double tuzhuangzhishix9 = consiciousController.getworkshopzhiliangzhishi("涂装车间", date);
                double tuzhuangrenzhix10 = consiciousController.getworkshopzhiliangrenzhi("涂装车间", date);
                double tuzhuangxinnianx11 = consiciousController.getworkshopzhiliangxinnian("涂装车间", date);
                double tuzhuangxingweix12 = consiciousController.getworkshopzhiliangxingwei("涂装车间", date);
                double tuzhuangEC = 1.38 * tuzhuangzhishix9 + 1.5 * tuzhuangrenzhix10 + 1.12 * tuzhuangxinnianx11 + 1.16 * tuzhuangxingweix12;
                //质量生态责任
                double tuzhuangzerenx5 = responsiilityController.getx5("涂装车间", date);
                double tuzhuangzerenx6 = responsiilityController.getx6("涂装车间", date);
                double tuzhuangzerenx7 = responsiilityController.getx7("涂装车间", date);
                double tuzhuangER = 1.1 * tuzhuangzerenx5 + 1.53 * tuzhuangzerenx6 + 1.6 * tuzhuangzerenx7;
                //质量生态持续
                double tuzhuangchixux3 = continueController.getx3("涂装车间", date);
                double tuzhuangchixux4 = continueController.getx4("涂装车间", date);
                double tuzhuangchixux5 = continueController.getx5("涂装车间", date);

                double tuzhuangES = tuzhuangchixux3 + tuzhuangchixux4 + tuzhuangchixux5;
                double tuzhuangQE = (tuzhuangER / 142.1) * 30 + (tuzhuangES / 430.6) * 30 + (tuzhuangEC / 464.86) * 20 + (tuzhuangEE / 582.3) * 20;

                //总装车间


                //质量生态环境
                double zongzhuanghuanjinx6 = environmentController.getWorkshopX6("总装车间", date);
                double zongzhuanghuanjinx7 = environmentController.getWorkshopX7("总装车间", date);
                double zongzhuanghuanjinx8 = environmentController.getWorkshopX8("总装车间", date);
                double zongzhuanghuanjinx9 = environmentController.getWorkshopX9("总装车间", date);
                double zongzhuanghuanjinx10 = environmentController.getWorkshopX10("总装车间", date);

                double zongzhuangEE = 1.4 * zongzhuanghuanjinx6 + 1.2 * zongzhuanghuanjinx7 + 1.1 * zongzhuanghuanjinx8 + 0.3 * zongzhuanghuanjinx9 + 0.28 * zongzhuanghuanjinx10; //
                //质量生态意识

                double zongzhuangzhishix9 = consiciousController.getworkshopzhiliangzhishi("总装车间", date);
                double zongzhuangrenzhix10 = consiciousController.getworkshopzhiliangrenzhi("总装车间", date);
                double zongzhuangxinnianx11 = consiciousController.getworkshopzhiliangxinnian("总装车间", date);
                double zongzhuangxingweix12 = consiciousController.getworkshopzhiliangxingwei("总装车间", date);
                double zongzhuangEC = 1.38 * zongzhuangzhishix9 + 1.5 * zongzhuangrenzhix10 + 1.12 * zongzhuangxinnianx11 + 1.16 * zongzhuangxingweix12;
                //质量生态责任
                double zongzhuangzerenx5 = responsiilityController.getx5("总装车间", date);
                double zongzhuangzerenx6 = responsiilityController.getx6("总装车间", date);
                double zongzhuangzerenx7 = responsiilityController.getx7("总装车间", date);
                double zongzhuangER = 1.1 * zongzhuangzerenx5 + 1.53 * zongzhuangzerenx6 + 1.6 * zongzhuangzerenx7;
                //质量生态持续
                double zongzhuangchixux3 = continueController.getx3("总装车间", date);
                double zongzhuangchixux4 = continueController.getx4("总装车间", date);
                double zongzhuangchixux5 = continueController.getx5("总装车间", date);

                double zongzhuangES = zongzhuangchixux3 + zongzhuangchixux4 + zongzhuangchixux5;
                double zongzhuangQE = (zongzhuangER / 142.1) * 30 + (zongzhuangES / 430.6) * 30 + (zongzhuangEC / 464.86) * 20 + (zongzhuangEE / 582.3) * 20;


                //机加车间


                //质量生态环境
                double jijiahuanjinx6 = environmentController.getWorkshopX6("机加车间", date);
                double jijiahuanjinx7 = environmentController.getWorkshopX7("机加车间", date);
                double jijiahuanjinx8 = environmentController.getWorkshopX8("机加车间", date);
                double jijiahuanjinx9 = environmentController.getWorkshopX9("机加车间", date);
                double jijiahuanjinx10 = environmentController.getWorkshopX10("机加车间", date);

                double jijiaEE = 1.4 * jijiahuanjinx6 + 1.2 * jijiahuanjinx7 + 1.1 * jijiahuanjinx8 + 0.3 * jijiahuanjinx9 + 0.28 * jijiahuanjinx10; //
                //质量生态意识

                double jijiazhishix9 = consiciousController.getworkshopzhiliangzhishi("机加车间", date);
                double jijiarenzhix10 = consiciousController.getworkshopzhiliangrenzhi("机加车间", date);
                double jijiaxinnianx11 = consiciousController.getworkshopzhiliangxinnian("机加车间", date);
                double jijiaxingweix12 = consiciousController.getworkshopzhiliangxingwei("机加车间", date);
                double jijiaEC = 1.38 * jijiazhishix9 + 1.5 * jijiarenzhix10 + 1.12 * jijiaxinnianx11 + 1.16 * jijiaxingweix12;
                //质量生态责任
                double jijiazerenx5 = responsiilityController.getx5("机加车间", date);
                double jijiazerenx6 = responsiilityController.getx6("机加车间", date);
                double jijiazerenx7 = responsiilityController.getx7("机加车间", date);
                double jijiaER = 1.1 * jijiazerenx5 + 1.53 * jijiazerenx6 + 1.6 * jijiazerenx7;
                //质量生态持续
                double jijiachixux3 = continueController.getx3("发动机工厂", date);
                double jijiachixux4 = continueController.getx4("发动机工厂", date);
                double jijiachixux5 = continueController.getx5("发动机工厂", date);

                double jijiaES = jijiachixux3 + jijiachixux4 + jijiachixux5;
                double jijiaQE = (jijiaER / 142.1) * 30 + (jijiaES / 430.6) * 30 + (jijiaEC / 464.86) * 20 + (jijiaEE / 582.3) * 20;
                //装配车间


                //质量生态环境

                //质量生态环境
                double zhuangpeihuanjinx6 = environmentController.getWorkshopX6("装配车间", date);
                double zhuangpeihuanjinx7 = environmentController.getWorkshopX7("装配车间", date);
                double zhuangpeihuanjinx8 = environmentController.getWorkshopX8("装配车间", date);
                double zhuangpeihuanjinx9 = environmentController.getWorkshopX9("装配车间", date);
                double zhuangpeihuanjinx10 = environmentController.getWorkshopX10("装配车间", date);

                double zhuangpeiEE = 1.4 * zhuangpeihuanjinx6 + 1.2 * zhuangpeihuanjinx7 + 1.1 * zhuangpeihuanjinx8 + 0.3 * zhuangpeihuanjinx9 + 0.28 * zhuangpeihuanjinx10; //
                //质量生态意识

                double zhuangpeizhishix9 = consiciousController.getworkshopzhiliangzhishi("装配车间", date);
                double zhuangpeirenzhix10 = consiciousController.getworkshopzhiliangrenzhi("装配车间", date);
                double zhuangpeixinnianx11 = consiciousController.getworkshopzhiliangxinnian("装配车间", date);
                double zhuangpeixingweix12 = consiciousController.getworkshopzhiliangxingwei("装配车间", date);
                double zhuangpeiEC = 1.38 * zhuangpeizhishix9 + 1.5 * zhuangpeirenzhix10 + 1.12 * zhuangpeixinnianx11 + 1.16 * zhuangpeixingweix12;
                //质量生态责任
                double zhuangpeizerenx5 = responsiilityController.getx5("装配车间", date);
                double zhuangpeizerenx6 = responsiilityController.getx6("装配车间", date);
                double zhuangpeizerenx7 = responsiilityController.getx7("装配车间", date);
                double zhuangpeiER = 1.1 * zhuangpeizerenx5 + 1.53 * zhuangpeizerenx6 + 1.6 * zhuangpeizerenx7;
                //质量生态持续
                double zhuangpeichixux3 = continueController.getx3("发动机工厂", date);
                double zhuangpeichixux4 = continueController.getx4("发动机工厂", date);
                double zhuangpeichixux5 = continueController.getx5("发动机工厂", date);

                double zhuangpeiES = zhuangpeichixux3 + zhuangpeichixux4 + zhuangpeichixux5;
                double zhuangpeiQE = (zhuangpeiER / 142.1) * 30 + (zhuangpeiES / 430.6) * 30 + (zhuangpeiEC / 464.86) * 20 + (zhuangpeiEE / 582.3) * 20;


                switch (date.substring(5, 7)) {
                    case "01":
                        inputIndexdata2Service.Inputdata2yiyue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2yiyue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2yiyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2yiyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2yiyue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2yiyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "02":
                        inputIndexdata2Service.Inputdata2eryue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2eryue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2eryue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2eryue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2eryue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2eryue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "03":
                        inputIndexdata2Service.Inputdata2sanyue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2sanyue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2sanyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2sanyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2sanyue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2sanyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "04":
                        inputIndexdata2Service.Inputdata2siyue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2siyue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2siyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2siyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2siyue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2siyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "05":
                        inputIndexdata2Service.Inputdata2wuyue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2wuyue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2wuyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2wuyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2wuyue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2wuyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "06":
                        inputIndexdata2Service.Inputdata2liuyue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2liuyue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2liuyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2liuyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2liuyue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2liuyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "07":
                        inputIndexdata2Service.Inputdata2qiyue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2qiyue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2qiyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2qiyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2qiyue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2qiyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "08":
                        inputIndexdata2Service.Inputdata2bayue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2bayue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2bayue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2bayue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2bayue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2bayue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "09":
                        inputIndexdata2Service.Inputdata2jiuyue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2jiuyue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2jiuyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2jiuyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2jiuyue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2jiuyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "10":
                        inputIndexdata2Service.Inputdata2shiyue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2shiyue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2shiyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2shiyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2shiyue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2shiyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "11":
                        inputIndexdata2Service.Inputdata2shiyiyue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2shiyiyue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2shiyiyue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2shiyiyue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2shiyiyue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2shiyiyue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                    case "12":
                        inputIndexdata2Service.Inputdata2shieryue(0, Double.parseDouble(df.format(chongyaQE)));
                        inputIndexdata2Service.Inputdata2shieryue(1, Double.parseDouble(df.format(cheshenQE)));
                        inputIndexdata2Service.Inputdata2shieryue(2, Double.parseDouble(df.format(tuzhuangQE)));
                        inputIndexdata2Service.Inputdata2shieryue(3, Double.parseDouble(df.format(zongzhuangQE)));
                        inputIndexdata2Service.Inputdata2shieryue(4, Double.parseDouble(df.format(jijiaQE)));
                        inputIndexdata2Service.Inputdata2shieryue(5, Double.parseDouble(df.format(zhuangpeiQE)));
                        break;
                }

            }catch (Exception e){
                return 0;
            }
            return 1;

    }

    @ApiOperation("质量: 更新首页班组数据") //不同车间 不同月份 月份方法传入参数  车间六个车间分别计算
    @GetMapping(value = "/InputIndexdata3save")
    public int InputIndexdata3save(String date){
        DecimalFormat df = new DecimalFormat("#.##");
        try{

        //质量生态环境
        double chongyahuanjinx3=environmentController.getGroupX3("冲压车间",date);
        double chongyahuanjinx4=environmentController.getGroupX4("冲压车间",date);
        double chongyahuanjinx5=environmentController.getGroupX5("冲压车间",date);

        double chongyaEE=1.17*chongyahuanjinx3+0.22*chongyahuanjinx4+1.2*chongyahuanjinx5;
        //质量生态意识

        double chongyazhishix5=consiciousController.getgroupzhiliangzhishi("冲压车间",date);
        double chongyarenzhix6=consiciousController.getgroupzhiliangrenzhi("冲压车间",date);
        double chongyaxinnianx7=consiciousController.getgroupzhiliangxinnian("冲压车间",date);
        double chongyaxingweix8=consiciousController.getgroupzhiliangxingwei("冲压车间",date);
        double chongyaEC=1.3*chongyazhishix5+1.23*chongyarenzhix6+0.6*chongyaxinnianx7+1.2*chongyaxingweix8;
        //质量生态责任
        double chongyazerenx3=responsiilityController.getx3("冲压车间",date);
        double chongyazerenx4=responsiilityController.getx4("冲压车间",date);
        double chongyaER=1.52*chongyazerenx3+1.1*chongyazerenx4;
        //质量生态持续
        double chongyachixux2=continueController.getx2("冲压车间",date);


        double chongyaES=chongyachixux2;
        double chongyaQE=(chongyaER/142.1)*30+(chongyaES/430.6)*30+(chongyaEC/464.86)*20+(chongyaEE/582.3)*20;

        //车身车间
        //质量生态环境
        double cheshenhuanjinx3=environmentController.getGroupX3("车身车间",date);
        double cheshenhuanjinx4=environmentController.getGroupX4("车身车间",date);
        double cheshenhuanjinx5=environmentController.getGroupX5("车身车间",date);

        double cheshenEE=1.17*cheshenhuanjinx3+0.22*cheshenhuanjinx4+1.2*cheshenhuanjinx5;
        //质量生态意识

        double cheshenzhishix5=consiciousController.getgroupzhiliangzhishi("车身车间",date);
        double cheshenrenzhix6=consiciousController.getgroupzhiliangrenzhi("车身车间",date);
        double cheshenxinnianx7=consiciousController.getgroupzhiliangxinnian("车身车间",date);
        double cheshenxingweix8=consiciousController.getgroupzhiliangxingwei("车身车间",date);
        double cheshenEC=1.3*cheshenzhishix5+1.23*cheshenrenzhix6+0.6*cheshenxinnianx7+1.2*cheshenxingweix8;
        //质量生态责任
        double cheshenzerenx3=responsiilityController.getx3("车身车间",date);
        double cheshenzerenx4=responsiilityController.getx4("车身车间",date);
        double cheshenER=1.52*cheshenzerenx3+1.1*cheshenzerenx4;
        //质量生态持续
        double cheshenchixux2=continueController.getx2("车身车间",date);


        double cheshenES=cheshenchixux2;
        double cheshenQE=(cheshenER/142.1)*30+(cheshenES/430.6)*30+(cheshenEC/464.86)*20+(cheshenEE/582.3)*20;


        //涂装车间
        //质量生态环境
        double tuzhuanghuanjinx3=environmentController.getGroupX3("涂装车间",date);
        double tuzhuanghuanjinx4=environmentController.getGroupX4("涂装车间",date);
        double tuzhuanghuanjinx5=environmentController.getGroupX5("涂装车间",date);

        double tuzhuangEE=1.17*tuzhuanghuanjinx3+0.22*tuzhuanghuanjinx4+1.2*tuzhuanghuanjinx5;
        //质量生态意识

        double tuzhuangzhishix5=consiciousController.getgroupzhiliangzhishi("涂装车间",date);
        double tuzhuangrenzhix6=consiciousController.getgroupzhiliangrenzhi("涂装车间",date);
        double tuzhuangxinnianx7=consiciousController.getgroupzhiliangxinnian("涂装车间",date);
        double tuzhuangxingweix8=consiciousController.getgroupzhiliangxingwei("涂装车间",date);
        double tuzhuangEC=1.3*tuzhuangzhishix5+1.23*tuzhuangrenzhix6+0.6*tuzhuangxinnianx7+1.2*tuzhuangxingweix8;
        //质量生态责任
        double tuzhuangzerenx3=responsiilityController.getx3("涂装车间",date);
        double tuzhuangzerenx4=responsiilityController.getx4("涂装车间",date);
        double tuzhuangER=1.52*tuzhuangzerenx3+1.1*tuzhuangzerenx4;
        //质量生态持续
        double tuzhuangchixux2=continueController.getx2("涂装车间",date);


        double tuzhuangES=tuzhuangchixux2;
        double tuzhuangQE=(tuzhuangER/142.1)*30+(tuzhuangES/430.6)*30+(tuzhuangEC/464.86)*20+(tuzhuangEE/582.3)*20;

        //总装车间
        //质量生态环境
        double zongzhuanghuanjinx3=environmentController.getGroupX3("总装车间",date);
        double zongzhuanghuanjinx4=environmentController.getGroupX4("总装车间",date);
        double zongzhuanghuanjinx5=environmentController.getGroupX5("总装车间",date);

        double zongzhuangEE=1.17*zongzhuanghuanjinx3+0.22*zongzhuanghuanjinx4+1.2*zongzhuanghuanjinx5;
        //质量生态意识

        double zongzhuangzhishix5=consiciousController.getgroupzhiliangzhishi("总装车间",date);
        double zongzhuangrenzhix6=consiciousController.getgroupzhiliangrenzhi("总装车间",date);
        double zongzhuangxinnianx7=consiciousController.getgroupzhiliangxinnian("总装车间",date);
        double zongzhuangxingweix8=consiciousController.getgroupzhiliangxingwei("总装车间",date);
        double zongzhuangEC=1.3*zongzhuangzhishix5+1.23*zongzhuangrenzhix6+0.6*zongzhuangxinnianx7+1.2*zongzhuangxingweix8;
        //质量生态责任
        double zongzhuangzerenx3=responsiilityController.getx3("总装车间",date);
        double zongzhuangzerenx4=responsiilityController.getx4("总装车间",date);
        double zongzhuangER=1.52*zongzhuangzerenx3+1.1*zongzhuangzerenx4;
        //质量生态持续
        double zongzhuangchixux2=continueController.getx2("总装车间",date);


        double zongzhuangES=zongzhuangchixux2;
        double zongzhuangQE=(zongzhuangER/142.1)*30+(zongzhuangES/430.6)*30+(zongzhuangEC/464.86)*20+(zongzhuangEE/582.3)*20;

        //机加车间
        //质量生态环境
        double jijiahuanjinx3=environmentController.getGroupX3("机加车间",date);
        double jijiahuanjinx4=environmentController.getGroupX4("机加车间",date);
        double jijiahuanjinx5=environmentController.getGroupX5("机加车间",date);

        double jijiaEE=1.17*jijiahuanjinx3+0.22*jijiahuanjinx4+1.2*jijiahuanjinx5;
        //质量生态意识

        double jijiazhishix5=consiciousController.getgroupzhiliangzhishi("机加车间",date);
        double jijiarenzhix6=consiciousController.getgroupzhiliangrenzhi("机加车间",date);
        double jijiaxinnianx7=consiciousController.getgroupzhiliangxinnian("机加车间",date);
        double jijiaxingweix8=consiciousController.getgroupzhiliangxingwei("机加车间",date);
        double jijiaEC=1.3*jijiazhishix5+1.23*jijiarenzhix6+0.6*jijiaxinnianx7+1.2*jijiaxingweix8;
        //质量生态责任
        double jijiazerenx3=responsiilityController.getx3("机加车间",date);
        double jijiazerenx4=responsiilityController.getx4("机加车间",date);
        double jijiaER=1.52*jijiazerenx3+1.1*jijiazerenx4;
        //质量生态持续
        double jijiachixux2=continueController.getx2("发动机工厂",date);


        double jijiaES=jijiachixux2;
        double jijiaQE=(jijiaER/142.1)*30+(jijiaES/430.6)*30+(jijiaEC/464.86)*20+(jijiaEE/582.3)*20;


        //装配车间
        //质量生态环境
        double zhuangpeihuanjinx3=environmentController.getGroupX3("装配车间",date);
        double zhuangpeihuanjinx4=environmentController.getGroupX4("装配车间",date);
        double zhuangpeihuanjinx5=environmentController.getGroupX5("装配车间",date);

        double zhuangpeiEE=1.17*zhuangpeihuanjinx3+0.22*zhuangpeihuanjinx4+1.2*zhuangpeihuanjinx5;
        //质量生态意识

        double zhuangpeizhishix5=consiciousController.getgroupzhiliangzhishi("装配车间",date);
        double zhuangpeirenzhix6=consiciousController.getgroupzhiliangrenzhi("装配车间",date);
        double zhuangpeixinnianx7=consiciousController.getgroupzhiliangxinnian("装配车间",date);
        double zhuangpeixingweix8=consiciousController.getgroupzhiliangxingwei("装配车间",date);
        double zhuangpeiEC=1.3*zhuangpeizhishix5+1.23*zhuangpeirenzhix6+0.6*zhuangpeixinnianx7+1.2*zhuangpeixingweix8;
        //质量生态责任
        double zhuangpeizerenx3=responsiilityController.getx3("装配车间",date);
        double zhuangpeizerenx4=responsiilityController.getx4("装配车间",date);
        double zhuangpeiER=1.52*zhuangpeizerenx3+1.1*zhuangpeizerenx4;
        //质量生态持续
        double zhuangpeichixux2=continueController.getx2("发动机工厂",date);


        double zhuangpeiES=zhuangpeichixux2;
        double zhuangpeiQE=(zhuangpeiER/142.1)*30+(zhuangpeiES/430.6)*30+(zhuangpeiEC/464.86)*20+(zhuangpeiEE/582.3)*20;

        switch (date.substring(5,7)){
            case "01":
                inputIndexdata3Service.updateIndexdata3(0,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "02":
                inputIndexdata3Service.updateIndexdata3(1,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "03":
                inputIndexdata3Service.updateIndexdata3(2,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "04":
                inputIndexdata3Service.updateIndexdata3(3,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "05":
                inputIndexdata3Service.updateIndexdata3(4,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "06":
                inputIndexdata3Service.updateIndexdata3(5,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "07":
                inputIndexdata3Service.updateIndexdata3(6,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "08":
                inputIndexdata3Service.updateIndexdata3(7,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "09":
                inputIndexdata3Service.updateIndexdata3(8,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "10":
                inputIndexdata3Service.updateIndexdata3(9,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "11":
                inputIndexdata3Service.updateIndexdata3(10,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
            case "12":
                inputIndexdata3Service.updateIndexdata3(11,Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                break;
        }   }catch (Exception e){
            return 0;
        }
        return 1;


    }

    @ApiOperation("质量: 更新首页工位数据") //不同车间 不同月份 月份方法传入参数  车间六个车间分别计算
    @GetMapping(value = "/InputIndexdata4save")
    public int InputIndexdata4save(String date){
        DecimalFormat df = new DecimalFormat("#.##");
        try {


            //质量生态环境
            double chongyahuanjinx1 = environmentController.getStationX1("冲压车间", date);
            double chongyahuanjinx2 = environmentController.getStationX2("冲压车间", date);

            double chongyaEE = 1.26 * chongyahuanjinx1 + 0.26 * chongyahuanjinx2;
            //质量生态意识

            double chongyazhishix1 = consiciousController.getstationzhiliangzhishi("冲压车间", date);
            double chongyarenzhix2 = consiciousController.getstationzhiliangrenzhi("冲压车间", date);
            double chongyaxinnianx3 = consiciousController.getstationzhiliangxinnian("冲压车间", date);
            double chongyaxingweix4 = consiciousController.getstationzhiliangxingwei("冲压车间", date);
            double chongyaEC = 1.2 * chongyazhishix1 + 0.89 * chongyarenzhix2 + 0.7 * chongyaxinnianx3 + 1.32 * chongyaxingweix4;
            //质量生态责任
            double chongyazerenx1 = responsiilityController.getx1("冲压车间", date);
            double chongyazerenx2 = responsiilityController.getx2("冲压车间", date);
            double chongyaER = 1.62 * chongyazerenx1 + 0.97 * chongyazerenx2;
            //质量生态持续
            double chongyachixux1 = continueController.getx1("冲压车间", date);


            double chongyaES = chongyachixux1;
            double chongyaQE = (chongyaER / 142.1) * 30 + (chongyaES / 430.6) * 30 + (chongyaEC / 464.86) * 20 + (chongyaEE / 582.3) * 20;

            //车身车间
            //质量生态环境
            double cheshenhuanjinx1 = environmentController.getStationX1("车身车间", date);
            double cheshenhuanjinx2 = environmentController.getStationX2("车身车间", date);

            double cheshenEE = 1.26 * cheshenhuanjinx1 + 0.26 * cheshenhuanjinx2;
            //质量生态意识

            double cheshenzhishix1 = consiciousController.getstationzhiliangzhishi("车身车间", date);
            double cheshenrenzhix2 = consiciousController.getstationzhiliangrenzhi("车身车间", date);
            double cheshenxinnianx3 = consiciousController.getstationzhiliangxinnian("车身车间", date);
            double cheshenxingweix4 = consiciousController.getstationzhiliangxingwei("车身车间", date);
            double cheshenEC = 1.2 * cheshenzhishix1 + 0.89 * cheshenrenzhix2 + 0.7 * cheshenxinnianx3 + 1.32 * cheshenxingweix4;
            //质量生态责任
            double cheshenzerenx1 = responsiilityController.getx1("车身车间", date);
            double cheshenzerenx2 = responsiilityController.getx2("车身车间", date);
            double cheshenER = 1.62 * cheshenzerenx1 + 0.97 * cheshenzerenx2;
            //质量生态持续
            double cheshenchixux1 = continueController.getx1("车身车间", date);


            double cheshenES = cheshenchixux1;
            double cheshenQE = (cheshenER / 142.1) * 30 + (cheshenES / 430.6) * 30 + (cheshenEC / 464.86) * 20 + (cheshenEE / 582.3) * 20;

            //涂装车间
            //质量生态环境
            double tuzhuanghuanjinx1 = environmentController.getStationX1("涂装车间", date);
            double tuzhuanghuanjinx2 = environmentController.getStationX2("涂装车间", date);

            double tuzhuangEE = 1.26 * tuzhuanghuanjinx1 + 0.26 * tuzhuanghuanjinx2;
            //质量生态意识

            double tuzhuangzhishix1 = consiciousController.getstationzhiliangzhishi("涂装车间", date);
            double tuzhuangrenzhix2 = consiciousController.getstationzhiliangrenzhi("涂装车间", date);
            double tuzhuangxinnianx3 = consiciousController.getstationzhiliangxinnian("涂装车间", date);
            double tuzhuangxingweix4 = consiciousController.getstationzhiliangxingwei("涂装车间", date);
            double tuzhuangEC = 1.2 * tuzhuangzhishix1 + 0.89 * tuzhuangrenzhix2 + 0.7 * tuzhuangxinnianx3 + 1.32 * tuzhuangxingweix4;
            //质量生态责任
            double tuzhuangzerenx1 = responsiilityController.getx1("涂装车间", date);
            double tuzhuangzerenx2 = responsiilityController.getx2("涂装车间", date);
            double tuzhuangER = 1.62 * tuzhuangzerenx1 + 0.97 * tuzhuangzerenx2;
            //质量生态持续
            double tuzhuangchixux1 = continueController.getx1("涂装车间", date);


            double tuzhuangES = tuzhuangchixux1;
            double tuzhuangQE = (tuzhuangER / 142.1) * 30 + (tuzhuangES / 430.6) * 30 + (tuzhuangEC / 464.86) * 20 + (tuzhuangEE / 582.3) * 20;

            //总装车间
            //质量生态环境
            double zongzhuanghuanjinx1 = environmentController.getStationX1("总装车间", date);
            double zongzhuanghuanjinx2 = environmentController.getStationX2("总装车间", date);

            double zongzhuangEE = 1.26 * zongzhuanghuanjinx1 + 0.26 * zongzhuanghuanjinx2;
            //质量生态意识

            double zongzhuangzhishix1 = consiciousController.getstationzhiliangzhishi("总装车间", date);
            double zongzhuangrenzhix2 = consiciousController.getstationzhiliangrenzhi("总装车间", date);
            double zongzhuangxinnianx3 = consiciousController.getstationzhiliangxinnian("总装车间", date);
            double zongzhuangxingweix4 = consiciousController.getstationzhiliangxingwei("总装车间", date);
            double zongzhuangEC = 1.2 * zongzhuangzhishix1 + 0.89 * zongzhuangrenzhix2 + 0.7 * zongzhuangxinnianx3 + 1.32 * zongzhuangxingweix4;
            //质量生态责任
            double zongzhuangzerenx1 = responsiilityController.getx1("总装车间", date);
            double zongzhuangzerenx2 = responsiilityController.getx2("总装车间", date);
            double zongzhuangER = 1.62 * zongzhuangzerenx1 + 0.97 * zongzhuangzerenx2;
            //质量生态持续
            double zongzhuangchixux1 = continueController.getx1("总装车间", date);


            double zongzhuangES = zongzhuangchixux1;
            double zongzhuangQE = (zongzhuangER / 142.1) * 30 + (zongzhuangES / 430.6) * 30 + (zongzhuangEC / 464.86) * 20 + (zongzhuangEE / 582.3) * 20;

            //机加车间
            //质量生态环境
            double jijiahuanjinx1 = environmentController.getStationX1("机加车间", date);
            double jijiahuanjinx2 = environmentController.getStationX2("机加车间", date);

            double jijiaEE = 1.26 * jijiahuanjinx1 + 0.26 * jijiahuanjinx2;
            //质量生态意识

            double jijiazhishix1 = consiciousController.getstationzhiliangzhishi("机加车间", date);
            double jijiarenzhix2 = consiciousController.getstationzhiliangrenzhi("机加车间", date);
            double jijiaxinnianx3 = consiciousController.getstationzhiliangxinnian("机加车间", date);
            double jijiaxingweix4 = consiciousController.getstationzhiliangxingwei("机加车间", date);
            double jijiaEC = 1.2 * jijiazhishix1 + 0.89 * jijiarenzhix2 + 0.7 * jijiaxinnianx3 + 1.32 * jijiaxingweix4;
            //质量生态责任
            double jijiazerenx1 = responsiilityController.getx1("机加车间", date);
            double jijiazerenx2 = responsiilityController.getx2("机加车间", date);
            double jijiaER = 1.62 * jijiazerenx1 + 0.97 * jijiazerenx2;
            //质量生态持续
            double jijiachixux1 = continueController.getx1("发动机工厂", date);


            double jijiaES = jijiachixux1;
            double jijiaQE = (jijiaER / 142.1) * 30 + (jijiaES / 430.6) * 30 + (jijiaEC / 464.86) * 20 + (jijiaEE / 582.3) * 20;

            //装配车间
            //质量生态环境
            double zhaungpeihuanjinx1 = environmentController.getStationX1("装配车间", date);
            double zhaungpeihuanjinx2 = environmentController.getStationX2("装配车间", date);

            double zhaungpeiEE = 1.26 * zhaungpeihuanjinx1 + 0.26 * zhaungpeihuanjinx2;
            //质量生态意识

            double zhaungpeizhishix1 = consiciousController.getstationzhiliangzhishi("装配车间", date);
            double zhaungpeirenzhix2 = consiciousController.getstationzhiliangrenzhi("装配车间", date);
            double zhaungpeixinnianx3 = consiciousController.getstationzhiliangxinnian("装配车间", date);
            double zhaungpeixingweix4 = consiciousController.getstationzhiliangxingwei("装配车间", date);
            double zhaungpeiEC = 1.2 * zhaungpeizhishix1 + 0.89 * zhaungpeirenzhix2 + 0.7 * zhaungpeixinnianx3 + 1.32 * zhaungpeixingweix4;
            //质量生态责任
            double zhaungpeizerenx1 = responsiilityController.getx1("装配车间", date);
            double zhaungpeizerenx2 = responsiilityController.getx2("装配车间", date);
            double zhaungpeiER = 1.62 * zhaungpeizerenx1 + 0.97 * zhaungpeizerenx2;
            //质量生态持续
            double zhaungpeichixux1 = continueController.getx1("发动机工厂", date);


            double zhaungpeiES = zhaungpeichixux1;
            double zhuangpeiQE = (zhaungpeiER / 142.1) * 30 + (zhaungpeiES / 430.6) * 30 + (zhaungpeiEC / 464.86) * 20 + (zhaungpeiEE / 582.3) * 20;

            switch (date.substring(5, 7)) {
                case "01":
                    inputIndexdata4Service.updateIndexdata4(0, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "02":
                    inputIndexdata4Service.updateIndexdata4(1, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "03":
                    inputIndexdata4Service.updateIndexdata4(2, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "04":
                    inputIndexdata4Service.updateIndexdata4(3, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "05":
                    inputIndexdata4Service.updateIndexdata4(4, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "06":
                    inputIndexdata4Service.updateIndexdata4(5, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "07":
                    inputIndexdata4Service.updateIndexdata4(6, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "08":
                    inputIndexdata4Service.updateIndexdata4(7, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "09":
                    inputIndexdata4Service.updateIndexdata4(8, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "10":
                    inputIndexdata4Service.updateIndexdata4(9, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "11":
                    inputIndexdata4Service.updateIndexdata4(10, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
                case "12":
                    inputIndexdata4Service.updateIndexdata4(11, Double.parseDouble(df.format(chongyaQE)),Double.parseDouble(df.format(cheshenQE)),Double.parseDouble(df.format(tuzhuangQE)),Double.parseDouble(df.format(zongzhuangQE)),Double.parseDouble(df.format(jijiaQE)),Double.parseDouble(df.format(zhuangpeiQE)));
                    break;
            }

        }catch (Exception e){
            return 0;
        }
        return 1;
    }
}
