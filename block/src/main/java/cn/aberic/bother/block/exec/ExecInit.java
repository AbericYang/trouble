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

package cn.aberic.bother.block.exec;

import cn.aberic.bother.block.exec.service.IExecFactory;
import cn.aberic.bother.block.exec.service.IExecInit;
import cn.aberic.bother.storage.IInit;
import cn.aberic.bother.storage.Init;

/**
 * 索引文件初始化接口实现基类——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/28 12:09
 * 邮箱：abericyang@gmail.com
 */
public class ExecInit extends Init implements IInit, IExecInit {

    private IExecFactory execFactory;
    private BlockExec blockExec;

    /**
     * 根据智能合约hash值操作区块文件；
     * 在智能合约被安装的时候就根据合约内容计算该合约hash；
     * 并以此hash匹配所有安装该合约的节点且同步数据
     *
     * @param contractHash 智能合约hash值
     */
    ExecInit(String contractHash) {
        super(contractHash);
        execFactory = new ExecFactory();
    }

    @Override
    public BlockExec getBlockExec() {
        if (null == blockExec) {
            blockExec = execFactory.createBlockExec(getStorageHash());
        }
        return blockExec;
    }
}
