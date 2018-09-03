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

package cn.aberic.bother.contract.exec.service;

import cn.aberic.bother.block.BlockAcquire;
import cn.aberic.bother.block.exec.service.IInit;
import cn.aberic.bother.contract.runnable.CallableSearchContractKeyIndexList;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.block.BlockInfo;
import cn.aberic.bother.entity.block.ValueWrite;
import cn.aberic.bother.entity.contract.ContractInfo;
import cn.aberic.bother.storage.FileComponent;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.storage.db.DBExec;
import cn.aberic.bother.tools.FileTool;
import cn.aberic.bother.tools.thread.ThreadTroublePool;
import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 智能合约数据索引文件对象操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/08/31 14:33
 * 邮箱：abericyang@gmail.com
 */
public interface IContractDataIndexFileExec extends IInit, IFile<ContractInfo> {

    Logger getLog();

    /**
     * 将根据旧版hash所指定智能合约数据文件夹重命名为新版hash
     *
     * @param contractOldHash 旧版hash
     */
    default boolean renameContractIndexFile(String contractOldHash) {
        // 旧版hash所在文件夹路径
        File file = new File(FileComponent.getContractDataIndexFileComponent(contractOldHash).getDir());
        // 将原文件夹更改为新版hash
        return file.renameTo(new File(getFileStatus().getDir()));
    }

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
     * @return 智能合约数据
     */
    default String get(BlockAcquire acquire, String key) {
        String[] results = new String[]{null};
        long time = new Date().getTime();
        String[] strings = DBExec.obtain().get(key).split(",");
        getLog().debug("DBExec getKey 耗时 = {}", (new Date().getTime() - time));

        time = new Date().getTime();
        Block block = acquire.getBlockByNumAndLine(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]));
        getLog().debug("Block getBlockByNumAndLine 耗时 = {}", (new Date().getTime() - time));

        time = new Date().getTime();
        block.getBody().getTransactions().forEach(transaction -> transaction.getRwSet().getWrites().forEach(write -> {
            if (StringUtils.equals(write.getStrings()[0], key)) {
                results[0] = write.getStrings()[1];
            }
        }));
        getLog().debug("Write getStrings 耗时 = {}", (new Date().getTime() - time));
        return results[0];
    }

    /**
     * 根据智能合约数据key获取智能合约数据
     *
     * @param key 智能合约数据key
     * @return 智能合约数据
     */
    default List<String> getHistory(BlockAcquire acquire, String key) {
        ThreadTroublePool troublePool = new ThreadTroublePool();
        List<String> strings = new ArrayList<>();
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        for (File file : files) {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                Future<List<String>> future = troublePool.submit(new CallableSearchContractKeyIndexList(acquire, key, file));
                try {
                    strings.addAll(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        troublePool.shutdown();
        return strings;
    }

}
