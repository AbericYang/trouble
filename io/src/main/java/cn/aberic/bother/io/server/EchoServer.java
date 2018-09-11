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

package cn.aberic.bother.io.server;

import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.io.server.filter.EchoServerFilter;
import cn.aberic.bother.io.server.handler.EchoServerHandler;
import cn.aberic.bother.tools.SystemTool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.SystemPropertyUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 作者：Aberic on 2018/9/9 18:02
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class EchoServer {

    private static EchoServer instance;

    public static EchoServer obtain() {
        if (null == instance) {
            synchronized (EchoServer.class) {
                if (null == instance) {
                    instance = new EchoServer();
                }
            }
        }
        return instance;
    }

    private EchoServer() {}

    public void start(int port) throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 创建Event-LoopGroup。
        // 因为正在使用的是NIO传输，所以指定NioEventLoopGroup来接受和处理新的连接
        // 并且将Channel 的类型指定为NioServer-SocketChannel
        // 是否使用Linux优化版
        EventLoopGroup boss = SystemTool.isLinux() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        EventLoopGroup worker = SystemTool.isLinux() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        // 是否可以使用sun.misc.Unsafe
        ByteBufAllocator byteBufAllocator = SystemPropertyUtil.getBoolean("bytebuf.pool", false) ? PooledByteBufAllocator.DEFAULT : UnpooledByteBufAllocator.DEFAULT;

        try {
            // 创建一个ServerBootstrap 实例
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    // 网络通信的场景下，要求低延迟，禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 允许重复使用本地地址和端口
                    .option(ChannelOption.SO_REUSEADDR, true)
                    // 设置TCP连接，当设置该选项以后，连接会测试链接的状态，
                    // 这个选项用于可能长时间没有数据交流的连接。当设置该选项以后，
                    // 如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, byteBufAllocator)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(64, 1024, 1024 * 1024))
                    // 指定所使用的NIO传输Channel
                    .channel(SystemTool.isLinux() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    // 使用指定的端口设置套接字地址/将本地地址设置为一个具有选定端口的InetSocket-Address
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new EchoServerFilter(Integer.MAX_VALUE, IOContext.LENGTH_FIELD_LENGTH, IOContext.LENGTH_FIELD_OFFSET,
                            IOContext.LENGTH_ADJUSTMENT, IOContext.INITIAL_BYTES_TO_STRIP, false, serverHandler));
            // 异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成
            ChannelFuture future = bootstrap.bind().sync();
            if (future.isSuccess()) {
                log.info("启动并监听连接 {}", future.channel().localAddress());
            }

            // 获取Channel 的CloseFuture，并且阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup，并释放所有的资源，包括所有被创建的线程
            boss.shutdownGracefully().sync();
        }
    }

}
