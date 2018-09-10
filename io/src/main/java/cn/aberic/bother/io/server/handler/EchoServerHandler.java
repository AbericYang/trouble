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

package cn.aberic.bother.io.server.handler;

import cn.aberic.bother.io.ChannelContextCache;
import cn.aberic.bother.tools.DateTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现了业务逻辑，该实例可初始化每一个新的Channel
 * <p>
 * 作者：Aberic on 2018/9/9 18:00
 * 邮箱：abericyang@gmail.com
 */
// 标示一个ChannelHandler可以被多个Channel安全地共享
@ChannelHandler.Sharable
@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /** 空闲次数 */
    private int idleCount = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 加入服务端所接收到的链接集合
        ChannelContextCache.obtain().serverPut(ctx.channel().remoteAddress().toString().split(":")[0].split("/")[1], ctx);
    }

    // 需要处理所有接收到的数据，所以重写channelRead()方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        // 将消息记录到控制台
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        System.out.println("Client IP is " + ctx.channel().remoteAddress().toString());
        // 将接收到的消息写给发送者，而不冲刷出站消息
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 将未决消息冲刷到远程节点
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
    }

    /**
     * 超时处理
     * 如果5秒没有接受客户端的心跳，就触发;
     * 如果超过两次，则直接关闭;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            // 如果读通道处于空闲状态，说明没有接收到心跳命令
            if (IdleState.READER_IDLE.equals(event.state())) {
                log.info("已经{}秒没有接收到客户端的信息了", 5);
                if (idleCount > 2) {
                    log.info("关闭这个不活跃的channel");
                    ctx.channel().close();
                }
                idleCount++;
            }
        } else {
            super.userEventTriggered(ctx, obj);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelContextCache.obtain().serverRemove(ctx.channel().remoteAddress().toString().split(":")[0].split("/")[1]);
        log.info("关闭连接 time = {}", DateTool.getCurrent("yyyy/MM/dd HH:mm:ss"));
        super.channelInactive(ctx);
    }

    // 重写exceptionCaught()方法允许对Throwable的任何子类型做出反应，在这里记录了异常并关闭了连接
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 打印异常栈跟踪
        cause.printStackTrace();
        // 关闭该Channel
        ctx.close();
    }

}