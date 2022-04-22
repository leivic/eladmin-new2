package me.zhengjie.modules.qe.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;

@Table(name = "consicious_control")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class ConsiciousControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String year;
    private String type;
    private String target;
    private String targettype;
    private String targetstandard;
    private String department;
    private String zoneperson;
    private String person_in_charge;
    private String goal;
    private String lashengoal;
    private String zhibiaofankuiren;
    private double yiyuegoal;
    private double yiyueshiji;
    private int yiyuezhuangtai;
    private double eryuegoal;
    private double eryueshiji;
    private int eryuezhuangtai;
    private double sanyuegoal;
    private double sanyueshiji;
    private int sanyuezhuangtai;
    private double siyuegoal;
    private double siyueshiji;
    private int siyuezhuangtai;
    private double wuyuegoal;
    private double wuyueshiji;
    private int wuyuezhuangtai;
    private double liuyuegoal;
    private double liuyueshiji;
    private int liuyuezhuangtai;
    private double qiyuegoal;
    private double qiyueshiji;
    private int qiyuezhuangtai;
    private double bayuegoal;
    private double bayueshiji;
    private int bayuezhuangtai;
    private double jiuyuegoal;
    private double jiuyueshiji;
    private int jiuyuezhuangtai;
    private double shiyuegoal;
    private double shiyueshiji;
    private int shiyuezhuangtai;
    private double shiyiyuegoal;
    private double shiyiyueshiji;
    private int shiyiyuezhuangtai;
    private double shieryuegoal;
    private double shieryueshiji;
    private int shieryuezhuangtai;

}
