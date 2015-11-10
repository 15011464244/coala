package com.example.koalademo;

import android.os.Handler;

public class NewThread implements Runnable {

	boolean suspendFlag;
	String name;
	public Handler t;

	public NewThread(String threadName) {
		this.name = threadName;
		t = new Handler();
		suspendFlag = false;
		t.post(this);
	}

	@Override
	public void run() {
//		try {
			System.out.println("执行");
			System.out.println("执行后");
//			while(true){
//				synchronized (this) {
//					while (suspendFlag) {
//						System.out.println("while1："+t.getState());
//						wait();
//						System.out.println("while2："+t.getState());
//					}
//				}
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void mysusped() {
		suspendFlag = true;
	}

	public synchronized void myresume() {
		suspendFlag = false;
		notify();
	}
}
