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

import cn.aberic.bother.block.BlockAcquire;

/**
 * 智能合约基本操作接口-smart contract
 * <p>
 * 当前接口原本可由 {@link ISystemContractExec} 以及 {@link IContractExec} 直接继承
 * <p>
 * 但为了不在用户编写的智能合约接口层暴露出来，故此接口需由 {@link ISystemContractExec} 以及 {@link IContractExec} 的实现对象来继承实现。
 * <p>
 * 作者：Aberic on 2018/8/30 23:11
 * 邮箱：abericyang@gmail.com
 */
public interface IContractBaseExec {

    /**
     * 获取区块获取操作对象
     * <p>
     * 智能合约通过该对象可以得到区块中的基本信息
     * <p>
     * 并以此对象可以进行溯源操作
     *
     * @return 区块获取操作对象
     */
    BlockAcquire getBlockAcquire();

    /**
     * 智能合约文件对象操作接口
     * <p>
     * 获取 {@link cn.aberic.bother.storage.IFile} 实现类。
     * <p>
     * 原本应该当前接口继承 {@link cn.aberic.bother.storage.IFile} 进行操作，但有关 {@link cn.aberic.bother.storage.IFile} 接口不方便直接暴露出去。
     * <p>
     * 所以这里采用的方案与 {@link cn.aberic.bother.block.exec.service.IBlockExec}和 {@link cn.aberic.bother.block.exec.service.IIndexExec} 不同
     *
     * @return {@link cn.aberic.bother.storage.IFile} 实现
     */
    ISystemContractFileExec getContractFileExec();

    /**
     * 智能合约数据文件对象操作接口
     * <p>
     * 获取 {@link cn.aberic.bother.storage.IFile} 实现类。
     * <p>
     * 原本应该当前接口继承 {@link cn.aberic.bother.storage.IFile} 进行操作，但有关 {@link cn.aberic.bother.storage.IFile} 接口不方便直接暴露出去。
     * <p>
     * 所以这里采用的方案与 {@link cn.aberic.bother.block.exec.service.IBlockExec}和 {@link cn.aberic.bother.block.exec.service.IIndexExec} 不同
     *
     * @return {@link cn.aberic.bother.storage.IFile} 实现
     */
    IContractDataFileExec getContractDataFileExec();

}
