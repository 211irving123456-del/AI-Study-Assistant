package com.aistudy.backend.filter;


import com.aistudy.backend.common.JwtUtil;
import com.aistudy.backend.common.UserContext;
import com.aistudy.backend.user.entity.User;
import com.aistudy.backend.user.mapper.UserMapper;


import jakarta.servlet.*;
import jakarta.servlet.http.*;


import java.io.IOException;



public class LoginFilter implements Filter {


    private final UserMapper userMapper;



    public LoginFilter(UserMapper userMapper){

        this.userMapper = userMapper;

    }





    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    )
            throws IOException, ServletException {



        HttpServletRequest req =
                (HttpServletRequest) request;


        HttpServletResponse resp =
                (HttpServletResponse) response;



        /*
         * ==========================
         * CORS响应头
         * ==========================
         */

        resp.setHeader(
                "Access-Control-Allow-Origin",
                "http://localhost:5173"
        );


        resp.setHeader(
                "Access-Control-Allow-Credentials",
                "true"
        );


        resp.setHeader(
                "Access-Control-Allow-Methods",
                "GET,POST,PUT,DELETE,OPTIONS"
        );


        resp.setHeader(
                "Access-Control-Allow-Headers",
                "*"
        );





        String path =
                req.getRequestURI();




        /*
         * ==========================
         * 放行OPTIONS预检请求
         * ==========================
         */

        if(
                "OPTIONS".equalsIgnoreCase(
                        req.getMethod()
                )
        ){


            chain.doFilter(
                    request,
                    response
            );


            return;

        }






        /*
         * ==========================
         * 不需要登录接口
         * ==========================
         */

        if(

                path.equals("/user/login")

                ||

                path.equals("/user/register")


                ||

                path.startsWith("/swagger-ui")


                ||

                path.startsWith("/v3/api-docs")


                ||

                path.startsWith("/webjars")

        ){



            chain.doFilter(
                    request,
                    response
            );


            return;

        }





        /*
         * ==========================
         * 获取Token
         * ==========================
         */


        String header =
                req.getHeader("Authorization");





        if(
                header == null

                ||

                !header.startsWith("Bearer ")
        ){


            writeError(
                    resp,
                    "请登录"
            );


            return;

        }





        String token =
                header.substring(7);





        if(token.isEmpty()){


            writeError(
                    resp,
                    "请登录"
            );


            return;

        }






        try {


            /*
             * ==========================
             * JWT解析用户id
             * ==========================
             */

            Integer userId =
                    JwtUtil.getUserId(token);





            /*
             * ==========================
             * 查询用户
             * ==========================
             */


            User user =
                    userMapper.findById(userId);





            if(user == null){


                throw new RuntimeException(
                        "用户不存在"
                );

            }







            /*
             * ==========================
             * 保存当前用户
             * ==========================
             */

            UserContext.setUser(user);






            try {


                chain.doFilter(
                        request,
                        response
                );



            }finally{


                /*
                 * 请求结束清理
                 */

                UserContext.remove();


            }






        }catch(Exception e){



            writeError(
                    resp,
                    "token无效"
            );


        }





    }









    /**
     * 返回错误JSON
     */
    private void writeError(
            HttpServletResponse resp,
            String message
    )
            throws IOException {



        resp.setContentType(
                "application/json;charset=UTF-8"
        );



        resp.getWriter()
                .write(

                """
                {
                  "code":401,
                  "message":"%s",
                  "data":null
                }
                """
                .formatted(message)

        );


    }




}