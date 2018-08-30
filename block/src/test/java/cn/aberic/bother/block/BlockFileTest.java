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

package cn.aberic.bother.block;

import cn.aberic.bother.entity.block.*;
import cn.aberic.bother.entity.enums.TransactionStatus;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.tools.SystemOut;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：Aberic on 2018/08/24 12:24
 * 邮箱：abericyang@gmail.com
 */
public class BlockFileTest {

    public static void main(String[] args) {
        writeBlock();
//        blockTest();
    }

    private static void blockTest() {
        SystemOut.println("================= entity file test start =================");
        BlockAcquire acquire = new BlockAcquire(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        SystemOut.println("================= getBlockFileCount ================= " + acquire.getFileCount());

        long time = new Date().getTime();
        int height = acquire.getHeight();
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | getHeight                 | entity height = " + height);

        time = new Date().getTime();
        Block block = acquire.getBlockByHeight(1125894);
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | getBlockByHeight           | entity = " + block.toJsonString());

        time = new Date().getTime();
        block = acquire.getBlockByHash("24075dc072d65b51f35906a447bd3e47beb5e4488bb175c5bba65fe7e2a52d7a");
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | getBlockByHash             | entity = " + block.toJsonString());

        time = new Date().getTime();
        block = acquire.getBlockByTransactionHash("4dedd27410aafc2a98339acda382b244a929249c6839f2218d84664f491312c8");
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | getBlockByTransactionHash  | entity = " + block.toJsonString());
        SystemOut.println("=================  entity file test end  =================");
    }

    private static void writeBlock() {
        BlockStorage blockStorage = new BlockStorage(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        for (int blockCount = 1130953; blockCount < 5000000; blockCount++) {
            SystemOut.println("=================  blockCount = " + blockCount + "  =================");
            BlockHeader header = BlockHeader.newInstance().create(true, 120, new Date().getTime());

            BlockBody body = new BlockBody();
            List<Transaction> transactions = new ArrayList<>();
            for (int transactionCount = 0; transactionCount < 10; transactionCount++) {
                Transaction transaction = new Transaction();
                transaction.setCreator(String.format("haha%s", transactionCount));
                transaction.setErrorMessage(String.format("error message %s", transactionCount));
                transaction.setSign(String.format("sign %s", transactionCount));
                transaction.setTransactionStatusCode(TransactionStatus.SUCCESS.getCode());
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
            blockStorage.save(block);
        }
    }

}
