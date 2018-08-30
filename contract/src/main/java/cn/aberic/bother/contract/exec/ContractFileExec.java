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

import cn.aberic.bother.contract.exec.service.IContractFileExec;
import cn.aberic.bother.encryption.MD5;
import cn.aberic.bother.entity.contract.Contract;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.storage.FileComponent;
import cn.aberic.bother.tools.FileTool;
import cn.aberic.bother.tools.exception.ContractFileNotFoundException;
import cn.aberic.bother.tools.exception.ContractHashException;
import cn.aberic.bother.tools.exception.ContractParamException;
import cn.aberic.bother.tools.exception.ContractRepetitionException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 智能合约文件对象操作-smart contract
 * <p>
 * 作者：Aberic on 2018/8/29 23:03
 * 邮箱：abericyang@gmail.com
 */
public class ContractFileExec implements IContractFileExec {

    /** 智能合约hash */
    private String contractHash;
    /** 智能合约上传的安装文件 */
    private File contractFile;

    /**
     * 实例化智能合约文件对象操作。
     * <p>
     * 通过上传智能合约安装文件来验证智能合约信息，即智能合约安装操作。
     * <p>
     * 基于上述操作为 {@link #contractHash} 赋值。
     *
     * @param contractFile 智能合约上传的安装文件
     */
    ContractFileExec(File contractFile) {
        this.contractFile = contractFile;
    }

    /**
     * 实例化智能合约文件对象操作。
     * <p>
     * 已传智能合约hash，根据已传参数操作智能合约
     *
     * @param contractFile 智能合约上传的安装文件
     * @param contractHash 智能合约hash
     */
    ContractFileExec(File contractFile, String contractHash) {
        // 设定的智能合约hash必须由系统生成，且不能与系统自由合约hash相同
        if (StringUtils.equalsIgnoreCase(contractHash, Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH)) {
            throw new ContractHashException();
        }
        this.contractFile = contractFile;
        this.contractHash = contractHash;
    }

    @Override
    public String getContractHash() {
        return contractHash;
    }

    @Override
    public FileComponent getFileStatus() {
        return FileComponent.getContractFileComponent();
    }

    @Override
    public String init(Contract contract) {
        // 即将部署的智能合约名称、版本号、版本名称及描述不能为空
        if (StringUtils.isEmpty(contract.getName()) ||
                StringUtils.isEmpty(contract.getVersionName()) ||
                StringUtils.isEmpty(contract.getBrief())) {
            throw new ContractParamException();
        }
        // 如果上传智能合约文件为空
        if (null == contractFile) {
            throw new ContractFileNotFoundException(contract.getName(), contract.getVersionName(), contract.getVersionCode());
        }
        // 如果智能合约hash为空，则表示首次安装该合约，安装并返回最终成功的hash
        if (StringUtils.isEmpty(contractHash)) {
            contractHash = MD5.md532(String.format("%s%s%s%s%s%s%s",
                    contract.getName(),
                    contract.getVersionName(),
                    contract.getVersionCode(),
                    contract.getBrief(),
                    contract.getDir(),
                    contract.getTimestamp(),
                    FileTool.getMD5(contractFile)));
        }
        contract.setHash(contractHash);
        contract.setDir(contractFile.getAbsolutePath());
        String jsonString = JSON.toJSONString(contract);
        // 验证并获取最新写入的合约文件
        File contractFile = verifyContract(contract);
        // File contractFile = getLastFile();
        try {
            // 如果最新写入的合约文件为null，则从0开始重新写入
            if (null == contractFile) {
                // 定义新的合约文件
                contractFile = createFirstFile();
                FileTool.writeFirstLine(contractFile, jsonString);
            } else {
                // 计算该内容的字节长度
                long contractSize = jsonString.getBytes().length;
                // 如果合约文件和待写入对象之和已经大于或等于24MB，则开辟新合约文件写入合约对象
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
        return contractHash;
    }

    /**
     * 验证待部署合约本地智能合约中是否存在重复
     * <p>
     * 如果重复，则抛出异常
     * <p>
     * 如果不重复，则返回可写入智能合约文件
     *
     * @param contract 待部署合约
     *
     * @return 写入智能合约文件
     */
    private File verifyContract(Contract contract) {
        File[] fileArray = new File[]{null};
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        files.forEach(file -> {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
                    while (it.hasNext()) {
                        String lineString = it.nextLine();
                        Contract lineContract = JSON.parseObject(lineString, new TypeReference<Contract>() {});
                        if (null == lineContract) {
                            continue;
                        }
                        if (StringUtils.equalsIgnoreCase(lineContract.getHash(), contractHash) &&
                                lineContract.getVersionCode() == contract.getVersionCode()) {
                            throw new ContractRepetitionException();
                        }
                    }
                    fileArray[0] = file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return fileArray[0];
    }

}
