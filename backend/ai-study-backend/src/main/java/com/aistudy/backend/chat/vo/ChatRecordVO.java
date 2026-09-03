package com.aistudy.backend.chat.vo;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ChatRecordVO {


    private Integer id;


    private String question;


    private String answer;


    private LocalDateTime createTime;


}