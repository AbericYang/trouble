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

package cn.aberic.bother.service.impl;

import cn.aberic.bother.contract.exec.PublicContractExec;
import cn.aberic.bother.contract.system.PublicContract;
import cn.aberic.bother.encryption.key.bean.Key;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.account.AccountUser;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.AccountInfo;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.response.IResponse;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.BusinessService;
import cn.aberic.bother.service.TransactionService;
import cn.aberic.bother.token.TokenManager;
import com.alibaba.fastjson.JSON;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.Charset;

/**
 * 账户、Token 操作业务实现
 * <p>
 * 作者：Aberic on 2018/09/05 11:09
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Service("businessService")
public class BusinessServiceImpl implements BusinessService, IResponse {

    @Override
    public AccountUser create(Token token) {
        Account account = new Account();
        Key rsaKey = KeyExec.obtain().createRSAKeyPair();
        Key eccKey = KeyExec.obtain().createECCDSAKeyPair();

        long timestamp = System.currentTimeMillis();

        AccountInfo info = new AccountInfo();
        info.setPriRSAKey(rsaKey.getPrivateKey());

        String address = Hashing.sha256().hashString(eccKey.getPublicKey(), Charset.forName("UTF-8")).toString();

        account.setAddress(address);
        account.setCount(BigDecimal.valueOf(token.getTotalSupply()));
        account.setPubRSAKey(rsaKey.getPublicKey());
        account.setPubECCKey(eccKey.getPublicKey());
        account.setJsonAccountInfoString(KeyExec.obtain().encryptByStrECDSA(eccKey.getPublicKey(), JSON.toJSONString(info)));
        account.setTimestamp(timestamp);

        token.setAccount(account);
        token.setTimestamp(timestamp);
        token.checkParams();
        token.build();
        TokenManager tokenManager = new TokenManager();
        tokenManager.createOrUpdateTmp(JSON.toJSONString(token));

        return new AccountUser(address, eccKey.getPrivateKey(), token.getHash());
    }

    @Override
    public String publish(Request request, TransactionService transactionService) {
        TokenManager tokenManager = new TokenManager();
        Token token = tokenManager.getUnPublish(request.getAddress());
        Account account = token.getAccount();
        String result = publish(token, account, request, transactionService);
        tokenManager.clear(account.getAddress());
        return result;
    }

    /**
     * 该操作会将此 Token 发布至公链，全网账本同步，需要附带可用账户且余额充足
     * <p>
     * 发布信息至公网账本将按字节收取存储手续费
     *
     * @param token   待发布 Token
     * @param account 待发布 Token 根账户
     * @param request 智能合约请求对象
     * @return 发布结果
     */
    private String publish(Token token, Account account, Request request, TransactionService transactionService) {
        PublicContract publicContract = new PublicContract();
        PublicContractExec exec = new PublicContractExec();
        // Token 上链
        request.setKey(token.getHash());
        token.setAccount(null);
        request.setValue(JSON.toJSONString(token));
        exec.setRequest(request);
        log.debug("invoke token = {}", publicContract.invoke(exec));
        // 账户上链
        request.setKey(account.getAddress());
        request.setValue(JSON.toJSONString(account));
        exec.setRequest(request);
        log.debug("invoke account = {}", publicContract.invoke(exec));
        if (transactionService.checkBlockVerify(exec.getContractHash(), exec.getTransaction())) {
            return exec.response().getResultResponse();
        }
        // exec.sendTransaction(exec.getBlock());
        return exec.response(IResponse.ResponseType.FAIL).getResultResponse();
    }

}
