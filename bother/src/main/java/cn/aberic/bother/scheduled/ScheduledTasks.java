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
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.io.OutBlock;
import cn.aberic.bother.tools.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
                // 执行出块操作
                new OutBlock(contractHash).publish();
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

}
