package com.gzsf.operation.controller;


import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test2 {
    public static void main(String[] args) throws Exception {

        //String str = ping("cmd.exe /c del D:\\data\\aaa.doc");
        String str = ping("D:\\QQLive\\QQLive.exe");
        System.out.print(str);
        //ccc();

    }

    public static String ping(String cmd) {

        try {
            String command = cmd;

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            CommandLine commandline = CommandLine.parse(command);


            DefaultExecutor exec = new DefaultExecutor();

            exec.setExitValues(null);

            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream,errorStream);

            exec.setStreamHandler(streamHandler);

            int pid = exec.execute(commandline);
            System.out.print(pid);

            String out = outputStream.toString("gbk");

            String error = errorStream.toString("gbk");

            return out+error;

        } catch (Exception e) {

            return e.toString();

        }

    }

    public static void aa() throws IOException {
        CommandLine cmdLine = new CommandLine("AcroRd32.exe");
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = new ExecuteWatchdog(6*1000);
        Executor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);
        executor.execute(cmdLine, resultHandler);

        // some time later the result handler callback was invoked so we
        // can safely request the exit value
        //resultHandler.waitFor();
    }

}
