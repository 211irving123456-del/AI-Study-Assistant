package com.aistudy.backend.common;


import lombok.Data;

import java.util.List;


@Data
public class PageResult<T> {


    /**
     * 总数量
     */
    private Long total;



    /**
     * 当前页数据
     */
    private List<T> records;



    public PageResult(
            Long total,
            List<T> records
    ){

        this.total = total;
        this.records = records;

    }

}