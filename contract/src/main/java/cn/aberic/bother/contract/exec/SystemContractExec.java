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

package cn.aberic.bother.contract.exec;

import cn.aberic.bother.block.BlockAcquire;
import cn.aberic.bother.contract.exec.service.*;
import cn.aberic.bother.encryption.MD5;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.contract.Contract;
import cn.aberic.bother.storage.Common;

import java.util.List;

/**
 * 系统智能合约操作接口实现-smart contract
 * <p>
 * 作者：Aberic on 2018/8/30 20:54
 * 邮箱：abericyang@gmail.com
 */
public class SystemContractExec implements ISystemContractExec, IContractBaseExec {

    @Override
    public BlockAcquire getBlockAcquire() {
        return new BlockAcquire(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
    }

    @Override
    public ISystemContractFileExec getContractFileExec() {
        return new SystemContractFileExec();
    }

    @Override
    public IContractDataFileExec getContractDataFileExec() {
        return new ContractDataFileExec(getContractHash());
    }

    @Override
    public IContractDataIndexFileExec getContractDataIndexFileExec() {
        return new ContractDataIndexFileExec(getContractHash());
    }

    @Override
    public Contract getContract() {
        return getContractFileExec().getContract();
    }

    @Override
    public void put(String key, Object obj, boolean force) {
        getContractDataIndexFileExec().put(getContractDataFileExec().put(key, obj, force));
    }

    @Override
    public Object get(String key) {
        return getContractDataIndexFileExec().get(getContractDataFileExec(), key);
    }

    @Override
    public List<Object> getHistory(String key) {
        return getContractDataIndexFileExec().getHistory(getContractDataFileExec(), key);
    }

    @Override
    public String getContractHash() {
        return getContractFileExec().getContractHash();
    }

    @Override
    public int getFileCount() {
        return getBlockAcquire().getFileCount();
    }

    @Override
    public int getHeight() {
        return getBlockAcquire().getHeight();
    }

    @Override
    public Block getBlockByHeight(int height) {
        return getBlockAcquire().getBlockByHeight(height);
    }

    @Override
    public Block getBlockByHash(String currentDataHash) {
        return getBlockAcquire().getBlockByHash(MD5.md516(currentDataHash));
    }

    @Override
    public Block getBlockByTransactionHash(String transactionHash) {
        return getBlockAcquire().getBlockByTransactionHash(MD5.md516(transactionHash));
    }
}
