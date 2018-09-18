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
import cn.aberic.bother.entity.node.NodeElection;
import cn.aberic.bother.entity.node.NodeHash;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.FileTool;
import cn.aberic.bother.tools.MsgPackTool;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 应答加入新节点消息业务处理接口
 * <p>
 * version 1.0
 * 当前版本是请求加入的新节点自行不断遍历所有网络中的楼来询问加入
 * 直到询问到尽头依旧没有找到可加入楼的时候
 * 会自行创建一个新楼来匹配到目前的网络环境中
 * <p>
 * 作者：Aberic on 2018/09/14 10:49
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public interface IMsgJoinService extends IMsgRequestService {

    /**
     * 应答加入新节点消息业务处理方案，由{@link IMsgReceiveService}继承并启用该方案
     *
     * @param address 当前指定通道的连接地址
     * @param channel 当前指定通道
     * @param msgData 协议消息对象
     */
    default void join(String address, Channel channel, MessageData msgData) throws InvalidProtocolBufferException {
        switch (msgData.getProtocol()) {
            case JOIN: // 接收到加入新节点协议
                log().debug("接收加入新节点[{}]协议，执行加入方案", address);
                // 获取请求节点基本信息
                NodeBase nodeBaseJoin = new NodeBase().protoByteArray2Bean(msgData.getBytes());
                nodeBaseJoin.setAddress(address);
                // 遍历当前请求加入新节点的请求合约Hash集合
                nodeBaseJoin.getHashes().forEach(joinsContractHash -> {
                    // 跟自身所持有的Hash进行比较，查找其中相同部分
                    if (Node.obtain().getNodeBase().getHashes().contains(joinsContractHash)) {
                        joinExec(channel, joinsContractHash, nodeBaseJoin);
                    } else {
                        log().debug("自身无此Hash合约，关闭远程连接及心跳允许");
                        shutdown();
                    }
                });
                break;
            case JOIN_ASK_ELECTION: // 接收到告知新的接入节点当前Hash合约的竞选节点地址
                // 作为客户端向当前Hash合约竞选节点发送请求加入协议
                send(MsgPackTool.bytes2String(msgData.getBytes()), ProtocolStatus.JOIN, Node.obtain().getNodeBase());
                shutdown();
                break;
            case JOIN_AS_ELECTION: // 接收到告知新的接入节点准许加入，且为当前Hash合约的竞选节点
                // 获取当前竞选节点集合的基本信息
                NodeElection election = new NodeElection().protoByteArray2Bean(msgData.getBytes());
                Node.obtain().putNodeElection(election.getContractHash(), election);
                // 请求与当前竞选节点集合中的Leader保持长连接
                sendHeartBeatKeepAsk(election.getNodeBases().get(0).getAddress(), election.getContractHash());
                shutdown();
                break;
            case JOIN_NEW_ELECTION: // 接收到告知当前Hash合约的所有竞选节点有新的竞选节点加入
                NodeHash nodeHash = new NodeHash().protoByteArray2Bean(msgData.getBytes());
                Node.obtain().add(nodeHash.getContractHash(), nodeHash.getNodeBase());
                break;
            case JOIN_TO_ASSIST: // 接收到告知新的接入节点当前Hash合约的竞选节点的协助节点
                // 获取当前Hash合约协助节点对象
                nodeHash = new NodeHash().protoByteArray2Bean(msgData.getBytes());
                // 赋值当前合约Hash访问的竞选节点指定协助节点
                Node.obtain().getNodeBaseAssistMap().put(nodeHash.getContractHash(), nodeHash.getNodeBase());
                // 向协助节点继续发送加入请求
                send(nodeHash.getNodeBase().getAddress(), ProtocolStatus.JOIN, Node.obtain().getNodeBase());
                shutdown();
                break;
            case JOIN_FOLLOW_ME: // 接收到告知新的接入节点当前Hash合约的基本信息并要求跟随自己
                Node nodeAssist = Node.obtain().getFromBytes(msgData.getBytes());
                if (nodeAssist.getAddressMap().size() != 1) { // 如果给定的合约Hash不为1，则再次请求锚节点加入
                    send(Constant.ANCHOR_IP, ProtocolStatus.JOIN, Node.obtain().getNodeBase());
                    shutdown();
                    break;
                }
                String contractHash = "";
                for (String key : nodeAssist.getAddressMap().keySet()) {
                    contractHash = key; // 获取当前操作Hash
                }
                // 获取所有已安装合约并比对当前合约是否其中之一，防作恶
                List<String> contractHashList = FileTool.getContractHashList();
                if (!contractHashList.contains(contractHash)) { // 如果给定的合约Hash与已安装不符，则再次请求锚节点加入
                    send(Constant.ANCHOR_IP, ProtocolStatus.JOIN, Node.obtain().getNodeBase());
                    shutdown();
                    break;
                }
                Node.obtain().putAddressElectionMap(contractHash, nodeAssist.getAddressElectionMap().get(contractHash));
                Node.obtain().putAddressMap(contractHash, nodeAssist.getAddressMap().get(contractHash));
                break;
        }
    }

    default void joinExec(Channel channel, String contractHash, NodeBase nodeBaseJoin) {
        // 判断自身在当前Hash合约中的身份
        if (Node.obtain().isElectionNode(contractHash)) { // 表示自身为当前Hash合约的竞选节点之一
            // 检查当前竞选节点集合是否满足Constant.ELECTION_COUNT数量
            if (Node.obtain().getNodeElectionMap().get(contractHash).full()) { // 如果满足Constant.ELECTION_COUNT数量，将自己的协助节点发回
                NodeBase nodeBaseAssist = Node.obtain().getNodeBaseAssistMap().get(contractHash);
                NodeHash nodeHash = new NodeHash(contractHash, nodeBaseAssist);
                push(channel, ProtocolStatus.JOIN_TO_ASSIST, nodeHash);
            } else { // 如果不满足，则将该节点当做竞选节点之一
                Node.obtain().add(contractHash, nodeBaseJoin);
                // 当前Hash合约竞选节点集合内部广播新节点加入
                Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().forEach(
                        nodeBase -> send(nodeBase.getAddress(), ProtocolStatus.JOIN_NEW_ELECTION, new NodeHash(contractHash, nodeBaseJoin)));
                // 将自身在当前竞选节点集合中的信息push给当前加入节点
                push(channel, ProtocolStatus.JOIN_AS_ELECTION, Node.obtain().getNodeElectionMap().get(contractHash));
            }
        } else if (Node.obtain().isAssistNode(contractHash)) { // 表示自身为当前Hash合约的协助节点之一
            // 将自己当前竞选中的节点集合以及备用节点集合发回
            Node node = Node.obtain();
            // 将无用信息赋空
            node.setNodeBase(null);
            // 使用迭代器的remove()方法删除元素
            node.getAddressElectionMap().entrySet().removeIf(stringStringEntry -> !StringUtils.equals(stringStringEntry.getKey(), contractHash));
            node.getAddressMap().entrySet().removeIf(stringStringEntry -> !StringUtils.equals(stringStringEntry.getKey(), contractHash));
            push(channel, ProtocolStatus.JOIN_FOLLOW_ME, node);
        } else if (Node.obtain().hasNode(contractHash)) { // 表示自身为当前Hash合约的普通节点
            // 告知新的接入地址当前Hash合约下的竞选节点地址
            push(channel, ProtocolStatus.JOIN_ASK_ELECTION, Node.obtain().getAddressElectionMap().get(contractHash));
        } else {
            log().debug("怪事，关闭远程连接及心跳允许");
            shutdown();
        }
    }

}
