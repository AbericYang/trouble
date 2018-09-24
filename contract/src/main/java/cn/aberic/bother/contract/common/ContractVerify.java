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

package cn.aberic.bother.contract.common;

import cn.aberic.bother.block.BlockAcquire;
import cn.aberic.bother.block.BlockStorage;
import cn.aberic.bother.contract.exec.PublicContractExec;
import cn.aberic.bother.contract.system.PublicContract;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.block.Transaction;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.response.IResponse;
import cn.aberic.bother.entity.response.Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * 合约执行验证对象
 * <p>
 * 作者：Aberic on 2018/09/21 11:41
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public class ContractVerify {

    private String contractHash;

    public ContractVerify(String contractHash) {
        this.contractHash = contractHash;
    }

    /**
     * 对交易集合进行验证
     *
     * @param transactions 交易集合
     * @return 通过验证的新交易集合
     */
    public List<Transaction> verifyTransactions(List<Transaction> transactions) {
        transactions.removeIf(transaction -> !verifyTransaction(transaction));
        return transactions;
    }

    /**
     * 对交易进行验证
     *
     * @param transaction 待验证交易
     * @return 验证通过与否
     */
    public boolean verifyTransaction(Transaction transaction) {
        switch (transaction.getRequest().getKey()) {
            case "openAccount":
                if (!KeyExec.obtain().verifyByStrECDSA(transaction.signString(), transaction.getSign(), transaction.getPubECCKey(), "UTF-8") ||
                        !verifyMethod(transaction.getRequest())) {
                    return false;
                }
            default:
                Account account = JSON.parseObject(new BlockStorage(contractHash).get(new BlockAcquire(contractHash), transaction.getCreator()), new TypeReference<Account>() {});
                if (null != account && !KeyExec.obtain().verifyByStrECDSA(transaction.signString(), transaction.getSign(), account.getPubECCKey(), "UTF-8") ||
                        !verifyMethod(transaction.getRequest())) {
                    return false;
                }
        }
        return true;
    }

    private boolean verifyMethod(Request request) {
        PublicContract contract = new PublicContract();
        PublicContractExec exec = new PublicContractExec();
        exec.setRequest(request);
        Response response = contract.invoke(exec);
        JSONObject jsonObject = JSON.parseObject(response.getResultResponse());
        return jsonObject.getInteger("code") == IResponse.ResponseType.SUCCESS.getCode();
    }

}
