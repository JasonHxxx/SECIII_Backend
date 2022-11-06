package team.software.collect.serviceimpl.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import team.software.collect.mapperservice.task.TaskrecStrategyMapper;
import team.software.collect.po.task.TaskrecStrategy;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskrecStrategyVO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskrecStrategyServiceImplTest {

    @Mock
    TaskrecStrategyMapper taskrecStrategyMapper;
    @InjectMocks
    TaskrecStrategyServiceImpl taskrecStrategyServiceImpl;

    @Before
    public void setUp() {
    }

    @Test
    public void testSetRule(){
        when(taskrecStrategyMapper.selectAll()).thenReturn(Arrays.asList(new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),0,"title")));
        when(taskrecStrategyMapper.updateByPrimaryKey(any())).thenReturn(1);
        when(taskrecStrategyMapper.selectByPrimaryKey(anyInt())).thenReturn(new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),0,"title"));

        ResultVO<TaskrecStrategyVO> result=taskrecStrategyServiceImpl.setRule(0);
        TaskrecStrategy taskrecStrategy=new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),1,"title");
        assertEquals(new ResultVO<>(Constant.REQUEST_SUCCESS, "策略设置成功", new TaskrecStrategyVO(taskrecStrategy)), result);

    }

    @Test
    public void testAddRule(){
        when(taskrecStrategyMapper.insert(any())).thenReturn(1);

        TaskrecStrategy taskrecStrategy=new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),0,"title");
        ResultVO<TaskrecStrategyVO> result = taskrecStrategyServiceImpl.addRule(new TaskrecStrategyVO(new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),1,"title")));
        assertEquals(new ResultVO<>(Constant.REQUEST_SUCCESS, "添加规则成功", new TaskrecStrategyVO(taskrecStrategy)), result);

        when(taskrecStrategyMapper.insert(any())).thenReturn(0);
        ResultVO<TaskrecStrategyVO> result1 = taskrecStrategyServiceImpl.addRule(new TaskrecStrategyVO(new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),1,"title")));
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "数据库插入错误，添加规则失败！"),result1);

    }

    @Test
    public void testModifyRule(){
        when(taskrecStrategyMapper.updateByPrimaryKey(any())).thenReturn(1);

        TaskrecStrategy taskrecStrategy=new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),1,"title");
        ResultVO<TaskrecStrategyVO> result = taskrecStrategyServiceImpl.modifyRule(new TaskrecStrategyVO(new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),1,"title")));
        assertEquals(new ResultVO<>(Constant.REQUEST_SUCCESS, "修改规则成功！", new TaskrecStrategyVO(taskrecStrategy)), result);

        when(taskrecStrategyMapper.updateByPrimaryKey(any())).thenReturn(0);
        ResultVO<TaskrecStrategyVO> result1 = taskrecStrategyServiceImpl.modifyRule(new TaskrecStrategyVO(new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),1,"title")));
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "数据库插入错误！"),result1);
    }

    @Test
    public void testDeleteRule(){
        when(taskrecStrategyMapper.deleteByPrimaryKey(anyInt())).thenReturn(1);

        ResultVO<TaskrecStrategyVO> result = taskrecStrategyServiceImpl.deleteRule(0);
        assertEquals(new ResultVO<>(Constant.REQUEST_SUCCESS, "删除规则成功"),result);

        when(taskrecStrategyMapper.deleteByPrimaryKey(anyInt())).thenReturn(0);

        ResultVO<TaskrecStrategyVO> result1 = taskrecStrategyServiceImpl.deleteRule(0);
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "删除规则失败"),result1);

    }

    @Test
    public void testGetRules(){
        when(taskrecStrategyMapper.selectAll()).thenReturn(Arrays.asList(new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),0,"title")));

        List<TaskrecStrategyVO> result = taskrecStrategyServiceImpl.getRules(0);
        assertEquals(Arrays.asList(new TaskrecStrategyVO(new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),0,"title"))),result);

    }

    @Test
    public void testGetRuleInUse(){
        TaskrecStrategy taskrecStrategy = new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),1,"title");
        TaskrecStrategy taskrecStrategy1 = new TaskrecStrategy(0,new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),new BigDecimal(1),0,"title");
        when(taskrecStrategyMapper.selectAll()).thenReturn(Arrays.asList(taskrecStrategy));

        ResultVO<TaskrecStrategyVO> result = taskrecStrategyServiceImpl.getRuleInUse();
        assertEquals(new ResultVO<>(Constant.REQUEST_SUCCESS, "已返回正在使用的规则！", new TaskrecStrategyVO(taskrecStrategy)), result);

        when(taskrecStrategyMapper.selectAll()).thenReturn(Arrays.asList(taskrecStrategy1));

        ResultVO<TaskrecStrategyVO> result1 = taskrecStrategyServiceImpl.getRuleInUse();
        assertEquals(new ResultVO<>(Constant.REQUEST_FAIL, "没有设置使用的规则！"), result1);

    }

}
