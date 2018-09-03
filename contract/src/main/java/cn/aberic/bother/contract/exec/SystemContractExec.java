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
import cn.aberic.bother.block.BlockStorage;
import cn.aberic.bother.contract.exec.service.*;
import cn.aberic.bother.encryption.MD5;
import cn.aberic.bother.entity.block.*;
import cn.aberic.bother.entity.contract.Contract;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.tools.exception.ContractPutValueException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统智能合约操作接口实现-smart contract
 * <p>
 * 作者：Aberic on 2018/8/30 20:54
 * 邮箱：abericyang@gmail.com
 */
public class SystemContractExec implements ISystemContractExec, IContractBaseExec {

    private RWSet rwSet;
    private List<ValueRead> reads;
    private List<ValueWrite> writes;
    private Request request;
    /** 是否有写入数据 */
    private boolean hasBeenWritten = false;

    public SystemContractExec() {
        rwSet = new RWSet();
        reads = new ArrayList<>();
        writes = new ArrayList<>();
    }

    /**
     * 设置请求参数对象
     *
     * @param request 请求对象
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /** 发送交易到 Leader 节点 */
    public void sendTransaction() {
        // TODO: 2018/9/2 临时生成区块，实际应发送至 Leader 节点统一打包
        BlockStorage storage = new BlockStorage(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        BlockHeader header = BlockHeader.newInstance().create(true, 120, new Date().getTime());
        BlockBody body = new BlockBody();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(getTransaction());
        body.setTxCount(transactions.size());
        body.setTransactions(transactions);
        Block block = new Block(header, body);
        BlockInfo blockInfo = storage.save(block);
        getContractDataIndexFileExec().put(blockInfo, writes);
    }

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
    public Transaction getTransaction() {
        if (!hasBeenWritten) {
            return null;
        }
        rwSet.setReads(reads);
        rwSet.setWrites(writes);
        Transaction transaction = new Transaction();
        transaction.setCreator("");
        transaction.setTimestamp(new Date().getTime());
        transaction.setRwSet(rwSet);
        return transaction.build();
    }

    @Override
    public Contract getContract() {
        return getContractFileExec().getContract();
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public String getTxID() {
        Transaction transaction = getTransaction();
        if (null == transaction) {
            throw new RuntimeException("there was no message been written");
        }
        return transaction.getHash();
    }

    @Override
    public void put(String key, String value) {
        if (null == value) {
            throw new ContractPutValueException();
        }
        ValueWrite write = new ValueWrite();
        write.setContractName(getContract().getName());
        write.setContractVersion(getContract().getVersionName());
        write.setStrings(new String[]{key, value});
        writes.add(write);
        hasBeenWritten = true;
    }

    @Override
    public String get(String key) {
        ValueRead read = new ValueRead();
        read.setContractName(getContract().getName());
        read.setContractVersion(getContract().getVersionName());
        read.setKey(key);
        reads.add(read);
        return getContractDataIndexFileExec().get(getBlockAcquire(), key);
    }

    @Override
    public List<String> getHistory(String key) {
        return getContractDataIndexFileExec().getHistory(getBlockAcquire(), key);
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
