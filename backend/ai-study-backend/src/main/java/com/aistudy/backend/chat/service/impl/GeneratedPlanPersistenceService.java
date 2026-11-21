package com.aistudy.backend.chat.service.impl;

import com.aistudy.backend.chat.dto.AIPlanResultDTO;
import com.aistudy.backend.chat.vo.GeneratePlanVO;
import com.aistudy.backend.plan.entity.StudyPlan;
import com.aistudy.backend.plan.service.StudyPlanService;
import com.aistudy.backend.task.entity.StudyTask;
import com.aistudy.backend.task.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将 AI 输出的计划与任务作为一个数据库事务保存。
 * AI 网络调用发生在该服务之外，避免在等待模型时持有数据库事务。
 */
@Service
public class GeneratedPlanPersistenceService {

    private final StudyPlanService studyPlanService;
    private final TaskService taskService;

    public GeneratedPlanPersistenceService(
            StudyPlanService studyPlanService,
            TaskService taskService
    ) {
        this.studyPlanService = studyPlanService;
        this.taskService = taskService;
    }

    @Transactional(rollbackFor = Exception.class)
    public GeneratePlanVO save(Integer userId, AIPlanResultDTO generated) {
        ValidatedPlan validated = validate(userId, generated);

        StudyPlan plan = new StudyPlan();
        plan.setUserId(userId);
        plan.setTitle(generated.getTitle().trim());
        plan.setContent(generated.getContent().trim());
        plan.setStartDate(validated.startDate());
        plan.setEndDate(validated.endDate());
        plan.setTargetHours(generated.getTargetHours());
        plan.setStatus(0);

        if (studyPlanService.add(plan) != 1 || plan.getId() == null) {
            throw new IllegalStateException("学习计划保存失败");
        }

        for (ValidatedTask item : validated.tasks()) {
            StudyTask task = new StudyTask();
            task.setUserId(userId);
            task.setPlanId(plan.getId());
            task.setTitle(item.title());
            task.setContent(item.content());
            task.setTaskDate(item.date());
            task.setStatus(0);

            if (taskService.add(task) != 1) {
                throw new IllegalStateException("每日学习任务保存失败，已回滚整个计划");
            }
        }

        GeneratePlanVO result = new GeneratePlanVO();
        result.setTitle(plan.getTitle());
        result.setContent(plan.getContent());
        result.setTargetHours(plan.getTargetHours());
        return result;
    }

    private static ValidatedPlan validate(Integer userId, AIPlanResultDTO plan) {
        if (userId == null || plan == null) {
            throw new IllegalArgumentException("用户或 AI 学习计划为空");
        }
        requireText(plan.getTitle(), "计划标题");
        requireText(plan.getContent(), "计划内容");
        if (plan.getTargetHours() == null || plan.getTargetHours() <= 0) {
            throw new IllegalArgumentException("目标学时必须为正整数");
        }

        LocalDate start = parseDate(plan.getStartDate(), "开始日期");
        LocalDate end = parseDate(plan.getEndDate(), "结束日期");
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("计划开始日期不能晚于结束日期");
        }
        if (plan.getTasks() == null || plan.getTasks().isEmpty()) {
            throw new IllegalArgumentException("AI 没有生成每日学习任务");
        }

        List<ValidatedTask> tasks = new ArrayList<>();
        LocalDate previous = null;
        for (AIPlanResultDTO.TaskDTO item : plan.getTasks()) {
            if (item == null) {
                throw new IllegalArgumentException("AI 生成的任务为空");
            }
            requireText(item.getTitle(), "任务标题");
            requireText(item.getContent(), "任务内容");
            LocalDate date = parseDate(item.getTaskDate(), "任务日期");
            if (date.isBefore(start) || date.isAfter(end)) {
                throw new IllegalArgumentException("任务日期超出学习计划周期");
            }
            if (previous != null && date.isBefore(previous)) {
                throw new IllegalArgumentException("任务必须按日期升序排列");
            }
            tasks.add(new ValidatedTask(item.getTitle().trim(), item.getContent().trim(), date));
            previous = date;
        }

        return new ValidatedPlan(start, end, List.copyOf(tasks));
    }

    private static LocalDate parseDate(String text, String name) {
        requireText(text, name);
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(name + "格式应为 YYYY-MM-DD", e);
        }
    }

    private static void requireText(String text, String name) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(name + "不能为空");
        }
    }

    private record ValidatedPlan(LocalDate startDate, LocalDate endDate, List<ValidatedTask> tasks) {}
    private record ValidatedTask(String title, String content, LocalDate date) {}
}
