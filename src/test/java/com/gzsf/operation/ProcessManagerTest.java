package com.gzsf.operation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessManagerTest {

    private ProcessManager processManager;
    @Before
    public void setUp() throws Exception {
        processManager=new ProcessManager();
    }

    @Test
    public void runCmd() {
      long i=  processManager.runCmd("notepad","C:\\");
      System.out.print(i);
    }

    @Test
    public void stopProcess() {
     boolean d=   processManager.stopProcess(6428);
     System.out.print(d);
    }

    @Test
    public void isRunning() {
    }
}