package me.zhengjie.modules.qe.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Table(name = "responsibility_chejianjichufen")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true) //开启链式调用注解
public class Responsibilitychejianjichufen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String chejian;
    private int fenshu;
}
