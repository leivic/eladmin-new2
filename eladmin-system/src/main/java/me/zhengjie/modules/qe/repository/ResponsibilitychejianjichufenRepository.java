package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.ResponsibilityDatasource1;
import me.zhengjie.modules.qe.domain.Responsibilitychejianjichufen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ResponsibilitychejianjichufenRepository extends JpaRepository<Responsibilitychejianjichufen,Integer> {
    @Query(value = "select * from responsibility_chejianjichufen  where " +" chejian = :chejian ",nativeQuery = true)
    List<Responsibilitychejianjichufen> findAllBychejian(@Param("chejian") String chejian);



    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同
    @Query(value = "update responsibility_chejianjichufen i set i.fenshu=?2 where i.id=?1",nativeQuery = true)
    void updatejichufen(int id,int fenshu);
}
