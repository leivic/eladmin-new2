package me.zhengjie.modules.qe.polo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true) //开启链式调用注解
public class Responsibilityzonglanchartdata { //返回一个这样的类,最后返回的结果就是 {xdata:[],cheshenchejiandata:[]......}
    private ArrayList xdata; //前端的横轴数据
    private ArrayList cheshenchejiandata;
    private ArrayList chongyachejiandata;
    private ArrayList tuzhuangchejiandata;
    private ArrayList zongzhuangchejiandata;
    private ArrayList jijiachejiandata;
    private ArrayList zhuangpeichejiandata;
}
