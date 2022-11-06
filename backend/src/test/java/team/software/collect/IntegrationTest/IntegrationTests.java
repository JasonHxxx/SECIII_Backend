package team.software.collect.IntegrationTest;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.collect.CollectApplication;
import team.software.collect.controller.*;
import team.software.collect.enums.Device;
import team.software.collect.enums.TestType;
import team.software.collect.enums.UserRole;
import team.software.collect.mapperservice.task.TaskrecStrategyMapper;
import team.software.collect.po.filepicture.FilePicture;
import team.software.collect.po.report.Report;
import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskrecStrategy;
import team.software.collect.po.user.UserInfo;
import team.software.collect.util.Constant;
import team.software.collect.util.PageInfoUtil;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.report.CommentVO;
import team.software.collect.vo.report.ReportVO;
import team.software.collect.vo.task.TaskOrderVO;
import team.software.collect.vo.task.TaskVO;
import team.software.collect.vo.task.TaskrecStrategyVO;
import team.software.collect.vo.user.UserFormVO;
import team.software.collect.vo.user.UserInfoVO;


import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectApplication.class)
@AutoConfigureMockMvc
public class IntegrationTests {
    @Autowired
    AdminController adminController;

    @Autowired
    FilePicController filePicController;

    @Autowired
    ReportController reportController;

    @Autowired
    TaskController taskController;

    @Autowired
    TaskOrderController taskOrderController;

    @Autowired
    UserInfoController userInfoController;


    @Test
    public void userInfoTest() throws Exception{
        // 登录
        UserFormVO userFormVO=new UserFormVO();
        userFormVO.setPhone("1");
        userFormVO.setPassword("1");
        ResultVO<UserInfoVO> resultVO=userInfoController.login(userFormVO);
        UserInfoVO reUserInfoVO = resultVO.getData();
        reUserInfoVO.setCreateTime(null);
        reUserInfoVO.setUid(null);
        resultVO.setData(reUserInfoVO);

        UserInfo userInfo1=new UserInfo(1, "user1", "1", "1", UserRole.TASKPOSTER);
        UserInfoVO userInfoVO1=new UserInfoVO(userInfo1);
        userInfoVO1.setCreateTime(null);
        userInfoVO1.setUid(null);
        assertEquals(userInfoVO1,reUserInfoVO);
        assertNull(userInfoVO1.getPassword());


        // 查看用户信息
        UserInfoVO userInfoVO2 = userInfoController.getUserInfo(2);
        userInfoVO2.setCreateTime(null);
        UserInfoVO reUserInfoVO2 = new UserInfoVO(new UserInfo(2, "user2", "2", "2", UserRole.WORKER));
        String str2 = userInfoVO2.getUid() + " " + userInfoVO2.getUname() + " " + userInfoVO2.getPhone() + " "
                + userInfoVO2.getPassword() + " " + userInfoVO2.getUserRole();
        String restr2 = reUserInfoVO2.getUid()  + " " + reUserInfoVO2.getUname() + " " + reUserInfoVO2.getPhone() + " "
                + reUserInfoVO2.getPassword() + " " + reUserInfoVO2.getUserRole();
        assertEquals(restr2, str2);
    }


