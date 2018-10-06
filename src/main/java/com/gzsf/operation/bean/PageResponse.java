package com.gzsf.operation.bean;

import com.github.pagehelper.Page;
import lombok.Data;

@Data
public class PageResponse<T> extends Response<Page<T>> {
    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private int pages;

    public PageResponse(int code, String msg, Page<T> data){
        super(code,msg,data);
        this.pages=data.getPages();
        this.total=data.getTotal();
    }
}
