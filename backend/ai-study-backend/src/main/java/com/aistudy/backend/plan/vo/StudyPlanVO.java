package com.aistudy.backend.plan.vo;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class StudyPlanVO {


    private Integer id;


    private String title;


    private String content;


    private LocalDate startDate;


    private LocalDate endDate;


    private Integer targetHours;


    private Integer status;


    private LocalDateTime createTime;

        /**
     * 已学习分钟
     */
    private Integer completedMinutes;


    /**
     * 完成百分比
     */
    private Double progress;


    /**
     * 剩余分钟
     */
    private Integer remainMinutes;
}