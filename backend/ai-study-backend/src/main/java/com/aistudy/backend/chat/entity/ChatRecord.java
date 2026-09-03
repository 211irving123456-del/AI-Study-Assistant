package com.aistudy.backend.chat.entity;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ChatRecord {


    private Integer id;


    private Integer userId;


    private String question;


    private String answer;


    private LocalDateTime createTime;


}