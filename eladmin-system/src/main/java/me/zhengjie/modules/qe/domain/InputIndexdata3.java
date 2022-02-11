package me.zhengjie.modules.qe.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="input_indexdata3")
public class InputIndexdata3 { //班组级别的 每个车间质量生态文明数据

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String yuefen;
    private double chongyachejian;
    private double cheshenchejian;
    private double tuzhuangchejian;
    private double zongzhuangchejian;
    private double jijiachejian;
    private double zhuangpeichejian;
}
