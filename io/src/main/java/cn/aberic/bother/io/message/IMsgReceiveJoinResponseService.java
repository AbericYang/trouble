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

package cn.aberic.bother.io.message;

import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.entity.node.NodeBase;
import cn.aberic.bother.entity.node.NodeHash;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

/**
 * 作者：Aberic on 2018/9/22 20:43
 * 邮箱：abericyang@gmail.com
 */
public interface IMsgReceiveJoinResponseService extends IMsgRequestService {

    /** 告知新的接入节点准许加入，且为当前Hash合约的竞选节点 */
    default void responseJoinAsElection(Channel channel, String contractHash, NodeBase nodeBaseJoin) {
        if (canConnect(nodeBaseJoin.getAddress())) {
            // 新增竞选节点
            Node.obtain().add(contractHash, nodeBaseJoin);
            // 当前Hash合约竞选节点集合内部广播新节点加入
            Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().forEach(
                    nodeBase -> send(nodeBase.getAddress(), ProtocolStatus.JOIN_NEW_ELECTION, new NodeHash(contractHash, nodeBaseJoin)));
            // 将自身在当前竞选节点集合中的信息push给当前加入节点
            push(channel, ProtocolStatus.JOIN_AS_ELECTION, Node.obtain().getNodeElectionMap().get(contractHash));
            shutdown();
        } else { // 强行新增
            responseJoinToAssist(channel, contractHash);
            shutdown();
        }
    }

    /** 告知新的接入节点准许加入，且为当前Hash合约竞选节点的协助节点 */
    default void responseJoinAsAssist(String address, Channel channel, String contractHash) {
        if (canConnect(address)) {
            NodeHash nodeHash = new NodeHash(contractHash, Node.obtain().getNodeBase().clear());
            push(channel, ProtocolStatus.JOIN_AS_ASSIST, nodeHash);
        } else {
            responseJoinFollowMe(channel, contractHash);
        }
    }

    /** 告知新的接入节点当前Hash合约的竞选节点的协助节点 */
    default void responseJoinToAssist(Channel channel, String contractHash) {
        NodeHash nodeHash = new NodeHash(contractHash, Node.obtain().getNodeBaseAssistMap().get(contractHash));
        push(channel, ProtocolStatus.JOIN_TO_ASSIST, nodeHash);
        shutdown();
    }

    /** 告知新的接入节点当前Hash合约的基本信息并要求跟随自己 */
    default void responseJoinFollowMe(Channel channel, String contractHash) {
        // 将自己当前竞选中的节点集合以及备用节点集合发回
        Node node = Node.obtain();
        // 将无用信息赋空
        // 使用迭代器的remove()方法删除元素
        node.getAddressElectionMap().entrySet().removeIf(stringStringEntry -> !StringUtils.equals(stringStringEntry.getKey(), contractHash));
        node.getAddressMap().entrySet().removeIf(stringStringEntry -> !StringUtils.equals(stringStringEntry.getKey(), contractHash));
        node.getNodeAssistMap().clear();
        node.getNodeElectionMap().clear();
        node.getNodeBase().clear();
        push(channel, ProtocolStatus.JOIN_FOLLOW_ME, node);
    }

}
