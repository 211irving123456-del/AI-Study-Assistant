package com.aistudy.backend.chat.service.impl;

import com.aistudy.backend.chat.dto.AIPlanResultDTO;
import com.aistudy.backend.plan.entity.StudyPlan;
import com.aistudy.backend.plan.service.StudyPlanService;
import com.aistudy.backend.task.entity.StudyTask;
import com.aistudy.backend.task.service.TaskService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GeneratedPlanPersistenceServiceTest {

    private final StudyPlanService planService = mock(StudyPlanService.class);
    private final TaskService taskService = mock(TaskService.class);
    private final GeneratedPlanPersistenceService service =
            new GeneratedPlanPersistenceService(planService, taskService);

    private static AIPlanResultDTO samplePlan() {
        AIPlanResultDTO plan = new AIPlanResultDTO();
        plan.setTitle("Java 学习");
        plan.setContent("每天学习一项内容");
        plan.setStartDate("2026-11-21");
        plan.setEndDate("2026-11-22");
        plan.setTargetHours(5);

        AIPlanResultDTO.TaskDTO first = new AIPlanResultDTO.TaskDTO();
        first.setTitle("Java 基础");
        first.setContent("变量与循环");
        first.setTaskDate("2026-11-21");

        AIPlanResultDTO.TaskDTO second = new AIPlanResultDTO.TaskDTO();
        second.setTitle("Java 集合");
        second.setContent("List 与 Map");
        second.setTaskDate("2026-11-22");

        plan.setTasks(List.of(first, second));
        return plan;
    }

    @Test
    void shouldSavePlanAndAllTasks() {
        when(planService.add(any(StudyPlan.class))).thenAnswer(invocation -> {
            StudyPlan plan = invocation.getArgument(0);
            plan.setId(42);
            return 1;
        });
        when(taskService.add(any(StudyTask.class))).thenReturn(1);

        var result = service.save(7, samplePlan());

        assertEquals("Java 学习", result.getTitle());
        verify(planService, times(1)).add(any(StudyPlan.class));
        verify(taskService, times(2)).add(argThat(
                task -> task.getUserId().equals(7) && task.getPlanId().equals(42)
        ));
    }

    @Test
    void shouldRejectInvalidPlanBeforeDatabaseWrites() {
        AIPlanResultDTO plan = samplePlan();
        plan.setTasks(List.of());
        assertThrows(IllegalArgumentException.class, () -> service.save(7, plan));
        verifyNoInteractions(planService, taskService);
    }

    @Test
    void shouldPropagateTaskInsertFailureToTriggerRollback() {
        when(planService.add(any(StudyPlan.class))).thenAnswer(invocation -> {
            StudyPlan plan = invocation.getArgument(0);
            plan.setId(42);
            return 1;
        });
        when(taskService.add(any(StudyTask.class))).thenReturn(1, 0);
        assertThrows(IllegalStateException.class, () -> service.save(7, samplePlan()));
        verify(taskService, times(2)).add(any(StudyTask.class));
    }
}
