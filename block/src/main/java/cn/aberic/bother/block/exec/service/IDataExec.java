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

package cn.aberic.bother.block.exec.service;

import cn.aberic.bother.block.BlockAcquire;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.block.BlockInfo;
import cn.aberic.bother.entity.block.ValueWrite;
import cn.aberic.bother.entity.contract.ContractInfo;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.storage.IInit;
import cn.aberic.bother.storage.db.DBExec;
import cn.aberic.bother.tools.FileTool;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 链上kv数据存取接口
 * <p>
 * 作者：Aberic on 2018/9/6 21:58
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public interface IDataExec extends IInit, IFile<ContractInfo> {

    Logger getLog();

    /**
     * 将智能合约数据key在智能合约数据文件中的基本信息存入索引
     *
     * @param blockInfo 区块在区块文件中的基本信息
     * @param writes    一笔操作的写入值对象集合
     */
    default void put(BlockInfo blockInfo, List<ValueWrite> writes) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        writes.forEach(write -> {
            map.put(write.getStrings()[0], String.format("%s,%s", blockInfo.getNum(), blockInfo.getLine()));
            if (sb.toString().length() != 0) {
                sb.append("\r\n");
            }
            sb.append(JSON.toJSONString(new ContractInfo(write.getStrings()[0], blockInfo.getNum(), blockInfo.getLine())));
        });
        String jsonString = sb.toString();
        // 获取最新写入的智能合约数据文件
        File indexFile = getLastFile();
        try {
            // 如果最新写入的智能合约数据文件为null，则从0开始重新写入
            if (null == indexFile) {
                // 定义新的智能合约数据文件
                indexFile = createFirstFile();
                FileTool.writeFirstLine(indexFile, jsonString);
            } else {
                // 计算该内容的字节长度
                long indexSize = jsonString.getBytes().length;
                // 如果智能合约数据文件和待写入对象之和已经大于或等于 256 MB，则开辟新智能合约数据文件写入智能合约数据
                if (indexFile.length() + indexSize >= 256 * 1000 * 1000) {
                    getLog().debug("contract data index file size great than 256 MB, now size = {}", indexFile.length());
                    indexFile = getNextFileByCurrentFile(indexFile);
                    getLog().debug("next contract data index file name = {}", indexFile.getName());
                    FileTool.writeFirstLine(indexFile, jsonString);
                } else {
                    FileTool.writeAppendLine(indexFile, jsonString);
                }
            }
            DBExec.obtain().put(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据智能合约数据key获取智能合约数据
     *
     * @param key 智能合约数据key
     *
     * @return 智能合约数据
     */
    default String get(BlockAcquire acquire, String key) {
        String[] results = new String[]{null};
        long time = System.currentTimeMillis();
        String dbResultStr = DBExec.obtain().get(key);
        if (null == dbResultStr) {
            return null;
        }
        String[] strings = dbResultStr.split(",");
        getLog().debug("DBExec getKey 耗时 = {}", (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        Block block = acquire.getBlockByNumAndLine(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]));
        getLog().debug("Block getBlockByNumAndLine 耗时 = {}", (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        block.getBody().getTransactions().forEach(transaction -> transaction.getRwSet().getWrites().forEach(write -> {
            if (StringUtils.equals(write.getStrings()[0], key)) {
                results[0] = write.getStrings()[1];
            }
        }));
        getLog().debug("Write getStrings 耗时 = {}", (System.currentTimeMillis() - time));
        return results[0];
    }

}
