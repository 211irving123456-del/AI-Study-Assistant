package com.aistudy.backend.plan.controller;


import com.aistudy.backend.common.Result;
import com.aistudy.backend.common.UserContext;


import com.aistudy.backend.plan.converter.StudyPlanConverter;
import com.aistudy.backend.plan.dto.StudyPlanAddDTO;
import com.aistudy.backend.plan.dto.StudyPlanUpdateDTO;

import com.aistudy.backend.plan.entity.StudyPlan;

import com.aistudy.backend.plan.service.StudyPlanService;

import com.aistudy.backend.plan.vo.StudyPlanVO;


import com.aistudy.backend.study.mapper.StudyRecordMapper;


import com.aistudy.backend.user.entity.User;


import jakarta.validation.Valid;


import org.springframework.web.bind.annotation.*;



import java.util.List;



@RestController
@RequestMapping("/plan")
public class StudyPlanController {



    private final StudyPlanService studyPlanService;


    /**
     * 学习记录统计
     */
    private final StudyRecordMapper studyRecordMapper;




    public StudyPlanController(
            StudyPlanService studyPlanService,
            StudyRecordMapper studyRecordMapper
    ){


        this.studyPlanService = studyPlanService;

        this.studyRecordMapper = studyRecordMapper;


    }









    /**
     * 添加学习计划
     */
    @PostMapping("/add")
    public Result<StudyPlanVO> add(
            @Valid
            @RequestBody StudyPlanAddDTO dto
    ){


        User user =
                UserContext.getUser();




        StudyPlan plan =
                StudyPlanConverter.convert(
                        dto
                );



        plan.setUserId(
                user.getId()
        );



        studyPlanService.add(
                plan
        );




        StudyPlanVO vo =
                StudyPlanConverter.convertVO(
                        plan
                );



        return Result.success(
                vo
        );


    }









    /**
     * 查询自己的学习计划
     *
     * 增加学习进度统计
     */
    @GetMapping("/list")
    public Result<List<StudyPlanVO>> list(){



        User user =
                UserContext.getUser();




        List<StudyPlan> list =
                studyPlanService.list(
                        user.getId()
                );






        List<StudyPlanVO> voList =

                list.stream()

                .map(plan->{



                    StudyPlanVO vo =

                            StudyPlanConverter.convertVO(
                                    plan
                            );





                    /**
                     * 查询该计划累计学习分钟
                     */
                    Integer completedMinutes =

                            studyRecordMapper.sumDuration(

                                    user.getId(),

                                    plan.getId()

                            );




                    if(completedMinutes == null){

                        completedMinutes = 0;

                    }






                    vo.setCompletedMinutes(
                            completedMinutes
                    );






                    /**
                     * 目标小时转分钟
                     */
                    Integer targetMinutes =

                            plan.getTargetHours()*60;






                    /**
                     * 完成百分比
                     */
                    double progress = 0;




                    if(targetMinutes > 0){


                        progress =

                                completedMinutes * 100.0

                                /

                                targetMinutes;


                    }





                    // 最大100%
                    if(progress > 100){

                        progress = 100;

                    }





                    vo.setProgress(
                            progress
                    );






                    vo.setRemainMinutes(

                            Math.max(

                                    targetMinutes-completedMinutes,

                                    0

                            )

                    );






                    return vo;


                })

                .toList();






        return Result.success(
                voList
        );


    }












    /**
     * 修改学习计划
     */
    @PutMapping("/update")
    public Result<String> update(
            @Valid
            @RequestBody StudyPlanUpdateDTO dto
    ){



        User user =
                UserContext.getUser();





        StudyPlan plan =

                StudyPlanConverter.convert(
                        dto
                );




        plan.setUserId(
                user.getId()
        );





        int result =

                studyPlanService.update(
                        plan
                );







        if(result > 0){


            return Result.success(
                    "修改成功"
            );


        }





        return Result.error(
                400,
                "无权限修改该计划"
        );


    }













    /**
     * 删除学习计划
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(
            @PathVariable Integer id
    ){



        User user =
                UserContext.getUser();






        int result =

                studyPlanService.delete(

                        id,

                        user.getId()

                );







        if(result > 0){


            return Result.success(
                    "删除成功"
            );


        }






        return Result.error(
                400,
                "无权限删除该计划"
        );


    }



}