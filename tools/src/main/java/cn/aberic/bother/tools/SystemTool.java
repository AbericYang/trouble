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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：Aberic on 2018/9/1 23:56
 * <p>
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

    /** 获取本机ip地址 */
    public static String getLocalIp() {
        String ip = "";
        //根据网卡取本机配置的IP
        InetAddress inet = null;
        try {
            inet = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        assert inet != null;
        ip = inet.getHostAddress();
        log.debug("ip = {}", ip);
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 获得外网IP
     *
     * @return 外网IP
     */
    public static String getInternetIp() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress inet;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements()) {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements()) {
                    inet = addrs.nextElement();
                    if (inet instanceof Inet4Address && inet.isSiteLocalAddress() && !inet.getHostAddress().equals(getLocalIp())) {
                        return inet.getHostAddress();
                    }
                }
            }

            // 如果没有外网IP，就返回内网IP
            return getLocalIp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断ip地址是否内网ip
     *
     * @param ipAddress ip地址
     *
     * @return 与否
     */
    public static Boolean isIntranetIp(String ipAddress) {
        String reg = "^(192\\.168|172\\.(1[6-9]|2\\d|3[0,1]))(\\.(2[0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){2}$|^10(\\.([2][0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){3}$";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ipAddress);
        return matcher.find();
    }

}
