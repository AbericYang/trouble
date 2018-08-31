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

package cn.aberic.bother.block;

import cn.aberic.bother.entity.block.*;
import cn.aberic.bother.entity.enums.TransactionStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：Aberic on 2018/9/1 01:33
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class ToString {

    public static void main(String[] args) {
        toStringTest();
    }

    private static void toStringTest() {

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
                valueRead.setContractName(String.format("contract_%s%s%s", 1, transactionCount, rwCount));
                valueRead.setContractVersion(String.format("v_%s%s%s", 1, transactionCount, rwCount));
                valueRead.setStrings(new String[]{String.valueOf(1), String.valueOf(transactionCount), String.valueOf(rwCount)});

                ValueWrite valueWrite = new ValueWrite();
                valueWrite.setNumber(transactionCount + rwCount);
                valueWrite.setContractName(String.format("contract_%s%s%s", 1, transactionCount, rwCount));
                valueWrite.setContractVersion(String.format("v_%s%s%s", 1, transactionCount, rwCount));
                valueWrite.setStrings(new String[]{String.valueOf(1), String.valueOf(transactionCount), String.valueOf(rwCount)});

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

        String s = block.toString();
        log.debug("block toString = {}", s);
    }
}
