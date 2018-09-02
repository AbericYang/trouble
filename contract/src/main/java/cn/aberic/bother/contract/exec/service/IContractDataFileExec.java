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
import cn.aberic.bother.entity.contract.ContractInfo;
import cn.aberic.bother.storage.FileComponent;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.tools.DeflaterTool;
import cn.aberic.bother.tools.FileTool;

import java.io.File;
import java.io.IOException;

/**
 * 智能合约数据文件对象操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/08/31 11:28
 * 邮箱：abericyang@gmail.com
 */
public interface IContractDataFileExec extends IInit, IFile<String> {

    /**
     * 将根据旧版hash所指定智能合约数据文件夹重命名为新版hash
     *
     * @param contractOldHash 旧版hash
     */
    default boolean renameContractFile(String contractOldHash) {
        // 旧版hash所在文件夹路径
        File file = new File(FileComponent.getContractDataFileComponent(contractOldHash).getDir());
        // 将原文件夹更改为新版hash
        return file.renameTo(new File(getFileStatus().getDir()));
    }

    /**
     * 根据 {@param key} 传入对象 {@param string}
     *
     * @param key   json key
     * @param string   传入对象
     * @return 智能合约数据key在智能合约数据文件中的基本信息
     */
    default ContractInfo put(String key, String string) {
        ContractInfo contractInfo = new ContractInfo();
        // 当前智能合约数据在智能合约数据文件中所在行号
        int line = 0;
        // 获取最新写入的智能合约数据文件
        File contractDataFile = getLastFile();
        try {
            // 得到即将存入的压缩字符串
            String compressJsonString = DeflaterTool.compress(string);
            // 如果最新写入的智能合约数据文件为null，则从0开始重新写入
            if (null == contractDataFile) {
                // 定义新的智能合约数据文件
                contractDataFile = createFirstFile();
                FileTool.writeFirstLine(contractDataFile, compressJsonString);
            } else {
                // 获取当前智能合约数据文件中的总行数，其值即为上一区块的行数
                line = FileTool.getFileLineCount(contractDataFile);
                // 重新生成待写入JSON String内容
                // 计算该内容的字节长度
                long contractDataSize = compressJsonString.getBytes().length;
                // 如果智能合约数据文件和待写入对象之和已经大于或等于 256 MB，则开辟新智能合约数据文件写入智能合约数据
                if (contractDataFile.length() + contractDataSize >= 256 * 1000 * 1000) {
                    System.out.println(String.format("contract data file size great than 64MB, now size = %s", contractDataFile.length()));
                    contractDataFile = getNextFileByCurrentFile(contractDataFile);
                    System.out.println(String.format("next contract data file name = %s", contractDataFile.getName()));
                    FileTool.writeFirstLine(contractDataFile, compressJsonString);
                } else {
                    FileTool.writeAppendLine(contractDataFile, compressJsonString);
                }
            }
            contractInfo.setKey(key);
            contractInfo.setLine(line + 1);
            contractInfo.setNum(getNumByFileName(contractDataFile.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contractInfo;
    }

}