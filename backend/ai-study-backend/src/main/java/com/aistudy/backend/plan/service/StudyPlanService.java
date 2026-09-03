package com.aistudy.backend.plan.service;


import com.aistudy.backend.plan.entity.StudyPlan;

import java.util.List;


public interface StudyPlanService {


    /**
     * 添加学习计划
     */
    int add(
            StudyPlan plan
    );



    /**
     * 查询用户计划
     */
    List<StudyPlan> list(
            Integer userId
    );



    /**
     * 删除计划
     */
    int delete(
            Integer id,
            Integer userId
    );



    /**
     * 修改计划
     */
    int update(
            StudyPlan plan
    );


        /**
         * 修改学习计划状态
         */
        int updateStatus(
                Integer id,
                Integer userId,
                Integer status
        );
}