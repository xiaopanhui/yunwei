package com.gzsf.operation.websocket;

import com.github.pagehelper.PageException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class test {

    public static void main(String[] args) throws ParseException {
        String str = "2018-12-05 00:00:00";
        String string = "2016-10-24 21:59:06";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.parse(str));
    }
}
