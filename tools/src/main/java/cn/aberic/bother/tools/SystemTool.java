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

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static java.net.NetworkInterface.getByInetAddress;

/**
 * 作者：Aberic on 2018/9/1 23:56
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class SystemTool {

    /** 获取本机mac地址 */
    public static String getLocalMac() {
        String macAddress = null;
        try {
            //得到IP，输出PC-201309011313/122.206.73.83
            InetAddress ia = InetAddress.getLocalHost();
            //获取网卡，获取地址
            byte[] mac = getByInetAddress(ia).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                //字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(str);
                }
            }
            macAddress = sb.toString().toUpperCase();
        } catch (UnknownHostException | SocketException e) {
            // e.printStackTrace();
        }
        log.debug("本机mac地址 = {}", macAddress);
        return macAddress;
    }
}
