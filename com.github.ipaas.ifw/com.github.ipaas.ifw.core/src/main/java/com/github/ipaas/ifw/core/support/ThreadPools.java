/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.ipaas.ifw.core.support;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工厂类
 * 
 * @author Chenql
 */
public class ThreadPools {

	/**
	 * 根据参数创建执行者服务
	 * 
	 * @param coreSize
	 *            -- 线程池核心线程数
	 * @param maxSize
	 *            -- 线程池最大线程数
	 * @param keepAlive
	 *            -- 线程最大空闲时间(单位:秒)
	 * @param nameTemplate
	 *            -- 线程名称模板
	 * @return -- 执行者服务
	 */
	public static ExecutorService newExecutorService(int coreSize, int maxSize, int keepAlive, final String nameTemplate) {

		BlockingQueue<Runnable> queue = new SynchronousQueue<Runnable>();
		ThreadFactory fac = new ThreadFactory() {
			// 创建一个新的线程, 同时设置它的名称和daemon模式
			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setName(nameTemplate + "@[" + System.identityHashCode(t) + "]");
				t.setDaemon(true);
				return t;
			}
		};
		return new ThreadPoolExecutor(coreSize, maxSize, keepAlive, TimeUnit.SECONDS, queue, fac);
	}

	/**
	 * 根据参数创建调度执行者服务
	 * 
	 * @param coreSize
	 *            -- 线程池核心线程数
	 * @param nameTemplate
	 *            -- 线程名称模板
	 * @return -- 调度执行者服务
	 */
	public static ScheduledExecutorService newScheduledExecutorService(int coreSize, final String nameTemplate) {

		ThreadFactory fac = new ThreadFactory() {
			// 创建一个新的线程, 同时设置它的名称和daemon模式
			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setName(nameTemplate + "@[" + System.identityHashCode(t) + "]");
				t.setDaemon(true);
				return t;
			}
		};
		return new ScheduledThreadPoolExecutor(coreSize, fac);
	}
}
