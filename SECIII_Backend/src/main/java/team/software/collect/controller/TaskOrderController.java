package team.software.collect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import team.software.collect.service.taskorder.TaskOrderService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskOrderVO;
import team.software.collect.vo.task.TaskVO;

import javax.annotation.Resource;
import java.util.List;

@Api(value="订单controller", tags = "3.订单接口")
@RestController
@RequestMapping("/task_order")
public class TaskOrderController {
    @Resource
    private TaskOrderService taskOrderService;

    @PostMapping("/confirmTask")
    @ApiOperation(value="众包工人：选择一个任务进行测试")
    public ResultVO<TaskOrderVO> createCourseOrder(@RequestParam Integer uid, @RequestParam Integer tid){
        return taskOrderService.confirmTask(uid,tid);
    }

    @GetMapping("/worker_user/{uid}")
    @ApiOperation(value="众包工人：查看已经完成的任务列表")
    public List<TaskVO> getFinishedTasks(@PathVariable Integer uid) {
        return taskOrderService.getFinishedTasks(uid);
    }
}
