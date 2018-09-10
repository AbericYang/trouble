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

package cn.aberic.bother.tools;

import cn.aberic.bother.entity.block.*;
import cn.aberic.bother.entity.enums.TransactionStatus;
import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：Aberic on 2018/9/10 21:03
 * 邮箱：abericyang@gmail.com
 */
public class MsgPackFileTest {

    public static void main(String[] args) throws IOException {
        List<Block> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list.add(createBlock(i));
        }

        byte[] msgBytes = MsgPackTool.toBytes(list);
        File msgFile = new File("/Users/Aberic/Documents/tmp/file/msgpack_file.data");
        msgFile.createNewFile();
        Files.write(msgBytes, msgFile);
        System.out.printf("msgpack序列化1w个对象后大小：%f kb\n", msgFile.length() / 1024.0d);


        byte[] jsonBytes = JSON.toJSONBytes(list);
        File jsonFile = new File("/Users/Aberic/Documents/tmp/file/json_file.data");
        jsonFile.createNewFile();
        Files.write(jsonBytes, jsonFile);
        System.out.printf("json序列化1w个对象后大小：%f kb\n", jsonFile.length() / 1024.0d);


        File jsonStringFile = new File("/Users/Aberic/Documents/tmp/file/json_string_file.data");
        jsonStringFile.createNewFile();
        FileTool.writeFirstLine(jsonStringFile, JSON.toJSONString(list));
        System.out.printf("json string 1w个对象后大小：%f kb\n", jsonStringFile.length() / 1024.0d);


        File jsonCompressStringFile = new File("/Users/Aberic/Documents/tmp/file/json_compress_string_file.data");
        jsonCompressStringFile.createNewFile();
        FileTool.writeFirstLine(jsonCompressStringFile, DeflaterTool.compress(JSON.toJSONString(list)));
        System.out.printf("json compress string 1w个对象后大小：%f kb\n", jsonCompressStringFile.length() / 1024.0d);

    }

    public static void saveTo(InputStream is, OutputStream out) throws IOException {
        try {
            byte[] buffer = new byte[1024 * 10];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            is.close();
        }
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

}
