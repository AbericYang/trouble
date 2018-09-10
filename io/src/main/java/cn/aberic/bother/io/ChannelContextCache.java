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

import cn.aberic.bother.entity.io.Remote;
import cn.aberic.bother.io.client.factory.IOClient;
import cn.aberic.bother.io.client.factory.IOClientFactory;
import cn.aberic.bother.io.client.factory.IONettyClientFactory;
import cn.aberic.bother.io.server.EchoServer;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 作者：Aberic on 2018/9/9 21:34
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class ChannelContextCache {

    /** 作为服务端所接收到的链接集合 */
    private Cache<String, ChannelHandlerContext> ctxServerCache;
    /** 作为客户端所接收到的链接集合 */
    private Cache<String, IOClient> ioClientCache;
    /** 作为应答端所接收到的链接集合 */
    private List<String> ips;
    private IOClientFactory ioClientFactory;

    private static ChannelContextCache instance;

    public static ChannelContextCache obtain() {
        if (null == instance) {
            synchronized (ChannelContextCache.class) {
                if (null == instance) {
                    instance = new ChannelContextCache();
                }
            }
        }
        return instance;
    }

    private ChannelContextCache() {
        ctxServerCache = CacheBuilder.newBuilder().maximumSize(10).expireAfterAccess(15, TimeUnit.MINUTES).build();
        ioClientCache = CacheBuilder.newBuilder().maximumSize(10).expireAfterAccess(15, TimeUnit.MINUTES).build();
        ips = new ArrayList<>();
        ioClientFactory = new IONettyClientFactory();
    }

    /**
     * 根据端口号启动监听服务
     *
     * @param port 端口号
     */
    public void start(int port) {
        try {
            EchoServer.obtain().start(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据远程地址对象启动客户端
     *
     * @param address 远程地址对象
     */
    public void startClient(Remote address) throws Exception {
        log.info("向服务端发起链接 address = {}，port = {}", address.getAddress(), address.getPort());
        ioClientPut(address.getAddress(), ioClientFactory.getOrCreate(address));
    }

    public void serverPut(String ip, ChannelHandlerContext ctx) {
        log.info("存储客户端链接上下文 {}", ip);
        ctxServerCache.put(ip, ctx);
    }

    public ChannelHandlerContext serverGet(String ip) {
        try {
            return ctxServerCache.getIfPresent(ip);
        } catch (Exception e) {
            return null;
        }
    }

    public void serverRemove(String ip) {
        ctxServerCache.invalidate(ip);
    }

    public IOClient ioClientGet(String ip) {
        try {
            return ioClientCache.getIfPresent(ip);
        } catch (Exception e) {
            return null;
        }
    }

    public void ioClientPut(String ip, IOClient ioClient) {
        log.info("存储服务端端链接上下文 {}", ip);
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

    public void sendByServer() {
        ctxServerCache.asMap().forEach((ip, ctx) -> {
            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        });
    }

    public void sendByServer(String ip) {
        ctxServerCache.getIfPresent(ip).writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    public void sendByIOClient() {
        ioClientCache.asMap().forEach((ip, ioClient) -> {
            if (ioClient.isConnected()) {
                ioClient.send(Unpooled.copiedBuffer(new byte[0x00]));
            }
        });
    }

    public void sendByIOClient(String ip, String msg) {
        ioClientCache.getIfPresent(ip).send(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }

    public void sendByIOClient(String ip, byte[] bytes) {
        ioClientCache.getIfPresent(ip).send(Unpooled.copiedBuffer(bytes));
    }

}
