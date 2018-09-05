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

package cn.aberic.bother.token;

import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.token.exec.TokenExec;
import cn.aberic.bother.token.exec.TokenTmpExec;
import cn.aberic.bother.token.exec.service.ITokenExec;
import cn.aberic.bother.token.exec.service.ITokenTmpExec;

/**
 * 账户基本操作对象
 * <p>
 * 作者：Aberic on 2018/9/3 20:56
 * 邮箱：abericyang@gmail.com
 */
public class TokenManager {

    private ITokenExec tokenExec;
    private ITokenTmpExec tokenTmpExec;

    public TokenManager() {
        tokenExec = new TokenExec();
        tokenTmpExec = new TokenTmpExec();
    }

    /**
     * 创建或更新 Token 信息
     *
     * @param tokenStr {@link Token} 字符串
     */
    public void createOrUpdate(String tokenStr) {
        tokenExec.createOrUpdate(tokenStr);
    }

    /**
     * 根据 Token hash 获取 Token 对象
     *
     * @param tokenHash Token hash
     *
     * @return Token 对象
     */
    public Token getByHash(String tokenHash) {
        return tokenExec.getByHash(tokenHash);
    }

    /** 创建或更新账户信息 */
    public void createOrUpdateTmp(String tokenStr) {
        tokenTmpExec.createOrUpdate(tokenStr);
    }

    /**
     * 通过账户地址得到 Token 信息
     * <p>
     * 注：此方法仅限未发布 Token 使用
     *
     * @param accountAddress 账户地址
     *
     * @return Token 信息
     */
    public Token getUnPublish(String accountAddress) { return tokenTmpExec.getUnPublish(accountAddress); }

}
