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
import cn.aberic.bother.block.exec.service.IDataExec;
import cn.aberic.bother.contract.runnable.CallableSearchContractKeyIndexList;
import cn.aberic.bother.entity.contract.ContractInfo;
import cn.aberic.bother.storage.FileComponent;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.storage.IInit;
import cn.aberic.bother.tools.thread.ThreadTroublePool;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
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
public interface IContractDataIndexFileExec extends IDataExec, IInit, IFile<ContractInfo> {

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
