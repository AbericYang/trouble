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

import cn.aberic.bother.entity.block.Transaction;
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.io.IOContext;

/**
 * 应答交易消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/20 11:26
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public interface IMsgTransactionService extends IMsgRequestService {

    /** 接收到交易协议 */
    default void transaction(Transaction transaction) {
        // 判断自身是否为竞选节点集合中的Leader，即出块节点
        if (Node.obtain().isElectionNodeLeader(transaction.getHash())) { // 如果是出块节点
            Node.obtain().addTransaction(transaction);
            IOContext.obtain().syncTransactionElection(transaction);
        } else if (Node.obtain().isElectionNode(transaction.getHash())) { // 如果是竞选节点
            IOContext.obtain().syncTransactionElection(transaction);
        } else { // 如果是普通节点
            IOContext.obtain().sendTransactionElection(transaction);
        }
    }

    /** 接收到同步交易协议 */
    default void transactionSync(Transaction transaction) {
        // 判断自身是否为竞选节点集合中的Leader，即出块节点
        if (Node.obtain().isElectionNodeLeader(transaction.getHash())) { // 如果是出块节点
            Node.obtain().addTransaction(transaction);
        } else if (Node.obtain().isElectionNode(transaction.getHash())) { // 如果是竞选节点
            Node.obtain().addTransaction(transaction);
        } else { // 如果是普通节点
            IOContext.obtain().sendTransactionElection(transaction);
        }
    }

}
