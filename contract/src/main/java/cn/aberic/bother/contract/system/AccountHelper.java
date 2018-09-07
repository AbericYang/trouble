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

import cn.aberic.bother.contract.exec.ERC20Token;
import cn.aberic.bother.contract.exec.PublicContractExec;
import cn.aberic.bother.contract.exec.service.IPublicContractExec;
import cn.aberic.bother.encryption.MD5;
import cn.aberic.bother.encryption.key.bean.Key;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.IResponse;
import cn.aberic.bother.entity.account.AccountUser;
import cn.aberic.bother.entity.account.Cheque;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.AccountBusiness;
import cn.aberic.bother.entity.contract.AccountInfo;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.tools.DateTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * 账户辅助对象
 * <p>
 * 作者：Aberic on 2018/09/07 12:17
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
class AccountHelper {

    /**
     * 账户开支票
     *
     * @param exec     系统级智能合约操作接口
     * @param business 账户处理事务
     * @return 支票流转字符串信息
     */
    String cheque(IPublicContractExec exec, AccountBusiness business) throws Exception {
        // 开支票的公网账户
        Account pubAccount = JSON.parseObject(exec.get(business.getAddress()), new TypeReference<Account>() {});
        // 开支票的公网账户余额
        BigDecimal pubCount = pubAccount.getCount();
        // 获取意图并解析为字符串数组
        String[] intents = business.getEncryption().split(",");
        Cheque cheque = new Cheque();
        cheque.setCount(new BigDecimal(intents[0]));
        cheque.setType(Integer.valueOf(intents[1]));
        cheque.setStartTimestamp(new Date().getTime());
        cheque.setEndTimestamp(DateTool.str2Date(intents[2], "yyyy/MM/dd HH:mm:ss").getTime());

        // 检查开支票的金额是否小于0
        if (cheque.getCount().compareTo(new BigDecimal(0)) < 0) {
            log.debug("支票金额 = {}", cheque.getCount().toPlainString());
            return exec.response(IResponse.Response.PARAM_ERROR);
        }

        // 检查该公网账户是否有足够余额开此支票
        if (cheque.getCount().compareTo(pubCount) > 0) {
            log.debug("账户金额 = {}", pubCount.toPlainString());
            // 如果余额不足，则返回对应信息
            return exec.response(IResponse.Response.ACCOUNT_LACK_OF_BALANCE);
        }

        // 获取账户详情对象
        AccountInfo info = JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(business.getPriECCKey(), pubAccount.getJsonAccountInfoString()),
                new TypeReference<AccountInfo>() {});
        // 将支票对象json字符串化后由账户 RSA 私钥进行加密返回
        return exec.response(KeyExec.obtain().encryptPriStrRSA(info.getPriRSAKey(), JSON.toJSONString(cheque)));
    }

    /**
     * 开户
     *
     * @param exec       系统级智能合约操作接口
     * @param erc20Token ERC20 接口实现
     * @param business   账户处理事务
     * @return 支票流转字符串信息
     */
    String openAccount(PublicContractExec exec, ERC20Token erc20Token, AccountBusiness business) {
        Token token = erc20Token.getToken();
        // 创建账户需要支付支票
        // 获取支票账户
        Account chequeAccount = JSON.parseObject(exec.get(business.getAddress()), new TypeReference<Account>() {});
        // 根据意图获取支票对象
        Cheque cheque = JSON.parseObject(KeyExec.obtain().decryptPubStrRSA(chequeAccount.getPubRSAKey(), business.getEncryption()),
                new TypeReference<Cheque>() {});
        // 如果链上已经记录了该支票信息，则表示该支票已被消费过
        if (null != exec.get(MD5.md516(cheque.toString()))) {
            return exec.response(IResponse.Response.CHEQUE_INVALID);
        }
        // 如果支票已经过期，则此支票无效
        if (new Date().getTime() > cheque.getEndTimestamp()) {
            return exec.response(IResponse.Response.CHEQUE_OVERDUE);
        }

        log.debug("开户 支票金额 = {}", cheque.getCount().toPlainString());
        // 检查开支票的金额是否小于0
        if (cheque.getCount().compareTo(new BigDecimal(0)) < 0) {
            return exec.response(IResponse.Response.PARAM_ERROR);
        }

        log.debug("开户 账户金额 = {}", chequeAccount.getCount().toPlainString());
        // 检查该公网账户是否有足够余额支付此支票
        if (cheque.getCount().compareTo(chequeAccount.getCount()) > 0) {
            // 如果余额不足，则返回对应信息
            return exec.response(IResponse.Response.ACCOUNT_LACK_OF_BALANCE);
        }

        // 支票满足使用条件，开始创建新账户
        // 创建新账户信息
        Account account = new Account();
        Key rsaKey = KeyExec.obtain().createRSAKeyPair();
        Key eccKey = KeyExec.obtain().createECCDSAKeyPair();

        AccountInfo info = new AccountInfo();
        info.setPriRSAKey(rsaKey.getPrivateKey());

        String address = Hashing.sha256().hashString(eccKey.getPublicKey(), Charset.forName("UTF-8")).toString();

        account.setAddress(address);
        account.setCount(BigDecimal.valueOf(0));
        account.setPubRSAKey(rsaKey.getPublicKey());
        account.setPubECCKey(eccKey.getPublicKey());
        account.setJsonAccountInfoString(KeyExec.obtain().encryptByStrECDSA(eccKey.getPublicKey(), JSON.toJSONString(info)));
        account.setTimestamp(new Date().getTime());

        // 得到即将存储的账户字符串
        String accountStr = JSON.toJSONString(account);
        // 计算本次账户及 Token 创建所需存储大小
        long size = accountStr.getBytes().length;
        // 计算本次账户创建所需存储费用
        BigDecimal consumption = new BigDecimal(size * 0.0001).setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
        log.debug("开户 存储费用 = {}", consumption.toPlainString());
        // 支付费用后支票余额
        BigDecimal balance = cheque.getCount().subtract(consumption);

        // 检查该支票是否有足够金额支付
        if (consumption.compareTo(cheque.getCount()) > 0) {
            // 如果支票金额不足，则返回对应信息
            return exec.response(IResponse.Response.CHEQUE_LACK_OF_BALANCE);
        }

        // 扣减存储费，并将多出来的 Token 根据支票属性判定所有
        if (cheque.getType() == 1 && balance.compareTo(new BigDecimal(0.0001)) <= 0) {
            // 先让账户创建所需存储费用增加0.0001，进入循环后循环减去0.0001，直到算出正确费用
            consumption = consumption.add(new BigDecimal(0.0001)).setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
            do {
                consumption = consumption.subtract(new BigDecimal(0.0001)).setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
                // 支付费用后支票余额
                balance = cheque.getCount().subtract(consumption);
                log.debug("开户 支票余额 = {}", balance.toPlainString());
                // 重新设置账户余额
                account.setCount(balance);
                // 得到新的即将存储的账户字符串
                accountStr = JSON.toJSONString(account);
                // 重新计算本次账户及 Token 创建所需存储大小
                size = accountStr.getBytes().length;
                // 重新计算本次账户及 Token 创建所需存储费用
                consumption = new BigDecimal(size * 0.0001).setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
                log.debug("开户 新存储费用 = {}", consumption.toPlainString());
                // 支票开票账户消费本次支票金额
                chequeAccount.setCount(chequeAccount.getCount().subtract(cheque.getCount()));

                // 检查该支票是否有足够金额支付
            } while (consumption.compareTo(cheque.getCount()) > 0);
        } else {
            // 支票开票账户消费本次支票金额
            chequeAccount.setCount(chequeAccount.getCount().subtract(consumption));
        }

        exec.setPriECCKey(eccKey.getPrivateKey());
        // 账户上链
        exec.put(account.getAddress(), JSON.toJSONString(account));
        // 更新支票开票账户金额
        exec.put(chequeAccount.getAddress(), JSON.toJSONString(chequeAccount));
        // 支票消费记录上链
        exec.put(MD5.md516(cheque.toString()), "1");

        return exec.response(new AccountUser(address, eccKey.getPrivateKey(), erc20Token.getTokenHash()));
    }

}
