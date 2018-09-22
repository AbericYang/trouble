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

import java.io.IOException;

/**
 * 作者：Aberic on 2018/9/2 00:07
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class SystemTest {

    public static void main(String[] args) throws IOException {
        log.debug("is linux = {}", SystemTool.isLinux());
        log.debug("intranet ip = {}", SystemTool.getLocalIp());
        log.debug("internet ip = {}", SystemTool.getInternetIp());
        log.debug("is intranet ip = {}", SystemTool.isIntranetIp(SystemTool.getInternetIp()));
        // info();
    }

    private static void info() throws IOException {
        SystemInfo.initSigar();
        try {
            // System信息，从jvm获取
            SystemInfo.property();
            System.out.println("----------------------------------");
            // cpu信息
            SystemInfo.cpu();
            System.out.println("----------------------------------");
            // 内存信息
            SystemInfo.memory();
            System.out.println("----------------------------------");
            // 操作系统信息
            SystemInfo.os();
            System.out.println("----------------------------------");
            // 用户信息
            SystemInfo.who();
            System.out.println("----------------------------------");
            // 文件系统信息
            SystemInfo.file();
            System.out.println("----------------------------------");
            // 网络信息
            SystemInfo.net();
            System.out.println("----------------------------------");
            // 以太网信息
            SystemInfo.ethernet();
            System.out.println("----------------------------------");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
