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

package cn.aberic.bother.contract.exec;

import cn.aberic.bother.block.exec.service.IInit;
import cn.aberic.bother.entity.contract.Contract;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.storage.FileComponent;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.tools.FileTool;
import cn.aberic.bother.tools.exception.ContractParamException;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 智能合约文件对象操作-smart contract
 * <p>
 * 作者：Aberic on 2018/8/29 23:03
 * 邮箱：abericyang@gmail.com
 */
public class ContractFileExec implements IInit, IFile<Contract> {

    private String contractHash;

    public ContractFileExec(String contractHash) {
        this.contractHash = contractHash;
    }

    @Override
    public String getContractHash() {
        return null;
    }

    @Override
    public FileComponent getFileStatus() {
        if (StringUtils.equals(contractHash, Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH)) {
            return FileComponent.getContractFileComponentDefault();
        }
        return FileComponent.getContractFileComponent(contractHash);
    }

    /**
     * 安装或者升级智能合约。
     * <p>
     * 此操作会判断本地合约是否有相同hash值得存在。
     * <p>
     * 如果有，则创建失败，如果没有，则返回当前合约的完整对象，包括提供给第三方进行安装部署的hash字段
     *
     * @param contract 智能合约
     * @return 完整智能合约对象
     */
    public Contract installOrUpgrade(Contract contract) {
        if (StringUtils.isEmpty(contract.getName()) ||
                StringUtils.isEmpty(contract.getVersionName()) ||
                StringUtils.isEmpty(contract.getBrief())) {
            throw new ContractParamException("For entity Contract, params can't be empty");
        }
        String jsonString = JSON.toJSONString(contract);
        // 获取最新写入的合约文件
        File contractFile = getLastFile();
        try {
            // 如果最新写入的合约文件为null，则从0开始重新写入
            if (null == contractFile) {
                // 定义新的区块文件
                contractFile = createFirstFile();
                FileTool.writeFirstLine(contractFile, jsonString);
            } else {
                // 计算该内容的字节长度
                long contractSize = jsonString.getBytes().length;
                // 如果区块文件和待写入对象之和已经大于或等于24MB，则开辟新区块文件写入区块对象
                if (contractFile.length() + contractSize >= 24 * 1000 * 1000) {
                    System.out.println(String.format("contract file size great than 24MB, now size = %s", contractFile.length()));
                    contractFile = getNextFileByCurrentFile(contractFile);
                    System.out.println(String.format("next contract file name = %s", contractFile.getName()));
                    FileTool.writeFirstLine(contractFile, jsonString);
                } else {
                    FileTool.writeAppendLine(contractFile, jsonString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contract;
    }

}
