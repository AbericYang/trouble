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

package cn.aberic.bother.block.runnable;

import cn.aberic.bother.block.exec.BlockExec;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.block.BlockInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 作者：Aberic on 2018/08/28 15:27
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class RunnableSearchBlockHashIndex implements Runnable {

    public interface SearchBlockHashIndexListener {
        void find(Block block);
    }

    private BlockExec blockExec;
    private String currentDataHash;
    private File file;
    private int blockFileNum;
    private SearchBlockHashIndexListener listener;

    public RunnableSearchBlockHashIndex(BlockExec blockExec, String currentDataHash, File file, int blockFileNum, SearchBlockHashIndexListener lintener) {
        this.blockExec = blockExec;
        this.currentDataHash = currentDataHash;
        this.file = file;
        this.blockFileNum = blockFileNum;
        this.listener = lintener;

    }

    @Override
    public void run() {
        try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
            boolean found = false;
            while (it.hasNext()) {
                BlockInfo blockInfo = null;
                String lineString = it.nextLine();
                if (StringUtils.isNotEmpty(lineString)) {
                    String[] strs = lineString.split(",");
                    blockInfo = new BlockInfo();
                    blockInfo.setNum(Integer.valueOf(strs[0]));
                    blockInfo.setLine(Integer.valueOf(strs[1]));
                    blockInfo.setBlockHash(strs[2]);
                }
                if (null != blockInfo && StringUtils.equalsIgnoreCase(blockInfo.getBlockHash(), currentDataHash)) {
                    log.debug("找到file，block-hash-index-file-num = {}" , blockFileNum);
                    found = true;
                    listener.find(blockExec.getByNumAndLine(blockInfo.getNum(), blockInfo.getLine()));
                    break;
                }
            }
            if (!found) {
                log.debug("未找到file，block-hash-index-file-num = {}" , blockFileNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
