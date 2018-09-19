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
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.entity.node.NodeBase;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.Channel;

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
    default void receive(Channel channel, MessageData msgData) throws InvalidProtocolBufferException {
        String address = channel.remoteAddress().toString().split(":")[0].split("/")[1];
        log().debug("请求协议：{}，数据ID：{}", msgData.getProtocol().getB(), msgData.getDataId());
        switch (msgData.getProtocol()) {
            case HEART: // 心跳协议
                log().debug("接收心跳协议，什么也不做");
                break;
            case JOIN: // 加入新节点协议
                log().debug("接收加入新节点[{}]协议，执行加入方案", address);
                // 获取请求节点基本信息
                NodeBase nodeBaseJoin = new NodeBase().protoByteArray2Bean(msgData.getBytes());
                nodeBaseJoin.setAddress(address);
                // 遍历当前请求加入新节点的请求合约Hash集合
                nodeBaseJoin.getHashes().forEach(joinsContractHash -> {
                    // 跟自身所持有的Hash进行比较，查找其中相同部分
                    if (Node.obtain().getNodeBase().getHashes().contains(joinsContractHash)) {
                        join(channel, joinsContractHash, nodeBaseJoin);
                    } else {
                        log().debug("自身无此Hash合约，关闭远程连接及心跳允许");
                        shutdown();
                    }
                });
            case JOIN_ASK_ELECTION: // 接收到告知新的接入节点当前Hash合约的竞选节点地址
                joinAskElection(msgData);
                break;
            case JOIN_AS_ELECTION: // 接收到告知新的接入节点准许加入，且为当前Hash合约的竞选节点
                joinAsElection(msgData);
                break;
            case JOIN_AS_ASSIST: // 接收到告知新的接入节点准许加入，且为当前Hash合约竞选节点的协助节点
                joinAsAssist(address, channel, msgData);
                break;
            case JOIN_NEW_ASSIST: // 接收到告知当前Hash合约的竞选节点有新的协助节点加入
                joinNewAssist(address, channel, msgData);
                break;
            case JOIN_NEW_ELECTION: // 接收到告知当前Hash合约的所有竞选节点有新的竞选节点加入
                joinNewElection(msgData);
                break;
            case JOIN_TO_ASSIST: // 接收到告知新的接入节点当前Hash合约的竞选节点的协助节点
                joinToAssist(msgData);
                break;
            case JOIN_FOLLOW_ME: // 接收到告知新的接入节点当前Hash合约的基本信息并要求跟随自己
                joinFollowMe(channel, msgData);
                break;
            case JOIN_FOLLOW_U: // 告知当前Hash合约的协助节点已经加入该协助节点信息
                joinFollowU(channel, msgData);
                break;
            case JOIN_RESULT_TO_UPGRADE_NODE_COUNT: // 接收到告知当前Hash合约的竞选节点更新其下属子节点总数
                joinResultToUpgradeNodeCount(address, msgData);
                break;
            case ELECTION: // 接收到发起选举协议
                election();
                break;
            case ELECTION_TO_LEADER_HEART_KEEP_ASK: // 接收到请求保持心跳协议
                electionToLeaderHeartKeepAsk(channel, address, msgData);
                break;
            case ELECTION_TO_LEADER_HEART_KEEP_ASK_CHANGE: // 接收到告知请求长连接节点当前Hash合约竞选节点集合Leader发生变更，并返回一个可以尝试再次发起请求长连接的节点地址
                electionToLeaderHeartKeepAskChange(msgData);
                break;
            case ELECTION_UPGRADE_NODE_COUNT: // 接收到告知当前Hash合约的竞选节点集合更新其下属子节点总数
                electionUpgradeNodeCount(address, msgData);
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
