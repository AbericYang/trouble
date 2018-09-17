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

package cn.aberic.bother.tools;

import cn.aberic.bother.tools.exception.SearchDataNotFoundException;
import cn.aberic.bother.tools.exception.SearchDataTimeoutException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者：Aberic on 2018/9/5 21:43
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public interface ITimeOut {

    /**
     * 循环检查对象是否为空，设置查找超时
     *
     * @param fileCount   待检查文件数量
     * @param resultCount 已检查文件数量
     * @param object      对象
     * @param time        比对时间
     * @param outTime     超时时间/ms
     *
     * @return 超时与否
     */
    default boolean checkTimeOut(int fileCount, AtomicInteger resultCount, Object object, long time, long outTime) throws SearchDataNotFoundException, SearchDataTimeoutException {
        if (null != object) {
            return true;
        } else {
            if (fileCount == resultCount.intValue()) {
                throw new SearchDataNotFoundException();
            }
            if (System.currentTimeMillis() - time > outTime) {
                throw new SearchDataTimeoutException();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
