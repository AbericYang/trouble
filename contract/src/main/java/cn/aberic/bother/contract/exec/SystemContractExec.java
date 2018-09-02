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
import cn.aberic.bother.entity.block.*;
import cn.aberic.bother.entity.contract.Contract;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.tools.SystemTool;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
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

    public SystemContractExec() {
        rwSet = new RWSet();
        reads = new ArrayList<>();
        writes = new ArrayList<>();
    }

    /**
     * 得到本次交易对象。
     * <p>
     * 如果写集结果的长度为0，则表示本次没有写入操作，不计入区块
     *
     * @param creator 如果是联盟链则不能为空
     *
     * @return 交易对象
     */
    public Transaction getTransaction(@Nullable String creator) {
        if (writes.size() <= 0) {
            return null;
        }
        rwSet.setReads(reads);
        rwSet.setWrites(writes);
        Transaction transaction = new Transaction();
        transaction.setCreator(StringUtils.isEmpty(creator) ? SystemTool.getLocalMac() : creator);
        transaction.setTimestamp(new Date().getTime());
        transaction.setRwSet(rwSet);
        return transaction.build();
    }

    public void setRequest(Request request) {
        this.request = request;
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
    public Contract getContract() {
        return getContractFileExec().getContract();
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public void put(String key, String value) {
        ValueWrite write = new ValueWrite();
        write.setContractName(getContract().getName());
        write.setContractVersion(getContract().getVersionName());
        write.setStrings(new String[]{key, value});
        writes.add(write);
        getContractDataIndexFileExec().put(getContractDataFileExec().put(key, value));
    }

    @Override
    public String get(String key) {
        ValueRead read = new ValueRead();
        read.setContractName(getContract().getName());
        read.setContractVersion(getContract().getVersionName());
        read.setKey(key);
        reads.add(read);
        return getContractDataIndexFileExec().get(getContractDataFileExec(), key);
    }

    @Override
    public List<String> getHistory(String key) {
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
