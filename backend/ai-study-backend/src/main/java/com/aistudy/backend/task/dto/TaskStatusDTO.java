package com.aistudy.backend.task.dto;


import lombok.Data;


@Data
public class TaskStatusDTO {


    private Integer id;


    /**
     * 0 未完成
     * 1 已完成
     */
    private Integer status;


}