package me.zhengjie.modules.qe.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.qe.domain.*;
import me.zhengjie.modules.qe.polo.*;
import me.zhengjie.modules.qe.repository.ResponsibilitychejianjichufenRepository;
import me.zhengjie.modules.qe.service.ResponsibilityDatasource1Service;
import me.zhengjie.modules.qe.service.ResponsibilityDatasource2Service;
import me.zhengjie.modules.qe.service.ResponsibilitychejianjichufenService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@Api(tags = "质量：质量生态责任")
@RequestMapping("/qe/reponsibility")
public class ResponsiilityController {
    @Autowired
    private ResponsibilityDatasource1Service responsibilityDatasource1Service;
    @Autowired
    private ResponsibilityDatasource2Service responsibilityDatasource2Service;
    @Autowired
    private ResponsibilitychejianjichufenRepository responsibilitychejianjichufenRepository;

    @Autowired
    private ResponsibilitychejianjichufenService responsibilitychejianjichufenService;

    @ApiOperation("新增基础数据1")
    @PostMapping(value = "/upload1")
    public int upload1(MultipartFile file, String file_type, String file_date, String level, String create_by) throws IOException {
        FileInputStream fns = (FileInputStream) file.getInputStream();
        XSSFWorkbook wb = new XSSFWorkbook(fns);//xssWorkbook少了hssworkbook的解析成 POIFSFileSystem数据类型这一步 通过POI的库实现的功能
        XSSFSheet sheetAt = wb.getSheetAt(0);
        if (sheetAt == null) {
            return 0; //前端通过返回的编码 判断回复什么样的信息 我返回一个int 返回0时，判断得到sheetA没有表
        }
        System.out.println(sheetAt.getLastRowNum());
        for(int i=1;i<sheetAt.getLastRowNum()+1;i++){ //判断第二列导入是否出错
            String zone=sheetAt.getRow(i).getCell(1).toString();
            if(zone.equals("冲压车间")==false&zone.equals("车身车间")==false&zone.equals("涂装车间")==false&zone.equals("总装车间")==false&zone.equals("机加车间")==false&zone.equals("装配车间")==false){
                return 0;//前端接收到0时 提醒数据还未导入成功 第二列有数据不是("冲压车间","涂装车间","总装车间","机加车间"...等等之一)
            }
        }

        for (int i = 1; i <sheetAt.getLastRowNum()+1 ; i++) {
            String dengji;
            try{
                dengji=sheetAt.getRow(i).getCell(4).toString();
            }catch (Exception e){
                dengji="";
            }
           if (dengji.equals("")){
                return 7;//当返回7时 提示等级那一列不能为空
           }

        }

        if(file_type.equals("PPSR系统管理问题")){
            for(int i=1;i<sheetAt.getLastRowNum()+1;i++){
                String shifouchonfu;
                try {
                    shifouchonfu = sheetAt.getRow(i).getCell(5).toString();
                }catch (Exception e){
                    shifouchonfu ="";
                }

                if(shifouchonfu.equals("是")==false&shifouchonfu.equals("否")==false){
                    return 2;//前端接收返回2时 是当选择PPSR系统管理问题时,是否重复那一列只能填是 或者否
                }
            }
        }else{ //不是PPSR系统管理问题 是否重复那一列只能为空
            for (int i = 1; i <sheetAt.getLastRowNum()+1 ; i++) {
                String shifouchonfu;
                try {
                    shifouchonfu = sheetAt.getRow(i).getCell(5).toString();
                }catch (Exception e){
                    shifouchonfu ="";
                }
                if(shifouchonfu.equals("")==false){
                    return 3;//前端接收返回3时 是当文件类型不是PPSR系统管理问题时,是否重复那一列只能为空 只能没有就报错
                }
            }
        }

        if(level.equals("车间")){
            for (int i=1;i<sheetAt.getLastRowNum()+1;i++){
                String zone2;
                try {
                    zone2 = sheetAt.getRow(i).getCell(2).toString();
                }catch (Exception e){
                    zone2 = "";
                }
                if (zone2.equals("")==false){
                    return 4;//前端接收返回4时，提示 当级别为车间时,第三列 工段/班组/工位如果必须为空  表明当前是车间级别的数据
                }
            }
        }

        if(level.equals("工段")|level.equals("班组")|level.equals("工位")){
            for (int i=1;i<sheetAt.getLastRowNum()+1;i++){
                String zone2;
                try {
                    zone2 = sheetAt.getRow(i).getCell(2).toString();
                }catch (Exception e){
                    zone2 = "";
                }
                if (zone2.equals("")==true){
                    return 5;//前端接收返回5时  提示 当级别为工段/班组/工位时 第三列必须不为空，如果为空则新增失败
                }
            }
        }

        if(level.equals("班组")|level.equals("工位")){
            if(file_type.equals("PPSR系统管理问题")==false&file_type.equals("工位互检发生问题")==false){
                return 6;// 当前端接收返回6时 提示当导入班组/工位级别的数据时，只能导入 PPSR系统管理问题和工位互检问题两项数据
            }
        }

        if(file_type.equals("外部抽查问题管理")){
            for (int i=1;i<sheetAt.getLastRowNum()+1;i++){
              String dengji = sheetAt.getRow(i).getCell(5).toString();
              if(dengji.equals("主要不符合")==false&dengji.equals("次要不符合")==false&dengji.equals("观察项")){
                  return 8;//当前端接收8时 提示导入类别为外部抽查问题时,等级只能是 "主要不符合" 几项
              }
            }
        }



        for (int i = 1; i < sheetAt.getLastRowNum() + 1; i++) {
            ResponsibilityDatasource1 responsibilityDatasource1 = new ResponsibilityDatasource1();
            responsibilityDatasource1.setDate(file_date);
            responsibilityDatasource1.setLevel(level);
            responsibilityDatasource1.setFile_type(file_type);
            responsibilityDatasource1.setCreate_by(create_by);
            responsibilityDatasource1.setZone(sheetAt.getRow(i).getCell(1).toString()); //取每行的第二列
            try {
                responsibilityDatasource1.setZone2(sheetAt.getRow(i).getCell(2).toString());
            } catch (Exception e) { //当setZone2 excel为空时会报错
                responsibilityDatasource1.setZone2("");//说明是车间级别的数据 没有输入2级区域 就设为空
            }
            responsibilityDatasource1.setWentimiaoshu(sheetAt.getRow(i).getCell(3).toString());
            responsibilityDatasource1.setDengji(sheetAt.getRow(i).getCell(4).toString());
            try {
                responsibilityDatasource1.setShifouchongfu(sheetAt.getRow(i).getCell(5).toString());
            }catch (Exception e){
                responsibilityDatasource1.setShifouchongfu(""); //是否重复的一栏可以为空
            }
             //是否重复excel上为空的话 就导不进去 会报空指针错误

            responsibilityDatasource1Service.saveData(responsibilityDatasource1);
        }
        return 1;
    }

    @ApiOperation("新增基础数据2")
    @PostMapping(value = "/upload2")
    public int upload2(MultipartFile file, String file_type, String file_date, String level, String create_by) throws IOException {
        FileInputStream fns = (FileInputStream) file.getInputStream();
        XSSFWorkbook wb = new XSSFWorkbook(fns);//xssWorkbook少了hssworkbook的解析成 POIFSFileSystem数据类型这一步 通过POI的库实现的功能
        XSSFSheet sheetAt = wb.getSheetAt(0);
        if (sheetAt == null) {
            return 0; //前端通过返回的编码 判断回复什么样的信息 我返回一个int 返回0时，判断得到sheetA没有表
        }

        for (int i = 1; i < sheetAt.getLastRowNum() + 1; i++) {
            ResponsibilityDatasource2 responsibilityDatasource2 = new ResponsibilityDatasource2();
            responsibilityDatasource2.setDate(file_date);
            responsibilityDatasource2.setLevel(level);
            responsibilityDatasource2.setFile_type(file_type);
            responsibilityDatasource2.setCreate_by(create_by);
            responsibilityDatasource2.setZone(sheetAt.getRow(i).getCell(1).toString());
            try {
                responsibilityDatasource2.setZone2(sheetAt.getRow(i).getCell(2).toString());
            } catch (Exception e) { //当setZone2 excel为空时会报错
                responsibilityDatasource2.setZone2("");//说明是车间级别的数据 没有输入2级区域 就设为空
            }
            responsibilityDatasource2.setFenshu(sheetAt.getRow(i).getCell(3).getNumericCellValue());
            responsibilityDatasource2Service.saveData(responsibilityDatasource2);
        }
        return 1;
    }

