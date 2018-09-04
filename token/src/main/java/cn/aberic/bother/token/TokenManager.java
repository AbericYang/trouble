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

/**
 * 账户基本操作对象
 *
 * 作者：Aberic on 2018/9/3 20:56
 * 邮箱：abericyang@gmail.com
 */
public class TokenManager {

    private ITokenExec tokenExec;
    private ITokenExec tokenTmpExec;

    public TokenManager() {
        tokenExec = new TokenExec();
        tokenTmpExec = new TokenTmpExec();
    }

    /** 创建或更新账户信息 */
    public Token publish(String accountAddress){
        return tokenTmpExec.publish(accountAddress);
    }

    /** 创建或更新账户信息 */
    public void createOrUpdateTmp(Token token){
        tokenTmpExec.createOrUpdate(token);
    }

    /** 创建或更新账户信息 */
    public void createOrUpdate(Token token){
        token.setAccount(null);
        tokenExec.createOrUpdate(token);
    }

}
