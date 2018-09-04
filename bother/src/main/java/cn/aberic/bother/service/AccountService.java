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

package cn.aberic.bother.service;

import cn.aberic.bother.bean.AccountUser;
import cn.aberic.bother.entity.contract.AccountBusiness;
import cn.aberic.bother.entity.contract.AccountBusinessEncrypt;
import cn.aberic.bother.entity.token.Token;

/**
 * 账户操作接口
 * <p>
 * 作者：Aberic on 2018/9/3 22:28
 * 邮箱：abericyang@gmail.com
 */
public interface AccountService {

    /**
     * 根据用户持有账户信息获取改账户的 RSA 私钥
     *
     * @param user 用户持有账户信息
     *
     * @return RSA 私钥
     */
    String getRSAPri(AccountUser user);

    /**
     * 对即将处理的业务进行账户 RSA 私钥加密
     * <p>
     * 改业务最终将提交到网络，并由账户 RSA 公钥解密后处理
     *
     * @param businessEncrypt 即将处理的业务
     *
     * @return 业务密文
     */
    String encryptBusiness(AccountBusinessEncrypt businessEncrypt);

    /**
     * 根据账户事务对象处理业务
     *
     * @param business 账户事务对象
     *
     * @return 业务结果
     */
    String business(AccountBusiness business);

    /**
     * 存已发布 Token 的账户
     *
     * @param token 待发布 Token
     *
     * @return 账户对象及其私钥
     */
    String save(Token token);

}
