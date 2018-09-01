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
 */

package cn.aberic.bother.tools.thread;

import java.util.List;
import java.util.concurrent.*;

/**
 * 开启一个线程池——公共方法包
 * * <p>
 * * 作者：Aberic on 2018/08/24 11:44
 * * 邮箱：abericyang@gmail.com
 */
public class ThreadTroublePool {

    private ThreadPoolExecutor executor;
    private final static int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();// 核心线程数为当前设备CPU核心数+1

    public ThreadTroublePool() {
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE + 1, Integer.MAX_VALUE, 10L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(5), new ThreadTroubleFactory()
        );
        executor.prestartCoreThread(); //预启动一个线程
    }

    /**
     * 执行线程
     *
     * @param runnable 执行线程
     */
    public void submit(Runnable runnable) {
        executor.execute(runnable);
    }

    public Future<List<String>> submit(Callable<List<String>> callable) { return executor.submit(callable); }

    public void shutdown() {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

}
