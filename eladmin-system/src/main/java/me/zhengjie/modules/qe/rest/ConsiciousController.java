package me.zhengjie.modules.qe.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.qe.domain.*;
import me.zhengjie.modules.qe.polo.Consiciouszhiliangzhishi;
import me.zhengjie.modules.qe.polo.EnvironmentHealthZone;
import me.zhengjie.modules.qe.service.ConsiciousGroupService;
import me.zhengjie.modules.qe.service.ConsiciousStationService;
import me.zhengjie.modules.qe.service.ConsiciousWorkshopService;
import me.zhengjie.modules.qe.service.ConsiciousZoneService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
@RestController
@RequiredArgsConstructor
@Api(tags = "质量：质量生态意识")
@RequestMapping("/qe/consicious")
public class ConsiciousController {
    @Autowired
    private ConsiciousStationService consiciousStationService;
    @Autowired
    private ConsiciousGroupService consiciousGroupService;
    @Autowired
    private ConsiciousZoneService consiciousZoneService;
    @Autowired
    private ConsiciousWorkshopService consiciousWorkshopService;


    @ApiOperation("增加质量生态意识工位数据 ")
    @PostMapping("/uploadstation")
    public int uploadstation(@RequestParam("file") MultipartFile file,String date)throws IOException {
        FileInputStream fns=(FileInputStream)file.getInputStream();
        XSSFWorkbook wb=new XSSFWorkbook(fns);//xssWorkbook少了hssworkbook的解析成 POIFSFileSystem数据类型这一步
        XSSFSheet sheetAt = wb.getSheetAt(0);
        List<ConsiciousStation> list=new ArrayList<>(); //LIST是一个抽象类 不能实例化为一个List对象

        for (int i = 2; i <sheetAt.getLastRowNum() + 1 ; i++) {
            String zone= sheetAt.getRow(i).getCell(0).toString();
            if(zone.equals("冲压车间")==false&zone.equals("车身车间")==false&zone.equals("涂装车间")==false&zone.equals("总装车间")==false&zone.equals("机加车间")==false&zone.equals("装配车间")==false){
                return 9;//返回9时  说明第一列没有输入冲压 车身 涂装 总装 机加 装配 其中之一
            }
        }
        if(sheetAt.getRow(1).getLastCellNum()!=7){//标题不是只有7列
            return 11;
        }



        for (int i = 2; i <sheetAt.getLastRowNum() + 1 ; i++) {
            ConsiciousStation consiciousStation=new ConsiciousStation();
            consiciousStation.setDate(date);
            try{
            consiciousStation.setZone(sheetAt.getRow(i).getCell(0).toString());
            consiciousStation.setStationindex(sheetAt.getRow(i).getCell(1).toString());
            consiciousStation.setStationname(sheetAt.getRow(i).getCell(2).toString());
            consiciousStation.setZhiliangzhishi(sheetAt.getRow(i).getCell(3).getNumericCellValue());
            consiciousStation.setZhiliangrenzhi(sheetAt.getRow(i).getCell(4).getNumericCellValue());
            consiciousStation.setZhiliangxinnian(sheetAt.getRow(i).getCell(5).getNumericCellValue());
            consiciousStation.setZhiliangxingwei(sheetAt.getRow(i).getCell(6).getNumericCellValue());}
            catch (Exception e){
                return 2;//return2时  提示有数据为空 一个数据都没存下来
            }
            list.add(i-2,consiciousStation);//将对象添加进List里面
        }

        consiciousStationService.savedata(list);



        return 1; //当return1时 数据成功存入
    }

    @ApiOperation("增加质量生态意识班组数据 ")
    @PostMapping("/uploadgroup")
    public int uploadgroup(@RequestParam("file") MultipartFile file,String date)throws IOException {
        FileInputStream fns=(FileInputStream)file.getInputStream();
        XSSFWorkbook wb=new XSSFWorkbook(fns);//xssWorkbook少了hssworkbook的解析成 POIFSFileSystem数据类型这一步
        XSSFSheet sheetAt = wb.getSheetAt(0);
        List<ConsiciousGroup> list=new ArrayList<>(); //LIST是一个抽象类 不能实例化为一个List对象

        for (int i = 2; i <sheetAt.getLastRowNum() + 1 ; i++) {
            String zone= sheetAt.getRow(i).getCell(0).toString();
            if(zone.equals("冲压车间")==false&zone.equals("车身车间")==false&zone.equals("涂装车间")==false&zone.equals("总装车间")==false&zone.equals("机加车间")==false&zone.equals("装配车间")==false){
                return 9;//返回9时  说明第一列没有输入冲压 车身 涂装 总装 机加 装配 其中之一
            }
        }

        if(sheetAt.getRow(1).getLastCellNum()!=6){//标题不是只有6列
            return 11;
        }



        for (int i = 2; i <sheetAt.getLastRowNum() + 1 ; i++) {
            ConsiciousGroup consiciousGroup=new ConsiciousGroup();
            consiciousGroup.setDate(date);
            try{
                consiciousGroup.setZone(sheetAt.getRow(i).getCell(0).toString());
                consiciousGroup.setGroupname(sheetAt.getRow(i).getCell(1).toString());
                consiciousGroup.setZhiliangzhishi(sheetAt.getRow(i).getCell(2).getNumericCellValue());
                consiciousGroup.setZhiliangrenzhi(sheetAt.getRow(i).getCell(3).getNumericCellValue());
                consiciousGroup.setZhiliangxinnian(sheetAt.getRow(i).getCell(4).getNumericCellValue());
                consiciousGroup.setZhiliangxingwei(sheetAt.getRow(i).getCell(5).getNumericCellValue());}
            catch (Exception e){
                return 2;//return2时  提示有数据为空 一个数据都没存下来
            }
            list.add(i-2,consiciousGroup);//将对象添加进List里面
        }

        consiciousGroupService.savadata(list);



        return 1; //当return1时 数据成功存入
    }

