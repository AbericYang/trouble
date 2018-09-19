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
            if (Node.obtain().getNodeElectionMap().get(strings.get(0)).getNodeBases().get(0).getTimestamp() !=
                    Node.obtain().getNodeBase().getTimestamp()) { // 如果自身不是竞选节点集合中的Leader，关闭连接
                shutdown();
            }
        } else { // 如果不是竞选节点
            log().debug("当前节点不是竞选节点，无法更新当前请求节点其下属节点总数");
            shutdown();
        }
    }

}