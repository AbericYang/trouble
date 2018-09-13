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
import cn.aberic.bother.entity.consensus.ConnectSelf;
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
interface IMsgReceiveService extends IMsgRequestService {

    Logger log();

    default void receive(Channel channel, MessageData msgData) {
        log().debug("请求协议：{}，数据ID：{}", msgData.getProtocol().getB(), msgData.getDataId());
        switch (msgData.getProtocol()) {
            case HEART: // 心跳协议-0x00
                log().debug("接收心跳协议，什么也不做");
                break;
            case JOIN: // 加入新节点协议，follow节点收到新节点加入通知后，发送此协议告知leader节点有新节点加入请求，leader节点直接处理该协议-0x01
                log().debug("接收加入新节点协议，执行加入方案");
                // 判断自己是否为最小小组的Leader节点
                if (ConnectSelf.obtain().getLevel() >= 1) {
                    // 返回保持心跳协议，继续执行加入新节点方案
                    keepHeartBeat(channel);
                    // 判断自身小组是否满员
                    if (ConnectSelf.obtain().getGroups().get(0).max()) {
                        // 满员则交由下一节点处理
                        // TODO: 2018/9/13 满员则交由下一节点处理
                    } else {
                        // 未满员，则让新节点加入
                        String address = channel.remoteAddress().toString().split(":")[0].split("/")[1];
                        ConnectSelf.obtain().getGroups().get(0).add(address);
                        // 新节点加入，执行广播更新所有子节点信息
                        pushAddNode(address);
                    }
                } else { // 交由当前小组Leader节点处理
                    // TODO: 2018/9/13 交由当前小组Leader节点处理
                }
                break;
            case ADD_NODE: // 由leader节点发出新增小组节点协议-0x02
                break;
            case UPGRADE_NODE: // 由leader节点发出更新小组节点集合协议-0x03
                break;
            case BLOCK: // 区块协议-0x51
                log().debug("接收区块协议，执行区块同步操作");
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
    }

}
