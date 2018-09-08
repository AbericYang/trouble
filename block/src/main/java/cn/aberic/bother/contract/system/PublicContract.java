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
import cn.aberic.bother.contract.exec.service.IPublicContract;
import cn.aberic.bother.contract.exec.service.IPublicContractExec;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.response.IResponse;
import cn.aberic.bother.entity.response.Response;
import cn.aberic.bother.tools.exception.RequestTypeNotFoundException;
import cn.aberic.bother.tools.exception.SearchDataNotFoundException;
import cn.aberic.bother.tools.exception.SearchDataTimeoutException;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统应用智能合约-app support
 * <p>
 * 作者：Aberic on 2018/8/29 20:49
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class PublicContract implements IPublicContract {

    private AccountHelper accountHelper;

    public PublicContract() {
        accountHelper = new AccountHelper();
    }

    @Override
    public Response invoke(IPublicContractExec exec) {
        TokenHelper tokenHelper = new TokenHelper((PublicContractExec) exec);
        Request request = exec.getRequest();
        switch (request.getKey()) {
            case "publish": // 发布新 Token
                return tokenHelper.publishToken(request);
            case "openAccount": // 开户
                return accountHelper.openAccount((PublicContractExec) exec, request);
            case "transfer": // 将自己的 count 个 Token 转给 addressTo 地址
                return tokenHelper.transfer(request);
            case "approve": // 批准 addressSpender 账户从自己的账户转移 count 个 Token。
                return tokenHelper.approve(request);
            case "transferFrom": // 与approve搭配使用，approve批准之后，调用本函数来转移token
                return tokenHelper.transferFrom(request);
        }
//        exec.put(request.getKey(), request.getValue());
        return exec.response(IResponse.ResponseType.REQUEST_TYPE_NOT_FOUND);
    }

    @Override
    public Response query(IPublicContractExec exec) {
        Request request = exec.getRequest();
        TokenHelper tokenHelper = new TokenHelper((PublicContractExec) exec);
        switch (request.getKey()) {
            case "cheque": // 账户开支票
                return accountHelper.cheque((PublicContractExec) exec, request);
            case "allowance": // 返回 addressSpender 还能提取token的个数
                return new TokenHelper((PublicContractExec) exec).allowance(request);
            case "name": // 返回string类型的ERC20 Token 的名字，例如：NoTroubleBother
                return exec.response(tokenHelper.name());
            case "symbol": // 返回string类型的ERC20 Token 的符号，也就是代币的简称，例如：NTB
                return exec.response(tokenHelper.symbol());
            case "decimals": // 支持几位小数点后几位。如果设置为3。也就是支持0.001表示
                return exec.response(tokenHelper.decimals());
            case "totalSupply": // 发行 Token 的总量
                return exec.response(tokenHelper.totalSupply());
            case "balanceOf": // 根据指定地址返回该 Token 的余额
                return exec.response(tokenHelper.balanceOf(request.getValue()));
            case "heightNum": // 查询区块高度
                return exec.response(exec.getHeight());
            case "height": // 根据高度查询区块对象
                try {
                    return exec.response(exec.getBlockByHeight(Integer.valueOf(request.getValue())));
                } catch (SearchDataNotFoundException e) {
                    return exec.response(IResponse.ResponseType.FAIL, "try to search data from file, but not found");
                } catch (SearchDataTimeoutException e) {
                    return exec.response(IResponse.ResponseType.FAIL, "try to search data from file, time out");
                }
            case "hash": // 根据区块hash查询区块对象
                try {
                    return exec.response(exec.getBlockByHash(request.getValue()));
                } catch (SearchDataNotFoundException e) {
                    return exec.response(IResponse.ResponseType.FAIL, "try to search data from file, but not found");
                } catch (SearchDataTimeoutException e) {
                    return exec.response(IResponse.ResponseType.FAIL, "try to search data from file, time out");
                }
            case "txHash": // 根据交易hash查询区块对象
                try {
                    return exec.response(exec.getBlockByTransactionHash(request.getValue()));
                } catch (SearchDataNotFoundException e) {
                    return exec.response(IResponse.ResponseType.FAIL, "try to search data from file, but not found");
                } catch (SearchDataTimeoutException e) {
                    return exec.response(IResponse.ResponseType.FAIL, "try to search data from file, time out");
                }
            case "history": // 根据 key 查询 value 历史记录
                return exec.response(exec.getHistory(request.getValue()));
        }
        throw new RequestTypeNotFoundException();
    }

}
