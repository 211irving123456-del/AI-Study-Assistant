package com.aistudy.backend.chat.controller;


import java.util.List;


import com.aistudy.backend.chat.vo.ChatRecordVO;
import com.aistudy.backend.chat.dto.ChatRequestDTO;
import com.aistudy.backend.chat.service.ChatService;
import com.aistudy.backend.chat.dto.GeneratePlanDTO;
import com.aistudy.backend.chat.vo.GeneratePlanVO;
import com.aistudy.backend.common.Result;
import com.aistudy.backend.common.UserContext;

import com.aistudy.backend.user.entity.User;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/chat")
public class ChatController {



    private final ChatService chatService;



    public ChatController(
            ChatService chatService
    ){

        this.chatService = chatService;

    }




    /**
     * 查询聊天历史
     */
    @GetMapping("/history")
    public Result<List<ChatRecordVO>> history(){


        User user =
                UserContext.getUser();



        return Result.success(
                chatService.history(
                        user.getId()
                )
        );


    }





    /**
     * AI聊天
     */
    @PostMapping
    public Result<String> chat(
            @Valid
            @RequestBody ChatRequestDTO dto
    ){


        User user =
                UserContext.getUser();



        String answer =
                chatService.chat(
                        user.getId(),
                        dto.getMessage()
                );



        return Result.success(
                answer
        );


    }

        @PostMapping("/generatePlan")
        public Result<GeneratePlanVO> generatePlan(
        @Valid
        @RequestBody GeneratePlanDTO dto
        ){


        User user =
                UserContext.getUser();



        return Result.success(
                chatService.generatePlan(
                        user.getId(),
                        dto.getRequirement()
                )
        );


        }

}