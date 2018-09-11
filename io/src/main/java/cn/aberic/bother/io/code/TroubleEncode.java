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
    protected void encode(ChannelHandlerContext ctx, MessageData msgData, ByteBuf out) throws Exception {
        int dataId = createDataId();
        msgData.setDataId(dataId);
        out.writeByte(msgData.getProtocolId());
        out.writeBytes(ByteTool.intToBytes(msgData.getDataId()));
        byte protocolId = msgData.getProtocolId();
        switch (protocolId) {
            case 0x00: // 创建并发送心跳包
                byte[] bytes = createHeart();
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
