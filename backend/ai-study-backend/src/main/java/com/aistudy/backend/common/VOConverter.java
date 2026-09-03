package com.aistudy.backend.common;


import com.aistudy.backend.user.entity.User;
import com.aistudy.backend.study.entity.StudyRecord;
import com.aistudy.backend.user.vo.UserVO;
import com.aistudy.backend.study.vo.StudyRecordVO;



public class VOConverter {



    public static UserVO convert(
            User user
    ){


        UserVO vo =
                new UserVO();



        vo.setId(
                user.getId()
        );


        vo.setUsername(
                user.getUsername()
        );


        vo.setEmail(
                user.getEmail()
        );


        return vo;

    }





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