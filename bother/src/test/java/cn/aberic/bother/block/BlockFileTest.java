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
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：Aberic on 2018/08/24 12:24
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class BlockFileTest {

    public static void main(String[] args) {
        writeBlock();
//        blockTest();
    }

    private static void blockTest() {
        log.debug("================= entity file test start =================");
        BlockAcquire acquire = new BlockAcquire(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);

        long time = new Date().getTime();
        int count = acquire.getFileCount();
        log.debug("处理时长 = {} | getBlockFileCount = {}", (new Date().getTime() - time), count);


        int height = acquire.getHeight();
        log.debug("处理时长 = {} | getHeight = {}", (new Date().getTime() - time), height);

        time = new Date().getTime();
        Block block = acquire.getBlockByHeight(45848);
        log.debug("处理时长 = {} | getBlockByHeight = {}", (new Date().getTime() - time), block.toJsonString());
//
//        time = new Date().getTime();
//        block = acquire.getBlockByHash("8552d5d383d84f3aa34338fb1edd2542e8d031111a46c8b55d6b0744c9a503e1");
//        log.debug("处理时长 = {} | getBlockByHash = {}", (new Date().getTime() - time), block.toJsonString());
//
//        time = new Date().getTime();
//        block = acquire.getBlockByTransactionHash("c3f0b966bcce948f3059129e8f524426c81002c683091e0f7028c8109c4454d5");
//        log.debug("处理时长 = {} | getBlockByTransactionHash = {}", (new Date().getTime() - time), block.toJsonString());
//
//        log.debug("================= entity file test end =================");
    }

    private static void writeBlock() {
        BlockStorage blockStorage = new BlockStorage(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        for (int blockCount = 45849; blockCount < 5000000; blockCount++) {
            log.debug("================= blockCount = {} =================", blockCount);
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

                RWSet rwSet = new RWSet();
                List<ValueRead> reads = new ArrayList<>();
                List<ValueWrite> writes = new ArrayList<>();
                for (int rwCount = 0; rwCount < 3; rwCount++) {

                    ValueRead valueRead = new ValueRead();
                    valueRead.setContractName(String.format("contract_%s%s%s", blockCount, transactionCount, rwCount));
                    valueRead.setContractVersion(String.format("v_%s%s%s", blockCount, transactionCount, rwCount));
                    valueRead.setKey(String.valueOf(blockCount));

                    ValueWrite valueWrite = new ValueWrite();
                    valueWrite.setContractName(String.format("contract_%s%s%s", blockCount, transactionCount, rwCount));
                    valueWrite.setContractVersion(String.format("v_%s%s%s", blockCount, transactionCount, rwCount));
                    valueWrite.setStrings(new String[]{String.valueOf(blockCount), String.valueOf(transactionCount), String.valueOf(rwCount)});


                    reads.add(valueRead);
                    writes.add(valueWrite);

                }
                rwSet.setReads(reads);
                rwSet.setWrites(writes);

                transaction.setRwSet(rwSet);

                transactions.add(transaction.build(null));
            }
            body.setTxCount(transactions.size());
            body.setTransactions(transactions);

            Block block = new Block(header, body);
            blockStorage.save(block);
        }
    }

}
