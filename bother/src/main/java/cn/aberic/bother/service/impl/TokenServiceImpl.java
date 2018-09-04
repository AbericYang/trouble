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

import cn.aberic.bother.entity.contract.AccountBusiness;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.TokenService;
import cn.aberic.bother.token.TokenManager;
import cn.aberic.bother.tools.exception.AccountBusinessTypeException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Token 操作接口实现
 * <p>
 * 作者：Aberic on 2018/9/3 22:12
 * 邮箱：abericyang@gmail.com
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Override
    public void saveTmp(Token token) {
        token.setTimestamp(new Date().getTime());
        token.checkParams();
        token.build();
        TokenManager manager = new TokenManager();
        manager.createOrUpdateTmp(token);
    }

    @Override
    public void publish(AccountBusiness accountBusiness, String accountAddress) {
        // TODO: 2018/9/3 存储费收取待实现
        // 获取该账户的加密事务
        AccountBusiness.Business business = accountBusiness.getBusiness();
        // 判断该事务是否用于发布 Token
        if (business.getCode() != AccountBusiness.Business.PUBLISH.getCode()) {
            throw new AccountBusinessTypeException();
        }
        // 根据当前待发布 Token 存储空间计算存储费
        // 根据account查询余额判断该账户是否有足够费用执行该笔操作
        // 如果没有，返回余额不足异常
        // 如果有，则继续下面步骤

        TokenManager manager = new TokenManager();
        manager.publish(accountAddress);
    }
}
