package me.zhengjie.modules.qe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;


@Table(name = "consicious_workshop")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class ConsiciousWorkshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String zone;
    private String date;
    private String workshopname;
    private double zhiliangzhishi;
    private double zhiliangrenzhi;
    private double zhiliangxinnian;
    private double zhiliangxingwei;
}
