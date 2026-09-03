package com.aistudy.backend.common;


import com.aistudy.backend.user.entity.User;


public class UserContext {


    private static final ThreadLocal<User> LOCAL =
            new ThreadLocal<>();



    public static void setUser(User user){

        LOCAL.set(user);

    }



    public static User getUser(){

        return LOCAL.get();

    }



    public static void remove(){

        LOCAL.remove();

    }

}