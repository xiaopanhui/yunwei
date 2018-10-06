package com.gzsf.operation;

import com.github.pagehelper.Page;
import com.gzsf.operation.bean.PageResponse;
import com.gzsf.operation.bean.Response;

public class ResponseUtils {
    public static <T> Response<T> success(T data){return new Response<T>(0,"Ok",data);}
    public static <T> Response<T> noUser(){return new Response<T>(100,"No User");}
    public static <T> Response<T> accessDenied(){return new Response<T>(403,"Access Denied");}
    public static <T> Response<T> systemError(){return new Response<T>(500,"System Error");}
    public static <T> Response<T> notFound(){return new Response<T>(404,"Not Found");}
    public static <T> Response<T> notLogin(){return new Response<T>(101,"Not Login");}
    public static <T>PageResponse<T> successPage(Page<T> data){return new PageResponse<T>(0,"OK",data);}

}
