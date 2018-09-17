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

package cn.aberic.bother.io.exec.client;

import cn.aberic.bother.entity.io.Remote;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.io.exec.factory.IOClient;
import cn.aberic.bother.io.exec.factory.IONettyClient;
import cn.aberic.bother.io.filter.EchoClientFilter;
import cn.aberic.bother.io.handler.EchoClientHandler;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.SystemTool;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.SystemPropertyUtil;

import java.net.InetSocketAddress;

/**
 * 作者：Aberic on 2018/9/9 19:42
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public class EchoClient {

    public IOClient createClient(Remote remote) throws Exception {
        final EchoClientHandler clientHandler = new EchoClientHandler();
        // 是否使用Linux优化版
        EventLoopGroup group = SystemTool.isLinux() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        // 是否可以使用sun.misc.Unsafe
        ByteBufAllocator byteBufAllocator = SystemPropertyUtil.getBoolean("bytebuf.pool", false) ? PooledByteBufAllocator.DEFAULT : UnpooledByteBufAllocator.DEFAULT;

        // 创建Bootstrap
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group) // 指定EventLoopGroup 以处理客户端事件；需要适用于NIO 的实现
                // 网络通信的场景下，要求低延迟
                .option(ChannelOption.TCP_NODELAY, true)
                // 允许重复使用本地地址和端口
                .option(ChannelOption.SO_REUSEADDR, true)
                // 设置TCP连接，当设置该选项以后，连接会测试链接的状态，
                // 这个选项用于可能长时间没有数据交流的连接。当设置该选项以后，
                // 如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, byteBufAllocator)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 1024 * 1024))
                // 适用于NIO 传输的Channel 类型
                .channel(NioSocketChannel.class)
                // 设置服务器的InetSocketAddress
                .remoteAddress(new InetSocketAddress(remote.getAddress(), Constant.NETTY_SERVER_PORT))
                // 在创建Channel 时向ChannelPipeline中添加一个EchoClientHandler 实例
                .handler(new EchoClientFilter(clientHandler));

        if (remote.getTimeOut() < 1000) {
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
        } else {
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, remote.getTimeOut());
        }
        // 异步地绑定服务；调用sync()方法阻塞等待直到绑定完成
        ChannelFuture future = bootstrap.connect().sync();
        if (future.awaitUninterruptibly(remote.getTimeOut()) && future.isSuccess() && future.channel().isActive()) {
            IOClient ioClient = new IONettyClient(bootstrap, remote, future.channel());
            clientHandler.setIoClient(ioClient);
            // 将 ioClient 置入缓存
            IOContext.obtain().ioClientPut(remote.getAddress(), ioClient);
            return ioClient;
        } else {
            future.cancel(true);
            future.channel().close();
            throw new Exception(remote.getAddress());
        }
    }

}
