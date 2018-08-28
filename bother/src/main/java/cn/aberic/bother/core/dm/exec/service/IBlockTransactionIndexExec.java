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

package cn.aberic.bother.core.dm.exec.service;

import cn.aberic.bother.core.dm.block.Block;
import cn.aberic.bother.core.dm.block.BlockInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * 区块交易索引文件本地读写接口——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/27 17:51
 * 邮箱：abericyang@gmail.com
 */
public interface IBlockTransactionIndexExec extends IInit, IExecInit, IIndexExec {

    @Override
    default String[] jsonStringByPropertyPreFilter() {
        return new String[]{"transactionHashList", "num", "line"};
    }

    /**
     * 根据交易hash获取区块对象
     *
     * @param transactionHash 交易hash
     *
     * @return 区块对象
     */
    default Block getByTransactionHash(String transactionHash) {
        Block[] blocks = new Block[]{null};
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        files.forEach(file -> {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
                     // 用5M的缓冲读取文本文件
                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf-8"), 5 * 1024 * 1024)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        BlockInfo blockInfo = JSON.parseObject(line, new TypeReference<BlockInfo>() {});
                        if (null != blockInfo) {
                            for (String transaction : blockInfo.getTransactionHashList()) {
                                if (StringUtils.equalsIgnoreCase(transaction, transactionHash)) {
                                    blocks[0] = getBlockExec().getByNumAndLine(blockInfo.getNum(), blockInfo.getLine());
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return blocks[0];
    }
}
