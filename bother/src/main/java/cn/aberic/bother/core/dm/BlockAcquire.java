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

package cn.aberic.bother.core.dm;

import cn.aberic.bother.common.Common;
import cn.aberic.bother.core.dm.block.Block;
import cn.aberic.bother.core.dm.exec.ExecObtain;
import cn.aberic.bother.core.dm.exec.BlockExec;

/**
 * 获取区块——数据操作层-data manipulation
 *
 * 作者：Aberic on 2018/08/24 11:27
 * 邮箱：abericyang@gmail.com
 */
public class BlockAcquire {

    /**
     * 获取本地区块文件个数
     *
     * @return 区块文件个数
     */
    public int getBlockFileCount() {
        BlockExec blockExec = ExecObtain.getBlockExec(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        return blockExec.getFileCount();
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param height 区块高度
     *
     * @return 区块对象
     */
    public Block getBlockByHeight(int height) {
        BlockExec blockExec = ExecObtain.getBlockExec(Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        return blockExec.getBlockByHeight(height);
    }

}
