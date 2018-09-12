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
interface IMsgReceiveService {

    Logger log();

    default void receive(Channel channel, MessageData msgData) {
        log().debug("请求协议：{}，数据ID：{}", msgData.getProtocol().getB(), msgData.getDataId());
        switch (msgData.getProtocol()) {
            case HEART: // 心跳协议-0x00
                log().debug("接收心跳，什么也不做");
                break;
            case JOIN: // 加入协议-0x01
                // 先判断自己是否Leader节点
                if (true) {
                    // TODO: 2018/9/12 自行处理本次业务
                }
                // 先判断自身小组状态是否饱和
                if (false) {
                    // TODO: 2018/9/12 满员则通知Leader节点辅助新加入节点寻找小组
                } else {
                    // TODO: 2018/9/12 未满员则直接通知Leader节点有新节点加入
                }
                log().debug("接收加入，执行加入方案");
                break;
            case JOIN_NEW: // 加入新节点协议，follow节点收到新节点加入通知后，发送此协议告知leader节点有新节点加入请求-0x02
                break;
            case BLOCK: // 区块协议-0x51
                try {
                    BlockProto.Block blockProto = BlockProto.Block.parseFrom(msgData.getBytes());
                    String jsonObject = JsonFormat.printer().print(blockProto);
                    // log().debug("jsonObject = {}", jsonObject);
                    Block block = new Gson().fromJson(jsonObject, Block.class);
                    log().debug("block = {}", block.toString());
                    // TODO: 2018/9/12 验证并存储
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            default: // 非法协议请求直接关闭异常节点连入
                log().info("关闭协议异常channel");
                channel.close();
                break;
        }
        // 将接收到的消息写给发送者，而不冲刷出站消息
        // channel.write(msgData);
    }

}