    @ApiOperation("增加质量生态意识工段数据 ")
    @PostMapping("/uploadworkshop")
    public int uploadworkshop(@RequestParam("file") MultipartFile file,String date)throws IOException {
        FileInputStream fns=(FileInputStream)file.getInputStream();
        XSSFWorkbook wb=new XSSFWorkbook(fns);//xssWorkbook少了hssworkbook的解析成 POIFSFileSystem数据类型这一步
        XSSFSheet sheetAt = wb.getSheetAt(0);
        List<ConsiciousWorkshop> list=new ArrayList<>(); //LIST是一个抽象类 不能实例化为一个List对象

        for (int i = 2; i <sheetAt.getLastRowNum() + 1 ; i++) {
            String zone= sheetAt.getRow(i).getCell(0).toString();
            if(zone.equals("冲压车间")==false&zone.equals("车身车间")==false&zone.equals("涂装车间")==false&zone.equals("总装车间")==false&zone.equals("机加车间")==false&zone.equals("装配车间")==false){
                return 9;//返回9时  说明第一列没有输入冲压 车身 涂装 总装 机加 装配 其中之一
            }
        }

        if(sheetAt.getRow(1).getLastCellNum()!=6){//标题不是只有6列
            return 11;
        }

        for (int i = 2; i <sheetAt.getLastRowNum() + 1 ; i++) {
            ConsiciousWorkshop consiciousWorkshop=new ConsiciousWorkshop();
            consiciousWorkshop.setDate(date);
            try{
                consiciousWorkshop.setZone(sheetAt.getRow(i).getCell(0).toString());
                consiciousWorkshop.setWorkshopname(sheetAt.getRow(i).getCell(1).toString());
                consiciousWorkshop.setZhiliangzhishi(sheetAt.getRow(i).getCell(2).getNumericCellValue());
                consiciousWorkshop.setZhiliangrenzhi(sheetAt.getRow(i).getCell(3).getNumericCellValue());
                consiciousWorkshop.setZhiliangxinnian(sheetAt.getRow(i).getCell(4).getNumericCellValue());
                consiciousWorkshop.setZhiliangxingwei(sheetAt.getRow(i).getCell(5).getNumericCellValue());}
            catch (Exception e){
                return 2;//return2时  提示有数据为空 一个数据都没存下来
            }
            list.add(i-2,consiciousWorkshop);//将对象添加进List里面
        }

        consiciousWorkshopService.savedata(list);



        return 1; //当return1时 数据成功存入
    }

    @ApiOperation("增加质量生态意识车间数据 ")
    @PostMapping("/uploadzone")
    public int uploadzone(@RequestParam("file") MultipartFile file,String date)throws IOException {
        FileInputStream fns=(FileInputStream)file.getInputStream();
        XSSFWorkbook wb=new XSSFWorkbook(fns);//xssWorkbook少了hssworkbook的解析成 POIFSFileSystem数据类型这一步
        XSSFSheet sheetAt = wb.getSheetAt(0);
        List<ConsiciousZone> list=new ArrayList<>(); //LIST是一个抽象类 不能实例化为一个List对象

        for (int i = 2; i <sheetAt.getLastRowNum() + 1 ; i++) {
            String zone= sheetAt.getRow(i).getCell(0).toString();
            if(zone.equals("冲压车间")==false&zone.equals("车身车间")==false&zone.equals("涂装车间")==false&zone.equals("总装车间")==false&zone.equals("机加车间")==false&zone.equals("装配车间")==false){
                return 9;//返回9时  说明第一列没有输入冲压 车身 涂装 总装 机加 装配 其中之一
            }
        }

        if(sheetAt.getRow(1).getLastCellNum()!=5){//标题不是只有5列
            return 11;
        }

        for (int i = 2; i <sheetAt.getLastRowNum() + 1 ; i++) {
            ConsiciousZone consiciousZone=new ConsiciousZone();
            consiciousZone.setDate(date);
            try{
                consiciousZone.setZone(sheetAt.getRow(i).getCell(0).toString());
                consiciousZone.setZhiliangzhishi(sheetAt.getRow(i).getCell(1).getNumericCellValue());
                consiciousZone.setZhiliangrenzhi(sheetAt.getRow(i).getCell(2).getNumericCellValue());
                consiciousZone.setZhiliangxinnian(sheetAt.getRow(i).getCell(3).getNumericCellValue());
                consiciousZone.setZhiliangxingwei(sheetAt.getRow(i).getCell(4).getNumericCellValue());}
            catch (Exception e){
                return 2;//return2时  提示有数据为空 一个数据都没存下来
            }
            list.add(i-2,consiciousZone);//将对象添加进List里面
        }

        consiciousZoneService.savedata(list);



        return 1; //当return1时 数据成功存入
    }

