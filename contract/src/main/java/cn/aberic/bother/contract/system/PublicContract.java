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
import cn.aberic.bother.contract.exec.service.IPublicContract;
import cn.aberic.bother.contract.exec.service.IPublicContractExec;
import cn.aberic.bother.entity.IResponse;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.tools.exception.ERC20TokenAddressNullException;
import cn.aberic.bother.tools.exception.ERC20TokenECCPrivateKeyNullException;
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

    public PublicContract() {
        tokenHelper = new TokenHelper();
    }

    @Override
    public String invoke(IPublicContractExec exec) {
        Request request = exec.getRequest();
        if (StringUtils.isEmpty(request.getAddress())) { // invoke 请求账户地址不能为空
            throw new ERC20TokenAddressNullException();
        }
        if (StringUtils.isEmpty(request.getPriECCKey())) { // invoke 请求账户 ECC 私钥不能为空
            throw new ERC20TokenECCPrivateKeyNullException();
        }
        ERC20Token erc20Token = new ERC20Token(Common.TOKEN_DEFAULT_SYSTEM_HASH, request.getAddress(), request.getPriECCKey(), exec);
        switch (request.getKey()) {
            // 发布新 Token
            case "publish":
                return tokenHelper.publishToken(erc20Token);
            // 将自己的 count 个 Token 转给 addressTo 地址
            case "transfer":
                return tokenHelper.transfer(erc20Token, exec);
            // 批准 addressSpender 账户从自己的账户转移 count 个 Token。
            case "approve":
                return tokenHelper.approve(erc20Token, exec);
            // 与approve搭配使用，approve批准之后，调用本函数来转移token。
            case "transferFrom":
                return tokenHelper.transferFrom(erc20Token, exec);
        }
        return exec.response(IResponse.Response.REQUEST_TYPE_NOT_FOUND);
    }

    @Override
    public String query(IPublicContractExec exec) {
        ERC20Token erc20Token = new ERC20Token(Common.TOKEN_DEFAULT_SYSTEM_HASH, exec);
        Request request = exec.getRequest();
        if (!StringUtils.isEmpty(request.getAddress())) {
            erc20Token.setAddress(request.getAddress());
        }
        if (!StringUtils.isEmpty(request.getPriECCKey())) {
            erc20Token.setPriECCKey(request.getPriECCKey());
        }
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
        // 无特殊要求，则根据 key 查 value 返回值
        return exec.response(exec.get(request.getKey()));
    }

}
