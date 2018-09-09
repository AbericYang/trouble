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

package cn.aberic.bother.io.client.factory;

import cn.aberic.bother.entity.io.Remote;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.*;

import java.util.concurrent.TimeUnit;

/**
 * 作者：Aberic on 2018/9/10 01:29
 * 邮箱：abericyang@gmail.com
 */
public class IONettyClient implements IOClient {

    /**
     * 共享定时器
     **/
    private static final Timer timer = new HashedWheelTimer();
    private static final int DEFAULT_HEARTBEAT_PERIOD = 3000;
    private Channel ch;
    private Remote address;

    public IONettyClient(Remote address, Channel ch) {
        this.ch = ch;
        this.address = address;
    }

    @Override
    public void startHeartBeat() {
        timer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) {
                try {
                    // 发送心跳请求
                    ch.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
                } catch (Throwable ignored) {
                } finally {
                    timer.newTimeout(this, DEFAULT_HEARTBEAT_PERIOD, TimeUnit.SECONDS);
                }
            }
        }, DEFAULT_HEARTBEAT_PERIOD, TimeUnit.SECONDS);
    }

    @Override
    public void send(Object msg) {
        ch.writeAndFlush(msg);
    }

    @Override
    public Remote getRemoteAddress() {
        return address;
    }

    @Override
    public boolean isConnected() {
        return ch.isActive();
    }

}
