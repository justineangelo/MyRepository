/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tspi.helpers;

import com.tspi.template.CoreTemplate;

/**
 *
 * @author JRANGEL
 */
public class ThreadHelper extends CoreTemplate implements Runnable{

    private Thread t = null;
    private String threadName = "";
    public ThreadHelper(String threadName){
        this.loggerDebug("INITIALIZED THREAD : " + threadName);
        this.threadName = threadName;
    }
    @Override
    public void run() {
        this.loggerDebug("START RUN THREAD " + this.threadName);
        //Place code here
        
        
        this.loggerDebug("END RUN THREAD " + this.threadName);
    }
    /**
     * Call this to run the thread
     */
    public void start(){
        this.loggerDebug("STARTING THREAD " + this.threadName);
        if(t == null){
            t = new Thread(this, threadName);
            t.start();
        }
    }
}