package com.aistudy.backend.plan.service.impl;


import com.aistudy.backend.plan.entity.StudyPlan;
import com.aistudy.backend.plan.mapper.StudyPlanMapper;
import com.aistudy.backend.plan.service.StudyPlanService;

import org.springframework.stereotype.Service;


import java.util.List;



@Service
public class StudyPlanServiceImpl implements StudyPlanService {



    private final StudyPlanMapper studyPlanMapper;



    public StudyPlanServiceImpl(
            StudyPlanMapper studyPlanMapper
    ){

        this.studyPlanMapper = studyPlanMapper;

    }




    @Override
    public int add(
            StudyPlan plan
    ){

        return studyPlanMapper.insert(
                plan
        );

    }





    @Override
    public List<StudyPlan> list(
            Integer userId
    ){

        return studyPlanMapper.list(
                userId
        );

    }





    @Override
    public int delete(
            Integer id,
            Integer userId
    ){

        return studyPlanMapper.delete(
                id,
                userId
        );

    }





    @Override
    public int update(
            StudyPlan plan
    ){

        return studyPlanMapper.update(
                plan
        );

    }
        @Override
        public int updateStatus(
                Integer id,
                Integer userId,
                Integer status
        ) {

        return studyPlanMapper.updateStatus(
                id,
                userId,
                status
        );
        }

}