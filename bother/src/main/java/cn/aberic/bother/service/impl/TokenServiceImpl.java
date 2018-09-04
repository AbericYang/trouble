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

package cn.aberic.bother.service.impl;

import cn.aberic.bother.account.AccountManager;
import cn.aberic.bother.bean.AccountUser;
import cn.aberic.bother.encryption.key.bean.Key;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.AccountBusiness;
import cn.aberic.bother.entity.contract.AccountInfo;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.TokenService;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.token.TokenManager;
import cn.aberic.bother.tools.exception.AccountBusinessTypeException;
import com.alibaba.fastjson.JSON;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Token 操作接口实现
 * <p>
 * 作者：Aberic on 2018/9/3 22:12
 * 邮箱：abericyang@gmail.com
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

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
        account.setPubKey(eccKey.getPublicKey());
        account.setJsonAccountInfoString(KeyExec.obtain().encryptByStrECDSA(eccKey.getPublicKey(), JSON.toJSONString(info)));

        token.setAccount(account);
        token.setTimestamp(timestamp);
        token.checkParams();
        token.build();
        TokenManager tokenManager = new TokenManager();
        tokenManager.createOrUpdateTmp(token);

        return new AccountUser(address, eccKey.getPrivateKey(), token.getHash());
    }

    @Override
    public Token publish(AccountBusiness accountBusiness) {
        // 判断该事务是否用于发布 Token
        if (accountBusiness.getBusiness() != AccountBusiness.Business.PUBLISH) {
            throw new AccountBusinessTypeException();
        }
        // TODO: 2018/9/3 存储费收取待实现
        // 获取公网账户管理器
        AccountManager accountPubManager = new AccountManager(Common.TOKEN_DEFAULT_SYSTEM_HASH);
        // 获取公网账户
        Account pubAccount = accountPubManager.getPubByAddress(accountBusiness.getPubAddress());
        // 解密事务意图
        String intent = KeyExec.obtain().decryptPriFileECDSA(pubAccount.getPubKey(), accountBusiness.getEncryption());
        // Account pubAccount = accountPubManager.getPubByAddress(accountBusiness.getPubAddress());
        // 根据当前待发布 Token 存储空间计算存储费
        // 根据account查询余额判断该账户是否有足够费用执行该笔操作
        // 如果没有，返回余额不足异常
        // 如果有，则继续下面步骤

        TokenManager tokenManager = new TokenManager();
        Token token = tokenManager.publish(accountBusiness.getAddress());
        Account account = token.getAccount();

        tokenManager.createOrUpdate(token);

        AccountManager accountManager = new AccountManager(token.getHash());
        accountManager.createOrUpdate(account);

        return token;
    }
}
