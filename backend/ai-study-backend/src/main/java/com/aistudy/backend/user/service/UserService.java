package com.aistudy.backend.user.service;

import com.aistudy.backend.common.PasswordUtil;
import com.aistudy.backend.user.entity.User;
import com.aistudy.backend.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import com.aistudy.backend.exception.BusinessException;


@Service
public class UserService {


    private final UserMapper userMapper;


    public UserService(UserMapper userMapper){

        this.userMapper=userMapper;

    }



    // 注册
    public int register(User user){

          // 1. 查询用户名是否存在

          User existUser =
            userMapper.findByUsername(
                    user.getUsername()
            );



         if(existUser != null){

        // 用户已经存在

             return 0;

         }

        // 1. 获取用户输入的密码
        String password =
                user.getPassword();


        // 2. BCrypt加密
        String encodePassword =
                PasswordUtil.encode(password);


        // 3. 设置加密后的密码
        user.setPassword(
                encodePassword
        );


        // 4. 保存数据库
        return userMapper.insert(user);

    }





    // 登录
    public User login(User user){


        // 根据用户名查询用户
        User dbUser =
                userMapper.findByUsername(
                        user.getUsername()
                );



        if(dbUser == null){

          throw new BusinessException(
                 "用户不存在"
        );

        }



        // 比较密码
        boolean result =
                PasswordUtil.matches(
                        user.getPassword(),
                        dbUser.getPassword()
                );



        if(result){

        return dbUser;

        }


        throw new BusinessException(
                "密码错误"
        );
    }
     public int updateProfile(User user){

         return userMapper.updateProfile(user);

        }

        public User findById(Integer id){

        return userMapper.findById(id);

        }

}