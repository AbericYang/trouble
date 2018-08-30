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

import cn.aberic.bother.encryption.MD5;
import cn.aberic.bother.entity.block.Block;

/**
 * 获取区块——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/24 11:27
 * 邮箱：abericyang@gmail.com
 */
public class BlockAcquire extends BlockAS {

    public BlockAcquire(String contractHash) {
        super(contractHash);
    }

    /**
     * 获取本地区块文件个数
     *
     * @return 区块文件个数
     */
    public int getFileCount() {
        return getBlockExec().getFileCount();
    }

    /**
     * 获取指定合约下账本高度
     *
     * @return 指定合约账本高度
     */
    public int getHeight() {
        return getBlockExec().getHeight();
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param height 区块高度
     * @return 区块对象
     */
    public Block getBlockByHeight(int height) {
        return getBlockIndexExec().getByHeight(height);
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param currentDataHash 当前区块hash
     * @return 区块对象
     */
    public Block getBlockByHash(String currentDataHash) {
        return getBlockIndexExec().getByCurrentDataHash(MD5.md516(currentDataHash));
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param transactionHash 交易hash
     * @return 区块对象
     */
    public Block getBlockByTransactionHash(String transactionHash) {
        return getBlockTransactionIndexExec().getByTransactionHash(MD5.md516(transactionHash));
    }
}
