package com.aistudy.backend.study.mapper;


import com.aistudy.backend.study.entity.StudyRecord;

import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface StudyRecordMapper {



    /**
     * 新增学习记录
     */
    @Insert("""
        INSERT INTO study_record
        (
        user_id,
        plan_id,
        title,
        content,
        duration
        )
        VALUES
        (
        #{userId},
        #{planId},
        #{title},
        #{content},
        #{duration}
        )
        """)
    int insert(StudyRecord record);





    /**
     * 查询用户所有学习记录
     */
    @Select("""
        SELECT *
        FROM study_record
        WHERE user_id=#{userId}
        ORDER BY create_time DESC
        """)
    List<StudyRecord> findByUserId(
            Integer userId
    );






    /**
     * 删除学习记录
     */
    @Delete("""
        DELETE FROM study_record
        WHERE id=#{id}
        AND user_id=#{userId}
        """)
    int deleteById(
            Integer id,
            Integer userId
    );







    /**
     * 修改学习记录
     */
    @Update("""
        UPDATE study_record
        SET
        title=#{title},
        content=#{content},
        duration=#{duration}
        WHERE id=#{id}
        AND user_id=#{userId}
        """)
    int update(
            StudyRecord studyRecord
    );







    /**
     * 分页查询
     */
    @Select("""
        SELECT *
        FROM study_record
        WHERE user_id=#{userId}
        LIMIT #{start},#{size}
        """)
    List<StudyRecord> page(
            Integer userId,
            Integer start,
            Integer size
    );








    /**
     * 查询学习记录数量
     */
    @Select("""
        SELECT COUNT(*)
        FROM study_record
        WHERE user_id=#{userId}
        """)
    Long count(
            Integer userId
    );








    /**
     * 查询某个学习计划累计学习时长
     *
     * 用于：
     * 学习计划完成进度
     * 目标小时统计
     */
    @Select("""
        SELECT 
        IFNULL(SUM(duration),0)
        FROM study_record
        WHERE user_id=#{userId}
        AND plan_id=#{planId}
        """)
    Integer sumDuration(
            Integer userId,
            Integer planId
    );



}