package com.aistudy.backend.task.converter;


import com.aistudy.backend.task.dto.TaskAddDTO;
import com.aistudy.backend.task.entity.StudyTask;
import com.aistudy.backend.task.vo.StudyTaskVO;



public class TaskConverter {



    public static StudyTask convert(
            TaskAddDTO dto
    ){


        StudyTask task =
                new StudyTask();


        task.setPlanId(
                dto.getPlanId()
        );


        task.setTitle(
                dto.getTitle()
        );


        task.setContent(
                dto.getContent()
        );


        task.setTaskDate(
                dto.getTaskDate()
        );


        return task;

    }






    public static StudyTaskVO convertVO(
            StudyTask task
    ){


        StudyTaskVO vo =
                new StudyTaskVO();


        vo.setId(
                task.getId()
        );


        vo.setPlanId(
                task.getPlanId()
        );


        vo.setTitle(
                task.getTitle()
        );


        vo.setContent(
                task.getContent()
        );


        vo.setTaskDate(
                task.getTaskDate()
        );


        vo.setStatus(
                task.getStatus()
        );


        vo.setCreateTime(
                task.getCreateTime()
        );


        return vo;

    }



}