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
import cn.aberic.bother.entity.response.IResponse;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.io.OutBlock;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.HttpTool;
import cn.aberic.bother.tools.MsgPackTool;
import com.alibaba.fastjson.JSONObject;
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
interface IMsgReceiveElectionService extends IMsgRequestService {

    /** 接收到发起选举协议 */
    default void election() {

    }

    /** 接收到请求保持心跳协议 */
    default void electionToLeaderHeartKeepAsk(Channel channel, String address, MessageData msgData) {
        // 获取请求该协议的合约Hash
        String contractHash = MsgPackTool.bytes2String(msgData.getBytes());
        // 先检查自身是否为指定Hash合约竞选节点集合中的Leader
        if (Node.obtain().isElectionNodeLeader(contractHash)) { // 如果是指定Hash合约竞选节点集合中的Leader
            // 确定本节点是否包含请求心跳的地址
            boolean hasAddress = false;
            for (NodeBase nodeBase : Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases()) {
                if (StringUtils.equals(nodeBase.getAddress(), address)) {
                    hasAddress = true;
                    break;
                }
            }
            if (!hasAddress) { // 如果竞选节点集合中没有当前请求地址
                log().debug("竞选节点集合中没有当前请求地址，关闭连接");
                shutdown();
                return;
            }
            pushKeep(channel); // 是Leader节点，告知保持长连接
        } else { // 不是Leader节点
            // 将合约Hash与建议请求地址打包到List中发送
            List<String> list = new ArrayList<>();
            list.add(contractHash);
            list.add(Node.obtain().getAddressElectionMap().get(contractHash));
            push(channel, ProtocolStatus.ELECTION_TO_LEADER_HEART_KEEP_ASK_CHANGE, list);
            shutdown();
        }
    }

    /** 接收到告知请求长连接节点当前Hash合约竞选节点集合Leader发生变更，并返回一个可以尝试再次发起请求长连接的节点地址 */
    default void electionToLeaderHeartKeepAskChange(MessageData msgData) {
        List<String> arrayResult = MsgPackTool.bytes2List(msgData.getBytes());
        if (null != arrayResult && arrayResult.size() == 2) { // 如果接收数据正常
            // 向新的竞选节点集合Leader节点发起心跳请求
            sendElectionToLeaderHeartBeatKeepAsk(arrayResult.get(1), arrayResult.get(0));
            shutdown();
        } else { // 接收数据不正确
            // 重新向锚节点发起加入请求
            IOContext.obtain().join(Constant.ANCHOR_IP);
            shutdown();
        }
    }

    /** 接收到告知当前Hash合约的竞选节点集合更新其下属子节点总数 */
    default void electionUpgradeNodeCount(String address, MessageData msgData) {
        List<String> strings = MsgPackTool.bytes2List(msgData.getBytes());
        // 如果接收到的参数不正确
        if (null == strings || strings.size() != 2) {
            log().debug("接收到告知当前Hash合约的竞选节点集合更新其下属子节点总数——参数不正确");
            shutdown();
            return;
        }
        // 判断自身是否为当前竞选节点
        if (Node.obtain().isElectionNode(strings.get(0))) { // 如果是竞选节点
            // 更新当前作为竞选节点之一请求地址的下属子节点总数
            Node.obtain().getNodeElectionMap().get(strings.get(0)).put(address, Integer.valueOf(strings.get(1)));
            if (Node.obtain().isElectionNodeLeader(strings.get(0))) { // 如果自身不是竞选节点集合中的Leader，关闭连接
                shutdown();
            }
        } else { // 如果不是竞选节点
            log().debug("当前节点不是竞选节点，无法更新当前请求节点其下属节点总数");
            shutdown();
        }
    }

