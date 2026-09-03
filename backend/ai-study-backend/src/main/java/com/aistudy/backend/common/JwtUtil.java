package com.aistudy.backend.common;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;


public class JwtUtil {


    private static final String SECRET_KEY =
            "AIStudyAssistantJwtSecretKey123456789";


    private static final Key KEY =
            new SecretKeySpec(
                    SECRET_KEY.getBytes(),
                    SignatureAlgorithm.HS256.getJcaName()
            );



    /**
     * 生成token
     */
    public static String generateToken(
            Integer userId
    ){


        return Jwts.builder()

                .setSubject(
                        userId.toString()
                )

                .setIssuedAt(
                        new Date()
                )

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                +
                                1000*60*60*24
                        )
                )

                .signWith(
                        KEY
                )

                .compact();

    }



    /**
     * 解析token
     */
    public static Integer getUserId(
            String token
    ){


        String id =
        Jwts.parserBuilder()

                .setSigningKey(KEY)

                .build()

                .parseClaimsJws(token)

                .getBody()

                .getSubject();



        return Integer.valueOf(id);

    }


}