    @ApiOperation("查看所有质量生态意识工位数据 ")
    @GetMapping("/findallstation")
    public Page<ConsiciousStation> findallstation(int page, int size, String sort){
        return consiciousStationService.findall(page, size, sort);
    }

    @ApiOperation("查看所有质量生态意识班组数据 ")
    @GetMapping("/findallgroup")
    public Page<ConsiciousGroup> findallgroup(int page, int size, String sort){
        return consiciousGroupService.findall(page, size, sort);
    }

    @ApiOperation("查看所有质量生态意识工段数据 ")
    @GetMapping("/findallworkshop")
    public Page<ConsiciousWorkshop> findallworkshop(int page, int size, String sort){
        return consiciousWorkshopService.findall(page, size, sort);
    }

    @ApiOperation("查看所有质量生态意识车间数据 ")
    @GetMapping("/findallzone")
    public Page<ConsiciousZone> findallzone(int page, int size, String sort){
        return consiciousZoneService.findall(page, size, sort);
    }


    @ApiOperation("根据ID删除工位数据 ")
    @PostMapping("/deletestationbyid")
    public void deletestationbyid(int id){
        consiciousStationService.deletebyid(id);
    }

    @ApiOperation("根据ID删除班组数据 ")
    @PostMapping("/deletegroupbyid")
    public void deletegroupbyid(int id){
        consiciousGroupService.deletebyid(id);
    }

    @ApiOperation("根据ID删除工段数据 ")
    @PostMapping("/deleteworkshopbyid")
    public void deleteworkshopbyid(int id){
        consiciousWorkshopService.deletebyid(id);
    }

    @ApiOperation("根据ID删除车间数据 ")
    @PostMapping("/deletezonebyid")
    public void deletezonebyid(int id){
        consiciousZoneService.deletevyid(id);
    }




    @ApiOperation("获取工位质量知识的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getstationzhiliangzhishi")
    public ArrayList<Consiciouszhiliangzhishi> getstationzhiliangzhishi(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getstationzhiliangzhishi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getstationzhiliangzhishi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getstationzhiliangzhishi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getstationzhiliangzhishi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getstationzhiliangzhishi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getstationzhiliangzhishi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getstationzhiliangzhishi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getstationzhiliangzhishi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getstationzhiliangzhishi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getstationzhiliangzhishi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getstationzhiliangzhishi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getstationzhiliangzhishi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取班组质量知识的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getgroupzhiliangzhishi")
    public ArrayList<Consiciouszhiliangzhishi> getgroupzhiliangzhishi(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getgroupzhiliangzhishi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getgroupzhiliangzhishi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getgroupzhiliangzhishi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getgroupzhiliangzhishi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getgroupzhiliangzhishi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getgroupzhiliangzhishi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getgroupzhiliangzhishi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getgroupzhiliangzhishi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getgroupzhiliangzhishi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getgroupzhiliangzhishi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getgroupzhiliangzhishi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getgroupzhiliangzhishi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取工段质量知识的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getworkshopzhiliangzhishi")
    public ArrayList<Consiciouszhiliangzhishi> getworkshopzhiliangzhishi(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getworkshopzhiliangzhishi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getworkshopzhiliangzhishi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getworkshopzhiliangzhishi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getworkshopzhiliangzhishi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getworkshopzhiliangzhishi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getworkshopzhiliangzhishi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getworkshopzhiliangzhishi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getworkshopzhiliangzhishi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getworkshopzhiliangzhishi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getworkshopzhiliangzhishi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getworkshopzhiliangzhishi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getworkshopzhiliangzhishi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取车间质量知识的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getzonezhiliangzhishi")
    public ArrayList<Consiciouszhiliangzhishi> getzonezhiliangzhishi(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getzonezhiliangzhishi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getzonezhiliangzhishi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getzonezhiliangzhishi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getzonezhiliangzhishi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getzonezhiliangzhishi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getzonezhiliangzhishi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getzonezhiliangzhishi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getzonezhiliangzhishi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getzonezhiliangzhishi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getzonezhiliangzhishi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getzonezhiliangzhishi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getzonezhiliangzhishi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    /*  "工位: 获取规定车间和月份的所有数据 再获取取出来的列表平均值后的质量知识*/
    public Double getstationzhiliangzhishi(String zone,String date){
        return consiciousStationService.findallbyzoneanddate(zone,date).stream().mapToDouble(ConsiciousStation::getZhiliangzhishi).average().orElse(0D); //车间名和日期格式不能错，x7不能为空
    }//反正是这一整个车间的平均值 你就别管工段重不重复

    public Double getstationzhiliangrenzhi(String zone,String date){
        return consiciousStationService.findallbyzoneanddate(zone,date).stream().mapToDouble(ConsiciousStation::getZhiliangrenzhi).average().orElse(0D); //车间名和日期格式不能错，x7不能为空
    }

    public Double getstationzhiliangxinnian(String zone,String date){
        return consiciousStationService.findallbyzoneanddate(zone,date).stream().mapToDouble(ConsiciousStation::getZhiliangxinnian).average().orElse(0D); //车间名和日期格式不能错，x7不能为空
    }

    public Double getstationzhiliangxingwei(String zone,String date){
        return consiciousStationService.findallbyzoneanddate(zone,date).stream().mapToDouble(ConsiciousStation::getZhiliangxingwei).average().orElse(0D); //车间名和日期格式不能错，x7不能为空
    }

    public Double getgroupzhiliangzhishi(String zone,String date){
        return consiciousGroupService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousGroup::getZhiliangzhishi).average().orElse(0D);
    }

    public Double getgroupzhiliangrenzhi(String zone,String date){
        return consiciousGroupService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousGroup::getZhiliangrenzhi).average().orElse(0D);
    }

    public Double getgroupzhiliangxinnian(String zone,String date){
        return consiciousGroupService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousGroup::getZhiliangxinnian).average().orElse(0D);
    }

    public Double getgroupzhiliangxingwei(String zone,String date){
        return consiciousGroupService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousGroup::getZhiliangxingwei).average().orElse(0D);
    }

