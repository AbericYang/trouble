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

package cn.aberic.bother.block.exec.service;

import cn.aberic.bother.block.runnable.RunnableSearchTransactionHashIndex;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.tools.thread.ThreadTroublePool;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;

/**
 * 区块交易索引文件本地读写接口——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/27 17:51
 * 邮箱：abericyang@gmail.com
 */
public interface IBlockTransactionIndexExec extends IInit, IExecInit, IIndexExec {

    /**
     * 根据交易hash获取区块对象
     *
     * @param transactionHash 交易hash
     * @return 区块对象
     */
    default Block getByTransactionHash(String transactionHash) {
        Block[] blocks = new Block[]{null};
        ThreadTroublePool troublePool = new ThreadTroublePool();
        boolean found = false;
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        for (File file : files) {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                troublePool.submit(new RunnableSearchTransactionHashIndex(troublePool, getBlockExec(), transactionHash,
                        file, getNumByFileName(file.getName()), block -> {
                    if (null != block) {
                        blocks[0] = block;
                    }
                }));
            }
            if (null != blocks[0]) {
                break;
            }
        }
        long time = new Date().getTime();
        while (!found) {
            if (null != blocks[0]) {
                found = true;
            } else {
                if (new Date().getTime() - time > 15000) {
                    found = true;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return blocks[0];
    }
}
