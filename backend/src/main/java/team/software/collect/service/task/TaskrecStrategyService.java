package team.software.collect.service.task;

import org.springframework.web.bind.annotation.RequestParam;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskrecStrategyVO;

import java.util.List;

public interface TaskrecStrategyService {
    ResultVO<TaskrecStrategyVO> setRule(Integer sid);
    ResultVO<TaskrecStrategyVO>  addRule(TaskrecStrategyVO taskrecStrategyVO);
    ResultVO<TaskrecStrategyVO>  modifyRule(TaskrecStrategyVO taskrecStrategyVO);
    ResultVO<TaskrecStrategyVO>  deleteRule(Integer sid);
    List<TaskrecStrategyVO> getRules(Integer uid);
    ResultVO<TaskrecStrategyVO> getRuleInUse();
}
