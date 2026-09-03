package com.aistudy.backend.chat.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class GeneratePlanDTO {


    @NotBlank(message = "需求不能为空")
    private String requirement;


}