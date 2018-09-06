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

import cn.aberic.bother.contract.exec.service.IERC20Token;
import cn.aberic.bother.contract.exec.service.IPublicContractExec;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.AccountInfo;
import cn.aberic.bother.entity.token.Token;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * ERC20 接口实现
 * <p>
 * 作者：Aberic on 2018/9/5 21:09
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class ERC20Token implements IERC20Token {

    private String tokenHash;
    private Token token;
    private String address;
    private String priECCKey;
    private IPublicContractExec publicContractExec;

    public ERC20Token(String tokenHash, IPublicContractExec publicContractExec) {
        this.tokenHash = tokenHash;
        this.publicContractExec = publicContractExec;
        this.token = JSON.parseObject(publicContractExec.get(tokenHash), new TypeReference<Token>() {});
    }

    public ERC20Token(String tokenHash, String priECCKey, IPublicContractExec publicContractExec) {
        this.tokenHash = tokenHash;
        this.priECCKey = priECCKey;
        this.publicContractExec = publicContractExec;
        this.token = JSON.parseObject(publicContractExec.get(tokenHash), new TypeReference<Token>() {});
    }

    public ERC20Token(String tokenHash, String address, String priECCKey, IPublicContractExec publicContractExec) {
        this.tokenHash = tokenHash;
        this.address = address;
        this.priECCKey = priECCKey;
        this.publicContractExec = publicContractExec;
        this.token = JSON.parseObject(publicContractExec.get(tokenHash), new TypeReference<Token>() {});
    }

    public void setPriECCKey(String priECCKey) {
        this.priECCKey = priECCKey;
    }

    @Override
    public String name() {
        return token.getName();
    }

    @Override
    public String symbol() {
        return token.getSymbol();
    }

    @Override
    public int decimals() {
        return token.getDecimals();
    }

    @Override
    public int totalSupply() {
        return token.getTotalSupply();
    }

    @Override
    public BigDecimal balanceOf(String address) {
        Account account = JSON.parseObject(publicContractExec.get(address), new TypeReference<Account>() {});
        AccountInfo info = JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(priECCKey, account.getJsonAccountInfoString()),
                new TypeReference<AccountInfo>() {});
        return info.getCount();
    }

    @Override
    public boolean transfer(String addressTo, BigDecimal count) {
        return false;
    }

    @Override
    public boolean approve(String addressSpender, BigDecimal count) {
        return false;
    }

    @Override
    public boolean transferFrom(String addressFrom, String addressTo, BigDecimal count) {
        return false;
    }

    @Override
    public BigDecimal allowance(String addressOwner, String addressSpender) {
        return null;
    }

}
