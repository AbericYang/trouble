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

import cn.aberic.bother.core.dm.block.Block;
import cn.aberic.bother.core.dm.block.BlockInfo;
import cn.aberic.bother.core.dm.exec.BlockExec;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.*;

/**
 * 作者：Aberic on 2018/08/28 15:27
 * 邮箱：abericyang@gmail.com
 */
public class RunnableSearchBlockHeightIndex implements Runnable {

    public interface SearchBlockHeightIndexListener {
        void find(Block block);
    }

    private BlockExec blockExec;
    private int height;
    private File file;
    private int blockFileNum;
    private SearchBlockHeightIndexListener listener;

    public RunnableSearchBlockHeightIndex(BlockExec blockExec, int height, File file, int blockFileNum, SearchBlockHeightIndexListener lintener) {
        this.blockExec = blockExec;
        this.height = height;
        this.file = file;
        this.blockFileNum = blockFileNum;
        this.listener = lintener;

    }

    @Override
    public void run() {
        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
             // 用5M的缓冲读取文本文件
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf-8"), 5 * 1024 * 1024)) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                BlockInfo blockInfo = JSON.parseObject(line, new TypeReference<BlockInfo>() {});
                if (null != blockInfo && blockInfo.getHeight() == height) {
                    System.out.println("找到file，block-height-index-file-num = " + blockFileNum);
                    found = true;
                    listener.find(blockExec.getByNumAndLine(blockInfo.getNum(), blockInfo.getLine()));
                    break;
                }
            }
            if (!found) {
                System.out.println("未找到file，block-height-index-file-num = " + blockFileNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
