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

import cn.aberic.bother.block.exec.service.IInit;
import cn.aberic.bother.contract.runnable.CallableSearchContractKeyIndexList;
import cn.aberic.bother.entity.contract.ContractInfo;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.storage.leveldb.LevelDB;
import cn.aberic.bother.tools.FileTool;
import cn.aberic.bother.tools.thread.ThreadTroublePool;
import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 智能合约数据索引文件对象操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/08/31 14:33
 * 邮箱：abericyang@gmail.com
 */
public interface IContractDataIndexFileExec extends IInit, IFile<ContractInfo> {

    /**
     * 将智能合约数据key在智能合约数据文件中的基本信息存入索引
     *
     * @param contractInfo 智能合约数据key在智能合约数据文件中的基本信息
     */
    default void put(ContractInfo contractInfo) {
        String jsonString = JSON.toJSONString(contractInfo);
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
                // 如果智能合约数据文件和待写入对象之和已经大于或等于64MB，则开辟新智能合约数据文件写入智能合约数据
                if (indexFile.length() + indexSize >= 64 * 1000 * 1000) {
                    System.out.println(String.format("contract data index file size great than 64MB, now size = %s", indexFile.length()));
                    indexFile = getNextFileByCurrentFile(indexFile);
                    System.out.println(String.format("next contract data index file name = %s", indexFile.getName()));
                    FileTool.writeFirstLine(indexFile, jsonString);
                } else {
                    FileTool.writeAppendLine(indexFile, jsonString);
                }
            }
            LevelDB.obtain().put(contractInfo.getKey(), String.format("%s,%s", contractInfo.getNum(), contractInfo.getLine()));
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
    default String get(IContractDataFileExec contractDataFileExec, String key) {
        String[] strings = LevelDB.obtain().get(key).split(",");
        return contractDataFileExec.getByNumAndLine(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]));
    }

    /**
     * 根据智能合约数据key获取智能合约数据
     *
     * @param key 智能合约数据key
     *
     * @return 智能合约数据
     */
    default List<String> getHistory(IContractDataFileExec contractDataFileExec, String key) {
        ThreadTroublePool troublePool = new ThreadTroublePool();
        List<String> strings = new ArrayList<>();
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        for (File file : files) {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                Future<List<String>> future = troublePool.submit(new CallableSearchContractKeyIndexList(contractDataFileExec, key, file));
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
