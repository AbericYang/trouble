/*
 * MIT License
 *
 * Copyright (c) 2018 Aberic Yang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cn.aberic.bother.common;

import cn.aberic.bother.core.dm.block.Block;
import cn.aberic.bother.core.dm.call.CallableSearchBlock;

import java.util.concurrent.*;

/**
 * 开启一个线程池——公共方法包
 *  * <p>
 *  * 作者：Aberic on 2018/08/24 11:44
 *  * 邮箱：abericyang@gmail.com
 */
public class ThreadTroublePool {

	/** 无界线程池，可以进行自动线程回收 */
	private ExecutorService mCacheThreadPool;

	private static ThreadTroublePool instance = null;

	public static ThreadTroublePool obtain() {
		if (null == instance) {
			synchronized (ThreadTroublePool.class) {
				if (null == instance) {
					instance = new ThreadTroublePool();
				}
			}
		}
		return instance;
	}

	private ThreadTroublePool() {
		mCacheThreadPool = Executors.newCachedThreadPool();
	}

	/**
	 * 执行定长核心线程池
	 *
	 * @param runnable
	 *            执行线程
	 */
	public void submitFixed(Runnable runnable) {
		mCacheThreadPool.execute(runnable);
	}

	/**
	 * 执行定长核心线程池
	 *
	 * @param callable
	 *            执行线程
	 */
	public Future<Block> submitFixed(CallableSearchBlock callable) {
		return mCacheThreadPool.submit(callable);
	}

}
