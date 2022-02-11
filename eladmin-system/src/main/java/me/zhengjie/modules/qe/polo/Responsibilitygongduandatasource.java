package me.zhengjie.modules.qe.polo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true) //开启链式调用注解
public class Responsibilitygongduandatasource {
    private String gongduan;
    private String chejian;
    private String date;
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
