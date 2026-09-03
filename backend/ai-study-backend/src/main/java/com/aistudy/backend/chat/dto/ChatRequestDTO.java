package com.aistudy.backend.chat.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ChatRequestDTO {


    /**
     * 用户问题
     */
    @NotBlank(message = "问题不能为空")
    private String message;

    public String getMessage() {

        return message;

    }



    public void setMessage(String message) {

        this.message = message;

    }

}