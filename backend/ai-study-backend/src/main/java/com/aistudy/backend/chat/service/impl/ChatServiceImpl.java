package com.aistudy.backend.chat.service.impl;


import com.aistudy.backend.chat.client.AIClient;
import com.aistudy.backend.chat.dto.AIPlanResultDTO;
import com.aistudy.backend.chat.entity.ChatRecord;
import com.aistudy.backend.chat.mapper.ChatRecordMapper;
import com.aistudy.backend.chat.service.ChatService;
import com.aistudy.backend.chat.vo.ChatRecordVO;
import com.aistudy.backend.chat.vo.GeneratePlanVO;

import com.aistudy.backend.plan.entity.StudyPlan;
import com.aistudy.backend.plan.service.StudyPlanService;

import com.aistudy.backend.task.entity.StudyTask;
import com.aistudy.backend.task.service.TaskService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChatServiceImpl implements ChatService {


    private final AIClient aiClient;

    private final ChatRecordMapper chatRecordMapper;

    private final StudyPlanService studyPlanService;

    private final TaskService taskService;

    private final ObjectMapper objectMapper;


    public ChatServiceImpl(
            AIClient aiClient,
            ChatRecordMapper chatRecordMapper,
            StudyPlanService studyPlanService,
            TaskService taskService,
            ObjectMapper objectMapper
    ) {

        this.aiClient = aiClient;

        this.chatRecordMapper = chatRecordMapper;

        this.studyPlanService = studyPlanService;

        this.taskService = taskService;

        this.objectMapper = objectMapper;
    }


    /**
     * AI聊天
     */
    @Override
    public String chat(
            Integer userId,
            String message
    ) {

        String context =
                buildContext(
                        userId,
                        message
                );


        String answer =
                aiClient.chat(
                        context
                );


        ChatRecord record =
                new ChatRecord();


        record.setUserId(
                userId
        );


        record.setQuestion(
                message
        );


        record.setAnswer(
                answer
        );


        chatRecordMapper.insert(
                record
        );


        return answer;
    }


    /**
     * 查询聊天历史
     */
    @Override
    public List<ChatRecordVO> history(
            Integer userId
    ) {

        return chatRecordMapper
                .list(userId)
                .stream()
                .map(
                        item -> {

                            ChatRecordVO vo =
                                    new ChatRecordVO();


                            vo.setId(
                                    item.getId()
                            );


                            vo.setQuestion(
                                    item.getQuestion()
                            );


                            vo.setAnswer(
                                    item.getAnswer()
                            );


                            vo.setCreateTime(
                                    item.getCreateTime()
                            );


                            return vo;
                        }
                )
                .collect(
                        Collectors.toList()
                );
    }


    /**
     * AI生成学习计划
     *
     * AI同时生成：
     *
     * 1. 学习计划
     * 2. 每日学习任务
     *
     * 然后自动保存：
     *
     * study_plan
     * study_task
     */
    @Override
    public GeneratePlanVO generatePlan(
            Integer userId,
            String requirement
    ) {


        // ==============================
        // 1. 构建AI提示词
        // ==============================

        String prompt = """

                你是一名专业的AI学习规划助手。

                请根据用户的学习需求，生成一份可以真正执行的学习计划。

                用户需求：
                %s

                请严格按照下面的JSON格式返回。

                不要返回Markdown。
                不要使用```json。
                不要添加任何JSON之外的解释文字。

                JSON格式：

                {
                  "title": "学习计划标题",
                  "content": "完整的学习计划说明",
                  "startDate": "YYYY-MM-DD",
                  "endDate": "YYYY-MM-DD",
                  "targetHours": 30,
                  "tasks": [
                    {
                      "title": "任务标题",
                      "content": "任务具体内容",
                      "taskDate": "YYYY-MM-DD"
                    }
                  ]
                }

                要求：

                1. 根据用户需求合理安排学习周期。
                2. startDate和endDate必须使用YYYY-MM-DD格式。
                3. targetHours必须是整数。
                4. tasks必须按照日期顺序排列。
                5. 每个task必须有title、content、taskDate。
                6. 每一天至少安排一个学习任务。
                7. 每个任务应该是可以独立完成的小任务。
                8. 不要把整个学习阶段作为一个任务。
                9. 任务应该具有明确的学习目标。
                10. 任务应该适合用户逐个完成。
                11. content需要对整个学习计划进行总结说明。
                12. 不要生成tasks为空的计划。

                """.formatted(requirement);


        // ==============================
        // 2. 调用AI
        // ==============================

        String result =
                aiClient.chat(
                        prompt
                );


        // ==============================
        // 3. JSON转换
        // ==============================

        AIPlanResultDTO aiPlan;

        try {

            aiPlan =
                    objectMapper.readValue(
                            result,
                            AIPlanResultDTO.class
                    );

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "AI生成学习计划格式错误，请重试",
                    e
            );
        }


        // ==============================
        // 4. 创建StudyPlan
        // ==============================

        StudyPlan plan =
                new StudyPlan();


        plan.setUserId(
                userId
        );


        plan.setTitle(
                aiPlan.getTitle()
        );


        plan.setContent(
                aiPlan.getContent()
        );


        if (
                aiPlan.getStartDate() != null
                &&
                !aiPlan.getStartDate().isBlank()
        ) {

            plan.setStartDate(
                    LocalDate.parse(
                            aiPlan.getStartDate()
                    )
            );
        }


        if (
                aiPlan.getEndDate() != null
                &&
                !aiPlan.getEndDate().isBlank()
        ) {

            plan.setEndDate(
                    LocalDate.parse(
                            aiPlan.getEndDate()
                    )
            );
        }


        plan.setTargetHours(
                aiPlan.getTargetHours()
        );


        // 0 = 未开始
        plan.setStatus(
                0
        );


        // ==============================
        // 5. 保存学习计划
        // ==============================

        studyPlanService.add(
                plan
        );


        // ==============================
        // 6. 自动创建StudyTask
        // ==============================

        if (aiPlan.getTasks() != null) {

            for (
                    AIPlanResultDTO.TaskDTO taskDTO
                    : aiPlan.getTasks()
            ) {


                StudyTask task =
                        new StudyTask();


                // 用户ID
                task.setUserId(
                        userId
                );


                // 学习计划ID
                task.setPlanId(
                        plan.getId()
                );


                // 任务标题
                task.setTitle(
                        taskDTO.getTitle()
                );


                // 任务内容
                task.setContent(
                        taskDTO.getContent()
                );


                // 任务日期
                if (
                        taskDTO.getTaskDate() != null
                        &&
                        !taskDTO.getTaskDate().isBlank()
                ) {

                    task.setTaskDate(
                            LocalDate.parse(
                                    taskDTO.getTaskDate()
                            )
                    );
                }


                // 0 = 未完成
                task.setStatus(
                        0
                );


                // 保存任务
                taskService.add(
                        task
                );
            }
        }


        // ==============================
        // 7. 返回学习计划
        // ==============================

        GeneratePlanVO vo =
                new GeneratePlanVO();


        vo.setTitle(
                plan.getTitle()
        );


        vo.setContent(
                plan.getContent()
        );


        vo.setTargetHours(
                plan.getTargetHours()
        );


        return vo;
    }


    /**
     * 构建AI上下文
     */
    private String buildContext(
            Integer userId,
            String message
    ) {


        List<ChatRecord> records =
                chatRecordMapper.list(
                        userId
                );


        Collections.reverse(
                records
        );


        StringBuilder sb =
                new StringBuilder();


        sb.append(
                """

                你是一个AI学习助手。

                你的任务：

                - 解答用户问题
                - 辅助学习
                - 制定学习计划
                - 提供学习建议

                回答要求：

                1. 逻辑清晰
                2. 分步骤说明
                3. 不编造信息

                """
        );


        sb.append(
                "历史聊天记录：\n\n"
        );


        for (
                ChatRecord record
                : records
        ) {


            sb.append(
                    "用户："
            )
            .append(
                    record.getQuestion()
            )
            .append(
                    "\n"
            );


            sb.append(
                    "AI："
            )
            .append(
                    record.getAnswer()
            )
            .append(
                    "\n\n"
            );
        }


        sb.append(
                "当前用户问题："
        );


        sb.append(
                message
        );


        return sb.toString();
    }
}