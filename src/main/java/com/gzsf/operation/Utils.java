package com.gzsf.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzsf.operation.model.LogItem;
import com.gzsf.operation.model.LogItems;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Utils {
    private final static ObjectMapper mapper=new ObjectMapper();
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

    public static boolean isEmpty(String str){
        return str==null||str.trim().equals("");
    }

    public static void close(Closeable closeable){
        if (closeable==null)return;
        try {
            closeable.close();
        }catch (Exception r){}
    }
    public static String parseVTL(String temple,Map<String,Object> params){
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String,Object> item: params.entrySet()){
            context.put(item.getKey(),item.getValue());
        }
        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context,stringWriter,"vtlTemple",temple);
        return stringWriter.toString();
    }

    public static LogItems StringToLogItems(String string){
        LogItems result=new LogItems();
        try {
            List list= mapper.readValue(string,List.class);
            for (int i = 0; i < list.size(); i++) {
                Map<String,Object> entry= (Map<String, Object>) list.get(i);
                LogItem item=new LogItem();
                item.setName(entry.getOrDefault("name","").toString());
                item.setKey(entry.getOrDefault("key","").toString());
                item.setType((entry.getOrDefault("type","TEXT")).toString());
                if (!Utils.isEmpty(item.getName()) && !Utils.isEmpty(item.getKey())){
                    result.add(item);
                }
            }
        } catch (IOException e) {
        }
        return result;
    }
}
