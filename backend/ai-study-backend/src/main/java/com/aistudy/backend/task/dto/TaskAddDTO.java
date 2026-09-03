package com.aistudy.backend.task.dto;


import lombok.Data;


import java.time.LocalDate;



@Data
public class TaskAddDTO {


    private Integer planId;


    private String title;


    private String content;


    private LocalDate taskDate;


}