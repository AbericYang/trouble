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

import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.tools.ByteTool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 作者：Aberic on 2018/9/10 22:49
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class TroubleEncode extends MessageToByteEncoder<MessageData> implements TroubleCode {

    @Override
    public Logger log() {
        return log;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageData msgData, ByteBuf out) {
        int dataId = createDataId();
        msgData.setDataId(dataId);
        out.writeByte(msgData.getProtocol().getB());
        out.writeBytes(ByteTool.intToBytes(msgData.getDataId()));
        switch (msgData.getProtocol()) {
            case HEART: // 心跳协议-0x00
            case KEEP: // 保持心跳协议-0x01
            case BYE: // 关闭心跳协议-0x02
            case OK: // 应答协议-0x03
            case CREATE_GROUP: // 当前没有可加入小组，自建小组并参与小组间选举协议-0x08
            case JOIN: // 加入新节点协议 follow节点收到新节点加入通知后，发送此协议告知leader节点有新节点加入请求 leader节点直接处理该协议-0x04
                byte[] bytes = createEmpty();
                out.writeBytes(ByteTool.intToBytes(bytes.length));
                out.writeBytes(bytes);
                break;
            default:
                bytes = createData(msgData.getBytes());
                out.writeBytes(ByteTool.intToBytes(bytes.length));
                out.writeBytes(bytes);
                break;
        }
    }

}
