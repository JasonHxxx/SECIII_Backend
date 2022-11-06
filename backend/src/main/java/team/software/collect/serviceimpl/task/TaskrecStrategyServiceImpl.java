package team.software.collect.serviceimpl.task;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import team.software.collect.mapperservice.task.TaskrecStrategyMapper;
import team.software.collect.po.task.TaskrecStrategy;
import team.software.collect.service.task.TaskrecStrategyService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskrecStrategyVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskrecStrategyServiceImpl implements TaskrecStrategyService {
    @Resource
    private TaskrecStrategyMapper taskrecStrategyMapper;

    @Override
    public ResultVO<TaskrecStrategyVO> setRule(Integer sid) {
        //先全部将uid设置为1，未使用状态
        List<TaskrecStrategy> taskrecStrategyList=taskrecStrategyMapper.selectAll();
        for(TaskrecStrategy taskrecStrategy:taskrecStrategyList){
            taskrecStrategy.setUid(0);
            taskrecStrategyMapper.updateByPrimaryKey(taskrecStrategy);
        }
        //设置管理员设的策略
        TaskrecStrategy taskrecStrategy=taskrecStrategyMapper.selectByPrimaryKey(sid);
        taskrecStrategy.setUid(1);
        taskrecStrategyMapper.updateByPrimaryKey(taskrecStrategy);
        return new ResultVO<>(Constant.REQUEST_SUCCESS,"策略设置成功",new TaskrecStrategyVO(taskrecStrategy));
    }

    @Override
    public ResultVO<TaskrecStrategyVO> addRule(TaskrecStrategyVO taskrecStrategyVO) {
        TaskrecStrategy taskrecStrategy=new TaskrecStrategy(taskrecStrategyVO);
        taskrecStrategy.setUid(0);
        if(taskrecStrategyMapper.insert(taskrecStrategy)>0)
            return new ResultVO<>(Constant.REQUEST_SUCCESS,"添加规则成功",new TaskrecStrategyVO(taskrecStrategy));
        return new ResultVO<>(Constant.REQUEST_FAIL,"数据库插入错误，添加规则失败！");
    }

    @Override
    public ResultVO<TaskrecStrategyVO>  modifyRule(TaskrecStrategyVO taskrecStrategyVO){
        TaskrecStrategy taskrecStrategy=new TaskrecStrategy(taskrecStrategyVO);
        if(taskrecStrategyMapper.updateByPrimaryKey(taskrecStrategy)>0)
            return new ResultVO<>(Constant.REQUEST_SUCCESS,"修改规则成功！",new TaskrecStrategyVO(taskrecStrategy));
        return new ResultVO<>(Constant.REQUEST_FAIL,"数据库插入错误！");
    }

    @Override
    public ResultVO<TaskrecStrategyVO> deleteRule(Integer sid) {
        if(taskrecStrategyMapper.deleteByPrimaryKey(sid)>0)
            return new ResultVO<>(Constant.REQUEST_SUCCESS,"删除规则成功");
        return new ResultVO<>(Constant.REQUEST_FAIL,"删除规则失败");
    }

    @Override
    public List<TaskrecStrategyVO> getRules(Integer uid) {
        List<TaskrecStrategyVO> re=new ArrayList<>();
        List<TaskrecStrategy> taskrecStrategyList=taskrecStrategyMapper.selectAll();
        for(TaskrecStrategy taskrecStrategy:taskrecStrategyList){
            re.add(new TaskrecStrategyVO(taskrecStrategy));
        }
        return re;
    }

    @Override
    public ResultVO<TaskrecStrategyVO> getRuleInUse(){
        List<TaskrecStrategy> taskrecStrategyList=taskrecStrategyMapper.selectAll();
        for(TaskrecStrategy taskrecStrategy:taskrecStrategyList){
            if(taskrecStrategy.getUid().equals(1))
                return new ResultVO<>(Constant.REQUEST_SUCCESS,"已返回正在使用的规则！", new TaskrecStrategyVO(taskrecStrategy));
        }

        return new ResultVO<>(Constant.REQUEST_FAIL,"没有设置使用的规则！");
    }
}
