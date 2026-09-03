package com.aistudy.backend.study.dto;


import jakarta.validation.constraints.*;


public class StudyRecordAddDTO {



    /**
     * 所属计划
     */
    @NotNull(message="计划不能为空")
    private Integer planId;




    @NotBlank(message="学习标题不能为空")
    private String title;



    @NotBlank(message="学习内容不能为空")
    private String content;




    @NotNull(message="学习时长不能为空")
    @Min(
        value = 1,
        message="学习时长必须大于0"
    )
    private Integer duration;





    public Integer getPlanId(){
        return planId;
    }


    public void setPlanId(Integer planId){
        this.planId=planId;
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