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

package cn.aberic.bother.core.dm.runnable;

import cn.aberic.bother.core.dm.entity.Block;
import cn.aberic.bother.core.dm.entity.BlockInfo;
import cn.aberic.bother.core.dm.exec.BlockExec;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * 作者：Aberic on 2018/08/28 15:27
 * 邮箱：abericyang@gmail.com
 */
public class RunnableSearchTransactionHashIndex implements Runnable {

    public interface SearchTransactionHashIndexListener {
        void find(Block block);
    }

    private BlockExec blockExec;
    private String transactionHash;
    private File file;
    private int blockFileNum;
    private SearchTransactionHashIndexListener listener;

    public RunnableSearchTransactionHashIndex(BlockExec blockExec, String transactionHash, File file, int blockFileNum, SearchTransactionHashIndexListener lintener) {
        this.blockExec = blockExec;
        this.transactionHash = transactionHash;
        this.file = file;
        this.blockFileNum = blockFileNum;
        this.listener = lintener;

    }

    @Override
    public void run() {
        try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
            boolean found = false;
            while (it.hasNext()) {
                String lineString = it.nextLine();
                BlockInfo blockInfo = JSON.parseObject(lineString, new TypeReference<BlockInfo>() {});
                if (null != blockInfo) {
                    for (String transaction : blockInfo.getTransactionHashList()) {
                        if (StringUtils.equalsIgnoreCase(transaction, transactionHash)) {
                            System.out.println("找到file，block-transaction-hash-index-file-num = " + blockFileNum);
                            found = true;
                            listener.find(blockExec.getByNumAndLine(blockInfo.getNum(), blockInfo.getLine()));
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
            }
            if (!found) {
                System.out.println("未找到file，block-transaction-hash-index-file-num = " + blockFileNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
