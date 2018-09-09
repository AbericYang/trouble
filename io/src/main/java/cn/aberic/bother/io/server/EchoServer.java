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

import cn.aberic.bother.tools.SystemTool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
        EventLoopGroup group;
        if (SystemTool.isLinux()) {
            group = new EpollEventLoopGroup();
        } else {
            group = new NioEventLoopGroup();
        }
        try {
            // 创建一个ServerBootstrap 实例
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(SystemTool.isLinux() ? EpollServerSocketChannel.class : NioServerSocketChannel.class) // 指定所使用的NIO传输Channel
                    .localAddress(new InetSocketAddress(port)) // 使用指定的端口设置套接字地址/将本地地址设置为一个具有选定端口的InetSocket-Address
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 添加一个EchoServer-Handler 到子Channel的ChannelPipeline
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // EchoServerHandlerTest 被标注为@Shareable，所以可以总是使用同样的实例
                            // 这里对于所有的客户端连接来说，都会使用同一个EchoServerHandler，
                            // 因为其被标注为@Sharable
                            ch.pipeline().addLast(serverHandler);
                        }
                    });
            // 异步地绑定服务器；调用sync()方法阻塞等待直到绑定完成
            ChannelFuture f = b.bind().sync();
            log.info("started and listening for connections on {}", f.channel().localAddress());
            // 获取Channel 的CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup，并释放所有的资源，包括所有被创建的线程
            group.shutdownGracefully().sync();
        }
    }

}
