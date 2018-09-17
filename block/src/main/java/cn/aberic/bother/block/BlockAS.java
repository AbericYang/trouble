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

package cn.aberic.bother.block;

import cn.aberic.bother.block.exec.BlockExec;
import cn.aberic.bother.block.exec.BlockIndexExec;
import cn.aberic.bother.block.exec.BlockTransactionIndexExec;
import cn.aberic.bother.block.exec.ExecFactory;
import cn.aberic.bother.block.exec.service.IExecFactory;

/**
 * 存储及获取区块基类——数据操作层-data manipulation
 *
 * 作者：Aberic on 2018/08/28 11:15
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public class BlockAS {

    /** 智能合约hash值 */
    protected String contractHash;
    private IExecFactory execFactory;
    private BlockExec blockExec;
    private BlockIndexExec blockIndexExec;
    private BlockTransactionIndexExec blockTransactionIndexExec;

    BlockAS(String contractHash) {
        this.contractHash = contractHash;
        execFactory = new ExecFactory();
    }

    BlockExec getBlockExec() {
        if (null == blockExec) {
            blockExec = execFactory.createBlockExec(contractHash);
        }
        return blockExec;
    }

    BlockIndexExec getBlockIndexExec() {
        if (null == blockIndexExec) {
            blockIndexExec = execFactory.createBlockIndexExec(contractHash);
        }
        return blockIndexExec;
    }

    BlockTransactionIndexExec getBlockTransactionIndexExec() {
        if (null == blockTransactionIndexExec) {
            blockTransactionIndexExec = execFactory.createBlockTransactionIndexExec(contractHash);
        }
        return blockTransactionIndexExec;
    }

}
