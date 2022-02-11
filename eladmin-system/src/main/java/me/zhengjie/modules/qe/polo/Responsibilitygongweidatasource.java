package me.zhengjie.modules.qe.polo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true) //开启链式调用注解
public class Responsibilitygongweidatasource { //工位基础数据的数据模型 数据类  一个实例就像是数据库中的一条记录
    private String chejian;
    private String date;
    private int jichufen;
    private int ppsr;
    private int ppsrchongfu;
    private int shenchanyizhi;
    private int faguixiang;
    private int shouhoufankui;
    private int quexianlanjie;
    private int shexianweigui;
    private int waibuchoucha;
    private int gongweihujian;
    private int anquanbaozhang;
    private int gechejiangongxu;
    private double shouhouwenti;
    private double quyufasheng;
    private double zhiliangwenti;
    private int zhiliangfangyu;
    private int zongji;
}