    @ApiOperation("查询所有文件数据1")
    @GetMapping(value = "/findAlldatasource1")
    public Page<ResponsibilityDatasource1> findAlldatasource1(int page, int size, String sort) { //这几个参数是从前端传过来的数据
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, sort);
        return responsibilityDatasource1Service.findAll(pageable);
    }


    @ApiOperation("查询所有文件数据2")
    @GetMapping(value = "/findAlldatasource2")
    public Page<ResponsibilityDatasource2> findAlldatasource2(int page, int size, String sort) { //这几个参数是从前端传过来的数据
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, sort);
        return responsibilityDatasource2Service.findAll(pageable);
    }
    public double getx8(String date,String zone){
        ArrayList<Responsibilitygongweidatasource> list=findAllchejianlistBydate(date);
        ArrayList<Responsibilitygongweidatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x1=list1.stream().mapToDouble(Responsibilitygongweidatasource::getQuexianlanjie).average().orElse(0D);
        return x1;
    }
    public double getx9(String date,String zone){
        ArrayList<Responsibilitygongweidatasource> list=findAllchejianlistBydate(date);
        ArrayList<Responsibilitygongweidatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x1=list1.stream().mapToDouble(Responsibilitygongweidatasource::getAnquanbaozhang).average().orElse(0D);
        return x1;
    }
    public double getx10(String date,String zone){
        ArrayList<Responsibilitygongweidatasource> list=findAllchejianlistBydate(date);
        ArrayList<Responsibilitygongweidatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x1=list1.stream().mapToDouble(Responsibilitygongweidatasource::getZhiliangfangyu).average().orElse(0D);
        return x1;
    }
    @ApiOperation("查找所有车间的数据表单")
    @GetMapping(value = "/findAllchejianlistBydate")
    public ArrayList<Responsibilitygongweidatasource> findAllchejianlistBydate(String date) {
        ArrayList<Responsibilitygongweidatasource> list = new ArrayList<>();


        List<ResponsibilityDatasource1> list1 = responsibilityDatasource1Service.findAllByLevel("车间", date); //根据车间和日期取出这个情况下的所有基础数据1

        // 新写的代码
        List<ResponsibilityDatasource2> listx =responsibilityDatasource2Service.findAllByLevel("车间",date); //根据车间和日期取出这个情况下的所有基础数据2     简单点，就是找出数据源2的数组里面，数据源1里面没有的东西
        ArrayList arrayListdata1=list1.stream().map(ResponsibilityDatasource1::getZone).distinct().collect(Collectors.toCollection(ArrayList::new)); //只有车间的不重复数组1
        ArrayList arrayListdatax=listx.stream().map(ResponsibilityDatasource2::getZone).distinct().collect(Collectors.toCollection(ArrayList::new));
        ArrayList arrayListextry=new ArrayList();
        arrayListdatax.forEach((e)->{
            if(arrayListdata1.contains(e)==false){
                arrayListextry.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        if(arrayListextry.size()>0) {
            for (int i = 0; i < arrayListextry.size(); i++) {
                Responsibilitygongweidatasource responsibilitygongweidatasource = new Responsibilitygongweidatasource();

                int jichufen; //基础分等数据 直接在下面的sql查询方法中解决
                int ppsr = 0;  //这部分数据要在内层循环中 循环list1的每一条数据 然后根据其文件类型 依次计算分数
                int ppsrchongfu = 0;
                int shenchanyizhi = 0;
                int faguixiang = 0;
                int shouhoufankui = 0;
                int quexianlanjie = 0;
                int shexianweigui = 0;
                int waibuchoucha = 0;
                int gongweihujian = 0;
                int anquanbaozhang;
                int gechejiangongxu = 0;
                double shouhouwenti; //直接在下面的sql查询方法中解决
                double quyufasheng; //直接在下面的sql查询方法中解决
                double zhiliangwenti; //直接在下面的sql查询方法中解决
                int zhiliangfangyu;
                int zongji;
                String zone = arrayListextry.get(i).toString();
                System.out.println("arrListextry的数据" + zone);
                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "售后问题整改措施落实情况").get(0);
                    shouhouwenti = responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "售后问题整改措施落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    shouhouwenti = 0;
                }

                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "区域发生问题汇总分析").get(0);
                    quyufasheng = responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "区域发生问题汇总分析").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    quyufasheng = 0;
                }

                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "质量问题记录、跟踪和落实情况").get(0);
                    zhiliangwenti = responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "质量问题记录、跟踪和落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    zhiliangwenti = 0;
                }

                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilitychejianjichufenRepository.findAllBychejian(zone);//这个zone就是从list1取出来的 循环当前的车间
                    jichufen = responsibilitychejianjichufenRepository.findAllBychejian(zone).get(0).getFenshu();
                } catch (Exception e) {
                    jichufen = 0;
                }

                quexianlanjie = ppsr + ppsrchongfu + shenchanyizhi + faguixiang;
                anquanbaozhang = shouhoufankui + shexianweigui + waibuchoucha;
                zhiliangfangyu = (int) (gongweihujian + gechejiangongxu + shouhouwenti + quyufasheng + zhiliangwenti);
                zongji = quexianlanjie + anquanbaozhang + zhiliangfangyu + jichufen;
                //将数据赋入新建的那个对象
                responsibilitygongweidatasource.setChejian(zone).setDate(date).setPpsr(ppsr).setJichufen(jichufen).setPpsrchongfu(ppsrchongfu)
                        .setShenchanyizhi(shenchanyizhi).setFaguixiang(faguixiang).setShouhoufankui(shouhoufankui).setQuexianlanjie(quexianlanjie)
                        .setShexianweigui(shexianweigui).setWaibuchoucha(waibuchoucha).setGongweihujian(gongweihujian).setAnquanbaozhang(anquanbaozhang)
                        .setGechejiangongxu(gechejiangongxu).setShouhouwenti(shouhouwenti).setQuyufasheng(quyufasheng).setZhiliangwenti(zhiliangwenti)
                        .setZhiliangfangyu(zhiliangfangyu).setZongji(zongji);
                //
                list.add(i, responsibilitygongweidatasource);
            }
        }




        // 更新的代码
        long count = list1.stream().map(ResponsibilityDatasource1::getZone).distinct().count(); //取出来的ResponsibilityDatasource1列表里有多少个种类的车间

        for (int i = 0; i < count; i++) { //循环每个不同的车间 一共就有这么多条数据  相当于每个车间就有一条 Responsibilitygongweidatasource的数据 然后所有Responsibilitygongweidatasource凑在一起 就是我们表格里面的数据
            Responsibilitygongweidatasource responsibilitygongweidatasource = new Responsibilitygongweidatasource();

            int jichufen; //基础分等数据 直接在下面的sql查询方法中解决
            int ppsr = 0;  //这部分数据要在内层循环中 循环list1的每一条数据 然后根据其文件类型 依次计算分数
            int ppsrchongfu = 0;
            int shenchanyizhi = 0;
            int faguixiang = 0;
            int shouhoufankui = 0;
            int quexianlanjie = 0;
            int shexianweigui = 0;
            int waibuchoucha = 0;
            int gongweihujian = 0;
            int anquanbaozhang;
            int gechejiangongxu = 0;
            double shouhouwenti; //直接在下面的sql查询方法中解决
            double quyufasheng; //直接在下面的sql查询方法中解决
            double zhiliangwenti; //直接在下面的sql查询方法中解决
            int zhiliangfangyu;
            int zongji;
            String zone = list1.stream().map(ResponsibilityDatasource1::getZone).distinct().collect(Collectors.toCollection(ArrayList::new)).get(i);// responsibilityDatasource1的集合的这次循环的数据 获得这次循环的 车间去重后 得到一个Arraylist集合 并且根据i 得到当前是哪个车间
            List<ResponsibilityDatasource1> list2 = responsibilityDatasource1Service.findAllByLevelanddateaAndZone("车间", date, zone);

            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "售后问题整改措施落实情况").get(0);
                shouhouwenti = responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "售后问题整改措施落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                shouhouwenti = 0;
            }

            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "区域发生问题汇总分析").get(0);
                quyufasheng = responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "区域发生问题汇总分析").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                quyufasheng = 0;
            }

            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "质量问题记录、跟踪和落实情况").get(0);
                zhiliangwenti = responsibilityDatasource2Service.findByLevelanddateandzoneandfiletype("车间", date, zone, "质量问题记录、跟踪和落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                zhiliangwenti = 0;
            }

            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilitychejianjichufenRepository.findAllBychejian(zone);//这个zone就是从list1取出来的 循环当前的车间
                jichufen = responsibilitychejianjichufenRepository.findAllBychejian(zone).get(0).getFenshu();
            } catch (Exception e) {
                jichufen = 0;
            }
            // 内层循环计算出一部分数据
            for (int j = 0; j < list2.size(); j++) {
                switch (list2.get(j).getFile_type()) {
                    case "PPSR系统管理问题":
                        if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("A")) {
                            ppsr = ppsr - 5; //每发生一例就减5
                        } else if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("B")) {
                            ppsr = ppsr - 3;
                        } else if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("C")) {
                            ppsr = ppsr - 2;
                        }else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("A")) {
                            ppsrchongfu = ppsrchongfu - 6;
                        } else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("B")) {
                            ppsrchongfu = ppsrchongfu - 4;
                        }else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("C")) {
                            ppsrchongfu = ppsrchongfu - 3;
                        }
                        break;
                    case "重复发生的问题":
                        ppsrchongfu = ppsrchongfu - 3;
                        break;
                    case "生产一致性问题":
                        if (list2.get(j).getDengji().equals("A")) {
                            shenchanyizhi = shenchanyizhi - 5;
                        } else if (list2.get(j).getDengji().equals("B")) {
                            shenchanyizhi = shenchanyizhi - 3;
                        }
                        break;
                    case "售后反馈质量问题":
                        if (list2.get(j).getDengji().equals("A")) {
                            shouhoufankui = shouhoufankui - 15;
                        } else if (list2.get(j).getDengji().equals("B")) {
                            shouhoufankui = shouhoufankui - 10;
                        } else if (list2.get(j).getDengji().equals("C")) {
                            shouhoufankui = shouhoufankui - 8;
                        }
                        break;
                    case "法规项问题管理":
                        if (list2.get(j).getDengji().equals("A")) {
                            faguixiang = faguixiang - 5;
                        } else if (list2.get(j).getDengji().equals("B")) {
                            faguixiang = faguixiang - 3;
                        }
                        break;
                    case "涉嫌违规车辆问题管理":
                        shexianweigui = shexianweigui - 5;
                        break;
                    case "外部抽查问题管理":
                        if (list2.get(j).getDengji().equals("主要不符合")) {
                            waibuchoucha = waibuchoucha - 10;
                        }
                        if (list2.get(j).getDengji().equals("次要不符合")) {
                            waibuchoucha = waibuchoucha - 8;
                        }
                        if (list2.get(j).getDengji().equals("观察项")) {
                            waibuchoucha = waibuchoucha - 3;
                        }
                        break;
                    case "工位互检发生问题":
                        if (list2.get(j).getDengji().equals("A")) {
                            if (gongweihujian + 3 < 21) {
                                gongweihujian = gongweihujian + 3;
                            } else if (gongweihujian + 3 > 20) {
                                gongweihujian = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("B")) {
                            if (gongweihujian + 2 < 21) {
                                gongweihujian = gongweihujian + 2;
                            } else if (gongweihujian + 2 > 20) {
                                gongweihujian = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("C")) {
                            if (gongweihujian + 1 < 21) {
                                gongweihujian = gongweihujian + 1;
                            } else if (gongweihujian + 1 > 20) {
                                gongweihujian = 20;
                            }
                        }
                        break;
                    case "各车间工序":
                        if (list2.get(j).getDengji().equals("A")) {
                            if (gechejiangongxu + 3 < 21) {
                                gechejiangongxu = gechejiangongxu + 3;
                            } else if (gechejiangongxu + 3 > 20) {
                                gechejiangongxu = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("B")) {
                            if (gechejiangongxu + 2 < 21) {
                                gechejiangongxu = gechejiangongxu + 2;
                            } else if (gechejiangongxu + 2 > 20) {
                                gechejiangongxu = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("C")) {
                            if (gechejiangongxu + 1 < 21) {
                                gechejiangongxu = gechejiangongxu + 1;
                            } else if (gechejiangongxu + 1 > 20) {
                                gechejiangongxu = 20;
                            }
                        }
                        break;


                }

            }
            quexianlanjie = ppsr + ppsrchongfu + shenchanyizhi + faguixiang;
            anquanbaozhang = shouhoufankui + shexianweigui + waibuchoucha;
            zhiliangfangyu = (int) (gongweihujian + gechejiangongxu + shouhouwenti + quyufasheng + zhiliangwenti);
            zongji = quexianlanjie + anquanbaozhang + zhiliangfangyu + jichufen;
            //将数据赋入新建的那个对象
            responsibilitygongweidatasource.setChejian(zone).setDate(date).setPpsr(ppsr).setJichufen(jichufen).setPpsrchongfu(ppsrchongfu)
                    .setShenchanyizhi(shenchanyizhi).setFaguixiang(faguixiang).setShouhoufankui(shouhoufankui).setQuexianlanjie(quexianlanjie)
                    .setShexianweigui(shexianweigui).setWaibuchoucha(waibuchoucha).setGongweihujian(gongweihujian).setAnquanbaozhang(anquanbaozhang)
                    .setGechejiangongxu(gechejiangongxu).setShouhouwenti(shouhouwenti).setQuyufasheng(quyufasheng).setZhiliangwenti(zhiliangwenti)
                    .setZhiliangfangyu(zhiliangfangyu).setZongji(zongji);
            //
            list.add(i, responsibilitygongweidatasource);
        }

        return list;
    }

    ;



    public double getx5(String date,String zone){
        ArrayList<Responsibilitygongduandatasource> list=findAllgongduanlistBydate(date);
        ArrayList<Responsibilitygongduandatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x1=list1.stream().mapToDouble(Responsibilitygongduandatasource::getQuexianlanjie).average().orElse(0D);
        return x1;
    }
    public double getx6(String date,String zone){
        ArrayList<Responsibilitygongduandatasource> list=findAllgongduanlistBydate(date);
        ArrayList<Responsibilitygongduandatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x1=list1.stream().mapToDouble(Responsibilitygongduandatasource::getAnquanbaozhang).average().orElse(0D);
        return x1;
    }
    public double getx7(String date,String zone){
        ArrayList<Responsibilitygongduandatasource> list=findAllgongduanlistBydate(date);
        ArrayList<Responsibilitygongduandatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x1=list1.stream().mapToDouble(Responsibilitygongduandatasource::getZhiliangfangyu).average().orElse(0D);
        return x1;
    }
    @ApiOperation("查找所有工段的数据表单")
    @GetMapping(value = "/findAllgongduanlistBydate")
    public ArrayList<Responsibilitygongduandatasource> findAllgongduanlistBydate(String date) {//最后返回一个工段LIST的表单
        ArrayList<Responsibilitygongduandatasource> list = new ArrayList<>(); //新建一个工段list


        List<ResponsibilityDatasource1> list1 = responsibilityDatasource1Service.findAllByLevel("工段", date); //根据工段和日期取出这个情况下的所有基础数据1

        //新代码  只判断工段有没有多出来的 车间查找可以从所属工段判断所属车间
        List<ResponsibilityDatasource2> listx =responsibilityDatasource2Service.findAllByLevel("工段",date);
        ArrayList arrayListdata1=list1.stream().map(ResponsibilityDatasource1::getZone2).distinct().collect(Collectors.toCollection(ArrayList::new)); //判断基础数据1中的不重复工段和基础数据2中的不重复工段
        ArrayList arrayListdatax=listx.stream().map(ResponsibilityDatasource2::getZone2).distinct().collect(Collectors.toCollection(ArrayList::new));
        ArrayList arrayListextry=new ArrayList(); //额外的工段数据
        arrayListdatax.forEach((e)->{
            if(arrayListdata1.contains(e)==false){
                arrayListextry.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        if(arrayListextry.size()>0) {
            for (int i = 0; i < arrayListextry.size(); i++) {

                Responsibilitygongduandatasource responsibilitygongduandatasource = new Responsibilitygongduandatasource();//新建一个工段类 一会儿要给这个工段类赋值

                int ppsr = 0;  //这部分数据要在内层循环中 循环list1的每一条数据 然后根据其文件类型 依次计算分数
                int ppsrchongfu = 0;
                int shenchanyizhi = 0;
                int faguixiang = 0;
                int shouhoufankui = 0;
                int quexianlanjie = 0;
                int shexianweigui = 0;
                int waibuchoucha = 0;
                int gongweihujian = 0;
                int anquanbaozhang;
                int gechejiangongxu = 0;
                double shouhouwenti; //直接在下面的sql查询方法中解决
                double quyufasheng; //直接在下面的sql查询方法中解决
                double zhiliangwenti; //直接在下面的sql查询方法中解决
                int zhiliangfangyu;
                int zongji;
                String zone2 = arrayListextry.get(i).toString();
                System.out.println("zone2是"+zone2);
                String zone = responsibilityDatasource2Service.findzoneByzone2(zone2).get(0).getZone();//根据工段查找车间名称
                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "售后问题整改措施落实情况").get(0);
                    shouhouwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "售后问题整改措施落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    shouhouwenti = 0;
                }


                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "区域发生问题汇总分析").get(0);
                    quyufasheng = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "区域发生问题汇总分析").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    quyufasheng = 0;
                }

                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "质量问题记录、跟踪和落实情况").get(0);
                    zhiliangwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "质量问题记录、跟踪和落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    zhiliangwenti = 0;
                }
                quexianlanjie = ppsr + ppsrchongfu + shenchanyizhi + faguixiang;
                anquanbaozhang = shouhoufankui + shexianweigui + waibuchoucha;
                zhiliangfangyu = (int) (gongweihujian + gechejiangongxu + shouhouwenti + quyufasheng + zhiliangwenti);
                zongji = quexianlanjie + anquanbaozhang + zhiliangfangyu;
                //将数据赋入新建的那个对象
                responsibilitygongduandatasource.setGongduan(zone2).setDate(date).setPpsr(ppsr).setPpsrchongfu(ppsrchongfu)
                        .setShenchanyizhi(shenchanyizhi).setFaguixiang(faguixiang).setShouhoufankui(shouhoufankui).setQuexianlanjie(quexianlanjie)
                        .setShexianweigui(shexianweigui).setWaibuchoucha(waibuchoucha).setGongweihujian(gongweihujian).setAnquanbaozhang(anquanbaozhang)
                        .setGechejiangongxu(gechejiangongxu).setShouhouwenti(shouhouwenti).setQuyufasheng(quyufasheng).setZhiliangwenti(zhiliangwenti)
                        .setZhiliangfangyu(zhiliangfangyu).setZongji(zongji).setChejian(zone);
                //
                list.add(i, responsibilitygongduandatasource);


            }
        }
        //新代码

        long count = list1.stream().map(ResponsibilityDatasource1::getZone2).distinct().count(); //取出来的ResponsibilityDatasource1列表里有多少个种类的工段

        for (int i = 0; i < count; i++) { //循环每个不同的工段 一共就有这么多条数据  相当于每个车间就有一条 Responsibilitygongweidatasource的数据 然后所有Responsibilitygongweidatasource凑在一起 就是我们表格里面的数据
            Responsibilitygongduandatasource responsibilitygongduandatasource = new Responsibilitygongduandatasource();//新建一个工段类 一会儿要给这个工段类赋值

            int ppsr = 0;  //这部分数据要在内层循环中 循环list1的每一条数据 然后根据其文件类型 依次计算分数
            int ppsrchongfu = 0;
            int shenchanyizhi = 0;
            int faguixiang = 0;
            int shouhoufankui = 0;
            int quexianlanjie = 0;
            int shexianweigui = 0;
            int waibuchoucha = 0;
            int gongweihujian = 0;
            int anquanbaozhang;
            int gechejiangongxu = 0;
            double shouhouwenti; //直接在下面的sql查询方法中解决
            double quyufasheng; //直接在下面的sql查询方法中解决
            double zhiliangwenti; //直接在下面的sql查询方法中解决
            int zhiliangfangyu;
            int zongji;
            String zone2 = list1.stream().map(ResponsibilityDatasource1::getZone2).distinct().collect(Collectors.toCollection(ArrayList::new)).get(i);// responsibilityDatasource1的集合的这次循环的数据 获得这次循环的 工段去重后 得到一个Arraylist集合 并且根据i 得到当前是哪个工段
            String zone = responsibilityDatasource1Service.findzoneByzone2(zone2).get(0).getZone();//根据工段查找车间名称
            List<ResponsibilityDatasource1> list2 = responsibilityDatasource1Service.findAllByLevelanddateaAndZone2("工段", date, zone2);

            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "售后问题整改措施落实情况").get(0);
                shouhouwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "售后问题整改措施落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                shouhouwenti = 0;
            }


            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "区域发生问题汇总分析").get(0);
                quyufasheng = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "区域发生问题汇总分析").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                quyufasheng = 0;
            }

            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "质量问题记录、跟踪和落实情况").get(0);
                zhiliangwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工段", date, zone2, "质量问题记录、跟踪和落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                zhiliangwenti = 0;
            }


            // 内层循环计算出一部分数据
            for (int j = 0; j < list2.size(); j++) {
                switch (list2.get(j).getFile_type()) {
                    case "PPSR系统管理问题":
                        if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("A")) {
                            ppsr = ppsr - 5; //每发生一例就减5
                        } else if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("B")) {
                            ppsr = ppsr - 3;
                        }else if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("C")) {
                            ppsr = ppsr - 2;
                        } else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("A")) {
                            ppsrchongfu = ppsrchongfu - 6;
                        } else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("B")) {
                            ppsrchongfu = ppsrchongfu - 4;
                        }else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("C")) {
                            ppsrchongfu = ppsrchongfu - 3;
                        }
                        break;
                    case "重复发生的问题":
                        ppsrchongfu = ppsrchongfu - 3;
                        break;
                    case "生产一致性问题":
                        if (list2.get(j).getDengji().equals("A")) {
                            shenchanyizhi = shenchanyizhi - 5;
                        } else if (list2.get(j).getDengji().equals("B")) {
                            shenchanyizhi = shenchanyizhi - 3;
                        }
                        break;
                    case "售后反馈质量问题":
                        if (list2.get(j).getDengji().equals("A")) {
                            shouhoufankui = shouhoufankui - 15;
                        } else if (list2.get(j).getDengji().equals("B")) {
                            shouhoufankui = shouhoufankui - 10;
                        } else if (list2.get(j).getDengji().equals("C")) {
                            shouhoufankui = shouhoufankui - 8;
                        }
                        break;
                    case "法规项问题管理":
                        if (list2.get(j).getDengji().equals("A")) {
                            faguixiang = faguixiang - 5;
                        } else if (list2.get(j).getDengji().equals("B")) {
                            faguixiang = faguixiang - 3;
                        }
                        break;
                    case "涉嫌违规车辆问题管理":
                        shexianweigui = shexianweigui - 5;
                        break;
                    case "外部抽查问题管理":
                        if (list2.get(j).getDengji().equals("主要不符合")) {
                            waibuchoucha = waibuchoucha - 10;
                        }
                        if (list2.get(j).getDengji().equals("次要不符合")) {
                            waibuchoucha = waibuchoucha - 8;
                        }
                        if (list2.get(j).getDengji().equals("观察项")) {
                            waibuchoucha = waibuchoucha - 3;
                        }
                        break;
                    case "工位互检发生问题":
                        if (list2.get(j).getDengji().equals("A")) {
                            if (gongweihujian + 3 < 21) {
                                gongweihujian = gongweihujian + 3;
                            } else if (gongweihujian + 3 > 20) {
                                gongweihujian = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("B")) {
                            if (gongweihujian + 2 < 21) {
                                gongweihujian = gongweihujian + 2;
                            } else if (gongweihujian + 2 > 20) {
                                gongweihujian = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("C")) {
                            if (gongweihujian + 1 < 21) {
                                gongweihujian = gongweihujian + 1;
                            } else if (gongweihujian + 1 > 20) {
                                gongweihujian = 20;
                            }
                        }
                        break;
                    case "各车间工序":
                        if (list2.get(j).getDengji().equals("A")) {
                            if (gechejiangongxu + 3 < 21) {
                                gechejiangongxu = gechejiangongxu + 3;
                            } else if (gechejiangongxu + 3 > 20) {
                                gechejiangongxu = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("B")) {
                            if (gechejiangongxu + 2 < 21) {
                                gechejiangongxu = gechejiangongxu + 2;
                            } else if (gechejiangongxu + 2 > 20) {
                                gechejiangongxu = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("C")) {
                            if (gechejiangongxu + 1 < 21) {
                                gechejiangongxu = gechejiangongxu + 1;
                            } else if (gechejiangongxu + 1 > 20) {
                                gechejiangongxu = 20;
                            }
                        }
                        break;


                }

            }
            quexianlanjie = ppsr + ppsrchongfu + shenchanyizhi + faguixiang;
            anquanbaozhang = shouhoufankui + shexianweigui + waibuchoucha;
            zhiliangfangyu = (int) (gongweihujian + gechejiangongxu + shouhouwenti + quyufasheng + zhiliangwenti);
            zongji = quexianlanjie + anquanbaozhang + zhiliangfangyu;
            //将数据赋入新建的那个对象
            responsibilitygongduandatasource.setGongduan(zone2).setDate(date).setPpsr(ppsr).setPpsrchongfu(ppsrchongfu)
                    .setShenchanyizhi(shenchanyizhi).setFaguixiang(faguixiang).setShouhoufankui(shouhoufankui).setQuexianlanjie(quexianlanjie)
                    .setShexianweigui(shexianweigui).setWaibuchoucha(waibuchoucha).setGongweihujian(gongweihujian).setAnquanbaozhang(anquanbaozhang)
                    .setGechejiangongxu(gechejiangongxu).setShouhouwenti(shouhouwenti).setQuyufasheng(quyufasheng).setZhiliangwenti(zhiliangwenti)
                    .setZhiliangfangyu(zhiliangfangyu).setZongji(zongji).setChejian(zone);
            //
            list.add(i, responsibilitygongduandatasource);
        }

        return list;
    }

    ;

    public double getx3(String date,String zone){
        ArrayList<Responsibilitybanzudatasource> list=findAllbanzulistBydate(date);
        ArrayList<Responsibilitybanzudatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x1=list1.stream().mapToDouble(Responsibilitybanzudatasource::getQuexianlanjie).average().orElse(0D);
        return x1;
    }

    public double getx4(String date,String zone){
        ArrayList<Responsibilitybanzudatasource> list=findAllbanzulistBydate(date);
        ArrayList<Responsibilitybanzudatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x1=list1.stream().mapToDouble(Responsibilitybanzudatasource::getZhiliangfangyu).average().orElse(0D);
        return x1;
    }
    @ApiOperation("查找所有班组的数据表单")
    @GetMapping(value = "/findAllbanzulistBydate")
    public ArrayList<Responsibilitybanzudatasource> findAllbanzulistBydate(String date) {//最后返回一个工段LIST的表单
        ArrayList<Responsibilitybanzudatasource> list = new ArrayList<>(); //新建一个班组list


        List<ResponsibilityDatasource1> list1 = responsibilityDatasource1Service.findAllByLevel("班组", date); //根据工段和日期取出这个情况下的所有基础数据1

        //新代码  只判断工段有没有多出来的 车间查找可以从所属工段判断所属车间
        List<ResponsibilityDatasource2> listx =responsibilityDatasource2Service.findAllByLevel("班组",date);
        ArrayList arrayListdata1=list1.stream().map(ResponsibilityDatasource1::getZone2).distinct().collect(Collectors.toCollection(ArrayList::new)); //判断基础数据1中的不重复工段和基础数据2中的不重复工段
        ArrayList arrayListdatax=listx.stream().map(ResponsibilityDatasource2::getZone2).distinct().collect(Collectors.toCollection(ArrayList::new));
        ArrayList arrayListextry=new ArrayList(); //额外的班组数据
        arrayListdatax.forEach((e)->{
            if(arrayListdata1.contains(e)==false){
                arrayListextry.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        if(arrayListextry.size()>0) {
            for (int i = 0; i < arrayListextry.size(); i++) {

                Responsibilitybanzudatasource responsibilitybanzudatasource = new Responsibilitybanzudatasource();//新建一个班组实例 一会儿要给这个班组实例赋值

                int ppsr = 0;  //这部分数据要在内层循环中 循环list1的每一条数据 然后根据其文件类型 依次计算分数
                int ppsrchongfu = 0;
                int shenchanyizhi = 0;
                int faguixiang = 0;
                int quexianlanjie = 0;
                int shouhoufankui = 0;
                int anquanbaozhang = 0;
                int gechejiangongxu = 0;
                double shouhouwenti; //直接在下面的sql查询方法中解决
                double quyufasheng; //直接在下面的sql查询方法中解决
                double zhiliangwenti; //直接在下面的sql查询方法中解决
                int zhiliangfangyu;
                int gongweihujian = 0;
                int zongji;
                String zone2 = arrayListextry.get(i).toString();// responsibilityDatasource1的集合的这次循环的数据 获得这次循环的 班组去重后 得到一个Arraylist集合 并且根据i 得到当前是哪个班组
                String zone = responsibilityDatasource2Service.findzoneByzone2(zone2).get(0).getZone();//根据班组查找车间名称


                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "售后问题整改措施落实情况").get(0);//这里之所以 get(0) 是因为人工输入 只输入一次嘛 所以按理说每个月只会有一次这个数据
                    shouhouwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "售后问题整改措施落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    shouhouwenti = 0;
                }


                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "区域发生问题汇总分析").get(0);
                    quyufasheng = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "区域发生问题汇总分析").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    quyufasheng = 0;
                }

                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "质量问题记录、跟踪和落实情况").get(0);
                    zhiliangwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "质量问题记录、跟踪和落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    zhiliangwenti = 0;
                }
                quexianlanjie = ppsr + ppsrchongfu;
                zhiliangfangyu = (int) (gongweihujian + shouhouwenti + zhiliangwenti);
                zongji = quexianlanjie + zhiliangfangyu;
                //将数据赋入新建的那个对象
                responsibilitybanzudatasource.setBanzu(zone2).setDate(date).setPpsr(ppsr).setPpsrchongfu(ppsrchongfu)
                        .setQuexianlanjie(quexianlanjie)
                        .setGongweihujian(gongweihujian)
                        .setShouhouwenti(shouhouwenti).setZhiliangwenti(zhiliangwenti)
                        .setZhiliangfangyu(zhiliangfangyu).setZongji(zongji).setChejian(zone);
                //
                list.add(i, responsibilitybanzudatasource);


            }
        }
        //新代码





        long count = list1.stream().map(ResponsibilityDatasource1::getZone2).distinct().count(); //取出来的ResponsibilityDatasource1列表里有多少个种类的班组

        for (int i = 0; i < count; i++) { //循环每个不同的工段 一共就有这么多条数据  相当于每个车间就有一条 Responsibilitygongweidatasource的数据 然后所有Responsibilitygongweidatasource凑在一起 就是我们表格里面的数据
            Responsibilitybanzudatasource responsibilitybanzudatasource = new Responsibilitybanzudatasource();//新建一个班组实例 一会儿要给这个班组实例赋值

            int ppsr = 0;  //这部分数据要在内层循环中 循环list1的每一条数据 然后根据其文件类型 依次计算分数
            int ppsrchongfu = 0;
            int shenchanyizhi = 0;
            int faguixiang = 0;
            int quexianlanjie = 0;
            int shouhoufankui = 0;
            int anquanbaozhang = 0;
            int gechejiangongxu = 0;
            double shouhouwenti; //直接在下面的sql查询方法中解决
            double quyufasheng; //直接在下面的sql查询方法中解决
            double zhiliangwenti; //直接在下面的sql查询方法中解决
            int zhiliangfangyu;
            int gongweihujian = 0;
            int zongji;
            String zone2 = list1.stream().map(ResponsibilityDatasource1::getZone2).distinct().collect(Collectors.toCollection(ArrayList::new)).get(i);// responsibilityDatasource1的集合的这次循环的数据 获得这次循环的 班组去重后 得到一个Arraylist集合 并且根据i 得到当前是哪个班组
            String zone = responsibilityDatasource1Service.findzoneByzone2(zone2).get(0).getZone();//根据班组查找车间名称
            List<ResponsibilityDatasource1> list2 = responsibilityDatasource1Service.findAllByLevelanddateaAndZone2("班组", date, zone2);//在Responsibilitydatasource1中查找所有当前班组的数据 作为list2

            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "售后问题整改措施落实情况").get(0);//这里之所以 get(0) 是因为人工输入 只输入一次嘛 所以按理说每个月只会有一次这个数据
                shouhouwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "售后问题整改措施落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                shouhouwenti = 0;
            }


            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "区域发生问题汇总分析").get(0);
                quyufasheng = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "区域发生问题汇总分析").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                quyufasheng = 0;
            }

            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "质量问题记录、跟踪和落实情况").get(0);
                zhiliangwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("班组", date, zone2, "质量问题记录、跟踪和落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                zhiliangwenti = 0;
            }


            // 内层循环计算出一部分数据
            for (int j = 0; j < list2.size(); j++) { //这里循环所有没问题 每种问题都可能只有一个
                switch (list2.get(j).getFile_type()) {
                    case "PPSR系统管理问题":
                        if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("A")) {
                            ppsr = ppsr - 5; //每发生一例就减5
                        } else if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("B")) {
                            ppsr = ppsr - 3;
                        }else if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("C")) {
                            ppsr = ppsr - 2;
                        } else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("A")) {
                            ppsrchongfu = ppsrchongfu - 6;
                        } else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("B")) {
                            ppsrchongfu = ppsrchongfu - 4;
                        }else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("C")) {
                            ppsrchongfu = ppsrchongfu - 3;
                        }
                        break;
                    case "重复发生的问题":
                        ppsrchongfu = ppsrchongfu - 3;
                        break;

                    case "工位互检发生问题":
                        if (list2.get(j).getDengji().equals("A")) {
                            if (gongweihujian + 3 < 21) {
                                gongweihujian = gongweihujian + 3;
                            } else if (gongweihujian + 3 > 20) {
                                gongweihujian = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("B")) {
                            if (gongweihujian + 2 < 21) {
                                gongweihujian = gongweihujian + 2;
                            } else if (gongweihujian + 2 > 20) {
                                gongweihujian = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("C")) {
                            if (gongweihujian + 1 < 21) {
                                gongweihujian = gongweihujian + 1;
                            } else if (gongweihujian + 1 > 20) {
                                gongweihujian = 20;
                            }
                        }
                        break;



                }

            }
            quexianlanjie = ppsr + ppsrchongfu;
            zhiliangfangyu = (int) (gongweihujian + shouhouwenti + zhiliangwenti);
            zongji = quexianlanjie + zhiliangfangyu;
            //将数据赋入新建的那个对象
            responsibilitybanzudatasource.setBanzu(zone2).setDate(date).setPpsr(ppsr).setPpsrchongfu(ppsrchongfu)
                    .setQuexianlanjie(quexianlanjie)
                    .setGongweihujian(gongweihujian)
                    .setShouhouwenti(shouhouwenti).setZhiliangwenti(zhiliangwenti)
                    .setZhiliangfangyu(zhiliangfangyu).setZongji(zongji).setChejian(zone);
            //
            list.add(i, responsibilitybanzudatasource);
        }

        return list;
    }

    ;

    public double getx1(String date,String zone){//从已有的查找所有工位数据表单的方法 来得到x1
        ArrayList<Responsibilityzhengongweidatasource> list=findAllgongweilistBydate(date);
        ArrayList<Responsibilityzhengongweidatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x1=list1.stream().mapToDouble(Responsibilityzhengongweidatasource::getQuexianlanjie).average().orElse(0D);
        return x1;
    }

    public double getx2(String date,String zone){//从已有的查找所有工位数据表单的方法 来得到x1
        ArrayList<Responsibilityzhengongweidatasource> list=findAllgongweilistBydate(date);
        ArrayList<Responsibilityzhengongweidatasource> list1=new ArrayList<>();

        list.forEach((e)->{
            if(e.getChejian().equals(zone)){
                list1.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        Double x2=list1.stream().mapToDouble(Responsibilityzhengongweidatasource::getZhiliangfangyu).average().orElse(0D);
        return x2;
    }
    @ApiOperation("查找所有工位的数据表单")
    @GetMapping(value = "/findAllgongweilistBydate")
    public ArrayList<Responsibilityzhengongweidatasource> findAllgongweilistBydate(String date) {//最后返回一个工段LIST的表单
        ArrayList<Responsibilityzhengongweidatasource> list = new ArrayList<>(); //新建一个工段list


        List<ResponsibilityDatasource1> list1 = responsibilityDatasource1Service.findAllByLevel("工位", date); //根据工段和日期取出这个情况下的所有基础数据1
        //新代码  只判断工段有没有多出来的 车间查找可以从所属工段判断所属车间
        List<ResponsibilityDatasource2> listx =responsibilityDatasource2Service.findAllByLevel("工位",date);
        ArrayList arrayListdata1=list1.stream().map(ResponsibilityDatasource1::getZone2).distinct().collect(Collectors.toCollection(ArrayList::new)); //判断基础数据1中的不重复工段和基础数据2中的不重复工段
        ArrayList arrayListdatax=listx.stream().map(ResponsibilityDatasource2::getZone2).distinct().collect(Collectors.toCollection(ArrayList::new));
        ArrayList arrayListextry=new ArrayList(); //额外的工位数据
        arrayListdatax.forEach((e)->{
            if(arrayListdata1.contains(e)==false){
                arrayListextry.add(e);//得到一个新的 数据源2中有的车间在数据源1中找不到的
            }
        });
        if(arrayListextry.size()>0) {
            for (int i = 0; i < arrayListextry.size(); i++) {

                Responsibilityzhengongweidatasource responsibilityzhengongweidatasource = new Responsibilityzhengongweidatasource();//新建一个工段类 一会儿要给这个工段类赋值

                int ppsr = 0;  //这部分数据要在内层循环中 循环list1的每一条数据 然后根据其文件类型 依次计算分数
                int ppsrchongfu = 0;
                int quexianlanjie;
                int gongweihujian = 0;
                double shouhouwenti; //直接在下面的sql查询方法中解决
                double zhiliangwenti; //直接在下面的sql查询方法中解决
                int zhiliangfangyu;
                int zongji;
                String zone2 = arrayListextry.get(i).toString();
                String zone = responsibilityDatasource2Service.findzoneByzone2(zone2).get(0).getZone();//根据工段查找车间名称


                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工位", date, zone2, "售后问题整改措施落实情况").get(0);
                    shouhouwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工位", date, zone2, "售后问题整改措施落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    shouhouwenti = 0;
                }


                try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工位", date, zone2, "质量问题记录、跟踪和落实情况").get(0);
                    zhiliangwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工位", date, zone2, "质量问题记录、跟踪和落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
                } catch (Exception e) {
                    zhiliangwenti = 0;
                }
                quexianlanjie = ppsr + ppsrchongfu;
                zhiliangfangyu = (int) (gongweihujian + shouhouwenti + zhiliangwenti);
                zongji = quexianlanjie + zhiliangfangyu;
                //将数据赋入新建的那个对象
                responsibilityzhengongweidatasource.setGongwei(zone2).setDate(date).setPpsr(ppsr).setPpsrchongfu(ppsrchongfu)
                        .setQuexianlanjie(quexianlanjie)
                        .setGongweihujian(gongweihujian)
                        .setShouhouwenti(shouhouwenti).setZhiliangwenti(zhiliangwenti)
                        .setZhiliangfangyu(zhiliangfangyu).setZongji(zongji).setChejian(zone);
                //
                list.add(i, responsibilityzhengongweidatasource);


            }
        }
        //新代码















        long count = list1.stream().map(ResponsibilityDatasource1::getZone2).distinct().count(); //取出来的ResponsibilityDatasource1列表里有多少个种类的工段

        for (int i = 0; i < count; i++) { //循环每个不同的工段 一共就有这么多条数据  相当于每个车间就有一条 Responsibilitygongweidatasource的数据 然后所有Responsibilitygongweidatasource凑在一起 就是我们表格里面的数据
            Responsibilityzhengongweidatasource responsibilityzhengongweidatasource = new Responsibilityzhengongweidatasource();//新建一个工段类 一会儿要给这个工段类赋值

            int ppsr = 0;  //这部分数据要在内层循环中 循环list1的每一条数据 然后根据其文件类型 依次计算分数
            int ppsrchongfu = 0;
            int quexianlanjie;
            int gongweihujian = 0;
            double shouhouwenti; //直接在下面的sql查询方法中解决
            double zhiliangwenti; //直接在下面的sql查询方法中解决
            int zhiliangfangyu;
            int zongji;
            String zone2 = list1.stream().map(ResponsibilityDatasource1::getZone2).distinct().collect(Collectors.toCollection(ArrayList::new)).get(i);// responsibilityDatasource1的集合的这次循环的数据 获得这次循环的 工段去重后 得到一个Arraylist集合 并且根据i 得到当前是哪个工段
            String zone = responsibilityDatasource1Service.findzoneByzone2(zone2).get(0).getZone();//根据工段查找车间名称

            List<ResponsibilityDatasource1> list2 = responsibilityDatasource1Service.findAllByLevelanddateaAndZone2("工位", date, zone2);

            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工位", date, zone2, "售后问题整改措施落实情况").get(0);
                shouhouwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工位", date, zone2, "售后问题整改措施落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                shouhouwenti = 0;
            }


            try { // 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工位", date, zone2, "质量问题记录、跟踪和落实情况").get(0);
                zhiliangwenti = responsibilityDatasource2Service.findByLevelanddateandzone2andfiletype("工位", date, zone2, "质量问题记录、跟踪和落实情况").get(0).getFenshu();//强转为int 数据库根据上面得到的zone查出来后是个List 所以我们就取List的第一条，再取那一条数据中的分数
            } catch (Exception e) {
                zhiliangwenti = 0;
            }


            // 内层循环计算出一部分数据
            for (int j = 0; j < list2.size(); j++) {
                switch (list2.get(j).getFile_type()) {
                    case "PPSR系统管理问题":
                        if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("A")) {
                            ppsr = ppsr - 5; //每发生一例就减5
                        } else if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("B")) {
                            ppsr = ppsr - 3;
                        }else if (list2.get(j).getShifouchongfu().equals("否") && list2.get(j).getDengji().equals("C")) {
                            ppsr = ppsr - 2;
                        } else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("A")) {
                            ppsrchongfu = ppsrchongfu - 6;
                        } else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("B")) {
                            ppsrchongfu = ppsrchongfu - 4;
                        }else if (list2.get(j).getShifouchongfu().equals("是") && list2.get(j).getDengji().equals("C")) {
                            ppsrchongfu = ppsrchongfu - 3;
                        }
                        break;
                    case "重复发生的问题":
                        ppsrchongfu = ppsrchongfu - 3;
                        break;

                    case "工位互检发生问题":
                        if (list2.get(j).getDengji().equals("A")) {
                            if (gongweihujian + 3 < 21) {
                                gongweihujian = gongweihujian + 3;
                            } else if (gongweihujian + 3 > 20) {
                                gongweihujian = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("B")) {
                            if (gongweihujian + 2 < 21) {
                                gongweihujian = gongweihujian + 2;
                            } else if (gongweihujian + 2 > 20) {
                                gongweihujian = 20;
                            }
                        } else if (list2.get(j).getDengji().equals("C")) {
                            if (gongweihujian + 1 < 21) {
                                gongweihujian = gongweihujian + 1;
                            } else if (gongweihujian + 1 > 20) {
                                gongweihujian = 20;
                            }
                        }
                        break;


                }

            }
            quexianlanjie = ppsr + ppsrchongfu;
            zhiliangfangyu = (int) (gongweihujian + shouhouwenti + zhiliangwenti);
            zongji = quexianlanjie + zhiliangfangyu;
            //将数据赋入新建的那个对象
            responsibilityzhengongweidatasource.setGongwei(zone2).setDate(date).setPpsr(ppsr).setPpsrchongfu(ppsrchongfu)
                    .setQuexianlanjie(quexianlanjie)
                    .setGongweihujian(gongweihujian)
                    .setShouhouwenti(shouhouwenti).setZhiliangwenti(zhiliangwenti)
                    .setZhiliangfangyu(zhiliangfangyu).setZongji(zongji).setChejian(zone);
            //
            list.add(i, responsibilityzhengongweidatasource);
        }

        return list;
    }

    ;


    @ApiOperation("查找前端总览总计图表的数据")
    @GetMapping(value = "/getzonglanzongjichartdata")
    public Responsibilityzonglanchartdata getzonglanzongjichartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getZongji());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }


    @ApiOperation("查找前端总览缺陷图表的数据")
    @GetMapping(value = "/getzonglanquexianchartdata")
    public Responsibilityzonglanchartdata getzonglanquexianchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getQuexianlanjie());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }


    @ApiOperation("查找前端总览安全保障图表的数据")
    @GetMapping(value = "/getzonglananquanchartdata")
    public Responsibilityzonglanchartdata getzonglananquanchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getAnquanbaozhang());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }


    @ApiOperation("查找前端总览质量防御图表的数据")
    @GetMapping(value = "/getzonglanzhiliangchartdata")
    public Responsibilityzonglanchartdata getzonglanzhiliangchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangfangyu());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }


    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表所有车间ppsr的数据")
    @GetMapping(value = "/getfenxippsrchartdata")
    public Responsibilityzonglanchartdata getfenxippsrchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getPpsr());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }


    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表所有车间ppsr重复的数据")
    @GetMapping(value = "/getfenxippsrchongfuchartdata")
    public Responsibilityzonglanchartdata getfenxippsrchongfuchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getPpsrchongfu());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }

    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表所有车间生产一致性问题管理的数据")
    @GetMapping(value = "/getfenxishenchanyizhichartdata")
    public Responsibilityzonglanchartdata getfenxishenchanyizhichartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getShenchanyizhi());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }

    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表所有车间法规项问题管理的数据")
    @GetMapping(value = "/getfenxifaguixiangchartdata")
    public Responsibilityzonglanchartdata getfenxifaguixiangchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }

    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表所有车间售后反馈问题管理的数据")
    @GetMapping(value = "/getfenxishouhoufankuichartdata")
    public Responsibilityzonglanchartdata getfenxishouhoufankuichartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getFaguixiang());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getShouhoufankui());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }

    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表所有车间外部抽查问题管理的数据")
    @GetMapping(value = "/getfenxiwaibuchouchachartdata")
    public Responsibilityzonglanchartdata getfenxiwaibuchouchachartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getWaibuchoucha());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }

    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表所有车间涉嫌车辆违规问题管理的数据")
    @GetMapping(value = "/getfenxishexiancheliangweiguichartdata")
    public Responsibilityzonglanchartdata getfenxishexiancheliangweiguichartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getShexianweigui());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }

    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表所有车间工位互检问题管理的数据")
    @GetMapping(value = "/getfenxigongweihujianchartdata")
    public Responsibilityzonglanchartdata getfenxigongweihujianchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getGongweihujian());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }

    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表所有车间各车间上工序的数据")
    @GetMapping(value = "/getfenxigechejianshanggongxuchartdata")
    public Responsibilityzonglanchartdata getfenxigechejianshanggongxuchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getGechejiangongxu());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }

    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析报表售后问题整改措施落实情况的数据")
    @GetMapping(value = "/getfenxishouhouwentichartdata")
    public Responsibilityzonglanchartdata getfenxishouhouwentichartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getShouhouwenti());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }

    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析区域发生的数据")
    @GetMapping(value = "/getfenxiquyufashengchartdata")
    public Responsibilityzonglanchartdata getfenxiquyufashengchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getQuyufasheng());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }


    /* 几个车间的PPSR系统管理数据   */
    @ApiOperation("查找前端分析质量问题纪录的数据")
    @GetMapping(value = "/getfenxizhiliangwentijiluchartdata")
    public Responsibilityzonglanchartdata getfenxizhiliangwentijiluchartdata(String year) { //传入年份和一个选择的总计
        Responsibilityzonglanchartdata responsibilityzonglanchartdata = new Responsibilityzonglanchartdata();

        // 要传入responsibilityzonglanchartdata 的七个list
        ArrayList xdataarrayList = new ArrayList();
        ArrayList cheshenarrayList = new ArrayList();
        ArrayList chongyaarrayList =new ArrayList();
        ArrayList tuzhaungarrayList =new ArrayList();
        ArrayList zongzhuangarrayList = new ArrayList();
        ArrayList jijiaarrayList  =new ArrayList();
        ArrayList zhaungpeiarrayList= new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            /*先循环每个月份 每个月份取一次总数据 然后循环数据里的每一条 */

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }//得到date

                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //

            } //里面在循环每个date处理
        }
        else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }
                ArrayList<Responsibilitygongweidatasource> totalarrayList=findAllchejianlistBydate(date); // 拿这个List是在下面的分支之外的
                if(totalarrayList.size()==0){ //如果查出来的数组List是空 就几个赋值LIST都加0
                    cheshenarrayList.add(i-1,0);
                    chongyaarrayList.add(i-1,0);
                    tuzhaungarrayList.add(i-1,0);
                    zongzhuangarrayList.add(i-1,0);
                    jijiaarrayList.add(i-1,0);
                    zhaungpeiarrayList.add(i-1,0);
                }else { //有数据查出来了数据的情况
                    int cheshenstatus=0,chongyastatus=0,tuzhuangstatus=0,zongzhuangstatus=0,jijiastatus=0,zhuangpeistatus=0;
                    for (int j = 0; j <totalarrayList.size() ; j++) {
                        Responsibilitygongweidatasource responsibilitygongweidatasource=totalarrayList.get(j);//
                        switch (responsibilitygongweidatasource.getChejian()){
                            case "车身车间":
                                cheshenarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                cheshenstatus=1;
                                break;
                            case "冲压车间":
                                chongyaarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                chongyastatus=1;
                                break;
                            case "涂装车间":
                                tuzhaungarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                tuzhuangstatus=1;
                                break;
                            case "总装车间":
                                zongzhuangarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                zongzhuangstatus=1;
                                break;
                            case "机加车间":
                                jijiaarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                jijiastatus=1;
                                break;
                            case "装配车间":
                                zhaungpeiarrayList.add(i-1,responsibilitygongweidatasource.getZhiliangwenti());
                                zhuangpeistatus=1;
                                break;
                        }
                    }
                    if(cheshenstatus==0){cheshenarrayList.add(i-1,0);}
                    if (chongyastatus==0){chongyaarrayList.add(i-1,0);}
                    if(tuzhuangstatus==0){tuzhaungarrayList.add(i-1,0);}
                    if(zongzhuangstatus==0){zongzhuangarrayList.add(i-1,0);}
                    if(jijiastatus==0){jijiaarrayList.add(i-1,0);}
                    if(zhuangpeistatus==0){zhaungpeiarrayList.add(i-1,0);}

                }






                xdataarrayList.add(i - 1, Integer.toString(i) + '月'); //




            }

        }
        //




        responsibilityzonglanchartdata.setXdata(xdataarrayList);//将做好的XDATA数据赋值进去
        responsibilityzonglanchartdata.setChongyachejiandata(chongyaarrayList);
        responsibilityzonglanchartdata.setCheshenchejiandata(cheshenarrayList);
        responsibilityzonglanchartdata.setTuzhuangchejiandata(tuzhaungarrayList);
        responsibilityzonglanchartdata.setZongzhuangchejiandata(zongzhuangarrayList);
        responsibilityzonglanchartdata.setJijiachejiandata(jijiaarrayList);
        responsibilityzonglanchartdata.setZhuangpeichejiandata(zhaungpeiarrayList);
        return responsibilityzonglanchartdata;
    }


    @ApiOperation("根据ID删除数据源1")
    @PostMapping(value = "/deletedatasource1byid")
    public void deletedatasource1byid(Integer id){
        responsibilityDatasource1Service.deletedatasource1byid(id);
    }

    @ApiOperation("根据ID删除数据源2")
    @PostMapping(value = "/deletedatasource2byid")
    public void deletedatasource2byid(Integer id){
        responsibilityDatasource2Service.deletedatasource2byid(id);
    }


    @ApiOperation("根据ID更新基础分")
    @PostMapping(value = "/updatejichufen")
    public void updatejichufen(int id,int fenshu){
        responsibilitychejianjichufenService.updatejichufen(id, fenshu);
    }


    @ApiOperation("发现所有基础分")
    @GetMapping(value = "/findalljichufen")
    public List<Responsibilitychejianjichufen> findalljichufen(){
       return responsibilitychejianjichufenService.findAlljichufen();
    }


}
