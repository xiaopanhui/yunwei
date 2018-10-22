package com.gzsf.operation;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void getParamsKey() {
    }

    @Test
    public void MD5() {
    }

    @Test
    public void SHA1() {
    }

    @Test
    public void getFormattedDate() {
    }

    @Test
    public void convertBytesToHexString() {
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void close() {
    }

    @Test
    public void parseVTL() {
        Map<String,Object> params=new HashMap<>();
        params.put("keyword","fff");
        params.put("start_time",new Date());
        params.put("end_time",new Date());
       String str=  Utils.parseVTL("select * from user where 1=1 \n" +
               "#if($start_time && $end_time)\n" +
               "    and created_at BETWEEN $start_time and $end_time\n" +
               "#end\n" +
               "#if($keyword)\n" +
               " and user_name like '%$keyword%'\n" +
               "#end\n" +
               " limit $offset $limit",params);

       System.out.println(str);
    }
}