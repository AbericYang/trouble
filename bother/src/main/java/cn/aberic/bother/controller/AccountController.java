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

package cn.aberic.bother.controller;

import cn.aberic.bother.bean.AccountUser;
import cn.aberic.bother.entity.contract.AccountBusiness;
import cn.aberic.bother.entity.contract.AccountBusinessEncrypt;
import cn.aberic.bother.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 作者：Aberic on 2018/9/2 19:10
 * 邮箱：abericyang@gmail.com
 */
@CrossOrigin
@RestController
@RequestMapping("account")
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * 根据用户持有账户信息获取改账户的 RSA 私钥
     *
     * @param user 用户持有账户信息
     *
     * @return RSA 私钥
     */
    @PostMapping(value = "pri")
    public String getRSAPri(@RequestBody AccountUser user) {
        return accountService.getRSAPri(user);
    }

    /**
     * 对即将处理的业务进行账户 RSA 私钥加密
     * <p>
     * 改业务最终将提交到网络，并由账户 RSA 公钥解密后处理
     *
     * @param businessEncrypt 即将处理的业务
     *
     * @return 业务密文
     */
    @PostMapping(value = "encrypt")
    public String encryptBusiness(AccountBusinessEncrypt businessEncrypt) {
        return accountService.encryptBusiness(businessEncrypt);
    }

    /**
     * 根据账户事务对象处理业务
     *
     * @param business 账户事务对象
     *
     * @return 业务结果
     */
    @PostMapping(value = "business")
    public String business(AccountBusiness business) {
        return accountService.business(business);
    }

}
