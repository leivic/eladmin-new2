package me.zhengjie.modules.qe.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="input_indexdata1")
public class InputIndexdata1 { //车间级别的 每个车间质量生态文明数据

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String chejian;
    private double yiyue;
    private double eryue;
    private double sanyue;
    private double siyue;
    private double wuyue;
    private double liuyue;
    private double qiyue;
    private double bayue;
    private double jiuyue;
    private double shiyue;
    private double shiyiyue;
    private double shieryue;
}
