package com.aistudy.backend.user.converter;

import com.aistudy.backend.user.dto.UserRegisterDTO;
import com.aistudy.backend.user.dto.UserUpdateDTO;

import com.aistudy.backend.user.entity.User;

import com.aistudy.backend.user.vo.UserVO;

public class UserConverter {



    // Entity -> VO
    public static UserVO convert(User user){


        UserVO vo = new UserVO();


        vo.setId(user.getId());


        vo.setUsername(
                user.getUsername()
        );


        vo.setEmail(
                user.getEmail()
        );


        return vo;

    }




    // RegisterDTO -> Entity
    public static User convert(UserRegisterDTO dto){


        User user = new User();


        user.setUsername(
                dto.getUsername()
        );


        user.setPassword(
                dto.getPassword()
        );


        user.setEmail(
                dto.getEmail()
        );


        return user;

    }


}