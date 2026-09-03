package com.aistudy.backend.plan.entity;


import java.time.LocalDate;
import java.time.LocalDateTime;



public class StudyPlan {


    private Integer id;


    private Integer userId;


    private String title;


    private String content;


    private LocalDate startDate;


    private LocalDate endDate;


    private Integer targetHours;


    /**
     * 0 未开始
     * 1 进行中
     * 2 已完成
     */
    private Integer status;


    private LocalDateTime createTime;



    public Integer getId() {

        return id;

    }


    public void setId(Integer id) {

        this.id = id;

    }



    public Integer getUserId() {

        return userId;

    }


    public void setUserId(Integer userId) {

        this.userId = userId;

    }



    public String getTitle() {

        return title;

    }


    public void setTitle(String title) {

        this.title = title;

    }



    public String getContent() {

        return content;

    }


    public void setContent(String content) {

        this.content = content;

    }



    public LocalDate getStartDate() {

        return startDate;

    }


    public void setStartDate(LocalDate startDate) {

        this.startDate = startDate;

    }



    public LocalDate getEndDate() {

        return endDate;

    }


    public void setEndDate(LocalDate endDate) {

        this.endDate = endDate;

    }



    public Integer getTargetHours() {

        return targetHours;

    }


    public void setTargetHours(Integer targetHours) {

        this.targetHours = targetHours;

    }



    public Integer getStatus() {

        return status;

    }


    public void setStatus(Integer status) {

        this.status = status;

    }



    public LocalDateTime getCreateTime() {

        return createTime;

    }


    public void setCreateTime(LocalDateTime createTime) {

        this.createTime = createTime;

    }


}