    public Double getworkshopzhiliangzhishi(String zone,String date){
        return consiciousWorkshopService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousWorkshop::getZhiliangzhishi).average().orElse(0D);
    }

    public Double getworkshopzhiliangrenzhi(String zone,String date){
        return consiciousWorkshopService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousWorkshop::getZhiliangrenzhi).average().orElse(0D);
    }

    public Double getworkshopzhiliangxinnian(String zone,String date){
        return consiciousWorkshopService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousWorkshop::getZhiliangxinnian).average().orElse(0D);
    }

    public Double getworkshopzhiliangxingwei(String zone,String date){
        return consiciousWorkshopService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousWorkshop::getZhiliangxingwei).average().orElse(0D);
    }

    public Double getzonezhiliangzhishi(String zone,String date){
        return consiciousZoneService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousZone::getZhiliangzhishi).average().orElse(0D);
    }

    public Double getzonezhiliangrenzhi(String zone,String date){
        return consiciousZoneService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousZone::getZhiliangrenzhi).average().orElse(0D);
    }

    public Double getzonezhiliangxinnian(String zone,String date){
        return consiciousZoneService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousZone::getZhiliangxinnian).average().orElse(0D);
    }

    public Double getzonezhiliangxingwei(String zone,String date){
        return consiciousZoneService.findallbyzoneanddate(zone, date).stream().mapToDouble(ConsiciousZone::getZhiliangxingwei).average().orElse(0D);
    }

    public Double getzonezhiliangzhishibydate(String date){
        return consiciousZoneService.findallbydate(date).stream().mapToDouble(ConsiciousZone::getZhiliangzhishi).average().orElse(0D);
    }

    public Double getzonezhiliangrenzhibydate(String date){
        return consiciousZoneService.findallbydate(date).stream().mapToDouble(ConsiciousZone::getZhiliangrenzhi).average().orElse(0D);
    }

    public Double getzonezhiliangxinnianbydate(String date){
        return consiciousZoneService.findallbydate(date).stream().mapToDouble(ConsiciousZone::getZhiliangxinnian).average().orElse(0D);
    }

    public Double getzonezhiliangxingweibydate(String date){
        return consiciousZoneService.findallbydate(date).stream().mapToDouble(ConsiciousZone::getZhiliangxingwei).average().orElse(0D);
    }


    @ApiOperation("获取工位质量认知的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getstationzhiliangrenzhi")
    public ArrayList<Consiciouszhiliangzhishi> getstationzhiliangrenzhi(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(Integer.toString(i)+'月');
                consiciouszhiliangzhishi.setChognya(getstationzhiliangrenzhi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getstationzhiliangrenzhi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getstationzhiliangrenzhi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getstationzhiliangrenzhi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getstationzhiliangrenzhi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getstationzhiliangrenzhi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(Integer.toString(i)+'月');
                consiciouszhiliangzhishi.setChognya(getstationzhiliangrenzhi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getstationzhiliangrenzhi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getstationzhiliangrenzhi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getstationzhiliangrenzhi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getstationzhiliangrenzhi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getstationzhiliangrenzhi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }


    @ApiOperation("获取班组质量认知的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getgroupzhiliangrenzhi")
    public ArrayList<Consiciouszhiliangzhishi> getgroupzhiliangrenzhi(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(Integer.toString(i)+'月');
                consiciouszhiliangzhishi.setChognya(getgroupzhiliangrenzhi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getgroupzhiliangrenzhi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getgroupzhiliangrenzhi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getgroupzhiliangrenzhi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getgroupzhiliangrenzhi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getgroupzhiliangrenzhi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(Integer.toString(i)+'月');
                consiciouszhiliangzhishi.setChognya(getgroupzhiliangrenzhi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getgroupzhiliangrenzhi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getgroupzhiliangrenzhi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getgroupzhiliangrenzhi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getgroupzhiliangrenzhi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getgroupzhiliangrenzhi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取工段质量认知的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getworkshopzhiliangrenzhi")
    public ArrayList<Consiciouszhiliangzhishi> getworkshopzhiliangrenzhi(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(Integer.toString(i)+'月');
                consiciouszhiliangzhishi.setChognya(getworkshopzhiliangrenzhi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getworkshopzhiliangrenzhi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getworkshopzhiliangrenzhi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getworkshopzhiliangrenzhi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getworkshopzhiliangrenzhi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getworkshopzhiliangrenzhi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(Integer.toString(i)+'月');
                consiciouszhiliangzhishi.setChognya(getworkshopzhiliangrenzhi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getworkshopzhiliangrenzhi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getworkshopzhiliangrenzhi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getworkshopzhiliangrenzhi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getworkshopzhiliangrenzhi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getworkshopzhiliangrenzhi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取车间质量认知的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getzonezhiliangrenzhi")
    public ArrayList<Consiciouszhiliangzhishi> getzonezhiliangrenzhi(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(Integer.toString(i)+'月');
                consiciouszhiliangzhishi.setChognya(getzonezhiliangrenzhi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getzonezhiliangrenzhi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getzonezhiliangrenzhi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getzonezhiliangrenzhi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getzonezhiliangrenzhi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getzonezhiliangrenzhi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(Integer.toString(i)+'月');
                consiciouszhiliangzhishi.setChognya(getzonezhiliangrenzhi("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getzonezhiliangrenzhi("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getzonezhiliangrenzhi("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getzonezhiliangrenzhi("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getzonezhiliangrenzhi("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getzonezhiliangrenzhi("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }


    @ApiOperation("获取工位质量信念的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getstationzhiliangxinnian")
    public ArrayList<Consiciouszhiliangzhishi> getstationzhiliangxinnian(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getstationzhiliangxinnian("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getstationzhiliangxinnian("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getstationzhiliangxinnian("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getstationzhiliangxinnian("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getstationzhiliangxinnian("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getstationzhiliangxinnian("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getstationzhiliangxinnian("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getstationzhiliangxinnian("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getstationzhiliangxinnian("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getstationzhiliangxinnian("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getstationzhiliangxinnian("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getstationzhiliangxinnian("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取班组质量信念的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getgroupzhiliangxinnian")
    public ArrayList<Consiciouszhiliangzhishi> getgroupzhiliangxinnian(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getgroupzhiliangxinnian("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getgroupzhiliangxinnian("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getgroupzhiliangxinnian("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getgroupzhiliangxinnian("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getgroupzhiliangxinnian("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getgroupzhiliangxinnian("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getgroupzhiliangxinnian("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getgroupzhiliangxinnian("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getgroupzhiliangxinnian("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getgroupzhiliangxinnian("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getgroupzhiliangxinnian("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getgroupzhiliangxinnian("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取工段质量信念的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getworkshopzhiliangxinnian")
    public ArrayList<Consiciouszhiliangzhishi> getworkshopzhiliangxinnian(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getworkshopzhiliangxinnian("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getworkshopzhiliangxinnian("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getworkshopzhiliangxinnian("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getworkshopzhiliangxinnian("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getworkshopzhiliangxinnian("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getworkshopzhiliangxinnian("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getworkshopzhiliangxinnian("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getworkshopzhiliangxinnian("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getworkshopzhiliangxinnian("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getworkshopzhiliangxinnian("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getworkshopzhiliangxinnian("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getworkshopzhiliangxinnian("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取车间质量信念的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getzonezhiliangxinnian")
    public ArrayList<Consiciouszhiliangzhishi> getzonezhiliangxinnian(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getzonezhiliangxinnian("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getzonezhiliangxinnian("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getzonezhiliangxinnian("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getzonezhiliangxinnian("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getzonezhiliangxinnian("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getzonezhiliangxinnian("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getzonezhiliangxinnian("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getzonezhiliangxinnian("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getzonezhiliangxinnian("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getzonezhiliangxinnian("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getzonezhiliangxinnian("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getzonezhiliangxinnian("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }


    @ApiOperation("获取工位质量行为的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getstationzhiliangxingwei")
    public ArrayList<Consiciouszhiliangzhishi> getstationzhiliangxingwei(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getstationzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getstationzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getstationzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getstationzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getstationzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getstationzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getstationzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getstationzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getstationzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getstationzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getstationzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getstationzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取班组质量行为的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getgroupzhiliangxingwei")
    public ArrayList<Consiciouszhiliangzhishi> getgroupzhiliangxingwei(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getgroupzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getgroupzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getgroupzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getgroupzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getgroupzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getgroupzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getgroupzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getgroupzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getgroupzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getgroupzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getgroupzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getgroupzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取工段质量行为的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getworkshopzhiliangxingwei")
    public ArrayList<Consiciouszhiliangzhishi> getworkshopzhiliangxingwei(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getworkshopzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getworkshopzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getworkshopzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getworkshopzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getworkshopzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getworkshopzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getworkshopzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getworkshopzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getworkshopzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getworkshopzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getworkshopzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getworkshopzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取车间质量行为的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getzonezhiliangxingwei")
    public ArrayList<Consiciouszhiliangzhishi> getzonezhiliangxingwei(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getzonezhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getzonezhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getzonezhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getzonezhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getzonezhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getzonezhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(getzonezhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(getzonezhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(getzonezhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(getzonezhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(getzonezhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(getzonezhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }


    @ApiOperation("获取工位质量氛围的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getstationzhiliangfenwei")
    public ArrayList<Consiciouszhiliangzhishi> getstationzhiliangfenwei(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(1.2*getstationzhiliangzhishi("冲压车间",date)+0.89*getstationzhiliangrenzhi("冲压车间",date)+0.7*getstationzhiliangxinnian("冲压车间",date)+1.32*getstationzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(1.2*getstationzhiliangzhishi("车身车间",date)+0.89*getstationzhiliangrenzhi("车身车间",date)+0.7*getstationzhiliangxinnian("车身车间",date)+1.32*getstationzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(1.2*getstationzhiliangzhishi("涂装车间",date)+0.89*getstationzhiliangrenzhi("涂装车间",date)+0.7*getstationzhiliangxinnian("涂装车间",date)+1.32*getstationzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(1.2*getstationzhiliangzhishi("总装车间",date)+0.89*getstationzhiliangrenzhi("总装车间",date)+0.7*getstationzhiliangxinnian("总装车间",date)+1.32*getstationzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(1.2*getstationzhiliangzhishi("机加车间",date)+0.89*getstationzhiliangrenzhi("机加车间",date)+0.7*getstationzhiliangxinnian("机加车间",date)+1.32*getstationzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(1.2*getstationzhiliangzhishi("装配车间",date)+0.89*getstationzhiliangrenzhi("装配车间",date)+0.7*getstationzhiliangxinnian("装配车间",date)+1.32*getstationzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(1.2*getstationzhiliangzhishi("冲压车间",date)+0.89*getstationzhiliangrenzhi("冲压车间",date)+0.7*getstationzhiliangxinnian("冲压车间",date)+1.32*getstationzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(1.2*getstationzhiliangzhishi("车身车间",date)+0.89*getstationzhiliangrenzhi("车身车间",date)+0.7*getstationzhiliangxinnian("车身车间",date)+1.32*getstationzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(1.2*getstationzhiliangzhishi("涂装车间",date)+0.89*getstationzhiliangrenzhi("涂装车间",date)+0.7*getstationzhiliangxinnian("涂装车间",date)+1.32*getstationzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(1.2*getstationzhiliangzhishi("总装车间",date)+0.89*getstationzhiliangrenzhi("总装车间",date)+0.7*getstationzhiliangxinnian("总装车间",date)+1.32*getstationzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(1.2*getstationzhiliangzhishi("机加车间",date)+0.89*getstationzhiliangrenzhi("机加车间",date)+0.7*getstationzhiliangxinnian("机加车间",date)+1.32*getstationzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(1.2*getstationzhiliangzhishi("装配车间",date)+0.89*getstationzhiliangrenzhi("装配车间",date)+0.7*getstationzhiliangxinnian("装配车间",date)+1.32*getstationzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取班组质量氛围的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getgroupzhiliangfenwei")
    public ArrayList<Consiciouszhiliangzhishi> getgroupzhiliangfenwei(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(1.3*getgroupzhiliangzhishi("冲压车间",date)+1.23*getgroupzhiliangrenzhi("冲压车间",date)+0.6*getgroupzhiliangxinnian("冲压车间",date)+1.2*getgroupzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(1.3*getgroupzhiliangzhishi("车身车间",date)+1.23*getgroupzhiliangrenzhi("车身车间",date)+0.6*getgroupzhiliangxinnian("车身车间",date)+1.2*getgroupzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(1.3*getgroupzhiliangzhishi("涂装车间",date)+1.23*getgroupzhiliangrenzhi("涂装车间",date)+0.6*getgroupzhiliangxinnian("涂装车间",date)+1.2*getgroupzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(1.3*getgroupzhiliangzhishi("总装车间",date)+1.23*getgroupzhiliangrenzhi("总装车间",date)+0.6*getgroupzhiliangxinnian("总装车间",date)+1.2*getgroupzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(1.3*getgroupzhiliangzhishi("机加车间",date)+1.23*getgroupzhiliangrenzhi("机加车间",date)+0.6*getgroupzhiliangxinnian("机加车间",date)+1.2*getgroupzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(1.3*getgroupzhiliangzhishi("装配车间",date)+1.23*getgroupzhiliangrenzhi("装配车间",date)+0.6*getgroupzhiliangxinnian("装配车间",date)+1.2*getgroupzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(1.3*getgroupzhiliangzhishi("冲压车间",date)+1.23*getgroupzhiliangrenzhi("冲压车间",date)+0.6*getstationzhiliangxinnian("冲压车间",date)+1.2*getgroupzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(1.3*getgroupzhiliangzhishi("车身车间",date)+1.23*getgroupzhiliangrenzhi("车身车间",date)+0.6*getstationzhiliangxinnian("车身车间",date)+1.2*getgroupzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(1.3*getgroupzhiliangzhishi("涂装车间",date)+1.23*getgroupzhiliangrenzhi("涂装车间",date)+0.6*getstationzhiliangxinnian("涂装车间",date)+1.2*getgroupzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(1.3*getgroupzhiliangzhishi("总装车间",date)+1.23*getgroupzhiliangrenzhi("总装车间",date)+0.6*getstationzhiliangxinnian("总装车间",date)+1.2*getgroupzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(1.3*getgroupzhiliangzhishi("机加车间",date)+1.23*getgroupzhiliangrenzhi("机加车间",date)+0.6*getstationzhiliangxinnian("机加车间",date)+1.2*getgroupzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(1.3*getgroupzhiliangzhishi("装配车间",date)+1.23*getgroupzhiliangrenzhi("装配车间",date)+0.6*getstationzhiliangxinnian("装配车间",date)+1.2*getgroupzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取工段质量氛围的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getworkshopzhiliangfenwei")
    public ArrayList<Consiciouszhiliangzhishi> getworkshopzhiliangfenwei(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(1.38*getworkshopzhiliangzhishi("冲压车间",date)+1.5*getworkshopzhiliangrenzhi("冲压车间",date)+1.12*getworkshopzhiliangxinnian("冲压车间",date)+1.16*getworkshopzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(1.38*getworkshopzhiliangzhishi("车身车间",date)+1.5*getworkshopzhiliangrenzhi("车身车间",date)+1.12*getworkshopzhiliangxinnian("车身车间",date)+1.16*getworkshopzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(1.38*getworkshopzhiliangzhishi("涂装车间",date)+1.5*getworkshopzhiliangrenzhi("涂装车间",date)+1.12*getworkshopzhiliangxinnian("涂装车间",date)+1.16*getworkshopzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(1.38*getworkshopzhiliangzhishi("总装车间",date)+1.5*getworkshopzhiliangrenzhi("总装车间",date)+1.12*getworkshopzhiliangxinnian("总装车间",date)+1.16*getworkshopzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(1.38*getworkshopzhiliangzhishi("机加车间",date)+1.5*getworkshopzhiliangrenzhi("机加车间",date)+1.12*getworkshopzhiliangxinnian("机加车间",date)+1.16*getworkshopzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(1.38*getworkshopzhiliangzhishi("装配车间",date)+1.5*getworkshopzhiliangrenzhi("装配车间",date)+1.12*getworkshopzhiliangxinnian("装配车间",date)+1.16*getworkshopzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(1.38*getworkshopzhiliangzhishi("冲压车间",date)+1.5*getworkshopzhiliangrenzhi("冲压车间",date)+1.12*getworkshopzhiliangxinnian("冲压车间",date)+1.16*getworkshopzhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(1.38*getworkshopzhiliangzhishi("车身车间",date)+1.5*getworkshopzhiliangrenzhi("车身车间",date)+1.12*getworkshopzhiliangxinnian("车身车间",date)+1.16*getworkshopzhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(1.38*getworkshopzhiliangzhishi("涂装车间",date)+1.5*getworkshopzhiliangrenzhi("涂装车间",date)+1.12*getworkshopzhiliangxinnian("涂装车间",date)+1.16*getworkshopzhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(1.38*getworkshopzhiliangzhishi("总装车间",date)+1.5*getworkshopzhiliangrenzhi("总装车间",date)+1.12*getworkshopzhiliangxinnian("总装车间",date)+1.16*getworkshopzhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(1.38*getworkshopzhiliangzhishi("机加车间",date)+1.5*getworkshopzhiliangrenzhi("机加车间",date)+1.12*getworkshopzhiliangxinnian("机加车间",date)+1.16*getworkshopzhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(1.38*getworkshopzhiliangzhishi("装配车间",date)+1.5*getworkshopzhiliangrenzhi("装配车间",date)+1.12*getworkshopzhiliangxinnian("装配车间",date)+1.16*getworkshopzhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }

    @ApiOperation("获取车间质量氛围的数据 ") //一定不能出错的地方 数据库中的日期格式，车间格
    @GetMapping(value = "/getzonezhiliangfenwei")
    public ArrayList<Consiciouszhiliangzhishi> getzonezhiliangfenwei(String year){
        ArrayList<Consiciouszhiliangzhishi> arrayList=new ArrayList<>();
        Calendar now=Calendar.getInstance();
        String date;
        if(String.valueOf(now.get(Calendar.YEAR)).equals(year)){ //如果是本年的数据

            int nowMonth=(now.get(Calendar.MONTH))+1;//取到当前月份
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=now.get(Calendar.YEAR)+"-0"+i; //这个date得到传入查询方法的月份
                }
                else {
                    date=now.get(Calendar.YEAR)+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(1.2*getzonezhiliangzhishi("冲压车间",date)+1.7*getzonezhiliangrenzhi("冲压车间",date)+1.2*getzonezhiliangxinnian("冲压车间",date)+1.14*getzonezhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(1.2*getzonezhiliangzhishi("车身车间",date)+1.7*getzonezhiliangrenzhi("车身车间",date)+1.2*getzonezhiliangxinnian("车身车间",date)+1.14*getzonezhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(1.2*getzonezhiliangzhishi("涂装车间",date)+1.7*getzonezhiliangrenzhi("涂装车间",date)+1.2*getzonezhiliangxinnian("涂装车间",date)+1.14*getzonezhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(1.2*getzonezhiliangzhishi("总装车间",date)+1.7*getzonezhiliangrenzhi("总装车间",date)+1.2*getzonezhiliangxinnian("总装车间",date)+1.14*getzonezhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(1.2*getzonezhiliangzhishi("机加车间",date)+1.7*getzonezhiliangrenzhi("机加车间",date)+1.2*getzonezhiliangxinnian("机加车间",date)+1.14*getzonezhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(1.2*getzonezhiliangzhishi("装配车间",date)+1.7*getzonezhiliangrenzhi("装配车间",date)+1.2*getzonezhiliangxinnian("装配车间",date)+1.14*getzonezhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }
        }else{ //如果不是本年的数据，默认该年有12个月份
            int nowMonth=12;
            for (int i = 1; i <=nowMonth ; i++) {//循环到当前月份
                if(i<10){
                    date=year+"-0"+i;
                }
                else {
                    date=year+"-"+i;
                }

                Consiciouszhiliangzhishi consiciouszhiliangzhishi=new Consiciouszhiliangzhishi();
                consiciouszhiliangzhishi.setDate(date);
                consiciouszhiliangzhishi.setChognya(1.2*getzonezhiliangzhishi("冲压车间",date)+1.7*getzonezhiliangrenzhi("冲压车间",date)+1.2*getzonezhiliangxinnian("冲压车间",date)+1.14*getzonezhiliangxingwei("冲压车间",date));//车间名和日期格式不能错，否则出现bug
                consiciouszhiliangzhishi.setCheshen(1.2*getzonezhiliangzhishi("车身车间",date)+1.7*getzonezhiliangrenzhi("车身车间",date)+1.2*getzonezhiliangxinnian("车身车间",date)+1.14*getzonezhiliangxingwei("车身车间",date));
                consiciouszhiliangzhishi.setTuzhuang(1.2*getzonezhiliangzhishi("涂装车间",date)+1.7*getzonezhiliangrenzhi("涂装车间",date)+1.2*getzonezhiliangxinnian("涂装车间",date)+1.14*getzonezhiliangxingwei("涂装车间",date));
                consiciouszhiliangzhishi.setZongzhuang(1.2*getzonezhiliangzhishi("总装车间",date)+1.7*getzonezhiliangrenzhi("总装车间",date)+1.2*getzonezhiliangxinnian("总装车间",date)+1.14*getzonezhiliangxingwei("总装车间",date));
                consiciouszhiliangzhishi.setJijia(1.2*getzonezhiliangzhishi("机加车间",date)+1.7*getzonezhiliangrenzhi("机加车间",date)+1.2*getzonezhiliangxinnian("机加车间",date)+1.14*getzonezhiliangxingwei("机加车间",date));
                consiciouszhiliangzhishi.setZhuangpei(1.2*getzonezhiliangzhishi("装配车间",date)+1.7*getzonezhiliangrenzhi("装配车间",date)+1.2*getzonezhiliangxinnian("装配车间",date)+1.14*getzonezhiliangxingwei("装配车间",date));
                arrayList.add(i-1,consiciouszhiliangzhishi);//往最后要返回的List集里面添加这一条list
            }}

        return arrayList;
    }
}
