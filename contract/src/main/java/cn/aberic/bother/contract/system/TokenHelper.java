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
import cn.aberic.bother.contract.exec.service.IPublicContractExec;
import cn.aberic.bother.entity.response.IResponse;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.response.Response;
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
    Response publishToken(ERC20Token erc20Token) {
        IPublicContractExec exec = erc20Token.getPublicContractExec();
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

        // 发布者公网账户余额
        BigDecimal pubCount = pubAccount.getCount();

        // 检查该公网账户是否有足够余额支付
        if (consumption.compareTo(pubCount) > 0) {
            // 如果余额不足，则返回对应信息
            return exec.response(IResponse.ResponseType.ACCOUNT_LACK_OF_BALANCE);
        }

        // 扣减存储费
        pubCount = pubCount.subtract(consumption);
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
        exec.put(erc20Token.getAddress(), JSON.toJSONString(pubAccount));

        return exec.response(IResponse.ResponseType.SUCCESS);
    }

    /**
     * 将自己的 count 个 Token 转给 addressTo 地址
     *
     * @param erc20Token ERC20 接口实现
     * @return 成功与否
     */
    Response transfer(ERC20Token erc20Token) {
        IPublicContractExec exec = erc20Token.getPublicContractExec();
        JSONObject json = exec.getRequest().getJsonValue();
        if (erc20Token.transfer(json.getString("addressTo"), json.getBigDecimal("count"))) {
            return exec.response(IResponse.ResponseType.SUCCESS);
        }
        return exec.response(IResponse.ResponseType.ACCOUNT_LACK_OF_BALANCE);
    }

    /**
     * 批准 addressSpender 账户从自己的账户转移 count 个 Token。
     *
     * @param erc20Token ERC20 接口实现
     * @return 成功与否
     */
    Response approve(ERC20Token erc20Token) {
        IPublicContractExec exec = erc20Token.getPublicContractExec();
        JSONObject json = exec.getRequest().getJsonValue();
        if (erc20Token.approve(json.getString("addressSpender"), json.getBigDecimal("count"))) {
            return exec.response(IResponse.ResponseType.SUCCESS);
        }
        return exec.response(IResponse.ResponseType.FAIL);
    }

    /**
     * 与approve搭配使用，approve批准之后，调用本函数来转移token。
     *
     * @param erc20Token ERC20 接口实现
     * @return 成功与否
     */
    Response transferFrom(ERC20Token erc20Token) {
        IPublicContractExec exec = erc20Token.getPublicContractExec();
        JSONObject json = exec.getRequest().getJsonValue();
        if (erc20Token.transferFrom(json.getString("addressFrom"), json.getString("addressTo"), json.getBigDecimal("count"))) {
            return exec.response(IResponse.ResponseType.SUCCESS);
        }
        return exec.response(IResponse.ResponseType.FAIL);
    }

    /**
     * 返回 addressSpender 还能提取token的个数。
     * <p>
     * 账户A有1000个 Token，想允许B账户随意调用100个 Token。
     * A账户按照以下形式调用approve函数approve(B,100)。
     * 当B账户想用这100个 Token 中的10个 Token 给C账户时，则调用transferFrom(A, C, 10)。
     * 这时调用allowance(A, B)可以查看B账户还能够调用A账户多少个token。
     *
     * @param erc20Token ERC20 接口实现
     * @return 能提取token的个数
     */
    Response allowance(ERC20Token erc20Token) {
        IPublicContractExec exec = erc20Token.getPublicContractExec();
        JSONObject json = exec.getRequest().getJsonValue();
        return exec.response(erc20Token.allowance(json.getString("addressOwner"), json.getString("addressSpender")).toPlainString());
    }

}
