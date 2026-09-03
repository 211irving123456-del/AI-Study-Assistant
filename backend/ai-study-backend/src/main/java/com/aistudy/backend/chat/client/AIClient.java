package com.aistudy.backend.chat.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class AIClient {


    /**
     * DeepSeek API Key
     *
     * 从 application.properties 中读取：
     *
     * deepseek.api-key=你的API_KEY
     */
    @Value("${deepseek.api-key}")
    private String apiKey;


    /**
     * HTTP客户端
     */
    private final RestTemplate restTemplate =
            new RestTemplate();


    /**
     * DeepSeek API地址
     */
    private static final String API_URL =
            "https://api.deepseek.com/chat/completions";


    /**
     * 当前使用的模型
     *
     * deepseek-chat 已经停止使用，
     * 现在改成官方最新的 deepseek-v4-flash。
     */
    private static final String MODEL =
            "deepseek-v4-flash";


    /**
     * AI聊天
     */
    public String chat(String message) {


        System.out.println(
                "=============================="
        );

        System.out.println(
                "开始调用 DeepSeek AI"
        );

        System.out.println(
                "模型：" + MODEL
        );

        System.out.println(
                "问题长度：" +
                (message == null ? 0 : message.length())
        );

        System.out.println(
                "=============================="
        );


        /*
         * ==============================
         * 1. 请求头
         * ==============================
         */

        HttpHeaders headers =
                new HttpHeaders();


        headers.setContentType(
                MediaType.APPLICATION_JSON
        );


        headers.setBearerAuth(
                apiKey
        );


        /*
         * ==============================
         * 2. messages
         * ==============================
         */

        List<Map<String, String>> messages =
                new ArrayList<>();


        /*
         * 用户消息
         */

        Map<String, String> userMessage =
                new HashMap<>();


        userMessage.put(
                "role",
                "user"
        );


        userMessage.put(
                "content",
                message
        );


        messages.add(
                userMessage
        );


        /*
         * ==============================
         * 3. 请求body
         * ==============================
         */

        Map<String, Object> body =
                new HashMap<>();


        /*
         * 使用新的 DeepSeek V4 Flash
         */

        body.put(
                "model",
                MODEL
        );


        body.put(
                "messages",
                messages
        );


        /*
         * 温度
         */

        body.put(
                "temperature",
                0.7
        );


        /*
         * 不使用流式输出
         *
         * 这样 RestTemplate 可以直接
         * 获取完整 JSON。
         */

        body.put(
                "stream",
                false
        );


        /*
         * ==============================
         * 4. 创建HTTP请求
         * ==============================
         */

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(
                        body,
                        headers
                );


        /*
         * ==============================
         * 5. 调用DeepSeek
         * ==============================
         */

        ResponseEntity<Map> response;


        try {


            response =
                    restTemplate.postForEntity(
                            API_URL,
                            entity,
                            Map.class
                    );


        } catch (Exception e) {


            System.err.println(
                    "================================"
            );

            System.err.println(
                    "DeepSeek API调用失败"
            );

            System.err.println(
                    "================================"
            );


            e.printStackTrace();


            throw new RuntimeException(
                    "DeepSeek AI服务调用失败：" +
                    e.getMessage(),
                    e
            );

        }


        /*
         * ==============================
         * 6. 获取响应
         * ==============================
         */

        Map result =
                response.getBody();


        System.out.println(
                "DeepSeek HTTP状态码：" +
                response.getStatusCode()
        );


        System.out.println(
                "DeepSeek返回结果："
        );


        System.out.println(
                result
        );


        /*
         * ==============================
         * 7. 防止返回空数据
         * ==============================
         */

        if (result == null) {


            throw new RuntimeException(
                    "DeepSeek返回结果为空"
            );

        }


        /*
         * ==============================
         * 8. 获取choices
         * ==============================
         */

        Object choicesObject =
                result.get("choices");


        if (choicesObject == null) {


            throw new RuntimeException(
                    "DeepSeek返回结果中不存在choices"
            );

        }


        if (!(choicesObject instanceof List)) {


            throw new RuntimeException(
                    "DeepSeek返回的choices格式错误"
            );

        }


        List choices =
                (List) choicesObject;


        if (choices.isEmpty()) {


            throw new RuntimeException(
                    "DeepSeek返回的choices为空"
            );

        }


        /*
         * ==============================
         * 9. 获取第一个choice
         * ==============================
         */

        Object firstObject =
                choices.get(0);


        if (!(firstObject instanceof Map)) {


            throw new RuntimeException(
                    "DeepSeek返回的choice格式错误"
            );

        }


        Map first =
                (Map) firstObject;


        /*
         * ==============================
         * 10. 获取message
         * ==============================
         */

        Object messageObject =
                first.get("message");


        if (!(messageObject instanceof Map)) {


            throw new RuntimeException(
                    "DeepSeek返回结果中不存在message"
            );

        }


        Map messageMap =
                (Map) messageObject;


        /*
         * ==============================
         * 11. 获取content
         * ==============================
         */

        Object contentObject =
                messageMap.get("content");


        /*
         * content可能为空
         */

        if (contentObject == null) {


            /*
             * 某些情况下可能存在
             * reasoning_content。
             *
             * 但是普通聊天最终应该使用
             * content作为回答。
             */

            Object reasoningObject =
                    messageMap.get(
                            "reasoning_content"
                    );


            if (
                    reasoningObject != null &&
                    !reasoningObject
                            .toString()
                            .trim()
                            .isEmpty()
            ) {


                return reasoningObject
                        .toString()
                        .trim();

            }


            throw new RuntimeException(
                    "DeepSeek返回content为空"
            );

        }


        /*
         * ==============================
         * 12. 转换成字符串
         * ==============================
         */

        String answer =
                contentObject
                        .toString()
                        .trim();


        /*
         * ==============================
         * 13. 最终检查
         * ==============================
         */

        if (answer.isEmpty()) {


            throw new RuntimeException(
                    "DeepSeek返回的AI回答为空"
            );

        }


        /*
         * ==============================
         * 14. 输出日志
         * ==============================
         */

        System.out.println(
                "================================"
        );


        System.out.println(
                "DeepSeek AI回答成功"
        );


        System.out.println(
                "回答长度：" +
                answer.length()
        );


        System.out.println(
                "================================"
        );


        /*
         * ==============================
         * 15. 返回AI回答
         * ==============================
         */

        return answer;

    }

}