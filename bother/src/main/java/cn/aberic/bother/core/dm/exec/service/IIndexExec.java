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

package cn.aberic.bother.core.dm.exec.service;

import cn.aberic.bother.core.dm.block.BlockInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.io.File;
import java.io.IOException;

/**
 * 索引文件本地读写公共接口——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/8/27 21:39
 * 邮箱：abericyang@gmail.com
 */
public interface IIndexExec extends IExec<BlockInfo> {

    String[] jsonStringByPropertyPreFilter();

    default BlockInfo createOrUpdate(BlockInfo blockInfo) {
        // 首先序列化时过滤掉无用属性
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(BlockInfo.class, jsonStringByPropertyPreFilter());
        String jsonString = JSON.toJSONString(blockInfo, filter);
        // 获取最新写入的区块文件
        File indexFile = getLastFile();
        try {
            // 如果最新写入的区块文件为null，则从0开始重新写入
            if (null == indexFile) {
                // 定义新的区块文件
                indexFile = createFirstFile();
                writeFirstLine(indexFile, jsonString);
            } else {
                // 计算该内容的字节长度
                long blockIndexSize = jsonString.getBytes().length;
                // 如果区块文件和待写入对象之和已经大于或等于24MB，则开辟新区块文件写入区块对象
                if (indexFile.length() + blockIndexSize >= 24 * 1000 * 1000) {
                    System.out.println(String.format("block index file size great than 24MB, now size = %s", indexFile.length()));
                    indexFile = getNextFileByCurrentFile(indexFile);
                    System.out.println(String.format("next block index file name = %s", indexFile.getName()));
                    writeFirstLine(indexFile, jsonString);
                } else {
                    writeAppendLine(indexFile, jsonString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blockInfo;
    }

}
