/**************************************************************************\
 * Copyright (c) 2018 Robert Koszewski                                    *
 *                                                                        *
 * Permission is hereby granted, free of charge, to any person obtaining  *
 * a copy of this software and associated documentation files (the        *
 * "Software"), to deal in the Software without restriction, including    *
 * without limitation the rights to use, copy, modify, merge, publish,    *
 * distribute, sublicense, and/or sell copies of the Software, and to     *
 * permit persons to whom the Software is furnished to do so, subject to  *
 * the following conditions:                                              *
 *                                                                        *
 * The above copyright notice and this permission notice shall be         *
 * included in all copies or substantial portions of the Software.        *
 *                                                                        *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,        *
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF     *
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND                  *
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE *
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION *
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION  *
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.        *
\**************************************************************************/

package com.robertkoszewski.wui;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		lock.lock();
		lock.lock();
		
		
		Thread t = new Thread() {
			@Override
			public void run() {
				System.out.println("PRE LOCK");
				lock.
			}
		};
		
		
		System.out.println(lock.isLocked());
		lock.unlock();
		System.out.println(lock.isLocked());
		lock.unlock();
		System.out.println(lock.isLocked());
		lock.unlock();
		System.out.println(lock.isLocked());
		
		
		*/
		
		
		
		ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
		final Wrapper i = new Wrapper();
		
		Thread rt = new Thread() {
			@Override
			public void run() {
				// Read Thread
				while(true) {
					rwlock.readLock().lock();
					System.out.println("R1 READING: " + i.i);
					
					rwlock.readLock().unlock();
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {}
				}
			}
		};
		rt.start();
		
		Thread rt2 = new Thread() {
			@Override
			public void run() {
				// Read Thread
				while(true) {
					rwlock.readLock().lock();
					System.out.println("R2 READING: " + i.i);
					
					rwlock.readLock().unlock();
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {}
				}
			}
		};
		rt2.start();
		
		
		Thread wt = new Thread() {
			@Override
			public void run() {
				// Write Thread
				while(true) {
					rwlock.writeLock().lock();
					
					System.out.print("T1 WRITING: " + i.i);
					
					if(i.i == 0) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {}
						
						i.i = 1;
					}else {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {}
						
						i.i = 0;
					}

					System.out.println(" TO " + i.i);
					rwlock.writeLock().unlock();
					
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {}
					
				}
				
			}
		};
		wt.start();
		
		
		Thread wt2 = new Thread() {
			@Override
			public void run() {
				// Write Thread
				while(true) {
					rwlock.writeLock().lock();
					
					System.out.print("T2 WRITING: " + i.i);
					
					if(i.i == 0) {
						
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {}
						
						i.i = 1;
					}else {
						
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {}
						
						
						i.i = 0;
					}
					
					

					System.out.println(" TO " + i.i);
					rwlock.writeLock().unlock();
					
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {}
					
				}
				
			}
		};
		wt2.start();
		
	}
	
	public static class Wrapper{
		int i = 0;
	}

}
