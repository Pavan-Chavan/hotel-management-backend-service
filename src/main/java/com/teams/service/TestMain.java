package com.teams.service;

import java.util.Date;

public class TestMain {
    public static void main(String[] args) {
        Date startTime = new Date();
        ThreadTestIn threadTestIn = new ThreadTestIn();
        ThreadTestDe threadTestDe = new ThreadTestDe();

        threadTestIn.start();
        threadTestDe.start();

        System.out.println("startTime=" +startTime);
    }
}

class ThreadTestIn extends Thread {
    public void run() {
        for (int i=50;i>0;i--){
            System.out.println("inc"+i);
            try{Thread.sleep(100);}
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Date entTime = new Date();
        System.out.println(" entTime" + entTime);
    }
}

class ThreadTestDe extends Thread {
    public void run() {
        for (int i=0;i<50;i++){
            System.out.println("dec"+i);
            try{Thread.sleep(100);}
            catch (Exception e){
                e.printStackTrace();
            }
        }
        Date entTime = new Date();
        System.out.println(" entTime" + entTime);
    }
}