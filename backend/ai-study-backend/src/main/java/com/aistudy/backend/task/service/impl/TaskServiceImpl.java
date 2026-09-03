package com.aistudy.backend.task.service.impl;

import com.aistudy.backend.plan.mapper.StudyPlanMapper;
import com.aistudy.backend.task.entity.StudyTask;
import com.aistudy.backend.task.mapper.TaskMapper;
import com.aistudy.backend.task.service.TaskService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;

    private final StudyPlanMapper studyPlanMapper;


    public TaskServiceImpl(
            TaskMapper taskMapper,
            StudyPlanMapper studyPlanMapper
    ) {

        this.taskMapper = taskMapper;

        this.studyPlanMapper = studyPlanMapper;
    }


    /**
     * 新增任务
     */
    @Override
    public int add(
            StudyTask task
    ) {

        return taskMapper.insert(task);
    }


    /**
     * 查询计划下任务
     */
    @Override
    public List<StudyTask> list(
            Integer userId,
            Integer planId
    ) {

        return taskMapper.list(
                userId,
                planId
        );
    }


    /**
     * 修改任务完成状态
     */
    @Override
    public int updateStatus(
            Integer id,
            Integer userId,
            Integer status
    ) {

        /*
         * 先找到当前任务
         */
        StudyTask currentTask = null;

        List<StudyTask> allTasks;


        /*
         * 查询当前用户的所有计划
         *
         * 找到这个任务属于哪个计划
         */
        List<com.aistudy.backend.plan.entity.StudyPlan> plans =
                studyPlanMapper.list(userId);


        for (
                com.aistudy.backend.plan.entity.StudyPlan plan
                : plans
        ) {

            allTasks =
                    taskMapper.list(
                            userId,
                            plan.getId()
                    );


            for (StudyTask task : allTasks) {

                if (
                        task.getId() != null
                        &&
                        task.getId().equals(id)
                ) {

                    currentTask = task;

                    break;
                }
            }


            if (currentTask != null) {

                break;
            }
        }


        /*
         * 找不到任务
         */
        if (currentTask == null) {

            return 0;
        }


        /*
         * 修改任务状态
         */
        int result =
                taskMapper.updateStatus(
                        id,
                        userId,
                        status
                );


        /*
         * 修改成功
         */
        if (result > 0) {

            Integer planId =
                    currentTask.getPlanId();


            /*
             * 查询这个计划还有多少
             * 未完成任务
             */
            int unfinished =
                    taskMapper.countUnfinished(
                            userId,
                            planId
                    );


            /*
             * =========================
             * 所有任务都完成
             * =========================
             */
            if (unfinished == 0) {

                studyPlanMapper.updateStatus(
                        planId,
                        userId,
                        2
                );

            }


            /*
             * =========================
             * 还有任务没有完成
             * =========================
             */
            else {

                studyPlanMapper.updateStatus(
                        planId,
                        userId,
                        1
                );

            }
        }


        return result;
    }


    /**
     * 删除任务
     */
    @Override
    public int delete(
            Integer id,
            Integer userId
    ) {

        return taskMapper.delete(
                id,
                userId
        );
    }
}