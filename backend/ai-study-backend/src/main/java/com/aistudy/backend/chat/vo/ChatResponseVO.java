package com.aistudy.backend.chat.vo;


import lombok.Data;


@Data
public class ChatResponseVO {


    /**
     * AI回复内容
     */
    private String answer;



    public ChatResponseVO(
            String answer
    ){

        this.answer = answer;

    }

}