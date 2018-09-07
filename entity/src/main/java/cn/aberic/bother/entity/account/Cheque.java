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

package cn.aberic.bother.entity.account;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 支票
 * <p>
 * 支票信息初始不会上链，一旦经过有且仅有一次交易之后就会上链，以防被多次重复使用
 * <p>
 * 当一个区块打包时，如果出现多个相同支票开销，仅第一笔生效
 * <p>
 * 作者：Aberic on 2018/09/07 13:49
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
public class Cheque {

    /** 支票金额 */
    private BigDecimal count;
    /** 支票属性：0、代付；1、赠予 */
    private int type;
    /** 支票开票时间戳 */
    private long startTimestamp;
    /** 支票有效截至时间戳 */
    private long endTimestamp;

}
