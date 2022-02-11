package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.InputIndexdata1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface InputIndexdata1Repository extends JpaRepository<InputIndexdata1,Integer> {


    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.yiyue=?2,i.eryue=?3,i.sanyue=?4,i.siyue=?5,i.wuyue=?6,i.liuyue=?7,i.qiyue=?8,i.bayue=?9,i.jiuyue=?10,i.shiyue=?11,i.shiyiyue=?12,i.shieryue=?13 where i.id=?1",nativeQuery = true)
    void updateIndexdata1( int id,double yiyue,double eryue,double sanyue,double siyue, double wuyue,double liuyue,double qiyue,double bayue, double jiuyue, double shiyue, double shiyiyue,double shieryue);
}
