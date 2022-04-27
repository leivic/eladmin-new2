package me.zhengjie.modules.qe.rest;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.bcel.internal.generic.SWITCH;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.qe.domain.ConsiciousControl;
import me.zhengjie.modules.qe.domain.ContinueDatasource;
import me.zhengjie.modules.qe.domain.ContinueFile;
import me.zhengjie.modules.qe.polo.*;
import me.zhengjie.modules.qe.service.ConsiciousControlService;
import me.zhengjie.modules.qe.service.ContinueDatasourceService;
import me.zhengjie.modules.qe.service.ContinueFileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "质量：质量生态持续")
@RequestMapping("/qe/continue")
public class ContinueController {
    @Autowired
    private ContinueFileService continueFileService;
    @Autowired
    private ContinueDatasourceService continueDatasourceService;
    @Autowired
    private ConsiciousControlService consiciousControlService;

    @PostMapping("/upload") //上传文件的方法
    public String upload(MultipartFile aaa, String file_type, String file_date, String zone, String create_by) throws IOException { //Httpsession session

        // 获取文件的原始名称
        System.out.println(aaa);
        String oldFileName = aaa.getOriginalFilename();
        String extension = "." + FilenameUtils.getExtension(oldFileName);
        // 生成新的文件名称
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                UUID.randomUUID().toString().replace("-", "") + extension;
        // 获取文件的大小
        long size = aaa.getSize();
        // 获取文件类型
        String type = aaa.getContentType();

        // 根据日期生成文件目录
        // ResourceUtils.getURL("classpath:") 是获取当前文件的resources路径
        String realPath = "C:/Users/b09161214/eladmin/file/tmp" + "/static/files";// 正式环境:"C:/Users/b09161214/.eladmin/file/tmp" + "/static/files" 开发环境:ResourceUtils.getURL("classpath:").getPath() + "/static/files"; 堡垒机环境 .eladmin 改为 eladmin
        // 日期文件夹
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath = realPath + "/" + date;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) dateDir.mkdirs();

        // 处理文件上传 这个方法实现文件上传
        aaa.transferTo(new File(dateDir, newFileName));

        // 将文件信息放入数据库
        ContinueFile continueFile = new ContinueFile();
        switch (file_type) {
            case "废品损失":
                continueFile.setX("x1");
                break;
            case "返修问题统计表":
                continueFile.setX("x2");
                break;
            case "质量停线时间统计表":
                continueFile.setX("x3");
                break;
            case "物料管理":
                continueFile.setX("x4");
                break;
            case "过程问题统计表":
                continueFile.setX("x5");
                break;
            case "质量资源损失(结果导向，指标完成)":
                continueFile.setX("x6");
                break;
            case "质量资源损失(过程一致性)":
                continueFile.setX("x7");
                break;
            case "满意度得分统计表":
                continueFile.setX("x8");
                break;
            case "实物质量得分统计表":
                continueFile.setX("x9");
                break;
            case "质量策划":
                continueFile.setX("x10");
                break;
            case "质量潜力提升跟踪表":
                continueFile.setX("x11");
                break;

        }
        continueFile.setFile_name(oldFileName).setFile_type(file_type).setFile_date(file_date).setZone(zone).setCreate_by(create_by).setPath("/files/" + date).setNewfilename(newFileName);
        System.out.println(continueFile);
        continueFileService.saveContinueFile(continueFile);


