package com.gzsf.operation.controller;

import java.io.File;
import java.io.IOException;

public class TestFile {

    public static void main(String [] args) throws IOException{
         String localFileFormat ="/data/file/%d/%d";
         localFileFormat.replace("/" ,File.separator);
        System.out.println(localFileFormat);
    }
}
