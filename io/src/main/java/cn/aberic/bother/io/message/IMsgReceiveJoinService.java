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
import cn.aberic.bother.entity.node.*;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.FileTool;
import cn.aberic.bother.tools.MsgPackTool;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 应答加入新节点消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/14 10:49
 * <p>
 * 邮箱：abericyang@gmail.com
 */
interface IMsgReceiveJoinService extends IMsgReceiveJoinResponseService {

    /** 接收到加入新节点协议 */
    default void join(String address, Channel channel, MessageData msgData) throws InvalidProtocolBufferException {
        log().debug("接收加入新节点[{}]协议，执行加入方案", address);
        // 获取请求节点基本信息
        NodeBase nodeBaseJoin = new NodeBase().protoByteArray2Bean(msgData.getBytes());
        nodeBaseJoin.setAddress(address);
        // 遍历当前请求加入新节点的请求合约Hash集合
        nodeBaseJoin.getHashes().forEach(joinsContractHash -> {
            // 跟自身所持有的Hash进行比较，查找其中相同部分
            if (Node.obtain().getNodeBase().getHashes().contains(joinsContractHash)) {
                joinAssist(address, channel, joinsContractHash, nodeBaseJoin);
            } else {
                log().debug("自身无此Hash合约，关闭远程连接及心跳允许");
                shutdown();
            }
        });
    }

    /** 接收到加入新节点协议协助方法 */
    default void joinAssist(String address, Channel channel, String contractHash, NodeBase nodeBaseJoin) {
        // 判断自身在当前Hash合约中的身份
        if (Node.obtain().isElectionNode(contractHash)) { // 表示自身为当前Hash合约的竞选节点之一
            // 检查当前竞选节点集合是否满足Constant.ELECTION_COUNT数量
            if (Node.obtain().getNodeElectionMap().get(contractHash).full()) { // 如果满足Constant.ELECTION_COUNT数量
                // 判断自身下属节点总数是否超额
                if (Node.obtain().getNodeElectionMap().get(contractHash).getNodeCount() >= Constant.NODE_MAX_COUNT_1) {
                    // 获取当前竞选节点集合中下属子节点总数最少的竞选节点地址
                    String addressIdlest = Node.obtain().getNodeElectionMap().get(contractHash).getIdlest();
                    if (addressIdlest != null && addressIdlest.equals("")) { // 节点总数最少也大于等于1000
                        responseJoinAsElection(channel, contractHash, nodeBaseJoin);
                        return;
                    } else if (StringUtils.isNotEmpty(addressIdlest)) {
                        // 告知新的接入地址当前Hash合约下其它竞选节点地址
                        push(channel, ProtocolStatus.JOIN_ASK_ELECTION, addressIdlest);
                        shutdown();
                        return;
                    } else { // 强行新增
                        responseJoinToAssist(channel, contractHash);
                        return;
                    }
                }
                // 先判断自己是否有协助节点
                if (Node.obtain().hasAssistNode(contractHash)) { // 如果有，则将自己的协助节点发回
                    responseJoinToAssist(channel, contractHash);
                    shutdown();
                } else { // 如果没有，则将此节点当做自身的协助节点
                    responseJoinAsAssist(address, channel, contractHash);
                }
            } else { // 如果不满足，则将该节点当做竞选节点之一
                responseJoinAsElection(channel, contractHash, nodeBaseJoin);
            }
        } else if (Node.obtain().isAssistNode(contractHash)) { // 表示自身为当前Hash合约的协助节点之一
            responseJoinFollowMe(channel, contractHash);
        } else if (Node.obtain().hasNode(contractHash)) { // 表示自身为当前Hash合约的普通节点
            // 告知新的接入地址当前Hash合约下的竞选节点地址
            push(channel, ProtocolStatus.JOIN_ASK_ELECTION, Node.obtain().getElectionAddress(contractHash));
        } else {
            log().debug("怪事，关闭远程连接及心跳允许");
            shutdown();
        }
    }

    /** 接收到告知新的接入节点当前Hash合约的竞选节点地址 */
    default void joinAskElection(MessageData msgData) {
        // 作为客户端向当前Hash合约竞选节点发送请求加入协议
        send(MsgPackTool.bytes2String(msgData.getBytes()), ProtocolStatus.JOIN, Node.obtain().getNodeBase());
        shutdown();
    }

    /** 接收到告知新的接入节点准许加入，且为当前Hash合约的竞选节点 */
    default void joinAsElection(MessageData msgData) throws InvalidProtocolBufferException {
        // 获取当前竞选节点集合的基本信息
        NodeElection election = new NodeElection().protoByteArray2Bean(msgData.getBytes());
        Node.obtain().putNodeElection(election.getContractHash(), election);
        // 请求与当前竞选节点集合中的Leader保持长连接
        sendElectionToLeaderHeartBeatKeepAsk(election.getNodeBases().get(0).getAddress(), election.getContractHash());
        shutdown();
    }