    @Test
    public void taskOrderTest() throws Exception{

        DecimalFormat df = new DecimalFormat("0.00");

        // 查看正在执行的任务列表
        List<TaskVO> unfinishedTaskVOs = taskOrderController.getUnfinishedTasks(2);
        List<TaskVO> reUnfinishedTaskVOs = new ArrayList<>();
        reUnfinishedTaskVOs.add(new TaskVO(new Task(2, 1, "task2", "intro2", 200, 1, TestType.FunctionTest.toString(), new BigDecimal(2.00), Device.MacOs.toString())));
        reUnfinishedTaskVOs.add(new TaskVO(new Task(3, 1, "task3", "intro3", 10,1,TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.MacOs.toString())));
        String str1 = "";
        String reStr1 = "";
        for (TaskVO taskVO : unfinishedTaskVOs) {
            str1 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";
        }

        for (TaskVO taskVO : reUnfinishedTaskVOs) {
            reStr1 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";
        }
        assertEquals(reStr1, str1);

        // 查看已完成的任务列表
        List<TaskVO> finishedTaskVOs = taskOrderController.getFinishedTasks(2);
        List<TaskVO> reFinishedTaskVOs = new ArrayList<>();
        reFinishedTaskVOs.add(new TaskVO(new Task(1, 1, "task1", "intro1", 100, 3, TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.Android.toString())));
        String str2 = "";
        String reStr2 = "";
        for (TaskVO taskVO : finishedTaskVOs) {
            str2 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";
        }

        for (TaskVO taskVO : reFinishedTaskVOs) {
            reStr2 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";
        }
        assertEquals(reStr2, str2);

        // 查看参与一个任务的所有众包工人
        List<UserInfoVO> userInfoVOs = taskOrderController.getUserByTid(2);
        List<UserInfoVO> reUserInfoVOs = new ArrayList<>();
        reUserInfoVOs.add(new UserInfoVO(new UserInfo(2, "user2", "2", "2", UserRole.WORKER)));
        String str3 = "";
        String reStr3 = "";

        for (UserInfoVO userInfoVO : userInfoVOs) {
            str3 += userInfoVO.getUid() + " " + userInfoVO.getUname() + " " + userInfoVO.getPhone() + " " + userInfoVO.getPassword()
                    + " " + userInfoVO.getUserRole() + " ";
        }

        for (UserInfoVO userInfoVO : reUserInfoVOs) {
            reStr3 += userInfoVO.getUid() + " " + userInfoVO.getUname() + " " + userInfoVO.getPhone() + " " + userInfoVO.getPassword()
                    + " " + userInfoVO.getUserRole() + " ";
        }
        assertEquals(reStr3, str3);

    }

