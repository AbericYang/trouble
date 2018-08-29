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

package cn.aberic.bother.core.sc.exec.service;

import cn.aberic.bother.common.exception.ContractParamException;
import cn.aberic.bother.core.sc.exec.ContractFileExec;
import cn.aberic.bother.common.file.IFile;
import cn.aberic.bother.core.dm.BlockAcquire;
import cn.aberic.bother.core.dm.entity.Block;
import cn.aberic.bother.core.sc.entity.Contract;
import cn.aberic.bother.eac.MD5;

/**
 * 智能合约区块操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/08/29 16:41
 * 邮箱：abericyang@gmail.com
 */
public interface IContractBlockExec {

    BlockAcquire getBlockAcquire();

    /**
     * 获取 {@link IFile} 实现类。
     * <p>
     * 原本应该当前接口继承 {@link IFile} 进行操作，但有关 {@link IFile} 接口不方便直接暴露出去。
     * <p>
     * 所以这里采用的方案与IBlockExec和IIndexExec不同
     *
     * @return {@link IFile} 实现
     */
    ContractFileExec getFileExec();

    default Contract set(Contract contract) throws ContractParamException {
        return getFileExec().set(contract);
    }

    /**
     * 获取本地区块文件个数
     *
     * @return 区块文件个数
     */
    default int getFileCount() {
        return getBlockAcquire().getFileCount();
    }

    /**
     * 获取指定合约下账本高度
     *
     * @return 指定合约账本高度
     */
    default int getHeight() {
        return getBlockAcquire().getHeight();
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param height 区块高度
     *
     * @return 区块对象
     */
    default Block getBlockByHeight(int height) {
        return getBlockAcquire().getBlockByHeight(height);
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param currentDataHash 当前区块hash
     *
     * @return 区块对象
     */
    default Block getBlockByHash(String currentDataHash) {
        return getBlockAcquire().getBlockByHash(MD5.md516(currentDataHash));
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param transactionHash 交易hash
     *
     * @return 区块对象
     */
    default Block getBlockByTransactionHash(String transactionHash) {
        return getBlockAcquire().getBlockByTransactionHash(MD5.md516(transactionHash));
    }

}
