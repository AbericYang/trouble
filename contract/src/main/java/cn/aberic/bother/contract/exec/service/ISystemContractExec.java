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

package cn.aberic.bother.contract.exec.service;

import cn.aberic.bother.entity.IResponse;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.contract.Contract;

/**
 * 系统级智能合约操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/8/30 21:45
 * 邮箱：abericyang@gmail.com
 */
public interface ISystemContractExec extends IResponse {

    Contract getContract();

    String getContractHash();

    /**
     * 获取本地区块文件个数
     *
     * @return 区块文件个数
     */
    int getFileCount();

    /**
     * 获取指定合约下账本高度
     *
     * @return 指定合约账本高度
     */
    int getHeight();

    /**
     * 根据区块高度获取区块对象
     *
     * @param height 区块高度
     *
     * @return 区块对象
     */
    Block getBlockByHeight(int height);

    /**
     * 根据区块高度获取区块对象
     *
     * @param currentDataHash 当前区块hash
     *
     * @return 区块对象
     */
    Block getBlockByHash(String currentDataHash);

    /**
     * 根据区块高度获取区块对象
     *
     * @param transactionHash 交易hash
     *
     * @return 区块对象
     */
    Block getBlockByTransactionHash(String transactionHash);

}