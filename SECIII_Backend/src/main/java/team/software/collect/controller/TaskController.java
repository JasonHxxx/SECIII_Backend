package team.software.collect.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import team.software.collect.service.task.TaskService;
import team.software.collect.util.Constant;
import team.software.collect.vo.ResultVO;
import team.software.collect.vo.task.TaskVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value="任务controller",tags={"任务发布、获取接口"})
@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskService taskService;

    @GetMapping("/post_user/{uid}")
    @ApiOperation(value="发包方：获取已发布任务",tags={"<tag01>--获取任务列表"},notes="get请求")
    public List<TaskVO> getPostedTasks(@PathVariable Integer uid) {
        return taskService.getPostedTasks(uid);
    }

    @PostMapping("/create")
    @ApiOperation(value="发包方：发布任务", notes="post请求")
    public ResultVO<TaskVO> postTask(@RequestBody TaskVO taskVO) {
        return taskService.postTask(taskVO);
    }

    @GetMapping("/all/{page}")
    @ApiOperation(value="用户：获取任务大厅的任务（正在招募中的任务）", tags={"<tag01>--获取任务列表"}, notes="get请求，根据page请求任务大厅一定长度的任务列表")
    @ApiImplicitParam(name = "page", value = "页码", paramType = "path", required = true)
    public PageInfo<TaskVO> getHallTasks(@PathVariable Integer page) {
        return taskService.getHallTasks(page, Constant.COURSE_PAGE_SIZE);
    }






}
