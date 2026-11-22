package com.aistudy.backend.task.controller;


import com.aistudy.backend.common.Result;
import com.aistudy.backend.common.UserContext;

import com.aistudy.backend.task.converter.TaskConverter;
import com.aistudy.backend.task.dto.TaskAddDTO;
import com.aistudy.backend.task.dto.TaskStatusDTO;
import com.aistudy.backend.task.entity.StudyTask;
import com.aistudy.backend.task.service.TaskService;
import com.aistudy.backend.task.vo.StudyTaskVO;

import com.aistudy.backend.user.entity.User;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;



import java.util.List;



@RestController
@RequestMapping("/task")
public class TaskController {


    private final TaskService taskService;



    public TaskController(
            TaskService taskService
    ){

        this.taskService = taskService;

    }





    /**
     * 添加任务
     */
    @PostMapping("/add")
    public Result<StudyTaskVO> add(
            @Valid
            @RequestBody TaskAddDTO dto
    ){


        User user =
                UserContext.getUser();



        StudyTask task =
                TaskConverter.convert(
                        dto
                );



        task.setUserId(
                user.getId()
        );



        task.setStatus(
                0
        );



        int inserted = taskService.add(task);

        if (inserted != 1) {
            return Result.error(403, "无权向该学习计划添加任务，或计划不存在");
        }

        return Result.success(
                TaskConverter.convertVO(task)
        );

    }







    /**
     * 查询某计划下任务
     */
    @GetMapping("/list/{planId}")
    public Result<List<StudyTaskVO>> list(
            @PathVariable Integer planId
    ){


        User user =
                UserContext.getUser();



        List<StudyTask> list =
                taskService.list(
                        user.getId(),
                        planId
                );



        List<StudyTaskVO> voList =
                list.stream()
                .map(
                        TaskConverter::convertVO
                )
                .toList();



        return Result.success(
                voList
        );

    }








    /**
     * 修改任务完成状态
     */
    @PutMapping("/status")
    public Result<String> updateStatus(
            @Valid
            @RequestBody TaskStatusDTO dto
    ){



        User user =
                UserContext.getUser();



        int result =
                taskService.updateStatus(
                        dto.getId(),
                        user.getId(),
                        dto.getStatus()
                );



        if(result > 0){

            return Result.success(
                    "修改成功"
            );

        }



        return Result.error(
                400,
                "修改失败"
        );

    }









    /**
     * 删除任务
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(
            @PathVariable Integer id
    ){


        User user =
                UserContext.getUser();



        int result =
                taskService.delete(
                        id,
                        user.getId()
                );



        if(result > 0){

            return Result.success(
                    "删除成功"
            );

        }



        return Result.error(
                400,
                "删除失败"
        );


    }


}