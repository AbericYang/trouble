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

package cn.aberic.bother.io.message;

import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.proto.BlockProto;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.Channel;
import org.slf4j.Logger;

/**
 * 应答消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/12 14:13
 * 邮箱：abericyang@gmail.com
 */
interface IMsgAnswerService {

    Logger log();

    default void exec(Channel channel, MessageData msgData) {
        log().debug("请求协议：{}，数据ID：{}", msgData.getProtocolId(), msgData.getDataId());
        switch (msgData.getProtocolId()) {
            case 0x00: // 接收到心跳包
                log().debug("接收心跳，什么也不做");
                break;
            case 0x01: // 接收到区块包
                try {
                    BlockProto.Block blockProto = BlockProto.Block.parseFrom(msgData.getBytes());
                    String jsonObject = JsonFormat.printer().print(blockProto);
                    log().debug("jsonObject = {}", jsonObject);
                    Block block = new Gson().fromJson(jsonObject, Block.class);
                    log().debug("block = {}", block.toString());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            default:
                log().info("关闭协议异常channel");
                channel.close();
                break;
        }
        // 将接收到的消息写给发送者，而不冲刷出站消息
//        channel.write(msgData);
    }

}
