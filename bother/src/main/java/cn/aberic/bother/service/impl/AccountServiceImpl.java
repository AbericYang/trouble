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
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.IResponse;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.AccountBusiness;
import cn.aberic.bother.entity.contract.AccountBusinessEncrypt;
import cn.aberic.bother.entity.contract.AccountInfo;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.AccountService;
import cn.aberic.bother.token.TokenManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.stereotype.Service;

/**
 * 账户操作接口实现
 * <p>
 * 作者：Aberic on 2018/9/3 22:34
 * 邮箱：abericyang@gmail.com
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService, IResponse {

    @Override
    public String getRSAPri(AccountUser user) {
        TokenManager tokenManager = new TokenManager();
        Token token = tokenManager.getUnPublish(user.getAddress());
        Account account = token.getAccount();
        AccountInfo info = JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(user.getPriKey(), account.getJsonAccountInfoString()),
                new TypeReference<AccountInfo>() {});
        return info.getPriKey();
    }

    @Override
    public String encryptBusiness(AccountBusinessEncrypt businessEncrypt) {
        return KeyExec.obtain().encryptPriStrRSA(businessEncrypt.getPriKey(), businessEncrypt.getEncryption());
    }

    @Override
    public String business(AccountBusiness business) {
        // TODO: 2018/9/5
        switch (business.getBusiness()) {
            case PUBLISH:
                break;
            case QUERY:
                break;
            case TRANSFER:
                break;
            case APPROVE:
                break;
            case TRANSFER_FROM:
                break;
            case ALLOWANCE:
                break;
        }
        return null;
    }

    @Override
    public String save(Token token) {
        Account account = token.getAccount();
        AccountManager manager = new AccountManager(token.getHash());
        manager.createOrUpdate(account);
        return response();
    }
}
