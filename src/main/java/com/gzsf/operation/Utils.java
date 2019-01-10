package com.gzsf.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzsf.operation.model.FieldItem;
import com.gzsf.operation.model.FieldItems;
import org.apache.poi.ss.usermodel.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.web.multipart.MultipartFile;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;


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

    public static boolean isEmpty(Object str){
        return str==null||str.toString().trim().equals("");
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
        return stringWriter.toString().replace("{","").replace("}","");
    }

    public static FieldItems StringToLogItems(String string){
        FieldItems result=new FieldItems();
        if (string==null){
            return result;
        }
        try {
            List list= mapper.readValue(string,List.class);
            for (Object aList : list) {
                Map<String, Object> entry = (Map<String, Object>) aList;
                FieldItem item = FieldItem.parseMap(entry);
                if (item != null) {
                    result.add(item);
                }
            }
        } catch (IOException e) {
        }
        return result;
    }

    public static List<Map<String, Object>> getMap(MultipartFile file){
        List<Map<String, Object>> res = new ArrayList<>();
        String fileName = file.getOriginalFilename();
        if (!fileName.contains("xls") && !fileName.contains("xlsx")) {
        } else {
            Workbook wb = null;
            try {
                wb = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = wb.getSheetAt(0);
                Row row = sheet.getRow(sheet.getFirstRowNum());
                List<String> list = new ArrayList<>();
                Iterator cells = row.cellIterator();
                while (cells.hasNext()){
                    Cell cell = (Cell) cells.next();
                    cell.setCellType(CellType.STRING);
                    list.add(cell.getStringCellValue());
                }
                for (int i = sheet.getFirstRowNum()+1; i <= sheet.getLastRowNum(); i++) {
                    Map<String,Object> map = new HashMap<>();
                    Row row1 = sheet.getRow(i);
                    Iterator cells1 = row1.cellIterator();
                    int flag = 0;
                    while (cells1.hasNext()){
                        Cell cell = (Cell) cells1.next();
                        cell.setCellType(CellType.STRING);
                        map.put(list.get(flag),cell.getStringCellValue());
                        flag++;
                    }
                    res.add(map);
                }
            }catch (IOException e) {
            }
        }
        return res;
    }
}
