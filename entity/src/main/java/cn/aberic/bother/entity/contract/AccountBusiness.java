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

package cn.aberic.bother.entity.contract;

import lombok.Getter;
import lombok.Setter;

/**
 * 账户处理事务
 * <p>
 * 作者：Aberic on 2018/09/04 11:39
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class AccountBusiness {

    /** 处理该事务的公网账户地址 */
    private String publicAddress;
    /** 处理该事务的账户地址 */
    private String address;
    /** 即将处理的具体事务，由该账户进行加密 */
    private String encryption;
    /** 加密事务 */
    private Business business;

    /** 加密事务，所操作地址均与加密后字符串一同传入 */
    @Getter
    public enum Business {

        /** 发布新 Token，根据指定地址返回该 Token 的余额 */
        PUBLISH("publish", 0),
        /** 查询，根据指定地址返回该 Token 的余额 */
        QUERY("", 10),
        /** 转账，转给 toAddress 账户 count 个 Token */
        TRANSFER("toAddress,count", 11),
        /** 授权，批准 addressSpender 账户从自己的账户转移 count 个 Token */
        APPROVE("addressSpender,count", 12),
        /** 授权转账，与上述APPROVE类型配合使用，当前address被addressFrom授权转账后，将 addressFrom 账户中 count 个 Token转移给 addressTo */
        TRANSFER_FROM("addressFrom,addressTo,count", 13),
        /** 授权转账余额查询，与上述APPROVE类型配合使用，返回 addressSpender 还能提取 Token 的个数 */
        ALLOWANCE("address", 14);

        /** 事务字符串格式 */
        private String format;
        /** 事务码 */
        private int code;

        /**
         * 当前交易状态
         *
         * @param format 事务字符串格式
         * @param code   事务码
         */
        Business(String format, int code) {
            this.format = format;
            this.code = code;
        }
    }

}