    @Test
    public void taskTest() throws Exception{
        DecimalFormat df = new DecimalFormat("0.00");

        // 获取已发布任务
        List<TaskVO> taskVOs = taskController.getPostedTasks(1);
        List<TaskVO> reTaskVOs = new ArrayList<>();
        reTaskVOs.add(new TaskVO(new Task(1, 1, "task1", "intro1", 100, 3, TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.Android.toString())));
        reTaskVOs.add(new TaskVO(new Task(2, 1, "task2", "intro2", 200, 1, TestType.FunctionTest.toString(), new BigDecimal(2.00), Device.MacOs.toString())));
        reTaskVOs.add(new TaskVO(new Task(3, 1, "task3", "intro3", 10, 1, TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.MacOs.toString())));
        reTaskVOs.add(new TaskVO(new Task(4, 1, "task4", "intro4", 100, 0, TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.Android.toString())));
        reTaskVOs.add(new TaskVO(new Task(5, 1, "task5", "intro5", 200, 0, TestType.FunctionTest.toString(), new BigDecimal(2.00), Device.Android.toString())));
        reTaskVOs.add(new TaskVO(new Task(6, 1, "task6", "intro6", 100, 0, TestType.FunctionTest.toString(), new BigDecimal(3.00), Device.Android.toString())));

        String str1 = "";
        String reStr1 = "";
        for (TaskVO taskVO : taskVOs) {
            str1 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";
        }

        for (TaskVO taskVO : reTaskVOs) {
            reStr1 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";
        }
        assertEquals(reStr1, str1);

        // 获取任务大厅的任务（正在招募中的任务）
        PageInfo<TaskVO> pageInfo1 = taskController.getHallTasks(1);
        for (int i = 0; i < pageInfo1.getList().size(); i++) {
            pageInfo1.getList().get(i).setBeginTime(null);
            pageInfo1.getList().get(i).setEndTime(null);
        }
        List<Task> reTasks1 = new ArrayList<>();
        reTasks1.add(new Task(1, 1, "task1", "intro1", 100, 3, TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.Android.toString()));
        reTasks1.add(new Task(2, 1, "task2", "intro2", 200, 1, TestType.FunctionTest.toString(), new BigDecimal(2.00), Device.MacOs.toString()));
        reTasks1.add(new Task(3, 1, "task3", "intro3", 10, 1, TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.MacOs.toString()));
        reTasks1.add(new Task(4, 1, "task4", "intro4", 100, 0, TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.Android.toString()));
        reTasks1.add(new Task(5, 1, "task5", "intro5", 200, 0, TestType.FunctionTest.toString(), new BigDecimal(2.00), Device.Android.toString()));
        PageInfo<Task> po1 = new PageInfo<>(reTasks1);
        PageInfo<TaskVO> rePageInfo1 = PageInfoUtil.convert(po1, TaskVO.class);
        String str2 = "";
        String reStr2 = "";

        for (int i = 0; i < pageInfo1.getList().size(); i++) {
            TaskVO taskVO = pageInfo1.getList().get(i);
            str2 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";

        }
        for (int i = 0; i <  rePageInfo1.getList().size(); i++) {
            TaskVO taskVO = rePageInfo1.getList().get(i);
            reStr2 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";

        }
        assertEquals(reStr2, str2);


        // 查看任务的详细信息
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        TaskVO taskVO3 = taskController.getTaskById(2, 4);
        TaskVO reTaskVO3 = new TaskVO(new Task(4, 1, "task4", "intro4", 100, 0, TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.Android.toString()));
//        reTaskVO3.setBeginTime(simpleDateFormat.parse("2022-02-27 21:04:51"));
//        reTaskVO3.setEndTime(simpleDateFormat.parse("2022-05-03 21:04:53"));
        String str3 = "";
        String reStr3 = "";
        str3 += taskVO3.getTid() + " " + taskVO3.getUid() + " " + taskVO3.getName() + " " + taskVO3.getIntro() + " " + taskVO3.getMaxWorkers()
                + " " + taskVO3.getWorkerCnt() + " " + taskVO3.getType() + " " + df.format(taskVO3.getDifficulty()) + " " + taskVO3.getDevice() + " ";
        reStr3 = reTaskVO3.getTid() + " " + reTaskVO3.getUid() + " " + reTaskVO3.getName() + " " + reTaskVO3.getIntro() + " " + reTaskVO3.getMaxWorkers()
                + " " + reTaskVO3.getWorkerCnt() + " " + reTaskVO3.getType() + " " + df.format(reTaskVO3.getDifficulty()) + " " + reTaskVO3.getDevice() + " ";
        assertEquals(str3, reStr3);

        // 获取所有任务
        PageInfo<TaskVO> pageInfo2 = taskController.getAllTasks(2);
        for (int i = 0; i < pageInfo2.getList().size(); i++) {
            pageInfo2.getList().get(i).setBeginTime(null);
            pageInfo2.getList().get(i).setEndTime(null);
        }
        List<Task> reTasks2 = new ArrayList<>();
        reTasks2.add(new Task(6, 1, "task6", "intro6", 100, 0, TestType.FunctionTest.toString(), new BigDecimal(3.00), Device.Android.toString()));
        PageInfo<Task> po2 = new PageInfo<>(reTasks2);
        PageInfo<TaskVO> rePageInfo2 = PageInfoUtil.convert(po2, TaskVO.class);

        String str4 = "";
        String reStr4 = "";

        for (int i = 0; i < pageInfo2.getList().size(); i++) {
            TaskVO taskVO = pageInfo2.getList().get(i);
            str4 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";

        }
        for (int i = 0; i <  rePageInfo2.getList().size(); i++) {
            TaskVO taskVO = rePageInfo2.getList().get(i);
            reStr4 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";

        }
        assertEquals(reStr4, str4);


        // 获取系统推荐的任务 用户已经接受过的任务会被过滤掉 uid设为1是管理员是错的
        PageInfo<TaskVO> pageInfo3 = taskController.getRecommendedTasks(1, 2);
        for (int i = 0; i < pageInfo3.getList().size(); i++) {
            pageInfo3.getList().get(i).setBeginTime(null);
            pageInfo3.getList().get(i).setEndTime(null);
        }
        List<Task> reTasks3 = new ArrayList<>();
        reTasks3.add(new Task(6, 1, "task6", "intro6", 100, 0, TestType.FunctionTest.toString(), new BigDecimal(3.00), Device.Android.toString()));
        reTasks3.add(new Task(5, 1, "task5", "intro5", 200, 0, TestType.FunctionTest.toString(), new BigDecimal(2.00), Device.Android.toString()));
        reTasks3.add(new Task(4, 1, "task4", "intro4", 100, 0, TestType.FunctionTest.toString(), new BigDecimal(1.00), Device.Android.toString()));
        PageInfo<Task> po3 = new PageInfo<>(reTasks3);
        PageInfo<TaskVO> rePageInfo3 = PageInfoUtil.convert(po3, TaskVO.class);

        String str5 = "";
        String reStr5 = "";

        for (int i = 0; i < pageInfo3.getList().size(); i++) {
            TaskVO taskVO = pageInfo3.getList().get(i);
            str5 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";

        }
        for (int i = 0; i <  rePageInfo3.getList().size(); i++) {
            TaskVO taskVO = rePageInfo3.getList().get(i);
            reStr5 += taskVO.getTid() + " " + taskVO.getUid() + " " + taskVO.getName() + " " + taskVO.getIntro() + " " + taskVO.getMaxWorkers()
                    + " " + taskVO.getWorkerCnt() + " " + taskVO.getType() + " " + df.format(taskVO.getDifficulty()) + " " + taskVO.getDevice() + " ";

        }
        assertEquals(reStr5, str5);

    }


