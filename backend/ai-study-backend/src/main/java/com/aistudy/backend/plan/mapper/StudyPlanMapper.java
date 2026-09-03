package com.aistudy.backend.plan.mapper;

import com.aistudy.backend.plan.entity.StudyPlan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudyPlanMapper {

    /**
     * 新增学习计划
     */
    @Insert("""
        insert into study_plan
        (
            user_id,
            title,
            content,
            start_date,
            end_date,
            target_hours,
            status
        )
        values
        (
            #{userId},
            #{title},
            #{content},
            #{startDate},
            #{endDate},
            #{targetHours},
            0
        )
    """)
    @Options(
        useGeneratedKeys = true,
        keyProperty = "id"
    )
    int insert(StudyPlan plan);


    /**
     * 查询用户所有学习计划
     */
    @Select("""
        select *
        from study_plan
        where user_id = #{userId}
        order by create_time desc
    """)
    List<StudyPlan> list(Integer userId);


    /**
     * 查询某个计划已经学习的总时长
     */
    @Select("""
        select IFNULL(sum(duration), 0)
        from study_record
        where plan_id = #{planId}
        and user_id = #{userId}
    """)
    Integer sumDuration(
            Integer userId,
            Integer planId
    );


    /**
     * 删除学习计划
     */
    @Delete("""
        delete from study_plan
        where id = #{id}
        and user_id = #{userId}
    """)
    int delete(
            Integer id,
            Integer userId
    );


    /**
     * 修改学习计划
     */
    @Update("""
        update study_plan
        set
            title = #{title},
            content = #{content},
            start_date = #{startDate},
            end_date = #{endDate},
            target_hours = #{targetHours},
            status = #{status}
        where id = #{id}
        and user_id = #{userId}
    """)
    int update(StudyPlan plan);

        /**
     * 修改学习计划状态
     */
    @Update("""
        update study_plan
        set status = #{status}
        where id = #{id}
        and user_id = #{userId}
    """)
    int updateStatus(
            Integer id,
            Integer userId,
            Integer status
    );
}