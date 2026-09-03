package com.aistudy.backend.chat.service;


import com.aistudy.backend.chat.vo.ChatRecordVO;
import com.aistudy.backend.chat.vo.GeneratePlanVO;


import java.util.List;



public interface ChatService {



    String chat(
            Integer userId,
            String message
    );



    List<ChatRecordVO> history(
            Integer userId
    );



    GeneratePlanVO generatePlan(
            Integer userId,
            String requirement
    );


}