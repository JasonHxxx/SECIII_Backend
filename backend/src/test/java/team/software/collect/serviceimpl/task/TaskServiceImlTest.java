package team.software.collect.serviceimpl.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.list.TransformedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.mapperservice.task.TaskOrderMapper;
import team.software.collect.mapperservice.task.TaskrecStrategyMapper;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskrecStrategy;
import team.software.collect.po.user.Portrait;
import team.software.collect.util.Constant;
import team.software.collect.util.PageInfoUtil;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImlTest {
    @Mock
    TaskMapper taskMapper;
    @Mock
    TaskrecStrategyMapper taskrecStrategyMapper;
    @Mock
    PortraitMapper portraitMapper;
    @Mock
    TaskOrderMapper taskOrderMapper;


    @InjectMocks
    TaskServiceIml taskServiceIml;

    @Before
    public void setUp() {
    }

    @Test
    public void testGetPostedTasks() {
        when(taskMapper.selectByUid(anyInt())).thenReturn(Arrays.<Task>asList(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android")));

        List<TaskVO> result = taskServiceIml.getPostedTasks(0);
        assertEquals(Arrays.<TaskVO>asList(new TaskVO(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android"))), result);
    }



    @Test
    public void testPostTask() {
        when(taskMapper.insert(any())).thenReturn(1);
        when(taskMapper.selectByUid(anyInt())).thenReturn(Arrays.<Task>asList(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android")));

        ResultVO<TaskVO> result = taskServiceIml.postTask(new TaskVO(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android")));
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "已存在同名任务！"), result);

        result = taskServiceIml.postTask(new TaskVO(new Task(0, 0, "differName", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android")));
        Task task = new Task(0, 0, "differName", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android");
        task.setBeginTime(new Date());
        assertEquals(new ResultVO<TaskVO>(Constant.REQUEST_SUCCESS, "任务发布成功。", new TaskVO(task)).toString(), result.toString());

        when(taskMapper.insert(any())).thenReturn(0);
        result = taskServiceIml.postTask(new TaskVO(new Task(0, 0, "differName", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android")));
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "服务器错误"), result);
    }

    @Test
    public void testGetHallTasks() {
        when(taskMapper.selectAll()).thenReturn(Arrays.<Task>asList(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android"), new Task(0, 0, "name", "intro", 0, 1, "type", BigDecimal.valueOf(0.0), "Android")));

        PageInfo<TaskVO> result = taskServiceIml.getHallTasks(1, 3);
        PageHelper.startPage(1, 3);
        List<Task> tasks=Arrays.<Task>asList(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android"),new Task(0, 0, "name", "intro", 0, 1, "type", BigDecimal.valueOf(0.0), "Android"));
        List<Task> tasksRecruiting=new ArrayList<>();
        int len=tasks.size();
        for(int i=0;i<len;i++){
            if(!tasks.get(i).getWorkerCnt().equals(tasks.get(i).getMaxWorkers()))
                tasksRecruiting.add(tasks.get(i));
        }
        PageInfo<Task> po = new PageInfo<>(tasksRecruiting);
        PageInfo<TaskVO> pageTasks = PageInfoUtil.convert(po, TaskVO.class);
        assertEquals(String.valueOf(pageTasks), String.valueOf(result));//转化为String再比较
    }

    @Test
    public void testGetTaskDetail() {
        when(taskMapper.selectByPrimaryKey(anyInt())).thenReturn(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android"));

        TaskVO result = taskServiceIml.getTaskDetail(0, 0);
        assertEquals(new TaskVO(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android")), result);

        when(taskMapper.selectByPrimaryKey(anyInt())).thenReturn(null);
        result = taskServiceIml.getTaskDetail(0, 0);
        assertEquals(new TaskVO(), result);

    }

    @Test
    public void testGetAllTasks() {
        when(taskMapper.selectAll()).thenReturn(Arrays.<Task>asList(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android")));

        PageInfo<TaskVO> result = taskServiceIml.getAllTasks(1, 3);
        PageHelper.startPage(1, 3);
        List<Task> tasks=Arrays.<Task>asList(new Task(0, 0, "name", "intro", 0, 0, "type", BigDecimal.valueOf(0.0), "Android"));
        PageInfo<Task> po = new PageInfo<>(tasks);
        PageInfo<TaskVO> pageTasks = PageInfoUtil.convert(po, TaskVO.class);
        assertEquals(String.valueOf(pageTasks), String.valueOf(result));
    }

    @Test
    public void testGetRecommendedTasks() throws Exception {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task(1, 0, "task1", "intro1", 5, 1, "FunctionTest", BigDecimal.valueOf(4.0), "Android");
        Task task2 = new Task(2, 0, "task2", "intro2", 10, 2, "PerformanceTest", BigDecimal.valueOf(5.0), "Linux");
        Task task3 = new Task(3, 0, "task3", "intro3", 9, 5, "StabilityTest", BigDecimal.valueOf(4.0), "Windows");
        task1.setEndTime(new Date(5));
        task2.setEndTime(new Date(5));
        task3.setEndTime(new Date(5));
        tasks.add(task2);
        tasks.add(task1);
        tasks.add(task3);
        when(taskMapper.selectAll()).thenReturn(tasks);

        List<TaskrecStrategy> taskrecStrategies1 = new ArrayList<>();
        List<TaskrecStrategy> taskrecStrategies2 = new ArrayList<>();
        List<TaskrecStrategy> taskrecStrategies3 = new ArrayList<>();
        TaskrecStrategy taskrecStrategy1 = new TaskrecStrategy(BigDecimal.valueOf(0.25), BigDecimal.valueOf(0.25), BigDecimal.valueOf(0), BigDecimal.valueOf(0.50));
        taskrecStrategy1.setUid(1);
        TaskrecStrategy taskrecStrategy2 = new TaskrecStrategy(BigDecimal.valueOf(0.3), BigDecimal.valueOf(0.2), BigDecimal.valueOf(0.25), BigDecimal.valueOf(0.25));
        taskrecStrategy2.setUid(0);
        TaskrecStrategy taskrecStrategy3 = new TaskrecStrategy(BigDecimal.valueOf(0.90), BigDecimal.valueOf(0.00), BigDecimal.valueOf(0), BigDecimal.valueOf(0.10));
        taskrecStrategy3.setUid(1);
        TaskrecStrategy taskrecStrategy4 = new TaskrecStrategy(BigDecimal.valueOf(0.25), BigDecimal.valueOf(0.50), BigDecimal.valueOf(0), BigDecimal.valueOf(0.25));
        taskrecStrategy4.setUid(1);
        taskrecStrategies1.add(taskrecStrategy1);
        taskrecStrategies1.add(taskrecStrategy2);
        taskrecStrategies2.add(taskrecStrategy2);
        taskrecStrategies2.add(taskrecStrategy3);
        taskrecStrategies3.add(taskrecStrategy2);
        taskrecStrategies3.add(taskrecStrategy4);

        when(taskOrderMapper.selectByUid(1)).thenReturn(new ArrayList<>());
        //1.不考虑活跃度，测试设备优先
        when(taskrecStrategyMapper.selectAll()).thenReturn(taskrecStrategies1);
        when(portraitMapper.selectByUid(1)).thenReturn(new Portrait(new BigDecimal(1), "1,1,1", new BigDecimal(0), "0,0,0,1,0,0"));
        PageInfo<TaskVO> result1 = taskServiceIml.getRecommendedTasks(0,1,1);

        //2.测试难度优先
        when(taskrecStrategyMapper.selectAll()).thenReturn(taskrecStrategies2);
        when(portraitMapper.selectByUid(1)).thenReturn(new Portrait(new BigDecimal(1), "1,1,1", new BigDecimal(0), "0,0,0,1,0,0"));
        PageInfo<TaskVO> result2 = taskServiceIml.getRecommendedTasks(0,1,1);
        //3.
        when(taskrecStrategyMapper.selectAll()).thenReturn(taskrecStrategies3);
        when(portraitMapper.selectByUid(1)).thenReturn(new Portrait(new BigDecimal(1), "1,1,0", new BigDecimal(0), "0,0,0,0,1,0"));
        PageInfo<TaskVO> result3 = taskServiceIml.getRecommendedTasks(0,1,1);

        List<Task> tsks1 = new ArrayList<>();
        tsks1.add(task2);
        PageInfo<Task> po = new PageInfo<>(tsks1);
        PageInfo<TaskVO> expected = PageInfoUtil.convert(po, TaskVO.class);
        String expectedStr="";
        for(int i=0;i<expected.getList().size();i++){
            expectedStr+=expected.getList().get(i).toString();
        }
        String resultStr="";
        for(int i=0;i<result1.getList().size();i++){
            resultStr+=result1.getList().get(i).toString();
        }
        Assert.assertEquals(expectedStr, resultStr);

        tsks1 = new ArrayList<>();
        tsks1.add(task1);
        expectedStr="";
        resultStr="";
        po = new PageInfo<>(tsks1);
        expected = PageInfoUtil.convert(po, TaskVO.class);
        for(int i=0;i<expected.getList().size();i++){
            expectedStr+=expected.getList().get(i).toString();
        }
        for(int i=0;i<result2.getList().size();i++){
            resultStr+=result2.getList().get(i).toString();
        }
        Assert.assertEquals(expectedStr, resultStr);

        resultStr="";
        for(int i=0;i<result3.getList().size();i++){
            resultStr+=result3.getList().get(i).toString();
        }
        Assert.assertEquals(expectedStr, resultStr);
    }
}
