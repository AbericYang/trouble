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

package cn.aberic.bother.core.dm.exec;

import cn.aberic.bother.core.dm.exec.service.*;

/**
 * 文件本地操作实现创建工厂接口实现——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/28 10:18
 * 邮箱：abericyang@gmail.com
 */
public class ExecFactory implements IExecFactory {

    @Override
    public BlockExec createBlockExec(String contractHash) {
        return new BlockExec(contractHash);
    }

    @Override
    public BlockIndexExec createBlockIndexExec(String contractHash) {
        return new BlockIndexExec(contractHash);
    }

    @Override
    public BlockTransactionIndexExec createBlockTransactionIndexExec(String contractHash) {
        return new BlockTransactionIndexExec(contractHash);
    }
}
