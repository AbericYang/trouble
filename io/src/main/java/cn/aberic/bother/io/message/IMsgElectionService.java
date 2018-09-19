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

import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.entity.node.NodeBase;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.MsgPackTool;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 应答选举消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/14 14:08
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public interface IMsgElectionService extends IMsgRequestService {

    /**
     * 应答选举消息业务处理方案，由{@link IMsgReceiveService}继承并启用该方案
     *
     * @param address 当前指定通道的连接地址
     * @param msgData 协议消息对象
     */
    default void election(Channel channel, String address, MessageData msgData) throws InvalidProtocolBufferException {
        switch (msgData.getProtocol()) {
            case ELECTION: // 接收到发起选举协议
                break;
            case ELECTION_TO_LEADER_HEART_KEEP_ASK: // 接收到请求保持心跳协议
                // 获取请求该协议的合约Hash
                String contractHash = MsgPackTool.bytes2String(msgData.getBytes());
                // 先检查自身是否在当前Hash合约竞选节点集合中
                if (Node.obtain().isElectionNode(contractHash)) {
                    // 确定本节点是否包含请求心跳的地址
                    boolean hasAddress = false;
                    for (NodeBase nodeBase: Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases()) {
                        if (StringUtils.equals(nodeBase.getAddress(), address)) {
                            hasAddress = true;
                            break;
                        }
                    }
                    // 检查自身是否为当前Hash合约竞选节点集合中的Leader，且当前Hash合约竞选节点集合包含有该节点地址
                    if (Node.obtain().isElectionNode(contractHash) && hasAddress) {
                        pushKeep(channel); // 是Leader节点，告知保持长连接
                    } else { // 自身为当前Hash合约竞选节点之一
                        // 将合约Hash与建议请求地址打包到List中发送
                        List<String> list = new ArrayList<>();
                        list.add(contractHash);
                        list.add(Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().get(0).getAddress());
                        push(channel, ProtocolStatus.ELECTION_TO_LEADER_HEART_KEEP_ASK_CHANGE, list);
                    }
                } else { // 不是Leader节点
                    // 将合约Hash与建议请求地址打包到List中发送
                    List<String> list = new ArrayList<>();
                    list.add(contractHash);
                    list.add(Node.obtain().getAddressElectionMap().get(contractHash));
                    push(channel, ProtocolStatus.ELECTION_TO_LEADER_HEART_KEEP_ASK_CHANGE, list);
                }
                break;
            case ELECTION_TO_LEADER_HEART_KEEP_ASK_CHANGE: // 接收到告知请求长连接节点当前Hash合约竞选节点集合Leader发生变更，并返回一个可以尝试再次发起请求长连接的节点地址
                List<String> arrayResult = MsgPackTool.bytes2List(msgData.getBytes());
                if (null != arrayResult && arrayResult.size() == 2) {
                    sendHeartBeatKeepAsk(arrayResult.get(1), arrayResult.get(0));
                } else {
                    IOContext.obtain().join(Constant.ANCHOR_IP);
                }
                break;
            case ELECTION_UPGRADE_NODE_COUNT: // 接收到告知当前Hash合约的竞选节点集合更新其下属子节点总数
                List<String> strings = MsgPackTool.bytes2List(msgData.getBytes());
                // 如果接收到的参数不正确
                if (null == strings || strings.size() != 2) {
                    log().debug("接收到告知当前Hash合约的竞选节点集合更新其下属子节点总数——参数不正确");
                    break;
                }
                // 判断自身是否为当前竞选节点
                if (Node.obtain().isElectionNode(strings.get(0))) { // 如果是竞选节点
                    // 更新当前作为竞选节点之一请求地址的下属子节点总数
                    Node.obtain().getNodeElectionMap().get(strings.get(0)).put(address, Integer.valueOf(strings.get(1)));
                } else { // 如果不是竞选节点
                    log().debug("当前节点不是竞选节点，无法更新当前请求节点其下属节点总数");
                }
                break;
        }
    }

    default void electionExec(String address, MessageData msgData) {
    }

}