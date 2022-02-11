package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.InputIndexdata3;
import me.zhengjie.modules.qe.domain.InputIndexdata4;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface InputIndexdata3Repository extends JpaRepository<InputIndexdata3,Integer> {

    @Transactional
    @Modifying
        //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update input_indexdata3 i set i.chongyachejian=?2,i.cheshenchejian=?3,i.tuzhuangchejian=?4,i.zongzhuangchejian=?5,i.jijiachejian=?6,i.zhuangpeichejian=?7 where i.id=?1",nativeQuery = true)
    void updateIndexdata3(int id,double chongyachejian,double cheshenchejian,double tuzhuangchejian,double zongzhuangchejian,double jijiachejian,double zhuangpeichejian);


}
