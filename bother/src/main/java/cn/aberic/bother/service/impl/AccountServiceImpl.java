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
import cn.aberic.bother.encryption.key.KeyExec;
import cn.aberic.bother.encryption.key.bean.Key;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.AccountService;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * 账户操作接口实现
 * <p>
 * 作者：Aberic on 2018/9/3 22:34
 * 邮箱：abericyang@gmail.com
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Override
    public Account saveTmp(Token token) {
        Account account = new Account();
        Key key = KeyExec.obtain().createECCDSAKeyPair();
        BigDecimal count = BigDecimal.valueOf(token.getTotalSupply());
        String address = Hashing.sha256().hashString(key.getPrivateKey(), Charset.forName("UTF-8")).toString();
        account.setPubKey(key.getPublicKey());
        account.setCount(count);
        account.setAddress(address);
        account.setTimestamp(new Date().getTime());
        AccountManager manager = new AccountManager(token.getHash());
        manager.createOrUpdateTmp(account);
        return account;
    }
}