    /** 接收到告知新的接入节点准许加入，且为当前Hash合约竞选节点的协助节点 */
    default void joinAsAssist(String address, Channel channel, MessageData msgData) throws InvalidProtocolBufferException {
        // 获取当前NodeHash
        NodeHash nodeHash = new NodeHash().protoByteArray2Bean(msgData.getBytes());
        // 在成为当前Hash合约竞选节点的辅助节点之前，需要先将自身初始化时的默认值或过往值移除
        Node.obtain().removeNodeElection(nodeHash.getContractHash());
        // 新增当前合约Hash访问的竞选中节点
        Node.obtain().putAddressElectionMap(nodeHash.getContractHash(), address);
        // 将自身作为当前合约Hash访问的竞选节点指定协助节点
        Node.obtain().putNodeBaseAssistMap(nodeHash.getContractHash(), Node.obtain().getNodeBase());
        // 新建协助节点对象
        NodeAssist assist = new NodeAssist();
        // 将自身及当前竞选节点加入当前竞选节点下的节点集合
        assist.add(Node.obtain().getNodeBase().clear());
        Node.obtain().putNodeAssistMap(nodeHash.getContractHash(), assist);
        nodeHash.setNodeBase(Node.obtain().getNodeBase().clear());
        send(channel, ProtocolStatus.JOIN_NEW_ASSIST, nodeHash);
    }

    /** 接收到告知当前Hash合约的竞选节点有新的协助节点加入 */
    default void joinNewAssist(String address, Channel channel, MessageData msgData) throws InvalidProtocolBufferException {
        // 获取当前NodeHash
        NodeHash nodeHash = new NodeHash().protoByteArray2Bean(msgData.getBytes());
        // 判断自身是否已有协助节点
        if (Node.obtain().hasAssistNode(nodeHash.getContractHash())) { // 如果已有，则通知其接入协助节点
            responseJoinToAssist(channel, nodeHash.getContractHash());
        } else { // 如果没有，则准许其作为协助节点接入
            NodeBase nodeBase = nodeHash.getNodeBase();
            nodeBase.setAddress(address);
            Node.obtain().putNodeBaseAssistMap(nodeHash.getContractHash(), nodeBase);
            pushKeep(channel);
        }
    }

    /** 接收到告知当前Hash合约的所有竞选节点有新的竞选节点加入 */
    default void joinNewElection(MessageData msgData) throws InvalidProtocolBufferException {
        NodeHash nodeHash = new NodeHash().protoByteArray2Bean(msgData.getBytes());
        if (Node.obtain().isElectionNode(nodeHash.getContractHash())) { // 如果自身是竞选节点
            // 获取当前竞选节点集合中下属子节点总数最少的竞选节点地址
            String addressIdlest = Node.obtain().getNodeElectionMap().get(nodeHash.getContractHash()).getIdlest();
            if (addressIdlest != null && addressIdlest.equals("")) { // 节点总数最少也大于等于1000
                Node.obtain().add(nodeHash.getContractHash(), nodeHash.getNodeBase());
                if (!Node.obtain().isElectionNodeLeader(nodeHash.getContractHash())) {
                    shutdown();
                }
            } else { // 如果竞选节点集合中最小子节点数存在小于1000的，则拒绝此次请求
                if (Node.obtain().isElectionNodeLeader(nodeHash.getContractHash())) { // 如果是Leader节点
                    // 通知新加入节点接入其协助节点
                    nodeHash.setNodeBase(Node.obtain().getNodeBaseAssistMap().get(nodeHash.getContractHash()));
                    send(nodeHash.getNodeBase().getAddress(), ProtocolStatus.JOIN_TO_ASSIST, nodeHash);
                } else { // 如果不是Leader节点，直接关闭本次连接
                    shutdown();
                }
            }
        }
    }

    /** 接收到告知新的接入节点当前Hash合约的竞选节点的协助节点 */
    default void joinToAssist(MessageData msgData) throws InvalidProtocolBufferException {
        // 获取当前Hash合约协助节点对象
        NodeHash nodeHash = new NodeHash().protoByteArray2Bean(msgData.getBytes());
        // 赋值当前合约Hash访问的竞选节点指定协助节点
        Node.obtain().putNodeBaseAssistMap(nodeHash.getContractHash(), nodeHash.getNodeBase());
        // 移除指定合约Hash的协助节点对象，即自身不再担任此Hash合约的协助节点
        Node.obtain().removeNodeAssistMap(nodeHash.getContractHash());
        // 向协助节点继续发送加入请求
        send(nodeHash.getNodeBase().getAddress(), ProtocolStatus.JOIN, Node.obtain().getNodeBase());
        shutdown();
    }

