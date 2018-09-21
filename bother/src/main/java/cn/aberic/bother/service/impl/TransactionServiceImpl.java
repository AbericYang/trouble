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

package cn.aberic.bother.service.impl;

import cn.aberic.bother.contract.common.ContractVerify;
import cn.aberic.bother.entity.block.Transaction;
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 作者：Aberic on 2018/09/20 10:07
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    @Override
    public boolean checkBlockVerify(String contractHash, Transaction transaction) {
        ContractVerify verify = new ContractVerify(contractHash);
        if (!verify.verifyTransaction(transaction)) {
            return false;
        }
        // 执行发送交易请求
        if (Node.obtain().isElectionNodeLeader(transaction.getHash())) { // 如果自身就是出块节点
            // 存储交易等待出块，并将交易同步至所有竞选节点
            if (!Node.obtain().addTransaction(transaction)) { // 如果自身已不再是出块节点
                // 将交易发送至竞选节点，由竞选节点代为转发或处理
                IOContext.obtain().syncTransactionElection(transaction);
            }
        } else if (Node.obtain().isElectionNode(transaction.getHash())) { // 如果自身是竞选节点
            // 将交易同步至所有竞选节点
            IOContext.obtain().syncTransactionElection(transaction);
        } else { // 如果是普通节点，直接将交易发送至竞选节点，由竞选节点代为转发或处理
            IOContext.obtain().sendTransactionElection(transaction);
        }
        return true;
    }

}
