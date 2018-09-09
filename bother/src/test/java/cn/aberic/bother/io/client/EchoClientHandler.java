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

package cn.aberic.bother.io.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 作者：Aberic on 2018/9/9 19:37
 * 邮箱：abericyang@gmail.com
 */
// 标示一个ChannelHandler 可以被多个Channel 安全地共享
// 在客户端使用的是SimpleChannelInboundHandler，而不是在Echo-ServerHandler 中所使用的ChannelInboundHandlerAdapter
// 这和两个因素的相互作用有关：业务逻辑如何处理消息以及Netty 如何管理资源。
// 在客户端，当channelRead0()方法完成时，已经有了传入消息，并且已经处理完它了。
// 当该方法返回时，SimpleChannelInboundHandler 负责释放指向保存该消息的ByteBuf 的内存引用。
// 在EchoServerHandler 中，仍然需要将传入消息回送给发送者，而write()操作是异步的，直到channelRead()方法返回后可能仍然没有完成。
// 为此，EchoServerHandler扩展了ChannelInboundHandlerAdapter，其在这个时间点上不会释放消息。
// 消息在EchoServerHandler 的channelReadComplete()方法中，当writeAndFlush()方法被调用时被释放
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    // 重写了channelActive()方法，其将在一个连接建立时被调用。
    // 这确保了数据将会被尽可能快地写入服务器.
    // 在这个场景下是一个编码了字符串”Netty rocks!”的字节缓冲区。
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知Channel是活跃的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    // 重写了channelRead0()方法。
    // 每当接收数据时，都会调用这个方法。
    // 需要注意的是，由服务器发送的消息可能会被分块接收。
    // 也就是说，如果服务器发送了5 字节，那么不能保证这5 字节会被一次性接收。
    // 即使是对于这么少量的数据，channelRead0()方法也可能会被调用两次，
    // 第一次使用一个持有3 字节的ByteBuf（Netty 的字节容器），第二次使用一个持有2 字节的ByteBuf。
    // 作为一个面向流的协议，TCP 保证了字节数组将会按照服务器发送它们的顺序被接收。
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 记录已接收消息的转储
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 在发生异常时，记录错误并关闭Channel
        cause.printStackTrace();
        ctx.close();
    }

}
