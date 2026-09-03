package com.aistudy.backend.study.controller;


import com.aistudy.backend.common.PageResult;
import com.aistudy.backend.common.Result;
import com.aistudy.backend.study.converter.StudyRecordConverter;
import com.aistudy.backend.study.converter.StudyRecordVOConverter;
import com.aistudy.backend.common.UserContext;

import com.aistudy.backend.study.dto.StudyRecordAddDTO;
import com.aistudy.backend.study.dto.StudyRecordUpdateDTO;

import com.aistudy.backend.study.entity.StudyRecord;
import com.aistudy.backend.user.entity.User;

import com.aistudy.backend.study.service.StudyRecordService;

import com.aistudy.backend.study.vo.StudyRecordVO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/study")
public class StudyRecordController {


    private final StudyRecordService studyRecordService;



    public StudyRecordController(
            StudyRecordService studyRecordService
    ){

        this.studyRecordService = studyRecordService;

    }




    /**
     * 添加学习记录
     */
    @PostMapping("/add")
    public Result<StudyRecordVO> add(
            @Valid
            @RequestBody StudyRecordAddDTO dto
    ){


        User user =
                UserContext.getUser();



        StudyRecord record =
                StudyRecordConverter.convert(dto);



        record.setUserId(
                user.getId()
        );



        studyRecordService.add(record);



        StudyRecordVO vo =
                StudyRecordVOConverter.convert(record);



        return Result.success(vo);

    }


    /**
     * 查询学习记录
     */
    @GetMapping("/list")
    public Result<List<StudyRecordVO>> list(){


        User user =
                UserContext.getUser();



        List<StudyRecord> list =
                studyRecordService.list(
                        user.getId()
                );



        List<StudyRecordVO> voList =
                list.stream()
                .map(
                        StudyRecordVOConverter::convert
                )
                .toList();



        return Result.success(voList);

    }






    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(
            @PathVariable Integer id
    ){


        User user =
                UserContext.getUser();



        int result =
                studyRecordService.delete(
                        id,
                        user.getId()
                );



        if(result>0){

            return Result.success(
                    "删除成功"
            );

        }



        return Result.error(
                400,
                "无权限删除"
        );


    }






    /**
     * 修改
     */
    @PutMapping("/update")
    public Result<String> update(
            @Valid
            @RequestBody StudyRecordUpdateDTO dto
    ){



        User user =
                UserContext.getUser();



        StudyRecord record =
                StudyRecordConverter.convert(dto);



        record.setUserId(
                user.getId()
        );



        int result =
                studyRecordService.update(
                        record
                );



        if(result>0){

            return Result.success(
                    "修改成功"
            );

        }



        return Result.error(
                400,
                "无权限修改"
        );


    }






    /**
     * 分页
     */
    @GetMapping("/page")
    public Result<PageResult> page(
            Integer page,
            Integer size
    ){



        User user =
                UserContext.getUser();



        PageResult result =
                studyRecordService.page(
                        user.getId(),
                        page,
                        size
                );



        return Result.success(result);

    }



}