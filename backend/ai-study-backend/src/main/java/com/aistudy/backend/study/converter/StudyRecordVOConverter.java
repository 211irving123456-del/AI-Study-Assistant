package com.aistudy.backend.study.converter;


import com.aistudy.backend.study.entity.StudyRecord;
import com.aistudy.backend.study.vo.StudyRecordVO;


public class StudyRecordVOConverter {


    public static StudyRecordVO convert(
            StudyRecord record
    ){


        StudyRecordVO vo =
                new StudyRecordVO();


        vo.setId(
                record.getId()
        );

        vo.setPlanId(record.getPlanId());


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
