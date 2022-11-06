package team.software.collect.serviceimpl.taskorder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.software.collect.mapperservice.task.TaskMapper;
import team.software.collect.mapperservice.task.TaskOrderMapper;
import team.software.collect.mapperservice.user.PortraitMapper;
import team.software.collect.po.task.Task;
import team.software.collect.po.task.TaskOrder;
import team.software.collect.po.user.Portrait;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskOrderVO;
import team.software.collect.vo.task.TaskVO;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

public class TaskOrderServiceImplTest {
    @Mock
    TaskOrderMapper taskOrderMapper;
    @Mock
    TaskMapper taskMapper;
    @Mock
    PortraitMapper portraitMapper;
    @InjectMocks
    TaskOrderServiceImpl taskOrderServiceImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConfirmTask() throws Exception {

        when(taskOrderMapper.insert(any())).thenReturn(1);
        List<TaskOrder> lst1 = new ArrayList<>();
        lst1.add(new TaskOrder(2, 1));
        lst1.add(new TaskOrder(2, 2));
        lst1.add(new TaskOrder(2, 3));
        when(taskOrderMapper.selectTasksByUserId(2)).thenReturn(lst1);

        List<TaskOrder> lst2 = new ArrayList<>();
        lst2.add(new TaskOrder(4, 1));
        when(taskOrderMapper.selectTasksByUserId(4)).thenReturn(lst2);


        when(taskMapper.selectByPrimaryKey(2)).thenReturn(new Task(2, 2, "task2", "intro2", 200, 1,  "B"));
        when(taskMapper.selectByPrimaryKey(3)).thenReturn(new Task(3, 1, "task3", "intro", 1, 1, "A"));
        when(taskMapper.updateByPrimaryKey(any())).thenReturn(1);

        when(portraitMapper.selectByUid(0)).thenReturn(new Portrait(0,new BigDecimal(1),"1,1,0",new BigDecimal(1),"1,0,0,0,0,0",2));
        when(portraitMapper.selectByUid(1)).thenReturn(new Portrait(1,new BigDecimal(1),"1,1,0",new BigDecimal(1),"1,0,0,0,0,0",2));
        when(portraitMapper.selectByUid(2)).thenReturn(new Portrait(2,new BigDecimal(1),"1,1,0",new BigDecimal(1),"1,0,0,0,0,0",2));
        when(portraitMapper.selectByUid(3)).thenReturn(new Portrait(3,new BigDecimal(1),"1,1,0",new BigDecimal(1),"1,0,0,0,0,0",2));
        when(portraitMapper.selectByUid(4)).thenReturn(new Portrait(4,new BigDecimal(1),"1,1,0",new BigDecimal(1),"1,0,0,0,0,0",2));

        when(portraitMapper.updateByPrimaryKey(any())).thenReturn(1);

        // TEST1: 已接受该任务
        ResultVO<TaskOrderVO> expected1 = new ResultVO<>(Constant.REQUEST_FAIL, "已接受该任务");
        ResultVO<TaskOrderVO> result1 = taskOrderServiceImpl.confirmTask(2,1);
        Assert.assertEquals(expected1, result1);

        // TEST2: 该任务招募人数已达上限！
        ResultVO<TaskOrderVO> expected2 = new ResultVO<>(Constant.REQUEST_FAIL, "该任务招募人数已达上限！");
        ResultVO<TaskOrderVO> result2 = taskOrderServiceImpl.confirmTask(4,3);
        Assert.assertEquals(expected2, result2);

        // TEST3: 接受任务失败，数据库错误！
        /*
        ResultVO<TaskOrderVO> expected3 = new ResultVO<>(Constant.REQUEST_FAIL, "接受任务失败，数据库错误！");
        ResultVO<TaskOrderVO> result3 = taskOrderServiceImpl.confirmTask(Integer.valueOf(0), Integer.valueOf(0));
        Assert.assertEquals(expected3, result3);
         */

        // TEST4: 接受任务成功
        TaskOrder taskOrder = new TaskOrder(4, 2);
        ResultVO<TaskOrderVO> expected4 = new ResultVO<>(Constant.REQUEST_SUCCESS, "接受任务成功", new TaskOrderVO(taskOrder));
        ResultVO<TaskOrderVO> result4 = taskOrderServiceImpl.confirmTask(4,2);
        Assert.assertEquals(expected4.toString(), result4.toString());
    }

    @Test
    public void testGetUnfinishedTasks() throws Exception {
        List<TaskOrder> lst1 = new ArrayList<>();
        TaskOrder taskOrder1 = new TaskOrder(2, 1);
        taskOrder1.setFinished(1);
        lst1.add(taskOrder1);
        lst1.add(new TaskOrder(2, 2));
        lst1.add(new TaskOrder(2, 3));
        when(taskOrderMapper.selectTasksByUserId(anyInt())).thenReturn(lst1);

        Task task1 = new Task(1,1,"task1", "intro1", 100, 2, "A");
        when(taskMapper.selectByPrimaryKey(1)).thenReturn(task1);

        Task task2 = new Task(2, 2, "task2", "intro2", 200, 1, "B");
        when(taskMapper.selectByPrimaryKey(2)).thenReturn(task2);

        Task task3 = new Task(3, 1, "task3", "intro3", 1, 1, "A");
        when(taskMapper.selectByPrimaryKey(3)).thenReturn(task3);

        List<TaskVO> expected = new ArrayList<>();
        expected.add(new TaskVO(new Task(2, 2, "task2", "intro2", 200, 1, "B")));
        expected.add(new TaskVO(new Task(3, 1, "task3", "intro3", 1, 1, "A")));
        List<TaskVO> result = taskOrderServiceImpl.getUnfinishedTasks(2);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetFinishedTasks() throws Exception {
        List<TaskOrder> lst1 = new ArrayList<>();
        TaskOrder taskOrder1 = new TaskOrder(2, 1);
        taskOrder1.setFinished(1);
        lst1.add(taskOrder1);
        lst1.add(new TaskOrder(2, 2));
        lst1.add(new TaskOrder(2, 3));
        when(taskOrderMapper.selectTasksByUserId(anyInt())).thenReturn(lst1);

        Task task1 = new Task(1,1,"task1", "intro1", 100, 2, "A");
        when(taskMapper.selectByPrimaryKey(1)).thenReturn(task1);

        Task task2 = new Task(2, 2, "task2", "intro2", 200, 1, "B");
        when(taskMapper.selectByPrimaryKey(2)).thenReturn(task2);

        Task task3 = new Task(3, 1, "task3", "intro3", 1, 1, "A");
        when(taskMapper.selectByPrimaryKey(3)).thenReturn(task3);

        List<TaskVO> expected = new ArrayList<>();
        expected.add(new TaskVO(new Task(1, 1, "task1", "intro1", 100, 2, "A")));
        List<TaskVO> result = taskOrderServiceImpl.getFinishedTasks(2);
        Assert.assertEquals(expected.toString(), result.toString());
    }
}