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

package cn.aberic.bother.io;

import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.io.Remote;
import cn.aberic.bother.io.exec.client.EchoClient;
import cn.aberic.bother.io.exec.factory.*;
import cn.aberic.bother.io.exec.server.EchoServer;
import cn.aberic.bother.tools.Common;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * IO网络事务
 * <p>
 * 作者：Aberic on 2018/9/9 21:34
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class IOContext {

    /** 长连接服务端读超时时间 */
    public static final long IO_SERVER_READ_TIME_OUT = 10;
    /** 长连接客户端写超时时间，需要比服务端读超时时间短，以维持心跳 */
    public static final long IO_SERVER_WRITE_TIME_OUT = 8;
    /** 作为服务端所接收到的链接集合 */
    private Cache<String, IOServer> ioServerCache;
    /** 作为客户端所接收到的链接集合 */
    private Cache<String, IOClient> ioClientCache;
    /** 作为应答端所接收到的链接集合 */
    private List<String> ips;
    private IOFactory ioServerFactory;
    private IOFactory ioClientFactory;

    private static IOContext instance;

    public static IOContext obtain() {
        if (null == instance) {
            synchronized (IOContext.class) {
                if (null == instance) {
                    instance = new IOContext();
                }
            }
        }
        return instance;
    }

    private IOContext() {
        ioServerCache = CacheBuilder.newBuilder().maximumSize(10).expireAfterAccess(15, TimeUnit.MINUTES).build();
        ioClientCache = CacheBuilder.newBuilder().maximumSize(10).expireAfterAccess(15, TimeUnit.MINUTES).build();
        ips = new ArrayList<>();
        ioServerFactory = new IONettyServerFactory();
        ioClientFactory = new IONettyClientFactory();
    }

    /** 根据端口号启动监听服务 */
    public void start() {
        try {
            EchoServer.obtain().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据远程地址对象启动客户端
     *
     * @param remote 远程地址对象
     */
    public void startClient(Remote remote) throws Exception {
        log.info("向服务端发起链接 address = {}", remote.getAddress());
        new EchoClient().createClient(remote);
    }

    public IOServer ioServerGet(String ip) {
        try {
            return (IOServer) ioServerFactory.getOrCreate(new Remote(ip, Common.NETTY_CONNECT_TIMEOUT_MILLIS));
        } catch (Exception e) {
            return null;
        }
    }

    public void ioServerPut(String ip, IOServer ioServer) {
        log.info("存储客户端链接上下文 {}", ip);
        ioServerCache.put(ip, ioServer);
    }

    public void ioServerRemove(String ip) {
        ioServerCache.invalidate(ip);
    }


    public IOClient ioClientGet(String ip) {
        try {
            return (IOClient) ioClientFactory.getOrCreate(new Remote(ip, Common.NETTY_CONNECT_TIMEOUT_MILLIS));
        } catch (Exception e) {
            return null;
        }
    }

    public void ioClientPut(String ip, IOClient ioClient) {
        log.info("存储服务端链接上下文 {}", ip);
        ioClientCache.put(ip, ioClient);
    }

    public void ioClientRemove(String ip) {
        ioClientCache.invalidate(ip);
    }

    public void add(String ip) {
        ips.add(ip);
    }

    public List<String> getIps() {
        return ips;
    }

    public void broadcast(MessageData msgData) {
        ioServerCache.asMap().forEach((ip, ioServer) -> {
            if (ioServer.isConnected()) {
                ioServer.send(msgData);
            }
        });
        ioClientCache.asMap().forEach((ip, ioClient) -> {
            if (ioClient.isConnected()) {
                ioClient.send(msgData);
            }
        });
    }

}
