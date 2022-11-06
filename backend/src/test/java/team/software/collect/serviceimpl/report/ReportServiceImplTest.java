package team.software.collect.serviceimpl.report;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.software.collect.enums.Device;
import team.software.collect.mapperservice.filepicture.FilePictureMapper;
import team.software.collect.mapperservice.report.ReportMapper;
import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.mapperservice.task.TaskOrderMapper;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.po.report.Report;
import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskOrder;
import team.software.collect.po.user.Portrait;
import team.software.collect.util.PageInfoUtil;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.CommentVO;
import team.software.collect.vo.report.ReportVO;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 * @progect: TaskOrderController.java
 * @package: team.software.collect.serviceimpl.report
 * @author: hewei
 * @email: heweibright@gmail.com
 * @create: 2022-03-05-14:15
 */
class ReportServiceImplTest {
    @Mock
    ReportMapper reportMapper;
    @Mock
    TaskOrderMapper taskOrderMapper;
    @Mock
    FilePictureMapper filePictureMapper;
    @Mock
    PortraitMapper portraitMapper;
    @Mock
    TaskMapper taskMapper;
    @InjectMocks
    ReportServiceImpl reportServiceImpl;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPostReport() {
        when(reportMapper.insert(any())).thenReturn(1);
        when(reportMapper.selectByPrimaryKey(anyInt())).thenReturn(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(4), "intro", "recovertips", "1,0,0,0,0,0"));
        when(taskOrderMapper.updateByPrimaryKey(any())).thenReturn(1);
        when(taskOrderMapper.selectByUid(anyInt())).thenReturn(Arrays.<TaskOrder>asList(new TaskOrder(Integer.valueOf(2), Integer.valueOf(2))));
        when(portraitMapper.selectByUid(anyInt())).thenReturn(new Portrait(Integer.valueOf(2),new BigDecimal(0.1),"1,1,0",new BigDecimal(0.1),"1,0,0,0,0,0"));
        when(portraitMapper.updateByPrimaryKey(any())).thenReturn(1);


        ResultVO<ReportVO> result = reportServiceImpl.postReport(new ReportVO(new Report(Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(4),"我是一个Intro", "这里是recoverTips", "1,0,0,0,0,0")));

