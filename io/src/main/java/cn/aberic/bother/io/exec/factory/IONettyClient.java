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

package cn.aberic.bother.io.exec.factory;

import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.io.Remote;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * IO 客户端操作实现
 * <p>
 * 作者：Aberic on 2018/9/10 01:29
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class IONettyClient implements IOClient {

    private static final int DEFAULT_HEARTBEAT_TRY_AGAIN_PERIOD = 5;
    private Bootstrap bootstrap;
    private Channel channel;
    private Remote remote;

    /** 是否关闭远程连接 */
    private boolean shutdown = false;

    public IONettyClient(Bootstrap bootstrap, Remote remote, Channel channel) {
        this.bootstrap = bootstrap;
        this.channel = channel;
        this.remote = remote;
    }

    @Override
    public void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture future = bootstrap.connect();
        future.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                log.info("与服务器建立连接成功");
                channel = futureListener.channel();
            } else {
                log.info("与服务器建立连接失败，{}秒后再次尝试", DEFAULT_HEARTBEAT_TRY_AGAIN_PERIOD);
                futureListener.channel().eventLoop().schedule(this::doConnect, DEFAULT_HEARTBEAT_TRY_AGAIN_PERIOD, TimeUnit.SECONDS);
            }
        });
    }

    @Override
    public void send(MessageData msgData) {
        channel.writeAndFlush(msgData);
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public Remote getRemote() {
        return remote;
    }

    @Override
    public void shutdown() {
        shutdown = true;
        channel.close();
    }

    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

}
