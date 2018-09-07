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

package cn.aberic.bother.contract.system;

import cn.aberic.bother.contract.exec.service.IPublicContractExec;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.storage.Common;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.math.BigDecimal;

/**
 * 作者：Aberic on 2018/9/7 20:32
 * 邮箱：abericyang@gmail.com
 */
public interface IHelper {

    default void cost(IPublicContractExec exec, BigDecimal count, Token token) {
        Account account = JSON.parseObject(exec.get(Common.TOKEN_DEFAULT_SECOND_HASH), new TypeReference<Account>() {});
        account.setCount(account.getCount().add(count).setScale(token.getDecimals(), BigDecimal.ROUND_HALF_UP));
        exec.put(account.getAddress(), JSON.toJSONString(account));
    }

}
