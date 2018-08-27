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

package cn.aberic.bother.core.dm.exec;

import cn.aberic.bother.SystemOut;
import cn.aberic.bother.common.Common;
import cn.aberic.bother.core.dm.block.*;
import cn.aberic.bother.core.dm.status.TransactionStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：Aberic on 2018/08/24 12:24
 * 邮箱：abericyang@gmail.com
 */
public class BlockFileTest {

    public static void main(String[] args) {
        SystemOut.println("================= block file test start =================");

        BlockExec blockExec = new BlockExec(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        BlockIndexExec blockIndexExec = new BlockIndexExec(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        BlockTransactionIndexExec blockTransactionIndexExec = new BlockTransactionIndexExec(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);

        SystemOut.println("================= getBlockFileCount ================= " + blockExec.getFileCount());

        SystemOut.println("================= block height ================= " + blockExec.getHeight());

        writeBlock(blockExec, blockIndexExec, blockTransactionIndexExec);

        int num = 0;
        int line = 4;

        long time = new Date().getTime();
        int lineCount = blockExec.getFileLineCountIfBigCharLine(num);
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | block line count = " + lineCount);

        time = new Date().getTime();
        Block block = blockExec.getByNumAndLine(num, line);
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | block height = " + block.getHeader().getHeight());

//        BlockAcquire acquire = new BlockAcquire();
//        long time = new Date().getTime();
//        Block block = acquire.getBlockByHeight(92000);
//        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | block hash = " + block.getHeader().getCurrentDataHash());

        SystemOut.println("=================  block file test end  =================");
    }

    private static void writeBlock(BlockExec blockExec, BlockIndexExec blockIndexExec, BlockTransactionIndexExec blockTransactionIndexExec) {
        for (int blockCount = 0; blockCount < 1000000; blockCount++) {
            BlockHeader header = BlockHeader.newInstance().create(true, 120, new Date().getTime());

            BlockBody body = new BlockBody();
            List<Transaction> transactions = new ArrayList<>();
            for (int transactionCount = 0; transactionCount < 10; transactionCount++) {
                Transaction transaction = new Transaction();
                transaction.setCreator(String.format("haha%s", transactionCount));
                transaction.setErrorMessage(String.format("error message %s", transactionCount));
                transaction.setSign(String.format("sign %s", transactionCount));
                transaction.setStatus(TransactionStatus.SUCCESS);
                transaction.setTimestamp(new Date().getTime());

                List<RWSet> rwSets = new ArrayList<>();
                for (int rwCount = 0; rwCount < 3; rwCount++) {
                    RWSet rwSet = new RWSet();

                    ValueRead valueRead = new ValueRead();
                    valueRead.setNumber(transactionCount + rwCount);
                    valueRead.setContractName(String.format("contract_%s%s%s", blockCount, transactionCount, rwCount));
                    valueRead.setContractVersion(String.format("v_%s%s%s", blockCount, transactionCount, rwCount));
                    valueRead.setStrings(new String[]{String.valueOf(blockCount), String.valueOf(transactionCount), String.valueOf(rwCount)});

                    ValueWrite valueWrite = new ValueWrite();
                    valueWrite.setNumber(transactionCount + rwCount);
                    valueWrite.setContractName(String.format("contract_%s%s%s", blockCount, transactionCount, rwCount));
                    valueWrite.setContractVersion(String.format("v_%s%s%s", blockCount, transactionCount, rwCount));
                    valueWrite.setStrings(new String[]{String.valueOf(blockCount), String.valueOf(transactionCount), String.valueOf(rwCount)});

                    rwSet.setValueRead(valueRead);
                    rwSet.setValueWrite(valueWrite);

                    rwSets.add(rwSet);
                }
                transaction.setRwSets(rwSets);

                transactions.add(transaction.build());
            }
            body.setTxCount(transactions.size());
            body.setTransactions(transactions);

            Block block = new Block(header, body);
            BlockInfo blockInfo = blockExec.createOrUpdate(block);
            blockIndexExec.createOrUpdate(blockInfo);
            blockTransactionIndexExec.createOrUpdate(blockInfo);
        }
    }

}
