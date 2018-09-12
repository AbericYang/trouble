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

package cn.aberic.bother.contract.system;

import cn.aberic.bother.contract.exec.PublicContractExec;
import cn.aberic.bother.encryption.MD5;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.AccountInfo;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.response.IResponse;
import cn.aberic.bother.entity.response.Response;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.tools.Common;
import cn.aberic.bother.tools.exception.AccountNotFoundException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 智能合约 Token 相关辅助对象
 * <p>
 * 作者：Aberic on 2018/09/06 15:30
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
class TokenHelper implements IHelper {

    private PublicContractExec exec;
    private Token token;

    TokenHelper(PublicContractExec exec) {
        this.exec = exec;
        token = JSON.parseObject(exec.get(Common.TOKEN_DEFAULT_PUBLIC_HASH), new TypeReference<Token>() {});
    }

    /**
     * 发布新 Token
     *
     * @return 执行结果
     */
    Response publishToken(Request request) {
        JSONObject jsonObject = JSON.parseObject(exec.get("publish"));
        // 发布者公网账户
        Account pubAccount = JSON.parseObject(exec.get(request.getAddress()), new TypeReference<Account>() {});
        // 发布者该 Token 下账户
        Account account = (Account) jsonObject.get("account");
        // 待发布 Token
        Token token = (Token) jsonObject.get("token");

        // 得到即将存储的账户字符串
        String accountStr = JSON.toJSONString(account);
        // 得到即将存储的 Token 不含账户信息的字符串
        token.setAccount(null);
        String tokenStr = JSON.toJSONString(token);
        // 计算本次账户及 Token 创建所需存储大小
        long size = accountStr.getBytes().length + tokenStr.getBytes().length;
        // 计算本次账户及 Token 创建所需存储费用
        BigDecimal cost = coefficient(size, token.getDecimals());

        // 发布者公网账户余额
        BigDecimal pubCount = pubAccount.getCount();

        // 检查该公网账户是否有足够余额支付
        if (cost.compareTo(pubCount) > 0) {
            // 如果余额不足，则返回对应信息
            return exec.response(IResponse.ResponseType.ACCOUNT_LACK_OF_BALANCE);
        }

        // 扣减存储费
        pubCount = pubCount.subtract(cost);
        if (pubCount.doubleValue() < 0) {
            return exec.response(IResponse.ResponseType.ACCOUNT_LACK_OF_BALANCE);
        }

        // Token 上链
        exec.put(token.getHash(), JSON.toJSONString(token));
        // 账户上链
        exec.put(account.getAddress(), JSON.toJSONString(account));

        // 更新公网账户详情余额
        pubAccount.setCount(pubCount);
        // 更新后数据上链
        exec.put(request.getAddress(), JSON.toJSONString(pubAccount));

        return exec.response();
    }

    /**
     * 返回string类型的ERC20 Token 的名字，例如：NoTroubleBother
     *
     * @return Token 的名字
     */
    String name() {
        return token.getName();
    }

    /**
     * 返回string类型的ERC20 Token 的符号，也就是代币的简称，例如：NTB。
     *
     * @return Token 的符号
     */
    String symbol() {
        return token.getSymbol();
    }

    /**
     * 支持几位小数点后几位。如果设置为3。也就是支持0.001表示。
     *
     * @return 支持小数位数
     */
    int decimals() {
        return token.getDecimals();
    }

    /**
     * 发行 Token 的总量
     *
     * @return 发行 Token 的总量
     */
    int totalSupply() {
        return token.getTotalSupply();
    }

    /**
     * 根据指定地址返回该 Token 的余额
     *
     * @param address 指定地址
     *
     * @return 余额
     */
    BigDecimal balanceOf(String address) {
        Account account = JSON.parseObject(exec.get(address), new TypeReference<Account>() {});
        return account.getCount();
    }

