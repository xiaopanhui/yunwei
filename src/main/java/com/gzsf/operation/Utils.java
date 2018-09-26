package com.gzsf.operation;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Utils {

    public static String getParamsKey(Map<String,Object> map){
        if (map==null||map.isEmpty()){
            return "";
        }
        TreeMap<String,Object> treeMap= new TreeMap<>(String::compareTo);
        treeMap.putAll(map);
        StringBuilder buffer=new StringBuilder();
        for (Map.Entry<String,Object> entry:treeMap.entrySet()){
            buffer.append("&");
            buffer.append(entry.getKey().trim());
            buffer.append("=");
            buffer.append(entry.getValue().toString().trim());
        }
        return MD5(buffer.toString());

    }

    public static String MD5(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(src.getBytes(Charset.forName("UTF-8")));
            return ConvertBytesToHexString(result).toUpperCase();
        } catch (Exception e) {

        }
        return "";
    }

    public static String SHA1(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] result = md.digest(src.getBytes(Charset.forName("UTF-8")));
            return ConvertBytesToHexString(result).toLowerCase();
        } catch (Exception e) {

        }
        return "";
    }
    private static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyMMdd");

    public static String getFormattedDate(Date date){
        return  simpleDateFormat.format(date);
    }

    public static String ConvertBytesToHexString(byte[] bytes) {
        if (bytes == null) return null;
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if ((0xff & bytes[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & bytes[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        return hexString.toString();
    }
}
