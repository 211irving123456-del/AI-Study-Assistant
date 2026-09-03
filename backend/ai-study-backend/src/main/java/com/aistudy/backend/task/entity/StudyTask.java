package com.aistudy.backend.task.entity;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class StudyTask {


    private Integer id;


    private Integer userId;


    private Integer planId;


    private String title;


    private String content;


    private LocalDate taskDate;


    /**
     * 0 未完成
     * 1 已完成
     */
    private Integer status;


    private LocalDateTime createTime;


}