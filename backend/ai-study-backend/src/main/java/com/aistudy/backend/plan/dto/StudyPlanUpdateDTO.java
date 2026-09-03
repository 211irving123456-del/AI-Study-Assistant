package com.aistudy.backend.plan.dto;


import java.time.LocalDate;



public class StudyPlanUpdateDTO {



    private Integer id;


    private String title;


    private String content;


    private LocalDate startDate;


    private LocalDate endDate;


    private Integer targetHours;


    private Integer status;




    public Integer getId() {

        return id;

    }


    public void setId(Integer id) {

        this.id = id;

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


}