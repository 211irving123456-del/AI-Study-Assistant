package com.aistudy.backend.chat.mapper;


import com.aistudy.backend.chat.entity.ChatRecord;
import org.apache.ibatis.annotations.*;


import java.util.List;


@Mapper
public interface ChatRecordMapper {



    @Insert("""
        insert into chat_record
        (
        user_id,
        question,
        answer
        )
        values
        (
        #{userId},
        #{question},
        #{answer}
        )
    """)
    int insert(ChatRecord record);




    @Select("""
        select *
        from chat_record
        where user_id=#{userId}
        order by create_time desc
        limit 10
    """)
    List<ChatRecord> list(Integer userId);



}