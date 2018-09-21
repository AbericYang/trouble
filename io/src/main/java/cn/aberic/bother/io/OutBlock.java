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

package cn.aberic.bother.io;

import cn.aberic.bother.block.BlockStorage;
import cn.aberic.bother.entity.block.*;
import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.node.Node;

import java.util.List;

/**
 * 出块对象
 * <p>
 * 作者：Aberic on 2018/09/20 17:19
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public class OutBlock {

    private String contractHash;
    /** 区块存储操作对象 */
    private BlockStorage storage;

    public OutBlock(String contractHash) {
        this.contractHash = contractHash;
        storage = new BlockStorage(contractHash);
    }

    /** 发布出块 */
    public void publish() {
        // 生成出块区块
        BlockOut blockOut = out(Node.obtain().getTransactions(contractHash));
        // 将出块区块在竞选节点集合中进行广播
        Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().forEach(nodeBase ->
                IOContext.obtain().broadcastElection(contractHash, new MessageData(ProtocolStatus.BLOCK_OUT, blockOut.bean2ProtoByteArray())));
        // 将出块区块广播给协助节点
        IOContext.obtain().push(Node.obtain().getAssistAddress(contractHash), new MessageData(ProtocolStatus.BLOCK_OUT, blockOut.bean2ProtoByteArray()));
        // 同步已出快的区块对象到本地
        storage.sync(blockOut);
    }

    /**
     * 将指定Hash合约下的交易集合打包成出块区块
     *
     * @param transactions 交易集合
     * @return 出块区块对象
     */
    private BlockOut out(List<Transaction> transactions) {
        BlockHeader header = BlockHeader.newInstance().create(contractHash);
        BlockBody body = new BlockBody();
        body.setTxCount(transactions.size());
        body.setTransactions(transactions);
        // 生成原始区块对象
        Block block = new Block(header, body);
        // 打包原始区块对象并生成区块出块对象
        return storage.packOut(block);
    }

}
