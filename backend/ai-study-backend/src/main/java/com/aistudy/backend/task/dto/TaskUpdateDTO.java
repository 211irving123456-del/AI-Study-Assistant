package com.aistudy.backend.task.dto;


import lombok.Data;

import java.time.LocalDate;


@Data
public class TaskUpdateDTO {


    private Integer id;


    private String title;


    private String content;


    private LocalDate taskDate;


}