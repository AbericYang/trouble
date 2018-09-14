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

import cn.aberic.bother.block.runnable.RunnableSearchBlockHashIndex;
import cn.aberic.bother.block.runnable.RunnableSearchBlockHeightIndex;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.storage.IInit;
import cn.aberic.bother.tools.ITimeOut;
import cn.aberic.bother.tools.exception.SearchDataNotFoundException;
import cn.aberic.bother.tools.exception.SearchDataTimeoutException;
import cn.aberic.bother.tools.thread.ThreadTroublePool;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 区块索引文件本地读写接口——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/27 16:54
 * 邮箱：abericyang@gmail.com
 */
public interface IBlockIndexExec extends IInit, IExecInit, IIndexExec, ITimeOut {

    /**
     * 根据区块高度获取区块对象
     *
     * @param height 区块高度
     * @return 区块对象
     */
    default Block getByHeight(int height) throws SearchDataNotFoundException, SearchDataTimeoutException {
        Block[] blocks = new Block[]{null};
        int fileCount = getFileCount();
        AtomicInteger count = new AtomicInteger(0);
        ThreadTroublePool troublePool = new ThreadTroublePool();
        boolean found = false;
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        for (File file: files) {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                troublePool.submit(new RunnableSearchBlockHeightIndex(getBlockExec(), height, file, getNumByFileName(file.getName()), block -> {
                    if (null != block) {
                        blocks[0] = block;
                        troublePool.shutdown();
                    }
                    count.getAndIncrement();
                }));
            }
            if (null != blocks[0]) {
                break;
            }
        }
        long time = System.currentTimeMillis();
        while (!found) {
            found = checkTimeOut(fileCount, count, blocks[0], time, 300000);
        }
        troublePool.shutdown();
        return blocks[0];
    }

    /**
     * 根据区块hash获取区块对象
     *
     * @param currentDataHash 区块hash
     * @return 区块对象
     */
    default Block getByCurrentDataHash(String currentDataHash) throws SearchDataNotFoundException, SearchDataTimeoutException {
        Block[] blocks = new Block[]{null};
        int fileCount = getFileCount();
        AtomicInteger count = new AtomicInteger(0);
        ThreadTroublePool troublePool = new ThreadTroublePool();
        boolean found = false;
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        for (File file: files) {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                troublePool.submit(new RunnableSearchBlockHashIndex(getBlockExec(), currentDataHash, file, getNumByFileName(file.getName()), block -> {
                    if (null != block) {
                        blocks[0] = block;
                        troublePool.shutdown();
                    }
                    count.getAndIncrement();
                }));
            }
            if (null != blocks[0]) {
                break;
            }
        }
        long time = System.currentTimeMillis();
        while (!found) {
            found = checkTimeOut(fileCount, count, blocks[0], time, 300000);
        }
        troublePool.shutdown();
        return blocks[0];
    }

}
