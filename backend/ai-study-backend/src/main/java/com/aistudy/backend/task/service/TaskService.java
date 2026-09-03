package com.aistudy.backend.task.service;

import com.aistudy.backend.task.entity.StudyTask;

import java.util.List;

public interface TaskService {

    /**
     * 新增任务
     */
    int add(StudyTask task);


    /**
     * 查询某个计划下的所有任务
     */
    List<StudyTask> list(
            Integer userId,
            Integer planId
    );


    /**
     * 修改任务完成状态
     */
    int updateStatus(
            Integer id,
            Integer userId,
            Integer status
    );


    /**
     * 删除任务
     */
    int delete(
            Integer id,
            Integer userId
    );
}