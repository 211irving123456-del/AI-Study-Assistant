package com.aistudy.backend.study.converter;


import com.aistudy.backend.study.dto.StudyRecordAddDTO;
import com.aistudy.backend.study.dto.StudyRecordUpdateDTO;
import com.aistudy.backend.study.entity.StudyRecord;
import com.aistudy.backend.study.vo.StudyRecordVO;



public class StudyRecordConverter {



    /**
     * AddDTO -> Entity
     */
    public static StudyRecord convert(
            StudyRecordAddDTO dto
    ){


        StudyRecord record =
                new StudyRecord();



        record.setPlanId(
                dto.getPlanId()
        );



        record.setTitle(
                dto.getTitle()
        );



        record.setContent(
                dto.getContent()
        );



        record.setDuration(
                dto.getDuration()
        );



        return record;

    }






    /**
     * UpdateDTO -> Entity
     */
    public static StudyRecord convert(
            StudyRecordUpdateDTO dto
    ){


        StudyRecord record =
                new StudyRecord();



        record.setId(
                dto.getId()
        );



        record.setTitle(
                dto.getTitle()
        );



        record.setContent(
                dto.getContent()
        );



        record.setDuration(
                dto.getDuration()
        );



        return record;

    }








    /**
     * Entity -> VO
     */
    public static StudyRecordVO convert(
            StudyRecord record
    ){


        StudyRecordVO vo =
                new StudyRecordVO();



        vo.setId(
                record.getId()
        );



        vo.setTitle(
                record.getTitle()
        );



        vo.setContent(
                record.getContent()
        );



        vo.setDuration(
                record.getDuration()
        );



        vo.setCreateTime(
                record.getCreateTime()
        );



        return vo;

    }



}