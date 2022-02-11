package me.zhengjie.modules.qe.polo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true) //开启链式调用注解
public class Responsibilityzhengongweidatasource {
    private String gongwei;
    private String chejian;
    private String date;
    private int ppsr;
    private int ppsrchongfu;
    private int quexianlanjie;
    private int gongweihujian;
    private double shouhouwenti;
    private double zhiliangwenti;
    private int zhiliangfangyu;
    private int zongji;
}
