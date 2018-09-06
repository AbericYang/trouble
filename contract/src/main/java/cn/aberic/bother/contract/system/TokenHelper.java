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
import cn.aberic.bother.contract.exec.service.ISystemContractExec;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.IResponse;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.AccountInfo;
import cn.aberic.bother.entity.token.Token;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.math.BigDecimal;

/**
 * 智能合约 Token 相关辅助对象
 * <p>
 * 作者：Aberic on 2018/09/06 15:30
 * 邮箱：abericyang@gmail.com
 */
class TokenHelper {

    /**
     * 发布新 Token
     *
     * @param erc20Token ERC20 接口实现
     * @return 执行结果
     */
    String publishToken(ERC20Token erc20Token) {
        ISystemContractExec exec = erc20Token.getSystemContractExec();
        JSONObject jsonObject = JSON.parseObject(exec.get("publish"));
        // 发布者公网账户
        Account pubAccount = JSON.parseObject(exec.get(erc20Token.getAddress()), new TypeReference<Account>() {});
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
        BigDecimal consumption = new BigDecimal(size * 0.0001);

        // 发布者公网账户详情
        AccountInfo pubInfo = JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(erc20Token.getPriECCKey(), pubAccount.getJsonAccountInfoString()),
                new TypeReference<AccountInfo>() {});
        // 发布者公网账户余额
        BigDecimal pubCount = pubInfo.getCount();

        // 检查该公网账户是否有足够余额支付
        if (consumption.compareTo(pubCount) < 0) {
            // 如果余额不足，则返回对应信息
            return exec.response(IResponse.Response.ACCOUNT_LACK_OF_BALANCE);
        }

        // 扣减存储费
        pubCount = pubCount.subtract(consumption);
        if (pubCount.doubleValue() < 0) {
            return exec.response(IResponse.Response.ACCOUNT_LACK_OF_BALANCE);
        }

        // Token 上链
        exec.put(token.getHash(), JSON.toJSONString(token));
        // 账户上链
        exec.put(account.getAddress(), JSON.toJSONString(account));

        // 更新公网账户详情余额
        pubInfo.setCount(pubCount);
        // 更新公网账户
        pubAccount.setJsonAccountInfoString(KeyExec.obtain().encryptByStrECDSA(pubAccount.getPubECCKey(), JSON.toJSONString(pubInfo)));
        // 更新后数据上链
        exec.put(erc20Token.getAddress(), JSON.toJSONString(pubAccount));

        return exec.response(IResponse.Response.SUCCESS);
    }

}
