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

import cn.aberic.bother.block.BlockAcquire;
import cn.aberic.bother.contract.exec.service.IContractBaseExec;
import cn.aberic.bother.contract.exec.service.IContractExec;
import cn.aberic.bother.entity.contract.Contract;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 智能合约操作接口实现-smart contract
 * <p>
 * 作者：Aberic on 2018/08/29 16:43
 * 邮箱：abericyang@gmail.com
 */
public class ContractExec extends SystemContractExec implements IContractExec, IContractBaseExec {

    /** 智能合约hash */
    private String contractHash;
    /** 旧版智能合约hash */
    private String contractOldHash;
    /** 智能合约上传的安装文件 */
    private File contractFile;

    /**
     * 实例化智能合约操作接口实现。
     * <p>
     * 此方法不出意外，将仅由 controller 层进行调用
     * <p>
     * 通过上传智能合约安装文件来利用 {@link #getContractFileExec()} 方法实现来为 {@link #contractHash} 赋值。
     *
     * @param contractFile    智能合约上传的安装文件
     * @param contractOldHash 旧版智能合约hash
     */
    public ContractExec(File contractFile, String contractOldHash) {
        this.contractFile = contractFile;
        this.contractOldHash = contractOldHash;
    }

    /**
     * 智能合约操作接口实现。
     * <p>
     * 此方法不出意外，将仅由 controller 层进行调用
     *
     * @param contractHash 智能合约hash
     */
    public ContractExec(String contractHash) {
        this.contractHash = contractHash;
    }

    @Override
    public BlockAcquire getBlockAcquire() {
        return new BlockAcquire(StringUtils.isEmpty(contractHash) ? getContractFileExec().getContractHash() : contractHash);
    }

    @Override
    public ContractFileExec getContractFileExec() {
        if (StringUtils.isEmpty(contractHash)) {
            return new ContractFileExec(contractFile);
        }
        return new ContractFileExec(contractHash);
    }

    @Override
    public String init(Contract contract) {
        contractHash = getContractFileExec().init(contract);
        // 上述初始化操作后会废弃旧版hash，需要将旧版hash中的智能合约数据文件夹重命名为新版hash，以便调用
        if (!getContractDataFileExec().renameContractFile(contractOldHash)) {
            throw new RuntimeException("rename contract file failed");
        }
        return contractHash;
    }

}
