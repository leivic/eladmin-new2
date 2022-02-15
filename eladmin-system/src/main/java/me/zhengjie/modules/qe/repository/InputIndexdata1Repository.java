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


    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.yiyue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1yiyue( int id,double yiyue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.eryue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1eryue( int id,double eryue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.sanyue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1sanyue( int id,double sanyue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.siyue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1siyue( int id,double siyue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.wuyue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1wuyue( int id,double wuyue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.liuyue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1liuyue( int id,double liuyue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.qiyue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1qiyue( int id,double qiyue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.bayue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1bayue( int id,double bayue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.jiuyue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1jiuyue( int id,double jiuyue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.shiyue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1shiyue( int id,double shiyue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.shiyiyue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1shiyiyue( int id,double shiyiyue); //i选择区域 月份选择月份

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata1 i set i.shieryue=?2 where i.id=?1",nativeQuery = true)
    void updateIndexdata1shieryue( int id,double shieryue); //i选择区域 月份选择月份
}