        return "1";
    }

    @GetMapping("/findAllContinue")
    public Page<ContinueFile> findAllContinue(int page, int size, String sort) {
        return continueFileService.findAllContinue(page, size, sort);
    }

    // 文件下载
    @GetMapping("/download")
    public void download(Integer id, String openStyle, HttpServletResponse response) throws IOException {
        // attachement是以附件形式下载, inline是在线打开
        openStyle = "inline".equals(openStyle) ? "inline" : "attachment";
        // 获取文件信息
        ContinueFile continueFile = continueFileService.findByid(id);

        // 根据 文件信息中文件名字 和 文件存储路径 获取文件真实路径
        String realPath = "C:/Users/b09161214/eladmin/file/tmp" + "/static" + continueFile.getPath();//正式环境 "C:/Users/b09161214/.eladmin/file/tmp" + "/static"+ continueFile.getPath(); 开发环境:ResourceUtils.getURL("classpath:").getPath() + "/static" + continueFile.getPath();
        // 获取文件输入流
        InputStream is = new FileInputStream(new File(realPath, continueFile.getNewfilename()));
        // 获取响应输出流
        response.setHeader("content-disposition", openStyle + ";fileName=" + URLEncoder.encode(continueFile.getFile_name(), "UTF-8"));
        ServletOutputStream os = response.getOutputStream();
        // 文件拷贝
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
        System.out.println("执行结束");

    }

    @PostMapping("/deletefile")
    public int deletefile(Integer id) throws IOException {
        ContinueFile continueFile = continueFileService.findByid(id);

        String realPath = "C:/Users/b09161214/eladmin/file/tmp" + "/static" + continueFile.getPath();
        File file = new File(realPath);

        if (file != null) {
            //文件不为空，执行删除
            file.delete(); //删除文件
            continueFileService.deletebyid(id); //删除数据
        } else {
            return 0; //前端接收
        }

        return 1;
    }

    @ApiOperation("增加质量分解指标数据 ")
    @PostMapping("/uploadgoaldata")
    public int uploadgoaldata(@RequestParam("file") MultipartFile file, String date) throws IOException {
        FileInputStream fns = (FileInputStream) file.getInputStream();
        XSSFWorkbook wb = new XSSFWorkbook(fns);//xssWorkbook少了hssworkbook的解析成 POIFSFileSystem数据类型这一步
        XSSFSheet sheetAt = wb.getSheetAt(0);
        String year = sheetAt.getRow(1).getCell(0).toString().substring(0, 4);
        List<ConsiciousControl> arr = new ArrayList<>();
        for (int i = 3; i < sheetAt.getLastRowNum() - 6; i++) {
            ConsiciousControl consiciousControl = new ConsiciousControl();
            consiciousControl.setYear(year);
            consiciousControl.setType(sheetAt.getRow(i).getCell(1).toString());
            consiciousControl.setTarget(sheetAt.getRow(i).getCell(2).toString());
            consiciousControl.setTargettype(sheetAt.getRow(i).getCell(3).toString());
            consiciousControl.setTargetstandard(sheetAt.getRow(i).getCell(4).toString());
            consiciousControl.setDepartment(sheetAt.getRow(i).getCell(5).toString());
            consiciousControl.setZoneperson(sheetAt.getRow(i).getCell(6).toString());
            consiciousControl.setPerson_in_charge(sheetAt.getRow(i).getCell(7).toString());
            consiciousControl.setGoal(sheetAt.getRow(i).getCell(8).toString());
            consiciousControl.setLashengoal(sheetAt.getRow(i).getCell(9).toString());
            consiciousControl.setZhibiaofankuiren(sheetAt.getRow(i).getCell(10).toString());
            arr.add(consiciousControl);
        }
        consiciousControlService.savedata(arr);
        return 1;
    }


    @PostMapping("/getgoaldata")
    public List<ConsiciousControl> getgoaldata(String year) {
        return consiciousControlService.findAllByYear(year);
    }

    // 数据源查找
    @GetMapping("/findByzoneanddate")
    public ContinueDatasource findByzoneanddate(String zone, String date) {
        return continueDatasourceService.findByDateAndZone(zone, date);
    }

    public double getx1(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX1();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX1();
    }

    public double getx2(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX2();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX2();
    }

    public double getx3(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX3();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX3();
    }

    public double getx4(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX4();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX4();
    }

    public double getx5(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX5();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX5();
    }

    public double getx6(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX6();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX6();
    }

    public double getx7(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX7();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX7();
    }

    public double getx8(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX8();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX8();
    }

    public double getx9(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX9();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX9();
    }

    public double getx10(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX10();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX10();
    }

    public double getx11(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX11();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX11();
    }

    public double getx12(String zone, String date) {
        try {
            findByzoneanddate(zone, date).getX12();
        } catch (Exception e) {
            return 0;
        }
        return findByzoneanddate(zone, date).getX12();
    }

    // 更新或者新增数据
    @PostMapping("/updateorsavecontinuedatasource")
    public String updateorsavecontinuedatasource(String zone, String date, double x1, double x2, double x3, double x4, double x5, double x6, double x7, double x8, double x9, double x10, double x11, double x12) {
        int conut = continueDatasourceService.findCountByDateAndZone(zone, date);
        if (conut == 0) { //如果这个月份和日期没存在，就新增数据
            ContinueDatasource continueDatasource = new ContinueDatasource();
            ContinueDatasource continueDatasource1 = new ContinueDatasource();
            ContinueDatasource continueDatasource2 = new ContinueDatasource();
            ContinueDatasource continueDatasource3 = new ContinueDatasource();
            ContinueDatasource continueDatasource4 = new ContinueDatasource();
            ContinueDatasource continueDatasource5 = new ContinueDatasource();

            continueDatasource.setZone(zone).setDate(date).setX1(x1).setX2(x2).setX3(x3).setX4(x4).setX5(x5).setX6(x6).setX7(x7).setX8(x8).setX9(x9).setX10(x10).setX11(x11).setX12(x12);
            continueDatasourceService.save(continueDatasource);
            continueDatasourceService.updateContinueDatasourcex1x2(date, x1, x2, x3); //更新该月的所有x1 x2 x3的数据

            if (continueDatasourceService.findCountByDateAndZone("冲压车间", date) == 0) { //如果没有冲压车间这条数据  新增一条,但是本来没有 所以除了 x1 x2 x3的数据都是0
                continueDatasource1.setZone("冲压车间").setDate(date).setX1(x1).setX2(x2).setX3(x3).setX4(0).setX5(0).setX6(0).setX7(0).setX8(0).setX9(0).setX10(0).setX11(0).setX12(0);
                continueDatasourceService.save(continueDatasource1);
            }
            if (continueDatasourceService.findCountByDateAndZone("车身车间", date) == 0) { //如果没有冲压车间这条数据  新增一条,但是本来没有 所以除了 x1 x2 x3的数据都是0
                continueDatasource2.setZone("车身车间").setDate(date).setX1(x1).setX2(x2).setX3(x3).setX4(0).setX5(0).setX6(0).setX7(0).setX8(0).setX9(0).setX10(0).setX11(0).setX12(0);
                continueDatasourceService.save(continueDatasource2);
            }
            if (continueDatasourceService.findCountByDateAndZone("涂装车间", date) == 0) { //如果没有冲压车间这条数据  新增一条,但是本来没有 所以除了 x1 x2 x3的数据都是0
                continueDatasource3.setZone("涂装车间").setDate(date).setX1(x1).setX2(x2).setX3(x3).setX4(0).setX5(0).setX6(0).setX7(0).setX8(0).setX9(0).setX10(0).setX11(0).setX12(0);
                continueDatasourceService.save(continueDatasource3);
            }
            if (continueDatasourceService.findCountByDateAndZone("总装车间", date) == 0) { //如果没有冲压车间这条数据  新增一条,但是本来没有 所以除了 x1 x2 x3的数据都是0
                continueDatasource4.setZone("总装车间").setDate(date).setX1(x1).setX2(x2).setX3(x3).setX4(0).setX5(0).setX6(0).setX7(0).setX8(0).setX9(0).setX10(0).setX11(0).setX12(0);
                continueDatasourceService.save(continueDatasource4);
            }
            if (continueDatasourceService.findCountByDateAndZone("发动机工厂", date) == 0) { //如果没有冲压车间这条数据  新增一条,但是本来没有 所以除了 x1 x2 x3的数据都是0
                continueDatasource5.setZone("发动机工厂").setDate(date).setX1(x1).setX2(x2).setX3(x3).setX4(0).setX5(0).setX6(0).setX7(0).setX8(0).setX9(0).setX10(0).setX11(0).setX12(0);
                continueDatasourceService.save(continueDatasource5);
            }
            return "1"; //前端接收返回的1 新增成功
        } else if (conut > 0) { //如果这个月份和日期已经存在, 就更新数据  现在 同月的 x1 x2 x3必定是相等的 而且要么都没有 要么都有
            continueDatasourceService.updateContinueDatasource(zone, date, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12);
            continueDatasourceService.updateContinueDatasourcex1x2(date, x1, x2, x3); //两个updatex1x2x3 确定x1x2x3三个数据只有两种情况、存在的x1 x2 x3 每月的每条都必定一样 要么就都没有
            return "2"; //前端接收返回的2 更新成功
        }
        return "0";
    }

    @GetMapping("/findCountcontinuedatasource")
    public int findCountcontinuedatasource(String zone, String date) {
        return continueDatasourceService.findCountByDateAndZone(zone, date);
    }

    @GetMapping("/gettotalcontinueBydate") //所有区域的数据
    public Continuetotaldata gettotalcontinueBydate(String year) {
        Continuetotaldata continuetotaldata = new Continuetotaldata();
        ArrayList arrayList1 = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("冲压车间", date);
                    arrayList1.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList1.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("车身车间", date);
                    arrayList2.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList2.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("涂装车间", date);
                    arrayList3.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList3.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("总装车间", date);
                    arrayList4.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList4.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("发动机工厂", date);
                    arrayList5.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList5.add(i - 1, 0);
                }
            }
            continuetotaldata.setChongyadata(arrayList1);
            continuetotaldata.setCheshendata(arrayList2);
            continuetotaldata.setTuzhuangdata(arrayList3);
            continuetotaldata.setZongzhuangdata(arrayList4);
            continuetotaldata.setFadongjidata(arrayList5);
        } else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）//为什么敢这样写 因为数据库所有x的default值是0 所以新增时所有的数据的值都是0
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("冲压车间", date);
                    arrayList1.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList1.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("车身车间", date);
                    arrayList2.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList2.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("涂装车间", date);
                    arrayList3.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList3.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("总装车间", date);
                    arrayList4.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList4.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("发动机工厂", date);
                    arrayList5.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList5.add(i - 1, 0);
                }

            }

            continuetotaldata.setChongyadata(arrayList1);
            continuetotaldata.setCheshendata(arrayList2);
            continuetotaldata.setTuzhuangdata(arrayList3);
            continuetotaldata.setZongzhuangdata(arrayList4);
            continuetotaldata.setFadongjidata(arrayList5);
        }


        return continuetotaldata;
    }

    ;

    @GetMapping("/gettotalcontinueBydateandzone") //自定义区域的数据
    public ArrayList gettotalcontinueBydateandzone(String zone, String year) {
        ArrayList arrayList = new ArrayList();
        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone(zone, date);
                    arrayList.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList.add(i - 1, 0);
                }


            }
        } else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone(zone, date);
                    arrayList.add(i - 1, (int) (continueDatasource.getX1() * 2 + continueDatasource.getX2() * 6 + continueDatasource.getX3() * 0.8 + continueDatasource.getX4() * 1.2 + continueDatasource.getX5() * 2 + continueDatasource.getX6() * 0.3 + continueDatasource.getX7() * 0.3 + continueDatasource.getX8() * 0.25 + continueDatasource.getX9() * 0.25 + continueDatasource.getX10() * 0.2 + continueDatasource.getX11() * 0.36 + continueDatasource.getX12() * 0.35));
                } catch (Exception e) {
                    arrayList.add(i - 1, 0);
                }

            }
        }


        return arrayList;
    }

    ;

    @GetMapping("/getcontinuezhiliangshuipinBydate") //和上方获取质量生态持续数据代码只有两句不同 其实写在一起 然后传个参数判断是哪种情况就行了 ，没必要写两个接口
    public Continuetotaldata getcontinuezhiliangshuipinBydate(String year) {
        Continuetotaldata continuetotaldata = new Continuetotaldata();
        ArrayList arrayList1 = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();

        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("冲压车间", date);
                    arrayList1.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList1.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("车身车间", date);
                    arrayList2.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList2.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("涂装车间", date);
                    arrayList3.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList3.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("总装车间", date);
                    arrayList4.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList4.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("发动机工厂", date);
                    arrayList5.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList5.add(i - 1, 0);
                }
            }
            continuetotaldata.setChongyadata(arrayList1);
            continuetotaldata.setCheshendata(arrayList2);
            continuetotaldata.setTuzhuangdata(arrayList3);
            continuetotaldata.setZongzhuangdata(arrayList4);
            continuetotaldata.setFadongjidata(arrayList5);
        } else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("冲压车间", date);
                    arrayList1.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList1.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("车身车间", date);
                    arrayList2.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList2.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("涂装车间", date);
                    arrayList3.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList3.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("总装车间", date);
                    arrayList4.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList4.add(i - 1, 0);
                }
                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("发动机工厂", date);
                    arrayList5.add(i - 1, (int) (0.52 * (continueDatasource.getX8() + continueDatasource.getX9()) + 2));
                } catch (Exception e) {
                    arrayList5.add(i - 1, 0);
                }

            }

            continuetotaldata.setChongyadata(arrayList1);
            continuetotaldata.setCheshendata(arrayList2);
            continuetotaldata.setTuzhuangdata(arrayList3);
            continuetotaldata.setZongzhuangdata(arrayList4);
            continuetotaldata.setFadongjidata(arrayList5);
        }


        return continuetotaldata;
    }

    ;


    @GetMapping("/getzhiliangtishen")//获取质量提升潜力的数据
    public ArrayList getzhiliangtishen(String date) {
        ArrayList arrayList = new ArrayList();


        try { //从冲压车间开始一个个添加进去
            ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("冲压车间", date);
            arrayList.add(0, (int) (1.13 * (continueDatasource.getX10() + continueDatasource.getX11() + continueDatasource.getX12()) + 1.25));
        } catch (Exception e) {
            arrayList.add(0, 0);
        }

        try { //从冲压车间开始一个个添加进去
            ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("车身车间", date);
            arrayList.add(1, (int) (1.13 * (continueDatasource.getX10() + continueDatasource.getX11() + continueDatasource.getX12()) + 1.25));
        } catch (Exception e) {
            arrayList.add(1, 0);
        }
        try { //从冲压车间开始一个个添加进去
            ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("涂装车间", date);
            arrayList.add(2, (int) (1.13 * (continueDatasource.getX10() + continueDatasource.getX11() + continueDatasource.getX12()) + 1.25));
        } catch (Exception e) {
            arrayList.add(2, 0);
        }

        try { //从冲压车间开始一个个添加进去
            ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("总装车间", date);
            arrayList.add(3, (int) (1.13 * (continueDatasource.getX10() + continueDatasource.getX11() + continueDatasource.getX12()) + 1.25));
        } catch (Exception e) {
            arrayList.add(3, 0);
        }
        try { //从冲压车间开始一个个添加进去
            ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("发动机工厂", date);
            arrayList.add(4, (int) (1.13 * (continueDatasource.getX10() + continueDatasource.getX11() + continueDatasource.getX12()) + 1.25));
        } catch (Exception e) {
            arrayList.add(4, 0);
        }
        return arrayList;
    }

    @GetMapping("/getContinuePiechartvalue") // 不要这么麻烦 取该月冲压车间的x1 x2 x3即可
    public ArrayList<ContinuePiechart> getContinuePiechartvalue(String month) {
        ArrayList<ContinuePiechart> continuePiechartList = new ArrayList<>();

        try { //从冲压车间开始一个个添加进去
            ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone("冲压车间", month);
            ContinuePiechart continuePiechart1 = new ContinuePiechart();
            ContinuePiechart continuePiechart2 = new ContinuePiechart();
            ContinuePiechart continuePiechart3 = new ContinuePiechart();
            continuePiechartList.add(0, continuePiechart1.setName("质量停线").setValue(continueDatasource.getX3()));
            continuePiechartList.add(1, continuePiechart2.setName("返修返工").setValue(continueDatasource.getX2()));
            continuePiechartList.add(2, continuePiechart3.setName("废品损失").setValue(continueDatasource.getX1()));
        } catch (Exception e) { //如果没找到那条数据 那么x1 x2 x3自然是0
            ContinuePiechart continuePiechart1 = new ContinuePiechart();
            ContinuePiechart continuePiechart2 = new ContinuePiechart();
            ContinuePiechart continuePiechart3 = new ContinuePiechart();
            continuePiechartList.add(0, continuePiechart1.setName("质量停线").setValue(0));
            continuePiechartList.add(1, continuePiechart2.setName("返修返工").setValue(0));
            continuePiechartList.add(2, continuePiechart3.setName("废品损失").setValue(0));
        }


        return continuePiechartList;
    }

    ;

    @GetMapping("/getzhiliangshuipin")
    public Continuezhiliangshuipin getzhiliangshuipin(String zone, String year) {
        Continuezhiliangshuipin continuezhiliangshuipin = new Continuezhiliangshuipin();
        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            ArrayList arrayListx8 = new ArrayList();
            ArrayList arrayListx9 = new ArrayList();
            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份

                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone(zone, date);
                    arrayListx8.add(i - 1, continueDatasource.getX8());
                    arrayListx9.add(i - 1, continueDatasource.getX9());
                } catch (Exception e) {
                    arrayListx8.add(i - 1, 0);
                    arrayListx9.add(i - 1, 0);
                }


            }
            continuezhiliangshuipin.setTiyanzhiliang(arrayListx8);
            continuezhiliangshuipin.setShiwuzhiliang(arrayListx9);
        } else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            ArrayList arrayListx8 = new ArrayList();
            ArrayList arrayListx9 = new ArrayList();
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone(zone, date);
                    arrayListx8.add(i - 1, continueDatasource.getX8());
                    arrayListx9.add(i - 1, continueDatasource.getX9());
                } catch (Exception e) {
                    arrayListx8.add(i - 1, 0);
                    arrayListx9.add(i - 1, 0);
                }

            }
            continuezhiliangshuipin.setTiyanzhiliang(arrayListx8);
            continuezhiliangshuipin.setShiwuzhiliang(arrayListx9);
        }
        return continuezhiliangshuipin;
    }

    ;

    @GetMapping("/getzhiliangtishenqianli")
    public Continuezhiliangtishen getzhiliangtishenqianli(String zone, String year) {
        Continuezhiliangtishen continuezhiliangtishen = new Continuezhiliangtishen();
        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据
            ArrayList arrayListx10 = new ArrayList();
            ArrayList arrayListx11 = new ArrayList();
            ArrayList arrayListx12 = new ArrayList();
            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份

                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone(zone, date);
                    arrayListx10.add(i - 1, (int) continueDatasource.getX10());
                    arrayListx11.add(i - 1, (int) continueDatasource.getX11());
                    arrayListx12.add(i - 1, (int) continueDatasource.getX12());
                } catch (Exception e) {
                    arrayListx10.add(i - 1, 0);
                    arrayListx11.add(i - 1, 0);
                    arrayListx12.add(i - 1, 0);
                }


            }
            continuezhiliangtishen.setXianzhuang(arrayListx12);
            continuezhiliangtishen.setZhiliangcehua(arrayListx10);
            continuezhiliangtishen.setZhixing(arrayListx11);
        } else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            ArrayList arrayListx10 = new ArrayList();
            ArrayList arrayListx11 = new ArrayList();
            ArrayList arrayListx12 = new ArrayList();
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone(zone, date);
                    arrayListx10.add(i - 1, (int) continueDatasource.getX10());
                    arrayListx11.add(i - 1, (int) continueDatasource.getX11());
                    arrayListx12.add(i - 1, (int) continueDatasource.getX12());
                } catch (Exception e) {
                    arrayListx10.add(i - 1, 0);
                    arrayListx11.add(i - 1, 0);
                    arrayListx12.add(i - 1, 0);
                }

            }
            continuezhiliangtishen.setXianzhuang(arrayListx12);
            continuezhiliangtishen.setZhiliangcehua(arrayListx10);
            continuezhiliangtishen.setZhixing(arrayListx11);
        }
        return continuezhiliangtishen;
    }

    ;

    @GetMapping("/getneibusunshi")
    public ArrayList getneibusunshi(String zone, String year) {
        ArrayList arrayList = new ArrayList();
        Calendar now = Calendar.getInstance();
        String date;
        if (String.valueOf(now.get(Calendar.YEAR)).equals(year)) { //如果是本年的数据

            int nowMonth = (now.get(Calendar.MONTH)) + 1;//取到当前月份
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = now.get(Calendar.YEAR) + "-0" + i;
                } else {
                    date = now.get(Calendar.YEAR) + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone(zone, date);
                    arrayList.add(i - 1, continueDatasource.getX6());
                } catch (Exception e) {
                    arrayList.add(i - 1, 0);
                }


            }
        } else { //如果不是本年的数据，默认该年有12个月份
            int nowMonth = 12;
            for (int i = 1; i <= nowMonth; i++) {//循环到当前月份
                if (i < 10) {
                    date = year + "-0" + i;
                } else {
                    date = year + "-" + i;
                }

                try { //循环 如果是空指针异常（没查到，就往集合里添加0，查到了就添加真实数据 ）
                    ContinueDatasource continueDatasource = continueDatasourceService.findByDateAndZone(zone, date);
                    arrayList.add(i - 1, continueDatasource.getX6());
                } catch (Exception e) {
                    arrayList.add(i - 1, 0);
                }

            }
        }
        return arrayList;
    }

    @GetMapping("/findAllBydatetypeAndZone")
    public Page<ContinueFile> findAllBydatetypeAndZone(int page, int size, String sort, String date, String type, String zone) {

        return continueFileService.findAllBydatetypeAndZone(page, size, sort, date, type, zone);
    }

    @PostMapping("/gettablelabel")
    public ArrayList<ControlProps> gettablelabel(String year) {
        ArrayList<ControlProps> arrayList = new ArrayList<>();
        String[] arrary1 = {"id", "年", "车型", "指标", "指标类型", "指标级别", "责任部门", "区域责任人", "指标负责人", "目标值", "拉伸指标", "指标反馈人", "1月目标", "1月实际", "2月目标", "2月实际", "3月目标", "3月实际", "4月目标", "4月实际", "5月目标", "5月实际", "6月目标", "6月实际", "7月目标", "7月实际", "8月目标", "8月实际", "9月目标", "9月实际", "10月目标", "10月实际", "11月目标", "11月实际", "12月目标", "12月实际"};
        String[] arrary2 = {"id", "year", "type", "target", "targettype", "targetstandard", "department", "zoneperson", "person_in_charge", "goal", "lashengoal", "zhibiaofankuiren", "yiyuegoal", "yiyueshiji", "eryuegoal", "eryueshiji", "sanyuegoal", "sanyueshiji", "siyuegoal", "siyueshiji", "wuyuegoal", "wuyueshiji", "liuyuegoal", "liuyueshiji", "qiyuegoal", "qiyueshiji", "bayuegoal", "bayueshiji", "jiuyuegoal", "jiuyueshiji", "shiyuegoal", "shiyueshiji", "shiyiyuegoal", "shiyiyueshiji", "shieryuegoal", "shieryueshiji"};
        Calendar cal = Calendar.getInstance();
        String y = String.valueOf(cal.get(Calendar.YEAR));//如果月份不相等时，展示所有数据
        int m = cal.get(Calendar.MONTH); //获得当前是几月
        int x = m * 2 + 12;
        if (year.equals(y) == false) {
            for (int i = 0; i < arrary1.length; i++) {
                ControlProps controlProps = new ControlProps();
                controlProps.setpropandlabel(arrary1[i], arrary2[i]);
                arrayList.add(controlProps);
            }
            return arrayList;
        } else { //当传入的year是今年时 代码要拆成一个个小的模块
            for (int i = 0; i < x + 2; i++) {
                ControlProps controlProps = new ControlProps();
                controlProps.setpropandlabel(arrary1[i], arrary2[i]);
                arrayList.add(controlProps);
            }
            return arrayList;
        }


    }

    @PostMapping("/updatezhiliangfenjiebyid")
    public void updatezhiliangfenjiebyid(String consiciousControljson) {
        ConsiciousControl consiciousControl = JSON.parseObject(consiciousControljson, ConsiciousControl.class);//将接口传过来的json数据转换为对应的实体类对象 fastjson的包
        consiciousControlService.changeConsiciousControlbyid(consiciousControl);//不能直接传入consiciousControljson 接收前端传过来的是json数据 不是java实体类对象
    }

    @PostMapping("/updatezhiliangzhuangtaibyid")
    public void updatezhiliangzhuangtaibyid(int id, int zhaungtai, int yuefen) {//更改状态
        consiciousControlService.changezhuangtai(id, zhaungtai, yuefen);
    }

    @PostMapping("/getzhiliangfenjiebumenbyyear") //得到月份图表的重庆部门数据 用作echarts图表的x轴
    public List getzhiliangfenjiebumenbyyear(String year) {
        List list = new ArrayList();

        List<ConsiciousControl> list1=consiciousControlService.findAllByYear(year);
        list.add(list1.stream().map(ConsiciousControl::getDepartment).distinct()); //stream取数据 清空列表
        return list;
    }

    @PostMapping("/finddepartmentbyyear") //echarts需要的月份图表数据
    public List finddepartmentbyyear( String year){
        return consiciousControlService.finddepartmentbyyear(year);
    };

    @PostMapping("/findtargetnumberbyyear")
     public List findtargetnumberbyyear(String year){ //前端eachrts需要的每个部门的指标数
         return consiciousControlService.findtargetnumberbyyear(year);
     };

    @PostMapping("/finddepartmenthegezhiliang") //得到某年某的各指标是合格还是不合格状态的数量（以部门区分）
    public List finddepartmenthegezhiliang (String year,int yiyuezhuangtai,String jiyue){
        System.out.print(consiciousControlService.finddepartmenthegezhiliang(year,yiyuezhuangtai,jiyue));
        return consiciousControlService.finddepartmenthegezhiliang(year,yiyuezhuangtai,jiyue);
    }

    //以下获得年度的每月状态表
    @PostMapping("/getzhibiaoshunumberarrbyyear")
    public List getzhibiaoshunumberarrbyyear(String year){ //获得这个年度的指标数量有多少
        List list=new ArrayList();//List是抽象类  不能生成实例
        List<ConsiciousControl> list1 =consiciousControlService.findAllByYear(year);
        long a=list1.stream().count();
        for (int i = 0; i <12 ; i++) {
            list.add(i,a);
        }
        return list;
    }

    @PostMapping("/getlvsexiangnumberarrbyyear")
    public List getlvsexiangnumberarrbyyear(String year){//获得这个年度绿色项的指标数量有多少
        List list=new ArrayList();
        List<ConsiciousControl> list1 =consiciousControlService.findAllByYear(year);
        long x1=list1.stream().filter(a->a.getYiyuezhuangtai()==1).count();
        long x2=list1.stream().filter(a->a.getEryuezhuangtai()==1).count();
        long x3=list1.stream().filter(a->a.getSanyuezhuangtai()==1).count();
        long x4=list1.stream().filter(a->a.getSiyuezhuangtai()==1).count();
        long x5=list1.stream().filter(a->a.getWuyuezhuangtai()==1).count();
        long x6=list1.stream().filter(a->a.getLiuyuezhuangtai()==1).count();
        long x7=list1.stream().filter(a->a.getQiyuezhuangtai()==1).count();
        long x8=list1.stream().filter(a->a.getBayuezhuangtai()==1).count();
        long x9=list1.stream().filter(a->a.getJiuyuezhuangtai()==1).count();
        long x10=list1.stream().filter(a->a.getShiyuezhuangtai()==1).count();
        long x11=list1.stream().filter(a->a.getShiyiyuezhuangtai()==1).count();
        long x12=list1.stream().filter(a->a.getShieryuezhuangtai()==1).count();
        list.add(0,x1);
        list.add(1,x2);
        list.add(2,x3);
        list.add(3,x4);
        list.add(4,x5);
        list.add(5,x6);
        list.add(6,x7);
        list.add(7,x8);
        list.add(8,x9);
        list.add(9,x10);
        list.add(10,x11);
        list.add(11,x12);
        return list;
    }

    @PostMapping("/gethongsexiangnumberarrbyyear")
    public List gethongsexiangnumberarrbyyear(String year){//获得这个年度红色项项的指标数量有多少
        List list=new ArrayList();
        List<ConsiciousControl> list1 =consiciousControlService.findAllByYear(year);
        long x1=list1.stream().filter(a->a.getYiyuezhuangtai()==2).count();
        long x2=list1.stream().filter(a->a.getEryuezhuangtai()==2).count();
        long x3=list1.stream().filter(a->a.getSanyuezhuangtai()==2).count();
        long x4=list1.stream().filter(a->a.getSiyuezhuangtai()==2).count();
        long x5=list1.stream().filter(a->a.getWuyuezhuangtai()==2).count();
        long x6=list1.stream().filter(a->a.getLiuyuezhuangtai()==2).count();
        long x7=list1.stream().filter(a->a.getQiyuezhuangtai()==2).count();
        long x8=list1.stream().filter(a->a.getBayuezhuangtai()==2).count();
        long x9=list1.stream().filter(a->a.getJiuyuezhuangtai()==2).count();
        long x10=list1.stream().filter(a->a.getShiyuezhuangtai()==2).count();
        long x11=list1.stream().filter(a->a.getShiyiyuezhuangtai()==2).count();
        long x12=list1.stream().filter(a->a.getShieryuezhuangtai()==2).count();
        list.add(0,x1);
        list.add(1,x2);
        list.add(2,x3);
        list.add(3,x4);
        list.add(4,x5);
        list.add(5,x6);
        list.add(6,x7);
        list.add(7,x8);
        list.add(8,x9);
        list.add(9,x10);
        list.add(10,x11);
        list.add(11,x12);
        return list;
    }
}
