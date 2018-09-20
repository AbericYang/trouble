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

import cn.aberic.bother.entity.account.AccountUser;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.service.BusinessService;
import cn.aberic.bother.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 作者：Aberic on 2018/09/05 10:02
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@CrossOrigin
@RestController
@RequestMapping("business")
public class BusinessController {

    @Resource
    private BusinessService businessService;
    @Resource
    private TransactionService transactionService;

    /**
     * 临时存储待发布 Token
     *
     * @param token 待发布 Token
     */
    @PostMapping(value = "create")
    public AccountUser create(@RequestBody Token token) {
        return businessService.create(token);
    }

    /**
     * 根据用户持有账户信息获取改账户的 RSA 私钥
     *
     * @param request 智能合约请求对象
     * @return RSA 私钥
     */
    @PostMapping(value = "publish")
    public String publish(@RequestBody Request request) {
        return businessService.publish(request, transactionService);
    }

}