    /** 申请竞选节点Leader强制更换协议 */
    default void electionLeaderChangeForceRequest(String contractHash) {
        long now = System.currentTimeMillis();
        // 检测是否超出出块时间 或者 已经无法连接到当前Hash的Leader节点
        if (now - Node.obtain().getNodeElectionMap().get(contractHash).getTimestamp() > Constant.NODE_ELECTION_OUT_BLOCK_TIME ||
                !canConnect(Node.obtain().getNodeElectionMap().get(contractHash).obtainLeaderAddress())) { // 如果到了出块时间
            // 如果当前节点为竞选节点集合中Leader节点的下一节点
            if (Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().get(1).getTimestamp() == Node.obtain().getNodeBase().getTimestamp()) {
                log().debug("当前节点为竞选节点集合中Leader节点的下一节点。开始出块");
                // 将当前Hash合约竞选节点集合中的Leader节点强制移除并广播出去
                Node.obtain().getNodeElectionMap().get(contractHash).removeLeader();
                // 广播告知当前Hash合约竞选节点集合中的所有节点强制更换Leader
                Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().forEach(nodeBase ->
                        IOContext.obtain().send(nodeBase.getAddress(), ProtocolStatus.ELECTION_LEADER_CHANGE_FORCE, contractHash));
                shutdown();
                // 执行出块操作
                new OutBlock(contractHash).publish();
            } else { // 如果当前节点不是竞选节点集合中Leader节点的下一节点
                log().debug("当前节点不是竞选节点集合中Leader节点的下一节点。同步下一节点出块，当前出块节点放弃出块权");
                // 同步下一节点出块，当前出块节点放弃出块权
                IOContext.obtain().send(
                        Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().get(1).getAddress(),
                        ProtocolStatus.ELECTION_LEADER_CHANGE_FORCE_REQUEST,
                        contractHash);
            }
        } else {
            // 关闭连接
            shutdown();
        }
    }

    /** 竞选节点Leader强制更换协议 */
    default void electionLeaderChangeForce(String address, String contractHash) {
        long now = System.currentTimeMillis();
        // 检测是否超出出块时间
        if (now - Node.obtain().getNodeElectionMap().get(contractHash).getTimestamp() > Constant.NODE_ELECTION_OUT_BLOCK_TIME ||
                !canConnect(Node.obtain().getNodeElectionMap().get(contractHash).obtainLeaderAddress())) { // 如果到了出块时间
            // 获取竞选节点集合中Leader节点的下一节点
            String nextLeaderAddress = Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().get(1).getAddress();
            // 如果当前请求节点为竞选节点集合中Leader节点的下一节点
            if (StringUtils.equals(nextLeaderAddress, address)) {
                IOContext.obtain().shutdown(Node.obtain().getNodeElectionMap().get(contractHash).obtainLeaderAddress());
                // 将当前Hash合约竞选节点集合中的Leader节点强制移除
                Node.obtain().getNodeElectionMap().get(contractHash).removeLeader();
                shutdown();
                // 向新的竞选节点Leader发送保持心跳协议
                sendElectionToLeaderHeartBeatKeepAsk(address, contractHash);
                // 更新接收到新Leader节点变更的时间戳
                Node.obtain().getNodeElectionMap().get(contractHash).setTimestamp(System.currentTimeMillis());
            } else {
                // 同步下一节点出块，当前出块节点放弃出块权
                IOContext.obtain().send(nextLeaderAddress, ProtocolStatus.ELECTION_LEADER_CHANGE_FORCE_REQUEST, contractHash);
            }
        }
    }

