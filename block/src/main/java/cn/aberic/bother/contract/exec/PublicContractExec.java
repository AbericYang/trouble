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
import cn.aberic.bother.contract.exec.service.IContractBaseExec;
import cn.aberic.bother.contract.exec.service.IContractDataIndexFileExec;
import cn.aberic.bother.contract.exec.service.IPublicContractExec;
import cn.aberic.bother.contract.exec.service.IPublicContractFileExec;
import cn.aberic.bother.entity.block.*;
import cn.aberic.bother.entity.contract.Contract;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.tools.exception.ContractPutValueException;
import cn.aberic.bother.tools.exception.SearchDataNotFoundException;
import cn.aberic.bother.tools.exception.SearchDataTimeoutException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统智能合约操作接口实现-smart contract
 * <p>
 * 作者：Aberic on 2018/8/30 20:54
 * 邮箱：abericyang@gmail.com
 */
public class PublicContractExec implements IPublicContractExec, IContractBaseExec {

    private RWSet rwSet;
    private List<ValueRead> reads;
    private List<ValueWrite> writes;
    private Request request;
    private String priECCKey;
    private String pubECCKey;

    public PublicContractExec() {
        rwSet = new RWSet();
        reads = new ArrayList<>();
        writes = new ArrayList<>();
    }

    public void setPriECCKey(String priECCKey) {
        this.priECCKey = priECCKey;
    }

    public String getPriECCKey() {
        return priECCKey;
    }

    public void setPubECCKey(String pubECCKey) {
        this.pubECCKey = pubECCKey;
    }

    /**
     * 设置请求参数对象
     *
     * @param request 请求对象
     */
    public void setRequest(Request request) {
        this.request = request;
        if (StringUtils.isNotEmpty(request.getPriECCKey())) {
            setPriECCKey(this.request.getPriECCKey());
        }
        this.request.setPriECCKey(null);
    }

    /** 发送交易到 Leader 节点 */
    public void sendTransaction() {
        // TODO: 2018/9/2 临时生成区块，实际应发送至 Leader 节点统一打包，此方法应当返回 Block 对象，并交由 Controller 进行转发
        BlockStorage storage = new BlockStorage(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        BlockHeader header = BlockHeader.newInstance().create(true, 120, new Date().getTime());
        BlockBody body = new BlockBody();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(getTransaction());
        body.setTxCount(transactions.size());
        body.setTransactions(transactions);
        Block block = new Block(header, body);
        BlockInfo blockInfo = storage.snyc(block);
        getContractDataIndexFileExec().put(blockInfo, writes);
    }

    @Override
    public BlockAcquire getBlockAcquire() {
        return new BlockAcquire(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
    }

    @Override
    public IPublicContractFileExec getContractFileExec() {
        return new PublicContractFileExec();
    }

    @Override
    public IContractDataIndexFileExec getContractDataIndexFileExec() {
        return new ContractDataIndexFileExec(getContractHash());
    }

    @Override
    public Transaction getTransaction() {
        rwSet.setReads(reads);
        rwSet.setWrites(writes);
        Transaction transaction = new Transaction();
        transaction.setContractName(getContract().getHash());
        transaction.setContractName(getContract().getName());
        transaction.setContractVersion(getContract().getVersionName());
        transaction.setCreator(request.getAddress());
        transaction.setTimestamp(new Date().getTime());
        transaction.setRwSet(rwSet);
        getRequest().setPriECCKey(null);
        if (null != pubECCKey) {
            transaction.setPubECCKey(pubECCKey);
        }
        transaction.setRequest(getRequest());
        return transaction.build(priECCKey);
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
        return transaction.getTxHash();
    }

    @Override
    public void put(String key, String value) {
        if (null == value) {
            throw new ContractPutValueException();
        }
        ValueWrite write = new ValueWrite();
        write.setStrings(new String[]{key, value});
        writes.add(write);
    }

    @Override
    public String get(String key) {
        ValueRead read = new ValueRead();
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
        return getContractFileExec().getStorageHash();
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
    public Block getBlockByHeight(int height) throws SearchDataNotFoundException, SearchDataTimeoutException {
        return getBlockAcquire().getBlockByHeight(height);
    }

    @Override
    public Block getBlockByHash(String currentDataHash) throws SearchDataNotFoundException, SearchDataTimeoutException {
        return getBlockAcquire().getBlockByHash(currentDataHash);
    }

    @Override
    public Block getBlockByTransactionHash(String transactionHash) throws SearchDataNotFoundException, SearchDataTimeoutException {
        return getBlockAcquire().getBlockByTransactionHash(transactionHash);
    }
}