    /**
     * 将自己的 count 个 Token 转给 addressTo 地址
     *
     * @return 成功与否
     */
    Response transfer(Request request) {
        JSONObject json = request.getJsonValue();
        // 待转账金额
        BigDecimal count = json.getBigDecimal("count").setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
        Account accountFrom = JSON.parseObject(exec.get(request.getAddress()), new TypeReference<Account>() {});
        Account accountTo = JSON.parseObject(exec.get(json.getString("addressTo")), new TypeReference<Account>() {});
        if (null == accountFrom) {
            return exec.response(IResponse.ResponseType.ACCOUNT_NOT_FOUND, request.getAddress());
        }
        if (null == accountTo) {
            return exec.response(IResponse.ResponseType.ACCOUNT_NOT_FOUND, json.getString("addressTo"));
        }
        // 判断转账方余额是否充足
        if (accountFrom.getCount().compareTo(count) < 0) {
            return exec.response(IResponse.ResponseType.ACCOUNT_LACK_OF_BALANCE);
        }
        // 计算本次账户及 Token 创建所需存储大小
        long size = accountFrom.getAddress().getBytes().length +
                JSON.toJSONString(accountFrom).getBytes().length +
                accountTo.getAddress().getBytes().length +
                JSON.toJSONString(accountTo).getBytes().length;
        // 计算本次账户创建所需存储费用
        BigDecimal cost = coefficient(size, token.getDecimals());
        log.debug("计算本次账户创建所需存储费用 cost = {}", cost);

        accountFrom.setCount(accountFrom.getCount().subtract(count));
        accountTo.setCount(accountTo.getCount().add(count.subtract(cost)));
        exec.put(accountFrom.getAddress(), JSON.toJSONString(accountFrom));
        exec.put(accountTo.getAddress(), JSON.toJSONString(accountTo));
        cost(exec, cost, token);
        return exec.response();
    }