    /** 接收到告知新的接入节点当前Hash合约的基本信息并要求跟随自己 */
    default void joinFollowMe(Channel channel, MessageData msgData) throws InvalidProtocolBufferException {
        Node nodeAssist = Node.obtain().getFromBytes(msgData.getBytes());
        if (nodeAssist.getAddressMap().size() != 1) { // 如果给定的合约Hash不为1，则再次请求锚节点加入
            send(Constant.ANCHOR_IP, ProtocolStatus.JOIN, Node.obtain().getNodeBase());
            shutdown();
            return;
        }
        // 获取当前操作Hash
        String contractHash = nodeAssist.getAddressMap().keySet().iterator().next();
        // 判断自身是否已加入其它协助节点
        if (Node.obtain().hasAssistNode(contractHash)) {
            log().debug("已加入其它协助节点，关闭连接");
            shutdown();
            return;
        }
        // 获取所有已安装合约并比对当前合约是否其中之一，防作恶
        List<String> contractHashList = FileTool.getContractHashList();
        if (!contractHashList.contains(contractHash)) { // 如果给定的合约Hash与已安装不符，则再次请求锚节点加入
            send(Constant.ANCHOR_IP, ProtocolStatus.JOIN, Node.obtain().getNodeBase());
            shutdown();
            return;
        }
        Node.obtain().putNodeBaseAssistMap(contractHash, nodeAssist.getNodeBase());
        Node.obtain().putAddressElectionMap(contractHash, nodeAssist.getAddressElectionMap().get(contractHash));
        Node.obtain().putAddressMap(contractHash, nodeAssist.getAddressMap().get(contractHash));
        send(channel, ProtocolStatus.JOIN_FOLLOW_U, new NodeHash(contractHash, Node.obtain().getNodeBase().clear()));
    }

    /** 告知当前Hash合约的协助节点已经加入该协助节点信息 */
    default void joinFollowU(Channel channel, MessageData msgData) throws InvalidProtocolBufferException {
        // 获取当前NodeHash
        NodeHash nodeHash = new NodeHash().protoByteArray2Bean(msgData.getBytes());
        // 判断自己是否是当前Hash合约的协助节点
        if (Node.obtain().isAssistNode(nodeHash.getContractHash())) { // 如果是协助节点
            // 将此新节点加入到协助节点所管理的下属节点集合中
            Node.obtain().getNodeAssistMap().get(nodeHash.getContractHash()).add(nodeHash.getNodeBase());
            // 通知当前竞选节点更新下属节点数量
            List<String> strings = new ArrayList<>();
            strings.add(nodeHash.getContractHash());
            strings.add(String.valueOf(Node.obtain().getNodeAssistMap().get(nodeHash.getContractHash()).size()));
            send(Node.obtain().getElectionAddress(nodeHash.getContractHash()), ProtocolStatus.JOIN_RESULT_TO_UPGRADE_NODE_COUNT, strings);
            // 对当前Hash合约下的子节点进行排序
            Node.obtain().getNodeAssistMap().get(nodeHash.getContractHash()).sort();
        } else { // 如果不是协助节点
            // 将自己的协助节点发回并关闭连接
            nodeHash.setNodeBase(Node.obtain().getNodeBaseAssistMap().get(nodeHash.getContractHash()));
            push(channel, ProtocolStatus.JOIN_TO_ASSIST, nodeHash);
            shutdown();
        }
    }

    /** 接收到告知当前Hash合约的竞选节点更新其下属子节点总数 */
    default void joinResultToUpgradeNodeCount(String address, MessageData msgData) {
        List<String> strings = MsgPackTool.bytes2List(msgData.getBytes());
        // 如果接收到的参数不正确
        if (null == strings || strings.size() != 2) {
            log().debug("接收到告知当前Hash合约的竞选节点更新其下属子节点总数——参数不正确");
            shutdown();
            return;
        }
        // 判断当前提交请求节点是否为协助节点
        if (!StringUtils.equals(address, Node.obtain().getAssistAddress(strings.get(0)))) {
            log().debug("当前提交请求节点不是协助节点");
            shutdown();
            return;
        }
        // 判断自身是否为当前竞选节点
        if (Node.obtain().isElectionNode(strings.get(0))) { // 如果是竞选节点
            // 更新自己的下属节点总数
            Node.obtain().getNodeElectionMap().get(strings.get(0)).setNodeCount(Integer.valueOf(strings.get(1)));
            // 将自己下属节点总数变更在竞选节点集合中进行广播
            Node.obtain().getNodeElectionMap().get(strings.get(0)).getNodeBases().forEach(nodeBase ->
                    send(nodeBase.getAddress(), ProtocolStatus.ELECTION_UPGRADE_NODE_COUNT, strings));
        } else { // 如果不是竞选节点
            log().debug("当前节点不是竞选节点，无法更新下属节点总数");
            shutdown();
        }
    }

}
