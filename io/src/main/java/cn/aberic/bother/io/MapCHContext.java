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
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：Aberic on 2018/9/9 21:34
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class MapCHContext {

    /** 作为服务端所接收到的链接集合 */
    private Map<String, ChannelHandlerContext> ctxServerMap;
    /** 作为客户端所接收到的链接集合 */
    private Map<String, IOClient> ioClientMap;
    /** 作为应答端所接收到的链接集合 */
    private List<String> ips;
    private IOClientFactory ioClientFactory;

    private static MapCHContext instance;

    public static MapCHContext obtain() {
        if (null == instance) {
            synchronized (MapCHContext.class) {
                if (null == instance) {
                    instance = new MapCHContext();
                }
            }
        }
        return instance;
    }

    private MapCHContext() {
        ctxServerMap = new HashMap<>();
        ioClientMap = new HashMap<>();
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

    public void startClient(Remote address) throws Exception {
        log.info("向服务端发起链接 address = {}，port = {}", address.getAddress(), address.getPort());
        ioClientPut(address.getAddress(), ioClientFactory.getOrCreate(address));
    }

    public void serverPut(String ip, ChannelHandlerContext ctx) {
        log.info("存储客户端链接上下文 {}", ip);
        ctxServerMap.put(ip, ctx);
    }

    public ChannelHandlerContext serverGet(String ip) {
        return ctxServerMap.get(ip);
    }

    public IOClient ioClientGet(String ip) {
        return ioClientMap.get(ip);
    }

    public void ioClientPut(String ip, IOClient ioClient) {
        log.info("存储服务端端链接上下文 {}", ip);
        ioClientMap.put(ip, ioClient);
    }

    public void writeAndFlushServer() {
        ctxServerMap.forEach((ip, ctx) -> {
            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        });
    }

    public void writeAndFlushServer(String ip) {
        ctxServerMap.get(ip).writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    public void writeAndFlushClient() {
//        ctxClientMap.forEach((ip, ctx) -> {
//            ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
//        });
    }

    public void writeAndFlushClient(String ip) {
//        ctxClientMap.get(ip).writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

}
