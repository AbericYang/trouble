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

import lombok.extern.slf4j.Slf4j;

/**
 * 作者：Aberic on 2018/9/1 23:56
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class SystemTool {

    /**
     * 是否达到出块标准配置需求
     * <p>
     * 暂时需求为JVM可用内存大于256MB，服务器配置4C
     *
     * @return 与否
     */
    private boolean isOutBlockNorm() {
        Runtime r = Runtime.getRuntime();
        log.debug("JVM可以使用的剩余内存: {}", r.freeMemory());
        log.debug("JVM可以使用的处理器数: {}", r.availableProcessors());
        return r.freeMemory() > 256 * 1024 * 1024 && r.availableProcessors() > 4;
    }

    /** 判断是linux系统还是其他系统，如果是Linux系统，返回true，否则返回false */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

}
