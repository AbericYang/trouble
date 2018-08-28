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
import cn.aberic.bother.core.dm.BlockAcquire;
import cn.aberic.bother.core.dm.BlockStorage;
import cn.aberic.bother.core.dm.block.*;
import cn.aberic.bother.core.dm.status.TransactionStatus;
import com.alibaba.fastjson.JSON;

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

        BlockAcquire acquire = new BlockAcquire(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);

        writeBlock();

        SystemOut.println("================= getBlockFileCount ================= " + acquire.getFileCount());

        long time = new Date().getTime();
        int height = acquire.getHeight();
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | block height = " + height);

        time = new Date().getTime();
        Block block = acquire.getBlockByHeight(43999);
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | getBlockByHeight | block = " + JSON.toJSONString(block));

        time = new Date().getTime();
        block = acquire.getBlockByHash("f0c60296c244bda7fcd3dcd17a109e9471b64d187ccf480c4aee50b5948010ce");
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | getBlockByHash | block = " + JSON.toJSONString(block));

        time = new Date().getTime();
        block = acquire.getBlockByTransactionHash("5c8e99c5b9ea8ace10745b7eae1f2f60a59a938bfe939313be94066e0183e289");
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | getBlockByTransactionHash | block = " + JSON.toJSONString(block));

        SystemOut.println("=================  block file test end  =================");
    }

    private static void writeBlock() {
        BlockStorage blockStorage = new BlockStorage(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        for (int blockCount = 46000; blockCount < 47000; blockCount++) {
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
            blockStorage.save(block);
        }
    }

}