    /** 告知当前Hash合约下的协助节点可变更为竞选节点协议 */
    default void electionLeaderChangeForAssistNode(String address, NodeElection election) {
        election.setTimestamp(System.currentTimeMillis());
        election.remove(address);
        Node.obtain().putNodeElection(election.getContractHash(), election);
        // 请求与当前竞选节点集合中的Leader保持长连接
        sendElectionToLeaderHeartBeatKeepAsk(election.getNodeBases().get(0).getAddress(), election.getContractHash());
        // 协助节点寻找当前可作为服务端的新的协助节点
        for (NodeBase nodeBase : Node.obtain().getNodeAssistMap().get(election.getContractHash()).getNodeBases()) {
            if (nodeBase.isReversible() && null != IOContext.obtain().ioServerGet(address)) {
                // 向竞备选协助节点发送post请求成为新的协助节点
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("contractHash", election.getContractHash());
                jsonObject.put("assist", Node.obtain().getNodeAssistMap().get(election.getContractHash()).toJsonString());
                String result = HttpTool.postNode(address, jsonObject.toJSONString());
                if (StringUtils.equals(result, IResponse.ResponseType.SUCCESS.getMsg())) {
                    // 通知其它下属节点，新的协助节点更新
                    NodeHash nodeHash = new NodeHash(election.getContractHash(), nodeBase);
                    IOContext.obtain().broadcastAssist(election.getContractHash(), new MessageData(ProtocolStatus.ELECTION_ASSIST_CHANGE_FOR_NODE, nodeHash.bean2ProtoByteArray()));
                    Node.obtain().getNodeAssistMap().remove(election.getContractHash());
                    Node.obtain().putNodeBaseAssistMap(election.getContractHash(), nodeBase);
                }
                break;
            }
        }
        shutdown();
    }

    /** 告知当前Hash合约下的节点当前协助节点已经变更协议 */
    default void electionAssistChangeForNode(String address, NodeHash nodeHash) {
        // 如果此消息是当前Hash合约下的协助节点发出，则执行
        if (StringUtils.equals(Node.obtain().getNodeBaseAssistMap().get(nodeHash.getContractHash()).getAddress(), address)) {
            Node.obtain().putNodeBaseAssistMap(nodeHash.getContractHash(), nodeHash.getNodeBase());
            sendElectionToAssistHeartKeepAsk(nodeHash.getNodeBase().getAddress(), nodeHash.getContractHash());
        }
    }

    /** 接收到请求当前协助节点保持心跳协议 */
    default void electionToAssistHeartKeepAsk(Channel channel, String address, String contractHash) {
        // 先检查自身是否为指定Hash合约竞选节点集的协助节点
        if (Node.obtain().isAssistNode(contractHash)) { // 如果是指定Hash合约竞选节点集的协助节点
            // 确定本节点是否包含请求心跳的地址
            boolean hasAddress = false;
            for (NodeBase nodeBase : Node.obtain().getNodeAssistMap().get(contractHash).getNodeBases()) {
                if (StringUtils.equals(nodeBase.getAddress(), address)) {
                    hasAddress = true;
                    break;
                }
            }
            if (!hasAddress) { // 如果竞选节点集合中没有当前请求地址
                log().debug("协助节点子节点集合中没有当前请求地址，告知其加入协议");
                // 告知新的接入地址当前Hash合约下的竞选节点地址
                push(channel, ProtocolStatus.JOIN_ASK_ELECTION, Node.obtain().getElectionAddress(contractHash));
                shutdown();
                return;
            }
            pushKeep(channel); // 都没问题，则告知保持长连接
        } else { // 不是指定Hash合约竞选节点集的协助节点
            // 告知新的接入地址当前Hash合约下的竞选节点地址
            push(channel, ProtocolStatus.JOIN_ASK_ELECTION, Node.obtain().getElectionAddress(contractHash));
            shutdown();
        }
    }

    /** 接收到告知当前Hash合约下的竞选节点当前协助节点无法连接协议 */
    default void electionLeaderAssistCanNotConnected(String address, Channel channel, String contractHash) {
        String assistAddress = Node.obtain().getAssistAddress(contractHash);
        // 如果真的无法连接到协助节点
        if (null == IOContext.obtain().ioServerGet(assistAddress) || !canConnect(assistAddress)) {
            // 判断当前请求节点是否可以作为服务端节点
            if (canConnect(address)) {
                NodeHash nodeHash = new NodeHash(contractHash, Node.obtain().getNodeBase().clear());
                push(channel, ProtocolStatus.JOIN_AS_ASSIST, nodeHash);
            } else {
                // 告知新的接入地址当前Hash合约下的竞选节点地址
                push(channel, ProtocolStatus.JOIN_ASK_ELECTION, Node.obtain().getElectionAddress(contractHash));
                shutdown();
            }
        }
    }

}