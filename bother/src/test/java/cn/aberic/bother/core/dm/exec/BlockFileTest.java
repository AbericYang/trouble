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
import cn.aberic.bother.core.dm.block.*;

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

        SystemOut.println("================= getBlockFileCount =================" + BlockFile.obtain().getBlockFileCount());

//        writeBlock();

        BlockAcquire acquire = new BlockAcquire();
        long time = new Date().getTime();
        Block block = acquire.getBlockByHeight(92000);
        SystemOut.println("处理时长 = " + (new Date().getTime() - time) + " | block hash = " + block.getHeader().getCurrentDataHash());

        SystemOut.println("=================  block file test end  =================");
    }

    private static void writeBlock() {
        for (int height = 0; height < 1200000; height++) {
            BlockHeader header = new BlockHeader();
            header.setPreviousDataHash("1234567");
            header.setConsentNodeCount(120);
            header.setSmoothlyOut(true);
            // header.setTimestamp(new Date().getTime());
            header.setTimestamp(123);
            header.setHeight(height);

            BlockBody body = new BlockBody();
            List<Transaction> transactions = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                Transaction transaction = new Transaction();
                transaction.setCreator(String.format("haha%s", i));
                transaction.setErrorMessage(String.format("error message %s", i));
                transaction.setSign(String.format("sign %s", i));
                transaction.setStatus(TransactionStatus.SUCCESS);
                // transaction.setTimestamp(new Date().getTime());
                transaction.setTimestamp(123L);

                List<RWSet> rwSets = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    RWSet rwSet = new RWSet();

                    ReadValue readValue = new ReadValue();
                    readValue.setNumber(i + j);
                    readValue.setContractName(String.format("contract_%s", i + j));
                    readValue.setContractVersion(String.format("v_%s", i + j));
                    readValue.setStrings(new String[]{String.valueOf(i), String.valueOf(j)});

                    WriteValue writeValue = new WriteValue();
                    writeValue.setNumber(i + j);
                    writeValue.setContractName(String.format("contract_%s", i + j));
                    writeValue.setContractVersion(String.format("v_%s", i + j));
                    writeValue.setStrings(new String[]{String.valueOf(i), String.valueOf(j)});

                    rwSet.setReadValue(readValue);
                    rwSet.setWriteValue(writeValue);

                    rwSets.add(rwSet);
                }
                transaction.setRwSets(rwSets);

                transactions.add(transaction.build());
            }
            body.setTxCount(transactions.size());
            body.setTransactions(transactions);

            Block block = new Block(header, body);
            BlockFile.obtain().createOrUpdate(block);
        }
    }

}
