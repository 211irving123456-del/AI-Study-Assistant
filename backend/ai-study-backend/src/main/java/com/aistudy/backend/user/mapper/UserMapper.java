package com.aistudy.backend.user.mapper;


import com.aistudy.backend.user.entity.User;

import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper {



    @Insert("""
            INSERT INTO `user`(username,password,email)
            VALUES(#{username},#{password},#{email})
            """)
    int insert(User user);



    @Select("""
            SELECT *
            FROM `user`
            WHERE username=#{username}
            """)
    User findByUsername(String username);

    @Update("""
        update `user`
        set username=#{username},
        email=#{email}
        where id=#{id}
        """)
    int updateProfile(User user);

    @Select("""
        select *
        from user
        where id=#{id}
        """)
    User findById(Integer id);

}