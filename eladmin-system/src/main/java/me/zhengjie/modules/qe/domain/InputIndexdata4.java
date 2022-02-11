package me.zhengjie.modules.qe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="input_indexdata4")
public class InputIndexdata4 { //工位级别的每个车间质量生态文明数据


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
