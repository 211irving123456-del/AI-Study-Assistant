package com.aistudy.backend.task.vo;


import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;



@Data
public class StudyTaskVO {


    private Integer id;


    private Integer planId;


    private String title;


    private String content;


    private LocalDate taskDate;


    private Integer status;


    private LocalDateTime createTime;


}