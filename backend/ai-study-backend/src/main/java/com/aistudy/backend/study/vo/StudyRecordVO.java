package com.aistudy.backend.study.vo;


import java.time.LocalDateTime;


public class StudyRecordVO {


    private Integer id;


    private Integer planId;


    private String title;


    private String content;


    private Integer duration;


    private LocalDateTime createTime;




    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id=id;
    }



    public Integer getPlanId() {
        return planId;
    }


    public void setPlanId(Integer planId) {
        this.planId=planId;
    }





    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title=title;
    }




    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content=content;
    }





    public Integer getDuration() {
        return duration;
    }


    public void setDuration(Integer duration) {
        this.duration=duration;
    }




    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime=createTime;
    }


}