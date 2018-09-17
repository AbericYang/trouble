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
 */

package cn.aberic.bother.io.handler;

import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.io.exec.factory.IOClient;
import cn.aberic.bother.io.message.IMsgService;
import cn.aberic.bother.tools.DateTool;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 作者：Aberic on 2018/9/9 19:37
 * <p>
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
@Slf4j
public class EchoClientHandler extends SimpleChannelInboundHandler<MessageData> implements IMsgService {

    /** 循环次数 */
    private int loopCount = 1;
    private IOClient ioClient;
    /** 是否保持心跳 */
    private boolean keepHeartBeat = false;

    @Override
    public Logger log() {
        return log;
    }

    public void setIoClient(IOClient ioClient) {
        this.ioClient = ioClient;
    }

    // 重写了channelActive()方法，其将在一个连接建立时被调用。
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("建立连接 time = {}", DateTool.getCurrent("yyyy/MM/dd HH:mm:ss"));
        ctx.fireChannelActive();
    }

    // 重写了channelRead0()方法。
    // 每当接收数据时，都会调用这个方法。
    // 需要注意的是，由服务器发送的消息可能会被分块接收。
    // 也就是说，如果服务器发送了5 字节，那么不能保证这5 字节会被一次性接收。
    // 即使是对于这么少量的数据，channelRead0()方法也可能会被调用两次，
    // 第一次使用一个持有3 字节的ByteBuf（Netty 的字节容器），第二次使用一个持有2 字节的ByteBuf。
    // 作为一个面向流的协议，TCP 保证了字节数组将会按照服务器发送它们的顺序被接收。
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageData msgData) {
        switch (msgData.getProtocol()) {
            case KEEP: // 保持心跳协议-0x01
                log().debug("保持心跳协议");
                keepHeartBeat = true;
                break;
            case BYE: // 关闭心跳协议-0x02
                log().debug("关闭心跳协议");
                keepHeartBeat = false;
                ioClient.shutdown();
                break;
            case JOIN_ACCEPT: // 告知新的接入地址可加入协议-0x05
                log().debug("告知新的接入地址可加入协议，保持心跳协议");
                keepHeartBeat = true;
                receive(ctx.channel(), msgData);
                break;
            default:
                receive(ctx.channel(), msgData);
                break;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            if (IdleState.WRITER_IDLE.equals(event.state()) && keepHeartBeat) {  // 如果写通道处于空闲状态,就发送心跳命令
                if (ioClient.isShutdown()) {
                    ctx.channel().close();
                }
                sendHeartBeat(ctx.channel());
                log.debug("发送心跳请求 {}, 第{}次", DateTool.getCurrent("yyyy/MM/dd HH:mm:ss"), loopCount);
                loopCount++;
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        IOContext.obtain().ioClientRemove(ctx.channel().remoteAddress().toString().split(":")[0].split("/")[1]);
        log.info("关闭连接 time = {}", DateTool.getCurrent("yyyy/MM/dd HH:mm:ss"));
        super.channelInactive(ctx);
        if (!ioClient.isShutdown() && keepHeartBeat) {
            ioClient.doConnect();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 在发生异常时，记录错误并关闭Channel
        cause.printStackTrace();
        ctx.close();
    }

}
