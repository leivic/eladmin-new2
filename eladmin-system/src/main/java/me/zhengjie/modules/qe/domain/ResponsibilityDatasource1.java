package me.zhengjie.modules.qe.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Table(name = "responsibility_datasource1")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true) //开启链式调用注解
public class ResponsibilityDatasource1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String file_type;
    private String date;
    private String level;
    private String zone;
    private String zone2;
    private String wentimiaoshu;
    private String dengji;
    private String shifouchongfu;
    private String zerenquyu="基础";
    private String create_by;
}
