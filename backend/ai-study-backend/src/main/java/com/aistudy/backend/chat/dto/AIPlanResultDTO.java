package com.aistudy.backend.chat.dto;

import lombok.Data;

import java.util.List;

@Data
public class AIPlanResultDTO {

    /**
     * 学习计划标题
     */
    private String title;

    /**
     * 学习计划详细内容
     */
    private String content;

    /**
     * 学习计划开始日期
     */
    private String startDate;

    /**
     * 学习计划结束日期
     */
    private String endDate;

    /**
     * 目标学习小时数
     */
    private Integer targetHours;

    /**
     * 每日学习任务
     */
    private List<TaskDTO> tasks;


    @Data
    public static class TaskDTO {

        /**
         * 任务标题
         */
        private String title;

        /**
         * 任务具体内容
         */
        private String content;

        /**
         * 任务日期
         */
        private String taskDate;
    }
}