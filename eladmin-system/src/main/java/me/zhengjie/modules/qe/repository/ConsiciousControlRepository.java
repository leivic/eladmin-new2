package me.zhengjie.modules.qe.repository;

import me.zhengjie.modules.qe.domain.ConsiciousControl;
import me.zhengjie.modules.qe.domain.ContinueFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Array;
import java.util.List;
public interface ConsiciousControlRepository  extends JpaRepository<ConsiciousControl,Integer> {

    @Transactional
    @Modifying //jpa的更新/删除操作必须要有这个注解 且写法和select不同 这种不一定改什么地方的sql 应该用动态sql来做 拼接sql语句
    @Query(value = "update consicious_control c set c.year=?2,c.type=?3,c.target=?4,c.targettype=?5,c.targetstandard=?6,c.department=?7,c.zoneperson=?8,c.person_in_charge=?9,c.goal=?10,c.lashengoal=?11,c.zhibiaofankuiren=?12,c.yiyuegoal=?13,c.yiyueshiji=?14,c.eryuegoal=?15,c.eryueshiji=?16,c.sanyuegoal=?17,c.sanyueshiji=?18,c.siyuegoal=?19,c.siyueshiji=?20,c.wuyuegoal=?21,c.wuyueshiji=?22,c.liuyuegoal=?23,c.liuyueshiji=?24,c.qiyuegoal=?25,c.qiyueshiji=?26,c.bayuegoal=?27,c.bayueshiji=?28,c.jiuyuegoal=?29,c.jiuyueshiji=?30,c.shiyuegoal=?31,c.shiyueshiji=?32,c.shiyiyuegoal=?33,c.shiyiyueshiji=?34,c.shieryuegoal=?35,c.shieryueshiji=?36 where c.id=?1",nativeQuery = true)
    void changeConsiciousControlbyid(
            int id, String year, String type, String target, String targettype, String targetstandard
            , String department,String zoneperson, String person_in_charge, String goal, String lashengoal, String zhibiaofankuiren
            , double yiyuemubiao,double yiyueshiji,double eryuemubiao,double eryueshiji,
            double sanyuemubiao,double sanyueshiji, double siyuemubiao, double siyueshiji,
            double wuyuemubiao,double wuyueshiji,double liuyuemubiao, double liuyueshiji,
            double qiyuemubiao,double qiyueshiji,double bayuemubiao, double bayueshiji,
            double jiuyuemubiao,double jiuyueshiji,double shiyuemubiao,double shiyueshiji,
            double shiyiyuemubiao,double shiyiyueshiji,double shieryuemubiao,double shieryueshiji);

    @Query(value = "select * from consicious_control  where " +" year = :year",nativeQuery = true)
    List<ConsiciousControl> findAllByYear(@Param("year") String year);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.yiyuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changeyiyuezhuangtaibyid(int id, int yiyuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.eryuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changeeryuezhuangtaibyid(int id, int eryuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.sanyuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changesanyuezhuangtaibyid(int id, int sanyuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.siyuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changesiyuezhuangtaibyid(int id, int siyuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.wuyuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changewuyuezhuangtaibyid(int id, int wuyuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.liuyuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changeliuyuezhuangtaibyid(int id, int liuyuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.qiyuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changeqiyuezhuangtaibyid(int id, int qiyuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.bayuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changebayuezhuangtaibyid(int id, int bayuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.jiuyuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changejiuyuezhuangtaibyid(int id, int jiuyuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.shiyuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changeshiyuezhuangtaibyid(int id, int shiyuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.shiyiyuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changeshiyiyuezhuangtaibyid(int id, int shiyiyuezhuangtai);

    @Transactional
    @Modifying
    @Query(value ="update consicious_control c set c.shieryuezhuangtai=?2 where c.id=?1",nativeQuery = true)
    void changeshieryuezhuangtaibyid(int id, int shieryuezhuangtai);
}
