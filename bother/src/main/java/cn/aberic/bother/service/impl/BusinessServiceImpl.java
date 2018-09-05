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
import cn.aberic.bother.encryption.key.bean.Key;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.IResponse;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.AccountBusiness;
import cn.aberic.bother.entity.contract.AccountBusinessEncrypt;
import cn.aberic.bother.entity.contract.AccountInfo;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.BusinessService;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.token.TokenManager;
import cn.aberic.bother.tools.DeflaterTool;
import cn.aberic.bother.tools.exception.AccountBusinessTypeException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        info.setCount(BigDecimal.valueOf(token.getTotalSupply()));
        info.setPriKey(rsaKey.getPrivateKey());
        info.setTimestamp(timestamp);

        String address = Hashing.sha256().hashString(eccKey.getPublicKey(), Charset.forName("UTF-8")).toString();

        account.setAddress(address);
        account.setPubRSAKey(rsaKey.getPublicKey());
        account.setPubECCKey(eccKey.getPublicKey());
        account.setJsonAccountInfoString(KeyExec.obtain().encryptByStrECDSA(eccKey.getPublicKey(), JSON.toJSONString(info)));

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
        AccountInfo info = JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(user.getPriKey(), account.getJsonAccountInfoString()),
                new TypeReference<AccountInfo>() {});
        return response(info.getPriKey());
    }

    @Override
    public String encryptBusiness(AccountBusinessEncrypt businessEncrypt) {
        return response(KeyExec.obtain().encryptPriStrRSA(businessEncrypt.getPriKey(), businessEncrypt.getEncryption()));
    }

    @Override
    public String business(AccountBusiness business) {
        // TODO: 2018/9/5
        TokenManager tokenManager = new TokenManager();
        Token token = tokenManager.getUnPublish(business.getAddress());
        Account account = token.getAccount();
        String execStr = KeyExec.obtain().decryptPubStrRSA(account.getPubRSAKey(), business.getEncryption());
        switch (business.getBusiness()) {
            case PUBLISH:
                return publishSystem(business, token, account, execStr, tokenManager);
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
     * @param business     账户处理事务对象
     * @param token        待发布 Token
     * @param account      待发布 Token 根账户
     * @param execStr      本次事务字符串
     * @param tokenManager Token 管理器
     * @return 发布结果
     */
    private String publishSystem(AccountBusiness business, Token token, Account account, String execStr, TokenManager tokenManager) {
        if (!StringUtils.equals(execStr, business.getBusiness().getFormat())) {
            throw new AccountBusinessTypeException();
        }
        try {
            long accountSize = DeflaterTool.compress(JSON.toJSONString(account)).length();
            long accountSizeJSON = JSON.toJSONString(account).length();
            long tokenSize = DeflaterTool.compress(JSON.toJSONString(token)).length();
            long tokenSizeJSON = JSON.toJSONString(token).length();
            log.debug("accountSize = {} | accountSizeJSON = {} | tokenSize = {} | tokenSizeJSON = {}", accountSize, accountSizeJSON, tokenSize, tokenSizeJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 验证该账户是否具备发布该 Token 的权限，即确认 Token 创始人信息
        if (StringUtils.equals(account.getAddress(), business.getAddress())) {
            tokenManager.createOrUpdate(JSON.toJSONString(token));
            AccountManager accountManager = new AccountManager(token.getHash());
            accountManager.createOrUpdate(JSON.toJSONString(account));
        }
        return response(Response.SUCCESS);
    }

    /**
     * 该操作会将此 Token 发布至公链，全网账本同步，需要附带可用账户且余额充足
     * <p>
     * 发布信息至公网账本将按字节收取存储手续费
     *
     * @param business     账户处理事务对象
     * @param token        待发布 Token
     * @param account      待发布 Token 根账户
     * @param execStr      本次事务字符串
     * @param tokenManager Token 管理器
     * @return 发布结果
     */
    private String publish(AccountBusiness business, Token token, Account account, String execStr, TokenManager tokenManager) {
        // 发布私有 Token 时，execStr需为以半角逗号分隔的 publish 和 公网账户私钥组合
        String[] execStrs = execStr.split(",");
        if (!StringUtils.equals(execStrs[0], business.getBusiness().getFormat())) {
            throw new AccountBusinessTypeException();
        }
        // 获取账户管理器
        AccountManager accountManager = new AccountManager(token.getHash());
        // 获取公网账户
        Account pubAccount = accountManager.getPubByAddress(business.getPubAddress());
        // 得到即将存储的账户字符串
        String accountStr = JSON.toJSONString(account);
        // 得到即将存储的 Token 不含账户信息的字符串
        token.setAccount(null);
        String tokenStr = JSON.toJSONString(token);
        // 计算本次账户及 Token 创建所需存储大小
        long size = accountStr.getBytes().length + tokenStr.getBytes().length;
        // 计算本次账户及 Token 创建所需存储费用
        BigDecimal consumption = new BigDecimal(size * 0.0001);
        BigDecimal pubAccountCount = getCountForAccount(Common.TOKEN_DEFAULT_SYSTEM_HASH, pubAccount.getAddress(), execStrs[1]).getCount();
        // 检查该公网账户是否有足够余额支付
        if (consumption.compareTo(pubAccountCount) < 0) {
            // 如果余额不足，则返回对应信息
            return response(Response.ACCOUNT_LACK_OF_BALANCE);
        }
        // 验证该账户是否具备发布该 Token 的权限，即确认 Token 创始人信息
        if (StringUtils.equals(account.getAddress(), business.getAddress())) {
            // TODO: 2018/9/5 将公网账户本次支出转给公共账户
            // 此操作需要根据系统智能合约进行交互，所操作结果将会写入账本
            tokenManager.createOrUpdate(tokenStr);
            accountManager.createOrUpdate(accountStr);
        } else {
            return response(Response.ACCOUNT_INFO_INVALID);
        }
        return response(Response.SUCCESS);
    }

    private AccountInfo getCountForAccount(String tokenHash, String address, String priECCKey) {
        AccountManager manager = new AccountManager(tokenHash);
        Account account = manager.getByAddress(address);
        return JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(priECCKey, account.getJsonAccountInfoString()), new TypeReference<AccountInfo>() {});
    }

}
