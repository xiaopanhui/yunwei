package com.gzsf.operation.controller;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SystemFileServiceTest {

    @Test
    public void getRootList() {
        SystemFileService systemFileService = new SystemFileService();
        String[] strings = systemFileService.getRootList();
        System.out.println(Arrays.toString(strings));
    }
    @Test
    public void getChridlen(){
        SystemFileService systemFileService = new SystemFileService();
        List<String> strings = systemFileService.getChridlen("C:\\");
        System.out.println((strings));
    }
}