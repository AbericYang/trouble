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

package cn.aberic.bother.common.thread;

import cn.aberic.bother.core.dm.block.Block;
import cn.aberic.bother.core.dm.block.BlockInfo;
import cn.aberic.bother.core.dm.exec.BlockExec;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * 作者：Aberic on 2018/8/25 22:03
 * 邮箱：abericyang@gmail.com
 */
public class CallableSearchBlock implements Callable<Block> {

    private BlockExec blockExec;
    private String transactionHash;
    private File file;
    private int blockFileNum;

    public CallableSearchBlock(BlockExec blockExec, String transactionHash, File file, int blockFileNum) {
        this.blockExec = blockExec;
        this.transactionHash = transactionHash;
        this.file = file;
        this.blockFileNum = blockFileNum;

    }

    @Override
    public Block call() {
        System.out.println("开启线程，当前区块编号 = " + blockFileNum);
//        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
//             // 用5M的缓冲读取文本文件
//             BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf-8"), 5 * 1024 * 1024)) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                BlockInfo blockInfo = JSON.parseObject(line, new TypeReference<BlockInfo>() {});
//                if (null != blockInfo && blockInfo.getHeight() == height) {
//                    return blockExec.getByNumAndLine(blockInfo.getNum(), blockInfo.getLine());
//                }
//            }
//            System.out.println("未找到block，blockFileNum = " + blockFileNum);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
             // 用5M的缓冲读取文本文件
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf-8"), 5 * 1024 * 1024)) {
            String line;
            while ((line = reader.readLine()) != null) {
                BlockInfo blockInfo = JSON.parseObject(line, new TypeReference<BlockInfo>() {});
                if (null != blockInfo) {
                    for (String transaction : blockInfo.getTransactionHashList()) {
                        if (StringUtils.equalsIgnoreCase(transaction, transactionHash)) {
                            System.out.println("找到block，blockFileNum = " + blockFileNum);
                            return blockExec.getByNumAndLine(blockInfo.getNum(), blockInfo.getLine());
                        }
                    }
                }
            }
            System.out.println("未找到block，blockFileNum = " + blockFileNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
