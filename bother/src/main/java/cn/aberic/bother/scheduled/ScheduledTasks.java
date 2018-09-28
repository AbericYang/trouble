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

package cn.aberic.bother.scheduled;

import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.entity.node.NodeAssist;
import cn.aberic.bother.entity.node.NodeBase;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.io.OutBlock;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.MsgPackTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 作者：Aberic on 2018/09/20 12:27
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Component
@Slf4j
public class ScheduledTasks {

    /**
     * fixedDelay = x 表示当前方法执行完毕x ms后，Spring scheduling会再次调用该方法<p>
     * 出块超时检测方案<p>
     * 每一次执行就会检查当前出块节点是否到出块时间<p>
     * 如果到出块时间，则发送给竞选节点集合中的协助节点进行催促
     */
    @Scheduled(fixedDelay = 5000)
    public void electionCheck() {
        log.debug("===============>>>>>>>>>> 出块超时检测 <<<<<<<<<<===============");
        long now = System.currentTimeMillis();
        Node.obtain().getNodeElectionMap().forEach((contractHash, nodeElection) -> {
            // 如果当前Hash有且仅有自身为竞选节点
            if (Node.obtain().getNodeElectionMap().size() == 1 && Node.obtain().isElectionNodeLeader(contractHash)) {
                if (Node.obtain().getTransactions(contractHash).size() > 0) {
                    // 执行出块操作
                    new OutBlock(contractHash).publish();
                }
            } else if (now - nodeElection.getTimestamp() > Constant.NODE_ELECTION_OUT_BLOCK_TIME) { // 如果到了出块时间
                log.debug("===============>>>>>>>>>> 出块超时 <<<<<<<<<<===============");
                if (!nodeElection.isLeader()) { // 如果自身不是出块节点
                    // 同步下一节点出块，当前出块节点放弃出块权
                    IOContext.obtain().send(
                            nodeElection.getNodeBases().get(1).getAddress(),
                            ProtocolStatus.ELECTION_LEADER_CHANGE_FORCE_REQUEST,
                            nodeElection.getContractHash());
                    log.debug("===============>>>>>>>>>> 出块超时，自身不是出块节点 <<<<<<<<<<===============");
                }
            }
        });
    }

    /**
     * fixedDelay = x 表示当前方法执行完毕x ms后，Spring scheduling会再次调用该方法<p>
     * 断线检查，作为普通节点如果与协助节点断开后的处理方案
     */
    @Scheduled(fixedDelay = 10000)
    public void disconnectCheck() {
        // 作为所遍历Hash合约的协助节点，检查被遍历Hash合约的竞选节点是否可用
        for (Map.Entry<String, NodeAssist> entry : Node.obtain().getNodeAssistMap().entrySet()) {
            if (null == IOContext.obtain().ioClientGet(Node.obtain().getElectionAddress(entry.getKey()))) {
                // 广播给当前Hash合约下的所有子节点，竞选节点无法连接
                IOContext.obtain().broadcastAssist(entry.getKey(), new MessageData(ProtocolStatus.ELECTION_LEADER_DEAD, MsgPackTool.string2Bytes(entry.getKey())));
                // 作为客户端向当前备选节点发送请求加入协议
                IOContext.obtain().join(Constant.ANCHOR_IP);
                Node.obtain().getAddressMap().get(entry.getKey()).getStringList().forEach(ipAddress -> IOContext.obtain().join(ipAddress));
            }
        }
        for (Map.Entry<String, NodeBase> entry : Node.obtain().getNodeBaseAssistMap().entrySet()) {
            if (null == IOContext.obtain().ioClientGet(Node.obtain().getAssistAddress(entry.getKey()))) {
                // 作为客户端向当前备选节点发送请求加入协议
                IOContext.obtain().join(Constant.ANCHOR_IP);
                Node.obtain().getAddressMap().get(entry.getKey()).getStringList().forEach(ipAddress -> IOContext.obtain().join(ipAddress));
            }
        }
    }

}
