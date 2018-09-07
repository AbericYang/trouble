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
import cn.aberic.bother.contract.system.IHelper;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.tools.exception.AccountNotFoundException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * ERC20 接口实现
 * <p>
 * 作者：Aberic on 2018/9/5 21:09
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
public class ERC20Token implements IERC20Token, IHelper {

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
        return account.getCount();
    }

    @Override
    public boolean transfer(String addressTo, BigDecimal count) {
        count = count.setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
        Account accountFrom = JSON.parseObject(publicContractExec.get(address), new TypeReference<Account>() {});
        Account accountTo = JSON.parseObject(publicContractExec.get(addressTo), new TypeReference<Account>() {});
        if (null == accountTo) {
            throw new AccountNotFoundException(addressTo);
        }
        // 判断转账方余额是否充足
        if (accountFrom.getCount().compareTo(count) < 0) {
            return false;
        }
        // 计算本次账户及 Token 创建所需存储大小
        long size = JSON.toJSONString(accountFrom).getBytes().length + JSON.toJSONString(accountTo).getBytes().length;
        // 计算本次账户创建所需存储费用
        BigDecimal cost = new BigDecimal(size * 0.0001).setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
        log.debug("计算本次账户创建所需存储费用 cost = {}", cost);

        accountFrom.setCount(accountFrom.getCount().subtract(count));
        accountTo.setCount(accountTo.getCount().add(count.subtract(cost)));
        publicContractExec.put(accountFrom.getAddress(), JSON.toJSONString(accountFrom));
        publicContractExec.put(accountTo.getAddress(), JSON.toJSONString(accountTo));
        cost(publicContractExec, cost, token);
        return true;
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
