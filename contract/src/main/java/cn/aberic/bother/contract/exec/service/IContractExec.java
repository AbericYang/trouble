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
import cn.aberic.bother.contract.exec.ContractFileExec;
import cn.aberic.bother.encryption.MD5;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.contract.Contract;

/**
 * 智能合约操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/08/29 16:41
 * 邮箱：abericyang@gmail.com
 */
public interface IContractExec {

    /**
     * 获取区块获取操作对象
     * <p>
     * 智能合约通过该对象可以得到区块中的基本信息
     * <p>
     * 并以此对象可以进行溯源操作
     *
     * @return 区块获取操作对象
     */
    BlockAcquire blockAcquire();

    /**
     * 获取 {@link cn.aberic.bother.storage.IFile} 实现类。
     * <p>
     * 原本应该当前接口继承 {@link cn.aberic.bother.storage.IFile} 进行操作，但有关 {@link cn.aberic.bother.storage.IFile} 接口不方便直接暴露出去。
     * <p>
     * 所以这里采用的方案与 {@link cn.aberic.bother.block.exec.service.IBlockExec}和 {@link cn.aberic.bother.block.exec.service.IIndexExec} 不同
     *
     * @return {@link cn.aberic.bother.storage.IFile} 实现
     */
    ContractFileExec contractFileExec();

    default String installOrUpgrade(Contract contract) {
        return contractFileExec().installOrUpgrade(contract);
    }

    /**
     * 获取本地区块文件个数
     *
     * @return 区块文件个数
     */
    default int getFileCount() {
        return blockAcquire().getFileCount();
    }

    /**
     * 获取指定合约下账本高度
     *
     * @return 指定合约账本高度
     */
    default int getHeight() {
        return blockAcquire().getHeight();
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param height 区块高度
     * @return 区块对象
     */
    default Block getBlockByHeight(int height) {
        return blockAcquire().getBlockByHeight(height);
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param currentDataHash 当前区块hash
     * @return 区块对象
     */
    default Block getBlockByHash(String currentDataHash) {
        return blockAcquire().getBlockByHash(MD5.md516(currentDataHash));
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param transactionHash 交易hash
     * @return 区块对象
     */
    default Block getBlockByTransactionHash(String transactionHash) {
        return blockAcquire().getBlockByTransactionHash(MD5.md516(transactionHash));
    }

}
