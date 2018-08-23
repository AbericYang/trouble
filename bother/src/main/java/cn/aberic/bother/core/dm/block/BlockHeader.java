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

package cn.aberic.bother.core.dm.block;

import lombok.Getter;
import lombok.Setter;

/**
 * 区块头部信息——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/8/23 21:43
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class BlockHeader {

    /** 当前区块hash */
    private String currentDataHash;
    /** 上一区块hash */
    private String previousDataHash;
    /** 是否由第一顺位节点出块 */
    private boolean smoothlyOut;
    /** 参与本次区块打包的投票节点个数 */
    private int consentNodeCount;
    /** 当前区块生成时间戳 */
    private long timestamp;

}
