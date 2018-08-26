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

package cn.aberic.bother.core.dm.call;

import cn.aberic.bother.core.dm.block.Block;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * 作者：Aberic on 2018/8/25 22:03
 * 邮箱：abericyang@gmail.com
 */
public class CallableSearchBlock implements Callable<Block> {

    private int height;
    private File file;
    private int blockFileNum;

    public CallableSearchBlock(int height, File file, int blockFileNum) {
        this.height = height;
        this.file = file;
        this.blockFileNum = blockFileNum;
    }

    @Override
    public Block call() {
        System.out.println("开启线程，当前区块编号 = " + blockFileNum);
        try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
            while (it.hasNext()) {
                String line = it.nextLine();
                if (StringUtils.isNotEmpty(line)) {
                    Block block = JSON.parseObject(line, new TypeReference<Block>() {});
                    if (block.getHeader().getHeight() == height) {
                        System.out.println("找到block，blockFileNum = " + blockFileNum);
                        return block;
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
