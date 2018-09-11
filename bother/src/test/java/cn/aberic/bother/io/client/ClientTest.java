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

package cn.aberic.bother.io.client;

import cn.aberic.bother.entity.block.*;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.enums.TransactionStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.io.Remote;
import cn.aberic.bother.io.IOContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：Aberic on 2018/9/9 19:42
 * 邮箱：abericyang@gmail.com
 */
public class ClientTest {

    public static void main(String[] args) throws Exception {

        Remote remote = new Remote();
        remote.setAddress("127.0.0.1");
        remote.setPort(63715);
        IOContext.obtain().startClient(remote);
        MessageData msgData = new MessageData((byte) 0x79, get());
        IOContext.obtain().sendByIOClient("127.0.0.1", msgData);
    }

    private static Block createBlock(int count) {
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
                valueRead.setKey(String.valueOf(count));

                ValueWrite valueWrite = new ValueWrite();
                valueWrite.setStrings(new String[]{String.valueOf(count), String.valueOf(transactionCount), String.valueOf(rwCount)});


                reads.add(valueRead);
                writes.add(valueWrite);

            }
            rwSet.setReads(reads);
            rwSet.setWrites(writes);

            transaction.setRwSet(rwSet);
            transaction.setContractName(String.format("contract_%s%s%s", count, transactionCount, reads.size()));
            transaction.setContractVersion(String.format("v_%s%s%s", count, transactionCount, writes.size()));

            transactions.add(transaction);
        }
        body.setTxCount(transactions.size());
        body.setTransactions(transactions);

        return new Block(header, body);
    }

    private static Account get() {
        Account account = new Account();
        account.setCount(new BigDecimal(1));
        account.setTimestamp(438756873L);
        account.setJsonAccountInfoString("jshdfkjhsdkhfksdhkjfksdhk");
        account.setPubRSAKey("ksdjflkjsdlkfjsldjflksjlkjlk");
        account.setAddress("knmlkmldkkflkfskd;s");
        account.setPubECCKey("cnowjodjwoefjoejfokejofjw");
        return account;
    }

}
