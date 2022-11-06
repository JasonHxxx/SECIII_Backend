package team.software.collect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import team.software.collect.po.user.UserInfo;
import team.software.collect.service.taskorder.TaskOrderService;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskOrderVO;
import team.software.collect.vo.task.TaskVO;
import team.software.collect.vo.user.UserInfoVO;

import javax.annotation.Resource;
import java.util.List;

@Api(value="订单controller", tags = "3.订单接口")
@RestController
@RequestMapping("/task_order")
public class TaskOrderController {
    @Resource
    private TaskOrderService taskOrderService;

    @PostMapping("/confirmTask")
    @ApiOperation(value="众包工人：接受一个任务")
    public ResultVO<TaskOrderVO> createCourseOrder(@RequestParam Integer uid, @RequestParam Integer tid){
        return taskOrderService.confirmTask(uid,tid);
    }

    @GetMapping("/worker_user/unfinished/{uid}")
    @ApiOperation(value="众包工人：查看正在执行的任务列表")
    public List<TaskVO> getUnfinishedTasks(@PathVariable Integer uid) {
        return taskOrderService.getUnfinishedTasks(uid);
    }

    @GetMapping("/worker_user/finished/{uid}")
    @ApiOperation(value="众包工人：查看已经完成的任务列表")
    public List<TaskVO> getFinishedTasks(@PathVariable Integer uid) {
        return taskOrderService.getFinishedTasks(uid);
    }

    @GetMapping("/{tid}")
    @ApiOperation(value="查看参与一个任务的所有众包工人")
    public List<UserInfoVO> getUserByTid(@PathVariable Integer tid) {
        return taskOrderService.getUserByTid(tid);
    }
}
