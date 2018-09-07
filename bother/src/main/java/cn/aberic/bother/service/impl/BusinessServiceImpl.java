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

import cn.aberic.bother.account.AccountManager;
import cn.aberic.bother.bean.AccountUser;
import cn.aberic.bother.contract.system.PublicContract;
import cn.aberic.bother.contract.exec.PublicContractExec;
import cn.aberic.bother.encryption.key.bean.Key;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.IResponse;
import cn.aberic.bother.entity.contract.*;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.BusinessService;
import cn.aberic.bother.token.TokenManager;
import cn.aberic.bother.tools.exception.AccountBusinessTypeException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * 账户、Token 操作业务实现
 * <p>
 * 作者：Aberic on 2018/09/05 11:09
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

        long timestamp = new Date().getTime();

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
    public String getRSAPri(AccountUser user) {
        TokenManager tokenManager = new TokenManager();
        Token token = tokenManager.getUnPublish(user.getAddress());
        Account account = token.getAccount();
        AccountInfo info = JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(user.getPriECCKey(), account.getJsonAccountInfoString()),
                new TypeReference<AccountInfo>() {});
        return response(info.getPriRSAKey());
    }

    @Override
    public String encryptBusiness(AccountBusinessEncrypt businessEncrypt) {
        return response(KeyExec.obtain().encryptPriStrRSA(businessEncrypt.getPriRSAKey(), businessEncrypt.getEncryption()));
    }

    @Override
    public String business(AccountBusiness business) {
        switch (business.getBusiness()) {
            case PUBLISH:
                TokenManager tokenManager = new TokenManager();
                Token token = tokenManager.getUnPublish(business.getAddress());
                Account account = token.getAccount();
                String execStr = KeyExec.obtain().decryptPubStrRSA(account.getPubRSAKey(), business.getEncryption());
                // 验证该账户是否具备发布该 Token 的权限，即确认 Token 创始人信息
                if (!StringUtils.equals(account.getAddress(), business.getAddress())) {
                    return response(Response.ACCOUNT_INFO_INVALID);
                }
                String result = publishSystem(business, token, account, execStr);
                tokenManager.clear(business.getAddress());
                return result;
            case QUERY:
                return null;
            case TRANSFER:
                return null;
            case APPROVE:
                return null;
            case TRANSFER_FROM:
                return null;
            case ALLOWANCE:
                return null;
        }
        throw new AccountBusinessTypeException();
    }

    @Override
    public AccountInfo getCount(String tokenHash, String address, String priECCKey) {
        return getCountForAccount(tokenHash, address, priECCKey);
    }

    /**
     * 该操作会将此 Token 发布至公链，全网账本同步，需要附带可用账户且余额充足
     * <p>
     * 发布信息至公网账本将按字节收取存储手续费
     *
     * @param business 账户处理事务对象
     * @param token    待发布 Token
     * @param account  待发布 Token 根账户
     * @param execStr  本次事务字符串
     * @return 发布结果
     */
    private String publishSystem(AccountBusiness business, Token token, Account account, String execStr) {
        if (!StringUtils.equals(execStr, business.getBusiness().getFormat())) {
            throw new AccountBusinessTypeException();
        }
        PublicContract publicContract = new PublicContract();
        PublicContractExec publicContractExec = new PublicContractExec();
        // Token 上链
        Request request = new Request();
        request.setKey(token.getHash());
        token.setAccount(null);
        request.setValue(JSON.toJSONString(token));
        request.setAddress(account.getAddress());
        request.setPriECCKey(business.getPriECCKey());
        request.setTokenHash(token.getHash());
        publicContractExec.setRequest(request);
        log.debug("invoke token = {}", publicContract.invoke(publicContractExec));
        // 账户上链
        request.setKey(account.getAddress());
        request.setValue(JSON.toJSONString(account));
        publicContractExec.setRequest(request);
        log.debug("invoke account = {}", publicContract.invoke(publicContractExec));
        publicContractExec.sendTransaction();
        return response(Response.SUCCESS);
    }

    /**
     * 该操作会将此 Token 发布至公链，全网账本同步，需要附带可用账户且余额充足
     * <p>
     * 发布信息至公网账本将按字节收取存储手续费
     *
     * @param business 账户处理事务对象
     * @param token    待发布 Token
     * @param account  待发布 Token 根账户
     * @param execStr  本次事务字符串
     * @return 发布结果
     */
    private String publish(AccountBusiness business, Token token, Account account, String execStr) {
        if (!StringUtils.equals(execStr, business.getBusiness().getFormat())) {
            throw new AccountBusinessTypeException();
        }
        PublicContract publicContract = new PublicContract();
        PublicContractExec publicContractExec = new PublicContractExec();
        // 拼接上链数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pubAddress", business.getPubAddress());
        jsonObject.put("priECCKey", business.getPriECCKey());
        jsonObject.put("account", account);
        jsonObject.put("token", token);

        // 智能合约请求数据
        Request request = new Request();
        request.setKey("publish");
        request.setValue(jsonObject.toJSONString());
        publicContractExec.setRequest(request);
        String requestStr = publicContract.invoke(publicContractExec);
        publicContractExec.sendTransaction();
        return requestStr;
    }

    private AccountInfo getCountForAccount(String tokenHash, String address, String priECCKey) {
        AccountManager manager = new AccountManager(tokenHash);
        Account account = manager.getByAddress(address);
        return JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(priECCKey, account.getJsonAccountInfoString()), new TypeReference<AccountInfo>() {});
    }

}
