package com.gzsf.operation;

import java.io.File;

public class test {
    public static void main(String[] args) {
        File file = new File("word");
        if(!file.exists()){
            file.mkdirs();
        }
        System.out.println(file.getAbsolutePath());
    }
}
