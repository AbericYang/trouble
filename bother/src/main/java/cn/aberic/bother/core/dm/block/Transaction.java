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

import java.util.List;

/**
 * 事务/交易/业务对象——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/8/20 21:28
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Transaction {

    /** 发起方 */
    private String creator;
    /** 发起方签名 */
    private String sign;
    /** 交易读写集 */
    private List<RWSet> rwSets;
    /** 交易时间戳 */
    private Long timestamp;
    /**
     * 交易hash
     * <p>
     * 为creator、sign、JSON.toJSONString(readSet)、JSON.toJSONString(writeSet)及timestamp拼接后md5
     */
    private String hash;
    /** 交易状态 */
    private TransactionStatus status = TransactionStatus.SUCCESS;
    /** 交易错误信息 */
    private String errorMessage;

}
