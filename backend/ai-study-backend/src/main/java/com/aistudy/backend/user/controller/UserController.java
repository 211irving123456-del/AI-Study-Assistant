package com.aistudy.backend.user.controller;


import com.aistudy.backend.common.Result;
import com.aistudy.backend.common.JwtUtil;

import com.aistudy.backend.user.converter.UserConverter;
import com.aistudy.backend.common.UserContext;
import com.aistudy.backend.user.dto.UserLoginDTO;
import com.aistudy.backend.user.dto.UserRegisterDTO;
import com.aistudy.backend.user.dto.UserUpdateDTO;

import com.aistudy.backend.user.entity.User;

import com.aistudy.backend.user.service.UserService;

import com.aistudy.backend.user.vo.UserVO;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user")
public class UserController {



    private final UserService userService;

    private final JwtUtil jwtUtil;



    public UserController(
            UserService userService,
            JwtUtil jwtUtil
    ){

        this.userService=userService;
        this.jwtUtil=jwtUtil;

    }






    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<String> login(
            @Valid
            @RequestBody UserLoginDTO dto
    ){



        User user = new User();



        user.setUsername(
                dto.getUsername()
        );


        user.setPassword(
                dto.getPassword()
        );



        User loginUser =
                userService.login(user);



        String token =
                jwtUtil.generateToken(
                        loginUser.getId()
                );



        return Result.success(
                token
        );

    }








    /**
     * 获取个人信息
     */
    @GetMapping("/info")
    public Result<UserVO> info(){



        User user =
                UserContext.getUser();




        return Result.success(
                UserConverter.convert(user)
        );


    }








    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<String> register(
            @Valid
            @RequestBody UserRegisterDTO dto
    ){



        User user =
                UserConverter.convert(dto);




        int result =
                userService.register(user);




        if(result==0){


            return Result.error(
                    400,
                    "用户名已存在"
            );

        }



        return Result.success(
                "注册成功"
        );


    }









    /**
     * 修改个人信息
     */
    @PutMapping("/profile")
    public Result<String> updateProfile(
            @Valid
            @RequestBody UserUpdateDTO dto
    ){



        User loginUser =
                UserContext.getUser();




        User user =
                new User();



        user.setId(
                loginUser.getId()
        );



        user.setUsername(
                dto.getUsername()
        );



        user.setEmail(
                dto.getEmail()
        );




        userService.updateProfile(user);



        return Result.success(
                "修改成功"
        );


    }







    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<String> logout(){



        return Result.success(
                "退出成功，请删除客户端token"
        );

    }



}