    @Test
    public void reportTest() throws Exception{

        // 获取已发布得测试报告
        List<ReportVO> reportVOs1 = reportController.getReportsByTid(1, 1);
        for (ReportVO reportVO : reportVOs1) {
            reportVO.setCreatTime(null);
        }
        List<ReportVO> reReportVOs1 = new ArrayList<>();
        reReportVOs1.add(new ReportVO(new Report(2, 1, -1, "一篇对于任务1的测试报告", "错误点1：没有考虑边界条件 错误点2：没有添加登陆验证功能，可能遭受攻击 建议：1.添加判断语句 2.在后端添加校验的代码", Device.Android.toString())));
        reReportVOs1.get(0).setRid(1);
        reReportVOs1.get(0).setScore(new BigDecimal(3.25));
        reReportVOs1.get(0).setCommentNum(2);
        String str1 = "";
        String reStr1 = "";
        for (ReportVO reportVO : reportVOs1) {
            str1 += reportVO.getRid() + " " + reportVO.getUid() + " " + reportVO.getTid() + " " + reportVO.getParentId() + " " + reportVO.getIntro()
                    + " " + reportVO.getRecovertips() + " " + reportVO.getDevice() + " " + reportVO.getScore() + " " + reportVO.getCreatTime()
                    + " " + reportVO.getCommentNum() + " ";
        }
        for (ReportVO reportVO : reReportVOs1) {
            reStr1 += reportVO.getRid() + " " + reportVO.getUid() + " " + reportVO.getTid() + " " + reportVO.getParentId() + " " + reportVO.getIntro()
                    + " " + reportVO.getRecovertips() + " " + reportVO.getDevice() + " " + reportVO.getScore() + " " + reportVO.getCreatTime()
                    + " " + reportVO.getCommentNum() + " ";
        }
        assertEquals(reStr1, str1);

        List<ReportVO> reportVOs2 = reportController.getReportsByTid(1, 3);
        assertTrue(reportVOs2.isEmpty());

        // 获取一个用户对一个任务发布得所有测试报告，包括协作报告
        List<ReportVO> reportVOs3 = reportController.getReportsByTidAndUid(2, 1);
        for (ReportVO reportVO : reportVOs3) {
            reportVO.setRid(null);
            reportVO.setCreatTime(null);
        }
        List<ReportVO> reReportVOs3 = new ArrayList<>();
        reReportVOs3.add(new ReportVO(new Report(2, 1, -1, "一篇对于任务1的测试报告", "错误点1：没有考虑边界条件 错误点2：没有添加登陆验证功能，可能遭受攻击 建议：1.添加判断语句 2.在后端添加校验的代码", Device.Android.toString())));
        reReportVOs3.get(0).setScore(new BigDecimal(3.25));
        reReportVOs3.get(0).setCommentNum(2);
        String str3 = "";
        String reStr3 = "";
        for (ReportVO reportVO : reportVOs3) {
            str3 += reportVO.getRid() + " " + reportVO.getUid() + " " + reportVO.getTid() + " " + reportVO.getParentId() + " " + reportVO.getIntro()
                    + " " + reportVO.getRecovertips() + " " + reportVO.getDevice() + " " + reportVO.getScore() + " " + reportVO.getCommentNum() + " ";
        }
        for (ReportVO reportVO : reReportVOs3) {
            reStr3 += reportVO.getRid() + " " + reportVO.getUid() + " " + reportVO.getTid() + " " + reportVO.getParentId() + " " + reportVO.getIntro()
                    + " " + reportVO.getRecovertips() + " " + reportVO.getDevice() + " " + reportVO.getScore() + " " + reportVO.getCommentNum() + " ";
        }
        assertEquals(reStr3, str3);


        // 获取相似报告
        PageInfo<ReportVO> pageInfo4 = reportController.getSimilarReports(1, 2, 1);
        assertTrue(pageInfo4.getList().isEmpty());

        // 获取测试报告得详细信息，包括测试报告得内容、别人的评价
        ReportVO reportVO5 = reportController.getReportByRid(2, 1);
        String reStr5 = "错误点1：没有考虑边界条件 错误点2：没有添加登陆验证功能，可能遭受攻击 建议：1.添加判断语句 2.在后端添加校验的代码";
        assertEquals(reStr5, reportVO5.getRecovertips());

        // 获取一个报告的父报告，指导某个报告没有父报告，第一个是原报告
        List<ReportVO> reportVOs6 = reportController.getParentReports(1);
        assertTrue(reportVOs6.size() == 1);

        // 获取测试报告对应的评价、分页展示
        PageInfo<CommentVO> pageInfo7 = reportController.getCommentsByRid(1, 1);
        assertEquals("这个报告写得还行", pageInfo7.getList().get(0).getComments());

        // 获得聚簇报告
        List<List<ReportVO>> list8 = reportController.getSimilarRelationship(3);
        assertTrue(list8.isEmpty());

        // 获得低质量报告
        PageInfo<ReportVO> pageInfo9 = reportController.getLowQualityReports(1, 1);
        assertTrue(pageInfo9.getList().size() == 1);

    }

    @Test
    public void filePicTest() throws Exception {
        List<FilePicture> filePictures2 = filePicController.getUrl(2, 1);
        assertEquals("98c1d400-e308-431b-906e-26c4e6a1e52c.png", filePictures2.get(0).getFileName());
    }

    @Test
    public void adminTest() throws Exception {
        // 得到推荐规则列表
        List<TaskrecStrategyVO> taskrecStrategyVOS1 = adminController.getRules(3);
        assertTrue(taskrecStrategyVOS1.size() == 2);

        // 得到正在使用的规则
        ResultVO<TaskrecStrategyVO> taskrecStrategyVOResultVO = adminController.getRuleInUse();
        assertTrue(taskrecStrategyVOResultVO.getData().getUid() == 1);
    }
}
