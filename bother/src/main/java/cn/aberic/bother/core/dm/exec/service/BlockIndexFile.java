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

package cn.aberic.bother.core.dm.exec.service;

import cn.aberic.bother.core.dm.block.BlockInfo;

/**
 * 区块索引文件本地读写——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/27 16:54
 * 邮箱：abericyang@gmail.com
 */
public interface BlockIndexFile extends FileService<BlockInfo> {

    /**
     * 创建并存储区块文件，如已存在且大小超过24M，则覆盖，否则下一行追加更新
     *
     * @param  blockInfo 区块索引对象
     */
    @Override
    default BlockInfo createOrUpdate(BlockInfo blockInfo) {
        return null;
    }

}