        ReportVO reportVO = new ReportVO();
        reportVO.setUid(2);
        reportVO.setTid(2);
        reportVO.setDevice("1,0,0,0,0,0");
        reportVO.setIntro("我是一个Intro");
        reportVO.setRecovertips("这里是recoverTips");
        reportVO.setCreatTime(new Date());
        reportVO.setParentId(4);
        reportVO.setScore(new BigDecimal(2.5));
        reportVO.setCommentNum(1);
        Assertions.assertEquals(new ResultVO<ReportVO>(Integer.valueOf(1), "报告上传成功！", reportVO).toString(), result.toString());
    }

    @Test
    void testGetReportsByTid() {
        when(reportMapper.selectByTid(anyInt())).thenReturn(Arrays.<Report>asList(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(4), "intro", "recovertips", "1,0,0,0,0,0")));

        List<ReportVO> result = reportServiceImpl.getReportsByTid(Integer.valueOf(0), Integer.valueOf(0));
        Assertions.assertEquals(Arrays.<ReportVO>asList(new ReportVO(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(4), "intro", "recovertips", "1,0,0,0,0,0"))), result);
    }

    @Test
    void testGetReportsByTidAndUid(){
        when(reportMapper.selectByTid(anyInt())).thenReturn(Arrays.<Report>asList(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(4), "intro", "recovertips", "1,0,0,0,0,0")));

        List<ReportVO> result = reportServiceImpl.getReportsByTidAndUid(Integer.valueOf(0), Integer.valueOf(0));
        Assertions.assertEquals(Arrays.<ReportVO>asList(new ReportVO(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(4), "intro", "recovertips", "1,0,0,0,0,0"))),result);
    }

    @Test
    void testGetParentReports(){
        when(reportMapper.selectByPrimaryKey(0)).thenReturn(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(1), "intro", "recovertips", "1,0,0,0,0,0"));
        when(reportMapper.selectByPrimaryKey(1)).thenReturn(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(-1), "intro", "recovertips", "1,0,0,0,0,0"));

        List<ReportVO> result = reportServiceImpl.getParentReports(Integer.valueOf(0));
        List<ReportVO> list = new ArrayList<>();
        list.add(new ReportVO(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(1), "intro", "recovertips", "1,0,0,0,0,0")));
        list.add(new ReportVO(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(-1), "intro", "recovertips", "1,0,0,0,0,0")));
        Assertions.assertEquals(list,result);
    }

    @Test
    void testPostCollaborativeReport(){
        Report r0=new Report(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(4),"intro", "recovertips", "1,0,0,0,0,0");
        r0.setRid(0);
        Report r1=new Report(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(2),"intro", "recovertips", "1,0,0,0,0,0");
        r1.setRid(1);

        when(reportMapper.insert(any())).thenReturn(1);
        when(reportMapper.selectByUid(anyInt())).thenReturn(Arrays.<Report>asList(r0));
        when(portraitMapper.selectByUid(anyInt())).thenReturn(new Portrait(Integer.valueOf(2),new BigDecimal(0.1),"1,1,0",new BigDecimal(0.1),"1,0,0,0,0,0"));
        when(portraitMapper.updateByPrimaryKey(any())).thenReturn(1);
        when(reportMapper.selectByPrimaryKey(anyInt())).thenReturn(r1);
        when(taskMapper.selectByPrimaryKey(anyInt())).thenReturn(new Task(Integer.valueOf(0), Integer.valueOf(0), "name", "intro", Integer.valueOf(0), Integer.valueOf(0), "type"));

        ReportVO reportVO=new ReportVO();
        reportVO.setTid(Integer.valueOf(0));
        reportVO.setRecovertips("recovertips");
        reportVO.setIntro("intro");
        reportVO.setUid(Integer.valueOf(0));
        reportVO.setDevice("1,0,0,0,0,0");
        reportVO.setParentId(2);
        reportVO.setScore(new BigDecimal(2.5));
        reportVO.setCreatTime(new Date());
        reportVO.setCommentNum(1);
        ResultVO<ReportVO> result=reportServiceImpl.postCollaborativeReport(Integer.valueOf(2),reportVO);
        Assertions.assertEquals(new ResultVO<ReportVO>(Integer.valueOf(1), "报告协作成功！", reportVO).toString(), result.toString());
    }

    @Test
    void testGetReportDetails(){
        when(reportMapper.selectByPrimaryKey(anyInt())).thenReturn(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(4), "intro", "recovertips", "1,0,0,0,0,0"));

        ReportVO result=reportServiceImpl.getReportDetails(Integer.valueOf(0),Integer.valueOf(0));
        Assertions.assertEquals(new ReportVO(new Report(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(4),"intro", "recovertips", "1,0,0,0,0,0")).toString(),result.toString());
    }

    @Test
    void testGetLowQualityReports(){
        when(reportMapper.selectByTid(anyInt())).thenReturn(Arrays.asList(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(4), "intro", "recovertips", "1,0,0,0,0,0")));

        PageInfo<ReportVO> result = reportServiceImpl.getLowQualityReports(1,1,0);
        PageHelper.startPage(1, 1);
        List<Report> re=new ArrayList<>(Arrays.asList(new Report(Integer.valueOf(0), Integer.valueOf(0),Integer.valueOf(4), "intro", "recovertips", "1,0,0,0,0,0")));
        PageInfo<Report> pageInfo=new PageInfo<>(re);
        Assertions.assertEquals(PageInfoUtil.convert(pageInfo, ReportVO.class).toString(),result.toString());
    }

    @Test
    void testGetSimilarReports(){
        Report report0 = new Report(0, 0,100, "intro", "recovertips", "Linux");
        report0.setRid(0);
        when(reportMapper.selectByPrimaryKey(0)).thenReturn(report0);

        List<Report> lst1 = new ArrayList<>();
        Report report1 = new Report(1, 0,101, "intro234", "recovertips234", "Linux");
        Report report2 = new Report(2, 0,102, "abcccc", "recovertips234", "Linux");
        Report report3 = new Report(3, 0,104, "abcccc", "1. abc2. cba3. bca", "Linux");
        Report report4 = new Report(4, 0,105, "intro", "recovertips", "Linux");

        lst1.add(report0);
        lst1.add(report1);
        lst1.add(report2);
        lst1.add(report3);
        lst1.add(report4);
        for (int i = 0; i < lst1.size(); i++) {
            lst1.get(i).setRid(i);
        }

        when(reportMapper.selectByTid(0)).thenReturn(lst1);

        List<FilePicture> lst2 = new ArrayList<>();
        List<FilePicture> lst3 = new ArrayList<>();
        List<FilePicture> lst4 = new ArrayList<>();
        List<FilePicture> lst5 = new ArrayList<>();
        List<FilePicture> lst6 = new ArrayList<>();

        FilePicture filePicture0_1 = new FilePicture(0, 0, "title0_1", "145644ef-0cd7-4aba-81f3-fd048e16f5ad.png", "png");
        FilePicture filePicture0_2 = new FilePicture(0, 0, "title0_2", "af922706-cfa6-42eb-a5e9-f07e4fef37c4.png", "png");
        FilePicture filePicture1 = new FilePicture(0, 1, "title1", "145644ef-0cd7-4aba-81f3-fd048e16f5ad.png", "png");
        FilePicture filePicture2 = new FilePicture(0, 2, "title2", "af922706-cfa6-42eb-a5e9-f07e4fef37c4.png", "png");
        FilePicture filePicture3 = new FilePicture(0,3,"title3","af922706-cfa6-42eb-a5e9-f07e4fef37c4.png","png");
        FilePicture filePicture4 = new FilePicture(0,4,"title4","145644ef-0cd7-4aba-81f3-fd048e16f5ad.png","png");
        lst2.add(filePicture0_1);
        lst2.add(filePicture0_2);
        lst3.add(filePicture1);
        lst4.add(filePicture2);
        lst5.add(filePicture3);
        lst6.add(filePicture4);
        when(filePictureMapper.selectByRid(report0.getRid())).thenReturn(lst2);
        when(filePictureMapper.selectByRid(report1.getRid())).thenReturn(lst3);
        when(filePictureMapper.selectByRid(report2.getRid())).thenReturn(lst4);
        when(filePictureMapper.selectByRid(report3.getRid())).thenReturn(lst5);
        when(filePictureMapper.selectByRid(report4.getRid())).thenReturn(lst6);

        PageInfo<ReportVO> result=reportServiceImpl.getSimilarReports(1, 1, 0,0);

        List<Report> li = new ArrayList<>();
        li.add(report1);
        li.add(report2);
        li.add(report3);
        PageInfo<Report> po=new PageInfo<>(li);
        PageInfo<ReportVO> pageInfo = PageInfoUtil.convert(po, ReportVO.class);

        Assertions.assertEquals(pageInfo.toString(),result.toString());
    }

    @Test
    void testGetClusters(){
        List<Report> lst1 = new ArrayList<>();
        Report report0 = new Report(0, 0,100, "这个项目的边界检验有问题", "检验边界", "Linux");
        Report report1 = new Report(1, 0,101, "项目这个的边界检验有一些问题", "看一下边界", "Linux");
        Report report2 = new Report(2, 0,102, "溢出问题逻辑", "扩大数组", "Linux");
        Report report3 = new Report(3, 0,104, "数组溢出问题", "把数组开大一点，然后存的时候进行判断", "Linux");
        Report report4 = new Report(4, 0,105, "边界问题", "检验", "Linux");

        lst1.add(report0);
        lst1.add(report1);
        lst1.add(report2);
        lst1.add(report3);
        lst1.add(report4);

        for (int i = 0; i < lst1.size(); i++) {
            lst1.get(i).setRid(i);
        }

        when(reportMapper.selectByTid(0)).thenReturn(lst1);

        List<FilePicture> lst2 = new ArrayList<>();
        List<FilePicture> lst3 = new ArrayList<>();
        List<FilePicture> lst4 = new ArrayList<>();
        List<FilePicture> lst5 = new ArrayList<>();
        List<FilePicture> lst6 = new ArrayList<>();

        FilePicture filePicture0_1 = new FilePicture(0, 0, "title0_1", "145644ef-0cd7-4aba-81f3-fd048e16f5ad.png", "png");
        FilePicture filePicture0_2 = new FilePicture(0, 0, "title0_2", "af922706-cfa6-42eb-a5e9-f07e4fef37c4.png", "png");
        FilePicture filePicture1 = new FilePicture(0, 1, "title1", "145644ef-0cd7-4aba-81f3-fd048e16f5ad.png", "png");
        FilePicture filePicture2 = new FilePicture(0, 2, "title2", "af922706-cfa6-42eb-a5e9-f07e4fef37c4.png", "png");
        FilePicture filePicture3 = new FilePicture(0,3,"title3","af922706-cfa6-42eb-a5e9-f07e4fef37c4.png","png");
        FilePicture filePicture4 = new FilePicture(0,4,"title4","145644ef-0cd7-4aba-81f3-fd048e16f5ad.png","png");
        lst2.add(filePicture0_1);
        lst2.add(filePicture0_2);
        lst3.add(filePicture1);
        lst4.add(filePicture2);
        lst5.add(filePicture3);
        lst6.add(filePicture4);
        when(filePictureMapper.selectByRid(report0.getRid())).thenReturn(lst2);
        when(filePictureMapper.selectByRid(report1.getRid())).thenReturn(lst3);
        when(filePictureMapper.selectByRid(report2.getRid())).thenReturn(lst4);
        when(filePictureMapper.selectByRid(report3.getRid())).thenReturn(lst5);
        when(filePictureMapper.selectByRid(report4.getRid())).thenReturn(lst6);

        List<List<ReportVO>> result = reportServiceImpl.getClusters(0);

        List<List<ReportVO>> li = new ArrayList<>();
        List<ReportVO> cluster1 = new ArrayList<>();
        List<ReportVO> cluster2 = new ArrayList<>();
        cluster1.add(new ReportVO(report0));
        cluster1.add(new ReportVO(report1));
        cluster1.add(new ReportVO(report4));
        cluster2.add(new ReportVO(report2));
        cluster2.add(new ReportVO(report3));
        li.add(cluster1);
        li.add(cluster2);
        Assertions.assertEquals(li.toString(),result.toString());
    }


}

