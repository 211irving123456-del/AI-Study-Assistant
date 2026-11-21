package com.aistudy.backend.task.mapper;

import com.aistudy.backend.task.entity.StudyTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TaskMapper {

    /**
     * 新增任务
     */
    @Insert("""
        INSERT INTO study_task
        (user_id, plan_id, title, content, task_date, status, create_time)
        SELECT #{userId}, p.id, #{title}, #{content}, #{taskDate}, #{status}, NOW()
        FROM study_plan p
        WHERE p.id = #{planId}
          AND p.user_id = #{userId}
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(StudyTask task);


    /**
     * 查询用户所有任务
     */
    @Select("""
        SELECT *
        FROM study_task
        WHERE user_id = #{userId}
        ORDER BY task_date ASC, id ASC
        """)
    List<StudyTask> findByUserId(Integer userId);


    /**
     * 根据用户ID和任务ID查询任务
     */
    @Select("""
        SELECT *
        FROM study_task
        WHERE id = #{id}
        AND user_id = #{userId}
        """)
    StudyTask findById(
            @Param("id") Integer id,
            @Param("userId") Integer userId
    );


    /**
     * 查询某个学习计划下的所有任务
     *
     * 注意：
     * TaskServiceImpl 使用的是 list(userId, planId)
     * 所以这里保留 list 方法。
     */
    @Select("""
        SELECT *
        FROM study_task
        WHERE user_id = #{userId}
        AND plan_id = #{planId}
        ORDER BY task_date ASC, id ASC
        """)
    List<StudyTask> list(
            @Param("userId") Integer userId,
            @Param("planId") Integer planId
    );


    /**
     * 根据计划ID查询任务
     *
     * 原有方法保留，避免影响其他代码。
     */
    @Select("""
        SELECT *
        FROM study_task
        WHERE plan_id = #{planId}
        AND user_id = #{userId}
        ORDER BY task_date ASC, id ASC
        """)
    List<StudyTask> findByPlanId(
            @Param("planId") Integer planId,
            @Param("userId") Integer userId
    );


    /**
     * 修改任务
     */
    @Update("""
        UPDATE study_task
        SET title = #{title},
            content = #{content},
            task_date = #{taskDate},
            status = #{status}
        WHERE id = #{id}
        AND user_id = #{userId}
        """)
    int update(StudyTask task);


    /**
     * 修改任务完成状态
     */
    @Update("""
        UPDATE study_task
        SET status = #{status}
        WHERE id = #{id}
        AND user_id = #{userId}
        """)
    int updateStatus(
            @Param("id") Integer id,
            @Param("userId") Integer userId,
            @Param("status") Integer status
    );


    /**
     * 删除任务
     */
    @Delete("""
        DELETE FROM study_task
        WHERE id = #{id}
        AND user_id = #{userId}
        """)
    int delete(
            @Param("id") Integer id,
            @Param("userId") Integer userId
    );


    /**
     * 查询计划下的任务总数
     */
    @Select("""
        SELECT COUNT(*)
        FROM study_task
        WHERE plan_id = #{planId}
        AND user_id = #{userId}
        """)
    int countByPlanId(
            @Param("planId") Integer planId,
            @Param("userId") Integer userId
    );


    /**
     * 查询计划下已完成任务数量
     */
    @Select("""
        SELECT COUNT(*)
        FROM study_task
        WHERE plan_id = #{planId}
        AND user_id = #{userId}
        AND status = 1
        """)
    int countCompletedByPlanId(
            @Param("planId") Integer planId,
            @Param("userId") Integer userId
    );


    /**
     * 查询计划下已完成任务数量
     *
     * 原有方法保留。
     */
    @Select("""
        SELECT COALESCE(SUM(
            CASE
                WHEN status = 1 THEN 1
                ELSE 0
            END
        ), 0)
        FROM study_task
        WHERE plan_id = #{planId}
        AND user_id = #{userId}
        """)
    int sumCompletedByPlanId(
            @Param("planId") Integer planId,
            @Param("userId") Integer userId
    );


    /**
     * 查询计划下未完成任务数量
     *
     * status = 0 表示未完成
     */
    @Select("""
        SELECT COUNT(*)
        FROM study_task
        WHERE plan_id = #{planId}
        AND user_id = #{userId}
        AND status = 0
        """)
    int countUnfinished(
            @Param("userId") Integer userId,
            @Param("planId") Integer planId
    );


    /**
     * 查询某一天的任务
     */
    @Select("""
        SELECT *
        FROM study_task
        WHERE user_id = #{userId}
        AND task_date = #{taskDate}
        ORDER BY id ASC
        """)
    List<StudyTask> findByUserIdAndDate(
            @Param("userId") Integer userId,
            @Param("taskDate") LocalDate taskDate
    );
}