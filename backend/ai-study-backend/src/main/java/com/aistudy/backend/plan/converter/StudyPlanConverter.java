package com.aistudy.backend.plan.converter;


import com.aistudy.backend.plan.dto.StudyPlanAddDTO;
import com.aistudy.backend.plan.dto.StudyPlanUpdateDTO;
import com.aistudy.backend.plan.entity.StudyPlan;
import com.aistudy.backend.plan.vo.StudyPlanVO;



public class StudyPlanConverter {


    /**
     * AddDTO -> Entity
     */
    public static StudyPlan convert(
            StudyPlanAddDTO dto
    ){

        StudyPlan plan =
                new StudyPlan();


        plan.setTitle(
                dto.getTitle()
        );


        plan.setContent(
                dto.getContent()
        );


        plan.setStartDate(
                dto.getStartDate()
        );


        plan.setEndDate(
                dto.getEndDate()
        );


        plan.setTargetHours(
                dto.getTargetHours()
        );


        return plan;

    }





    /**
     * UpdateDTO -> Entity
     */
    public static StudyPlan convert(
            StudyPlanUpdateDTO dto
    ){

        StudyPlan plan =
                new StudyPlan();


        plan.setId(
                dto.getId()
        );


        plan.setTitle(
                dto.getTitle()
        );


        plan.setContent(
                dto.getContent()
        );


        plan.setStartDate(
                dto.getStartDate()
        );


        plan.setEndDate(
                dto.getEndDate()
        );


        plan.setTargetHours(
                dto.getTargetHours()
        );


        plan.setStatus(
                dto.getStatus()
        );


        return plan;

    }





    /**
     * Entity -> VO
     */
    public static StudyPlanVO convertVO(
            StudyPlan plan
    ){

        StudyPlanVO vo =
                new StudyPlanVO();


        vo.setId(
                plan.getId()
        );


        vo.setTitle(
                plan.getTitle()
        );


        vo.setContent(
                plan.getContent()
        );


        vo.setStartDate(
                plan.getStartDate()
        );


        vo.setEndDate(
                plan.getEndDate()
        );


        vo.setTargetHours(
                plan.getTargetHours()
        );


        vo.setStatus(
                plan.getStatus()
        );


        vo.setCreateTime(
                plan.getCreateTime()
        );


        return vo;

    }


}