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
import cn.aberic.bother.contract.exec.service.ISystemContract;
import cn.aberic.bother.contract.exec.service.ISystemContractExec;
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
public class SystemContract implements ISystemContract {

    private TokenHelper tokenHelper;

    public SystemContract() {
        tokenHelper = new TokenHelper();
    }

    @Override
    public String invoke(ISystemContractExec exec) {
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
        }
        // 无特殊要求，则直接将 k/v 数据上链
        exec.put(request.getKey(), request.getValue());
        return exec.response(IResponse.Response.SUCCESS);
    }

    @Override
    public String query(ISystemContractExec exec) {
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
            // 发行 Token 的总量
            case "balanceOf":
                return exec.response(erc20Token.balanceOf(request.getValue()));
            // 根据高度查询区块对象
            case "height":
                return exec.response(exec.getHeight());
            // 根据高度查询区块对象
            case "byHeight":
                return exec.response(exec.getBlockByHeight(Integer.valueOf(request.getValue())));
            // 根据区块hash查询区块对象
            case "hash":
                return exec.response(exec.getBlockByHash(request.getValue()));
            // 根据交易hash查询区块对象
            case "txHash":
                return exec.response(exec.getBlockByTransactionHash(request.getValue()));
            case "history":
                return exec.response(exec.getHistory(request.getValue()));
        }
        // 无特殊要求，则根据 key 查 value 返回值
        return exec.response(exec.get(request.getKey()));
    }

}
