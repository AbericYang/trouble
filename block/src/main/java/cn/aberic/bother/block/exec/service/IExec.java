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

package cn.aberic.bother.block.exec.service;

import cn.aberic.bother.entity.block.BlockInfo;
import cn.aberic.bother.storage.IFile;

/**
 * 文件本地读写接口——数据操作层-data manipulation
 * <p>
 * 该接口服务提供了区块文件{@link cn.aberic.bother.entity.block.Block}
 * 及区块索引文件{@link BlockInfo}对象的
 * 基本操作方案
 * <p>
 * 作者：Aberic on 2018/08/27 12:13
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public interface IExec<T> extends IFile<T> {

    /**
     * 根据{@link T}对象创建或更新后存储{@link T}文件
     *
     * @param t {@link T}对象
     *
     * @return 区块在区块文件中的基本信息对象
     */
    BlockInfo createOrUpdate(T t);

}
