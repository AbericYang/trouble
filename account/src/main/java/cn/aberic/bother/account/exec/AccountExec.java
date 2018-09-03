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

package cn.aberic.bother.account.exec;

import cn.aberic.bother.account.exec.service.IAccountExec;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.storage.FileComponent;
import cn.aberic.bother.storage.Init;
import org.apache.commons.lang3.StringUtils;

/**
 * 账户文件本地读写接口实现
 *
 * 作者：Aberic on 2018/9/3 19:37
 * 邮箱：abericyang@gmail.com
 */
public class AccountExec extends Init implements IAccountExec {

    /**
     * 根据token hash值操作账户文件；
     *
     * @param tokenHash token hash值
     */
    public AccountExec(String tokenHash) {
        super(tokenHash);
    }

    @Override
    public FileComponent getFileStatus() {
        if (StringUtils.equals(getContractHash(), Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH)) {
            return FileComponent.getAccountFileComponentDefault();
        }
        return FileComponent.getAccountFileComponent(getContractHash());
    }

}
