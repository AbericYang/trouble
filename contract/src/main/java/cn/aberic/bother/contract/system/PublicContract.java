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
import cn.aberic.bother.contract.exec.service.IPublicContract;
import cn.aberic.bother.contract.exec.service.IPublicContractExec;
import cn.aberic.bother.entity.response.IResponse;
import cn.aberic.bother.entity.contract.AccountBusiness;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.response.Response;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.tools.exception.ERC20TokenAddressNullException;
import cn.aberic.bother.tools.exception.RequestTypeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 系统应用智能合约-app support
 * <p>
 * 作者：Aberic on 2018/8/29 20:49
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class PublicContract implements IPublicContract {

    private TokenHelper tokenHelper;
    private AccountHelper accountHelper;

    public PublicContract() {
        tokenHelper = new TokenHelper();
        accountHelper = new AccountHelper();
    }

    @Override
    public Response invoke(IPublicContractExec exec) {
        Request request = exec.getRequest();
        AccountBusiness business = request.getBusiness();
        if (StringUtils.isEmpty(business.getAddress())) { // invoke 请求账户地址不能为空
            throw new ERC20TokenAddressNullException();
        }
        ERC20Token erc20Token = new ERC20Token(Common.TOKEN_DEFAULT_PUBLIC_HASH, business.getAddress(), business.getPriECCKey(), exec);
        switch (business.getIntent()) {
            case PUBLISH:
                return tokenHelper.publishToken(erc20Token);
            case OPEN_ACCOUNT:
                return accountHelper.openAccount((PublicContractExec) exec, erc20Token, business);
            case TRANSFER:
                return tokenHelper.transfer(erc20Token);
            case APPROVE:
                return tokenHelper.approve(erc20Token);
            case TRANSFER_FROM:
                return tokenHelper.transferFrom(erc20Token);
        }
//         exec.put(request.getKey(), request.getValue());
        return exec.response(IResponse.ResponseType.REQUEST_TYPE_NOT_FOUND);
    }

    @Override
    public Response query(IPublicContractExec exec) {
        ERC20Token erc20Token = new ERC20Token(Common.TOKEN_DEFAULT_PUBLIC_HASH, exec);
        Request request = exec.getRequest();
        AccountBusiness business = request.getBusiness();
        if (null != business && !StringUtils.isEmpty(business.getAddress())) {
            erc20Token.setAddress(business.getAddress());
        }
        if (null != business && !StringUtils.isEmpty(business.getPriECCKey())) {
            erc20Token.setPriECCKey(business.getPriECCKey());
        }
        if (null != business && null != business.getIntent()) {
            return intentExec(exec, business);
        }
        return keyExec(exec, request, erc20Token);
    }

    /**
     * 处理意图查询事务
     *
     * @param exec     系统级智能合约操作接口
     * @param business 账户处理事务
     * @return 查询结果
     */
    private Response intentExec(IPublicContractExec exec, AccountBusiness business) {
        switch (business.getIntent()) {
            case CHEQUE:
                try {
                    return accountHelper.cheque(exec, business);
                } catch (Exception e) {
                    return exec.response(IResponse.ResponseType.FAIL, e.getMessage());
                }
            case ALLOWANCE:
        }
        return null;
    }

    /**
     * 处理键查询事务
     *
     * @param exec       系统级智能合约操作接口
     * @param request    智能合约请求对象
     * @param erc20Token ERC20 接口实现
     * @return 查询结果
     */
    private Response keyExec(IPublicContractExec exec, Request request, ERC20Token erc20Token) {
        switch (request.getKey()) {
            // 返回string类型的ERC20 Token 的名字，例如：NoTroubleBother
            case "name":
                return exec.response(erc20Token.name());
            // 返回string类型的ERC20 Token 的符号，也就是代币的简称，例如：NTB。
            case "symbol":
                return exec.response(erc20Token.symbol());
            // 支持几位小数点后几位。如果设置为3。也就是支持0.001表示。
            case "decimals":
                return exec.response(erc20Token.decimals());
            // 发行 Token 的总量
            case "totalSupply":
                return exec.response(erc20Token.totalSupply());
            // 根据指定地址返回该 Token 的余额
            case "balanceOf":
                return exec.response(erc20Token.balanceOf(request.getValue()));
            // 查询区块高度
            case "heightNum":
                return exec.response(exec.getHeight());
            // 根据高度查询区块对象
            case "height":
                return exec.response(exec.getBlockByHeight(Integer.valueOf(request.getValue())));
            // 根据区块hash查询区块对象
            case "hash":
                return exec.response(exec.getBlockByHash(request.getValue()));
            // 根据交易hash查询区块对象
            case "txHash":
                return exec.response(exec.getBlockByTransactionHash(request.getValue()));
            // 根据 key 查询 value 历史记录
            case "history":
                return exec.response(exec.getHistory(request.getValue()));
        }
        throw new RequestTypeNotFoundException();
    }

}
