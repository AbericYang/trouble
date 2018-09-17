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

package cn.aberic.bother.io.code;

import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.tools.ByteTool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * 作者：Aberic on 2018/9/10 22:52
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class TroubleDecode extends ByteToMessageDecoder implements TroubleCode {

    /**
     * 在{@link cn.aberic.bother.entity.io.MessageData}类中定义了type、length和dataId，这都放在消息头部
     * type占1个字节，length和dataId各占4个字节所以头部总长度是9个字节
     */
    private static final int HEADER_SIZE = 9;

    @Override
    public Logger log() {
        return log;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int size = in.readableBytes();
        if (size < HEADER_SIZE) {
            throw new Exception("错误的消息");
        }

        byte[] bytes = new byte[]{in.getByte(5), in.getByte(6), in.getByte(7), in.getByte(8)};
        int length = ByteTool.bytesToInt(bytes);
        log().debug("数据包大小：{}，数据体长度：{}", size, length);
        if (size < length) {
            return;
        }

        byte protocolId = in.readByte();

        ByteBuf buf = in.readBytes(4);
        bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        int dataId = ByteTool.bytesToInt(bytes);

        buf = in.readBytes(4);
        bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        length = ByteTool.bytesToInt(bytes);
        log().debug("数据包大小：{}，协议号：{}，数据体长度：{}， 数据ID：{}", size, protocolId, length, dataId);
        if (size < length) {
            return;
        }
        // 在读的过程中，每读一次读过的字节即被抛弃，即指针会往前跳
        MessageData msgData = new MessageData();
        ProtocolStatus protocolStatus = ProtocolStatus.get(protocolId);
        if (null == protocolStatus) {
            throw new RuntimeException("无此传输协议");
        }
        msgData.setProtocol(protocolStatus);
        msgData.setLength(length);
        msgData.setDataId(dataId);

        if (in.readableBytes() < length) {
            throw new Exception("消息不正确");
        }

        buf = in.readBytes(length);
        bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        List<Byte> receiveBytesList = new LinkedList<>();
        for (byte b : bytes) {
            // 将接收到的字节流加入接收队列
            receiveBytesList.add(b);
        }
        out.add(analysis(msgData, receiveBytesList));
    }
}
