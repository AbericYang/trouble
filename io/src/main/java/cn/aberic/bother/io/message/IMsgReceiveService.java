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
import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.entity.node.NodeElection;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.MsgPackTool;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * 应答消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/12 14:13
 * <p>
 * 邮箱：abericyang@gmail.com
 */
interface IMsgReceiveService extends IMsgJoinService, IMsgElectionService {

    /**
     * 应答消息业务处理方案
     *
     * @param channel 当前指定通道
     * @param msgData 协议消息对象
     */
    default void receive(Channel channel, MessageData msgData) {
        String address = channel.remoteAddress().toString().split(":")[0].split("/")[1];
        log().debug("请求协议：{}，数据ID：{}", msgData.getProtocol().getB(), msgData.getDataId());
        switch (msgData.getProtocol()) {
            case HEART: // 心跳协议
                log().debug("接收心跳协议，什么也不做");
                break;
            case HEART_KEEP_ASK: // 接收到请求保持心跳协议
                // 获取请求该协议的合约Hash
                String contractHash = MsgPackTool.bytes2String(msgData.getBytes());
                // 得到当前Hash合约竞选节点对象
                NodeElection election = Node.obtain().getNodeElectionMap().get(contractHash);
                // 先检查自身是否在当前Hash合约竞选节点集合中
                if (null != election) {
                    // 检查自身是否为当前Hash合约竞选节点集合中的Leader，且当前Hash合约竞选节点集合包含有该节点地址
                    if (election.getNodeBases().get(0).getTimestamp() == Node.obtain().getNodeBase().getTimestamp() &&
                            Node.obtain().getAddressElectionsMap().get(contractHash).getStringList().contains(address)) {
                        pushKeepHeartBeat(channel); // 是Leader节点，告知保持长连接
                    } else { // 自身为当前Hash合约竞选节点之一
                        // 将合约Hash与建议请求地址打包到List中发送
                        List<String> list = new ArrayList<>();
                        list.add(contractHash);
                        list.add(election.getNodeBases().get(0).getAddress());
                        push(channel, ProtocolStatus.HEART_KEEP_ASK_CHANGE, list);
                    }
                } else { // 不是Leader节点
                    // 将合约Hash与建议请求地址打包到List中发送
                    List<String> list = new ArrayList<>();
                    list.add(contractHash);
                    list.add(Node.obtain().getAddressElectionMap().get(contractHash));
                    push(channel, ProtocolStatus.HEART_KEEP_ASK_CHANGE, list);
                }
                break;
            case HEART_KEEP_ASK_CHANGE: // 告知请求长连接节点当前Hash合约竞选节点集合Leader发生变更，并返回一个可以尝试再次发起请求长连接的节点地址
                List<String> arrayResult = MsgPackTool.bytes2List(msgData.getBytes());
                if (null != arrayResult && arrayResult.size() == 2) {
                    sendHeartBeatKeepAsk(arrayResult.get(1), arrayResult.get(0));
                } else {
                    IOContext.obtain().join(Constant.ANCHOR_IP);
                }
                break;
            case JOIN: // 加入新节点协议
            case JOIN_AS_ELECTION: // 告知新的接入节点准许加入，且为当前Hash合约的竞选节点
            case JOIN_ASK_ELECTION: // 告知新的接入节点当前Hash合约的竞选节点地址
            case JOIN_NEW_ELECTION: // 告知当前Hash合约的竞选节点有新的竞选节点加入
                try {
                    join(address, channel, msgData);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case ELECTION: // 接收到通知同组节点尽快完成投票操作
                try {
                    election(address, msgData);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case BLOCK: // 区块协议-0x51
                log().debug("接收区块协议，执行区块同步操作");
                try {
                    Block block = new Block().protoByteArray2Bean(msgData.getBytes());
                    log().debug("block = {}", block.toJsonString());
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
