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

package cn.aberic.bother.service.impl;

import cn.aberic.bother.contract.exec.PublicContractExec;
import cn.aberic.bother.contract.system.PublicContract;
import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.AccountInfo;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.response.Response;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.ContractService;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.tools.exception.AccountNotFoundException;
import cn.aberic.bother.tools.thread.ThreadTroublePool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 作者：Aberic on 2018/9/8 14:43
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Service("contractService")
public class ContractServiceImpl implements ContractService {

    @Override
    public String invoke(Request request) {
        PublicContract contract = new PublicContract();
        PublicContractExec exec = new PublicContractExec();
        switch (request.getKey()) {
            case "approve":
                request = approve(request, exec);
                break;
        }
        exec.setRequest(request);
        Response response = contract.invoke(exec);
        if (response.isSend()) {
            new ThreadTroublePool().submit(exec::sendTransaction);
        }
        return response.getResultResponse();
    }

    /** 批准 addressSpender 账户从自己的账户转移 count 个 Token。 */
    private Request approve(Request request, PublicContractExec exec) {
        Token token = JSON.parseObject(exec.get(Common.TOKEN_DEFAULT_PUBLIC_HASH), new TypeReference<Token>() {});
        JSONObject json = request.getJsonValue();
        // 待转账金额
        BigDecimal count = json.getBigDecimal("count").setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP);
        Account accountFrom = JSON.parseObject(exec.get(request.getAddress()), new TypeReference<Account>() {});
        Account accountSpender = JSON.parseObject(exec.get(json.getString("addressSpender")), new TypeReference<Account>() {});
        if (null == accountFrom) {
            throw new AccountNotFoundException(request.getAddress());
        }
        if (null == accountSpender) {
            throw new AccountNotFoundException(json.getString("addressSpender"));
        }
        // 获取账户详情对象
        AccountInfo info = JSON.parseObject(KeyExec.obtain().decryptPriStrECDSA(request.getPriECCKey(), accountFrom.getJsonAccountInfoString()),
                new TypeReference<AccountInfo>() {});
        String value = KeyExec.obtain().encryptPriStrRSA(info.getPriRSAKey(), count.toPlainString());
        request.getJsonValue().remove("count");
        request.setPriECCKey(null);
        request.setValue(value);
        return request;
    }

}
