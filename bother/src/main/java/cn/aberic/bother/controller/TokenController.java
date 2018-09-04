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

package cn.aberic.bother.controller;

import cn.aberic.bother.bean.AccountUser;
import cn.aberic.bother.entity.contract.AccountBusiness;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.TokenService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 作者：Aberic on 2018/09/03 12:05
 * 邮箱：abericyang@gmail.com
 */
@CrossOrigin
@RestController
@RequestMapping("token")
public class TokenController {

    @Resource
    private TokenService tokenService;

    /**
     * 临时存储待发布 Token
     *
     * @param token 待发布 Token
     */
    @PostMapping(value = "create")
    public AccountUser create(@RequestBody Token token) {
        return tokenService.create(token);
    }

    /**
     * 发布 Token
     * <p>
     * 该操作会将此 Token 发布至公链，全网账本同步，需要附带可用账户且余额充足
     * <p>
     * 发布信息至公网账本将按字节收取存储手续费
     *
     * @param accountBusiness 附带可用账户处理事务对象
     */
    @PostMapping(value = "publish")
    public Token publish(@RequestBody AccountBusiness accountBusiness) {
        return tokenService.publish(accountBusiness);
    }

    @GetMapping(value = "test")
    public AccountBusiness test() {
        AccountBusiness accountBusiness = new AccountBusiness();
        accountBusiness.setAddress("123");
        accountBusiness.setEncryption("123");
        accountBusiness.setBusiness(AccountBusiness.Business.ALLOWANCE);
        return accountBusiness;
    }

}
