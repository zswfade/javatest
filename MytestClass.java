package com.example.testlib;

public class MytestClass {
    static class ThreadMsgBusyDeal extends Thread{
        public void run() {
            for(int i=0;i<10;i++){
                System.out.println("i:"+i);
            }
          test1 mystart=new test1();
            Thread t1=new Thread(mystart,"huoche");
            t1.start();//等待当前线程运行完成后再运行
          /*  try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            for(int a=20;a<30;a++){
                System.out.println("a:"+a);
            }
        }
    }
    public static class test1 implements Runnable{
        public void run(){
          System.out.println("wodexiancheng");
        }

    }
    public static void main(String []args) {
        //java 8所支持
        new Thread(()->{
         System.out.println("this is a new function !");
        }).start();
        ThreadMsgBusyDeal testthread=new ThreadMsgBusyDeal();
        testthread.start();
    }
}