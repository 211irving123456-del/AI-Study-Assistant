package com.aistudy.backend.study.service;


import com.aistudy.backend.common.PageResult;
import com.aistudy.backend.study.entity.StudyRecord;
import com.aistudy.backend.study.mapper.StudyRecordMapper;

import org.springframework.stereotype.Service;


import java.util.List;



@Service
public class StudyRecordService {



    private final StudyRecordMapper mapper;



    public StudyRecordService(
            StudyRecordMapper mapper
    ){

        this.mapper=mapper;

    }





    public int add(
            StudyRecord record
    ){

        return mapper.insert(record);

    }





    public List<StudyRecord> list(
            Integer userId
    ){

        return mapper.findByUserId(
                userId
        );

    }





    public int delete(
            Integer id,
            Integer userId
    ){

        return mapper.deleteById(
                id,
                userId
        );

    }





    public int update(
            StudyRecord record
    ){

        return mapper.update(
                record
        );

    }





    public PageResult page(
            Integer userId,
            Integer page,
            Integer size
    ){


        if(page==null){

            page=1;

        }


        if(size==null){

            size=10;

        }



        Integer start =
                (page-1)*size;



        List<StudyRecord> records =
                mapper.page(
                        userId,
                        start,
                        size
                );



        Long total =
                mapper.count(
                        userId
                );



        return new PageResult(
                total,
                records
        );


    }

}