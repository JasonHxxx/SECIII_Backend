package team.software.collect.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import team.software.collect.service.task.TaskrecStrategyService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskrecStrategyVO;

import javax.annotation.Resource;
import java.util.List;

@Api(value="管理员controller", tags = "6.管理员界面的一部分接口")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    TaskrecStrategyService taskrecStrategyService;

    @PostMapping("/setRule")
    @ApiOperation(value="管理员：设置推荐规则", notes = "设置规则必须调用这个接口，addRule的uid前端设置为1后端也会默认存0")
    public ResultVO<TaskrecStrategyVO>  setRule(@RequestParam Integer sid){
        return taskrecStrategyService.setRule(sid);
    }

    @PostMapping("/addRule")
    @ApiOperation(value="管理员：增加推荐规则",notes = "前端判断一下和是不是等于1，或者第四个数自动填充")
    public ResultVO<TaskrecStrategyVO>  addRule(@RequestBody TaskrecStrategyVO taskrecStrategyVO){
        return taskrecStrategyService.addRule(taskrecStrategyVO);
    }

    @PostMapping("/modifyRule")
    @ApiOperation(value="管理员：修改推荐规则",notes = "前端判断一下和是不是等于1，或者第四个数自动填充")
    public ResultVO<TaskrecStrategyVO>  modifyRule(@RequestBody TaskrecStrategyVO taskrecStrategyVO){
        return taskrecStrategyService.modifyRule(taskrecStrategyVO);
    }

    @PostMapping("/deleteRule")
    @ApiOperation(value="管理员：删除推荐规则")
    public ResultVO<TaskrecStrategyVO>  deleteRule(@RequestParam Integer sid){
        return taskrecStrategyService.deleteRule(sid);
    }

    @PostMapping("/getRules")
    @ApiOperation(value="管理员：得到推荐规则列表")
    public List<TaskrecStrategyVO> getRules(@RequestParam Integer uid){
        return taskrecStrategyService.getRules(uid);
    }

    @PostMapping("/getRuleInUse")
    @ApiOperation(value="管理员：得到正在使用的规则")
    public ResultVO<TaskrecStrategyVO> getRuleInUse(){
        return taskrecStrategyService.getRuleInUse();
    }
}
