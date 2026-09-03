package com.aistudy.backend.study.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class StudyRecordUpdateDTO {


    @NotNull(message="记录id不能为空")
    private Integer id;



    @NotBlank(message="学习标题不能为空")
    private String title;



    @NotBlank(message="学习内容不能为空")
    private String content;



    @NotNull(message="学习时长不能为空")
    private Integer duration;




    public Integer getId(){
        return id;
    }


    public void setId(Integer id){
        this.id=id;
    }



    public String getTitle(){
        return title;
    }


    public void setTitle(String title){
        this.title=title;
    }



    public String getContent(){
        return content;
    }


    public void setContent(String content){
        this.content=content;
    }



    public Integer getDuration(){
        return duration;
    }


    public void setDuration(Integer duration){
        this.duration=duration;
    }

}