    /**
     * 批准 addressSpender 账户从自己的账户转移 count 个 Token。
     *
     * @return 成功与否
     */
    Response approve(Request request) {
        JSONObject json = request.getJsonValue();
        // 待授权金额
        BigDecimal count = json.getBigDecimal("count").setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
        Account accountFrom = JSON.parseObject(exec.get(request.getAddress()), new TypeReference<Account>() {});
        Account accountSpender = JSON.parseObject(exec.get(json.getString("addressSpender")), new TypeReference<Account>() {});
        if (null == accountFrom) {
            throw new AccountNotFoundException(request.getAddress());
        }
        if (null == accountSpender) {
            throw new AccountNotFoundException(json.getString("addressSpender"));
        }
        // 余额不足，禁止授权操作
        if (count.compareTo(accountFrom.getCount()) > 0) {
            return exec.response(IResponse.ResponseType.ACCOUNT_LACK_OF_BALANCE);
        }

        String value = KeyExec.obtain().encryptPubStrRSA(accountSpender.getPubRSAKey(), count.toPlainString());
        request.getJsonValue().remove("count");

        String keyFormat = request.getAddress() + json.getString("addressSpender");
        if (keyFormat.length() != 128) {
            return exec.response(IResponse.ResponseType.FAIL);
        }
        String key = MD5.md516(keyFormat);

        // 计算本次账户及 Token 创建所需存储大小
        long size = key.getBytes().length +
                value.getBytes().length +
                accountFrom.getAddress().getBytes().length +
                JSON.toJSONString(accountFrom).getBytes().length;
        // 计算本次账户创建所需存储费用
        BigDecimal cost = coefficient(size, token.getDecimals());
        log.debug("计算本次账户创建所需存储费用 cost = {}", cost);

        count = count.add(cost);

        // 余额不足消费本次记录
        if (count.compareTo(accountFrom.getCount()) > 0) {
            return exec.response(IResponse.ResponseType.ACCOUNT_COST_GT_BALANCE_OWNER);
        }

        accountFrom.setCount(accountFrom.getCount().subtract(cost).setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP));
        exec.put(accountFrom.getAddress(), JSON.toJSONString(accountFrom));
        exec.put(key, value);
        cost(exec, cost, token);
        return exec.response();
    }

    /**
     * 与approve搭配使用，approve批准之后，调用本函数来转移token。
     *
     * @return 成功与否
     */
    Response transferFrom(Request request) {
        JSONObject json = exec.getRequest().getJsonValue();
        Account account = JSON.parseObject(exec.get(request.getAddress()), new TypeReference<Account>() {});
        Account accountOwner = JSON.parseObject(exec.get(json.getString("addressOwner")), new TypeReference<Account>() {});
        Account accountTo = JSON.parseObject(exec.get(json.getString("addressTo")), new TypeReference<Account>() {});
        if (null == account) {
            return exec.response(IResponse.ResponseType.ACCOUNT_NOT_FOUND, request.getAddress());
        }
        if (null == accountOwner) {
            return exec.response(IResponse.ResponseType.ACCOUNT_NOT_FOUND, json.getString("addressOwner"));
        }
        if (null == accountTo) {
            return exec.response(IResponse.ResponseType.ACCOUNT_NOT_FOUND, json.getString("addressTo"));
        }

        String keyFormat = json.getString("addressOwner") + request.getAddress();
        if (keyFormat.length() != 128) {
            return exec.response(IResponse.ResponseType.FAIL);
        }
        String key = MD5.md516(keyFormat);

        // 获取账户详情对象
        AccountInfo info = JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(exec.getPriECCKey(), account.getJsonAccountInfoString()),
                new TypeReference<AccountInfo>() {});
        // 可使用授权账户余额
        BigDecimal balance = new BigDecimal(KeyExec.obtain().decryptPriStrRSA(info.getPriRSAKey(), exec.get(key)));
        log.debug("可使用授权账户余额 balance = {}", balance);
        // 待支付金额
        BigDecimal count = json.getBigDecimal("count").setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
        log.debug("待支付金额 count = {}", count);
        // 加密使用后的可使用授权账户余额
        String value = KeyExec.obtain().encryptPubStrRSA(account.getPubRSAKey(), balance.subtract(count).toPlainString());
        // 授权账户余额
        BigDecimal balanceOwner = accountOwner.getCount();
        log.debug("授权账户余额 balanceOwner = {}", balanceOwner);

        // 本次交易大小
        long size = accountOwner.getAddress().getBytes().length +
                JSON.toJSONString(accountOwner).getBytes().length +
                accountTo.getAddress().getBytes().length +
                JSON.toJSONString(accountTo).getBytes().length +
                key.getBytes().length +
                value.getBytes().length;
        // 本次交易存储费
        BigDecimal cost = coefficient(size, token.getDecimals());
        log.debug("计算本次账户创建所需存储费用 cost = {}", cost);
        // 本次交易扣除授权账户费用
        count = count.add(cost);
        log.debug("本次交易扣除授权账户费用 count = {}", count);

        // 如果待支付金额大于可使用授权账户余额，则交易失败
        if (count.compareTo(balance) > 0) {
            return exec.response(IResponse.ResponseType.ACCOUNT_COST_GT_BALANCE);
        }
        // 如果待支付金额大于授权账户余额，则交易失败
        if (count.compareTo(balanceOwner) > 0) {
            return exec.response(IResponse.ResponseType.ACCOUNT_COST_GT_BALANCE_OWNER);
        }

        accountOwner.setCount(accountOwner.getCount().subtract(count));
        accountTo.setCount(accountTo.getCount().add(count.subtract(cost)));
        exec.put(accountOwner.getAddress(), JSON.toJSONString(accountOwner));
        exec.put(accountTo.getAddress(), JSON.toJSONString(accountTo));
        exec.put(key, value);
        cost(exec, cost, token);

        return exec.response(IResponse.ResponseType.SUCCESS);
    }

    /**
     * 返回 addressSpender 还能提取token的个数。
     * <p>
     * 账户A有1000个 Token，想允许B账户随意调用100个 Token。
     * A账户按照以下形式调用approve函数approve(B,100)。
     * 当B账户想用这100个 Token 中的10个 Token 给C账户时，则调用transferFrom(A, C, 10)。
     * 这时调用allowance(A, B)可以查看B账户还能够调用A账户多少个token。
     *
     * @return 能提取token的个数
     */
    Response allowance(Request request) {
        JSONObject json = exec.getRequest().getJsonValue();
        Account accountOwner = JSON.parseObject(exec.get(json.getString("addressOwner")), new TypeReference<Account>() {});
        Account accountSpender = JSON.parseObject(exec.get(request.getAddress()), new TypeReference<Account>() {});
        if (null == accountSpender) {
            return exec.response(IResponse.ResponseType.ACCOUNT_NOT_FOUND, request.getAddress());
        }
        if (null == accountOwner) {
            return exec.response(IResponse.ResponseType.ACCOUNT_NOT_FOUND, json.getString("addressOwner"));
        }
        // 获取账户详情对象
        AccountInfo info = JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(exec.getPriECCKey(), accountSpender.getJsonAccountInfoString()),
                new TypeReference<AccountInfo>() {});
        return exec.response(new BigDecimal(KeyExec.obtain().decryptPriStrRSA(info.getPriRSAKey(),
                exec.get(MD5.md516(json.getString("addressOwner") + request.getAddress())))));
    }

}
