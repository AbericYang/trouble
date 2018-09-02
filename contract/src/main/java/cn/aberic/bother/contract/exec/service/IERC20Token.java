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

package cn.aberic.bother.contract.exec.service;

import java.math.BigDecimal;

/**
 * 作者：Aberic on 2018/9/2 10:31
 * 邮箱：abericyang@gmail.com
 */
public interface IERC20Token {

    /**
     * 返回string类型的ERC20 Token 的名字，例如：NoTroubleBother
     *
     * @return Token 的名字
     */
    String name();

    /**
     * 返回string类型的ERC20 Token 的符号，也就是代币的简称，例如：NTB。
     *
     * @return Token 的符号
     */
    String symbol();

    /**
     * 支持几位小数点后几位。如果设置为3。也就是支持0.001表示。
     *
     * @return 支持小数位数
     */
    int decimals();

    /**
     * 发行 Token 的总量
     *
     * @return 发行 Token 的总量
     */
    int totalSupply();

    /**
     * 根据指定地址返回该 Token 的余额
     *
     * @param address 指定地址
     *
     * @return 余额
     */
    BigDecimal balanceOf(String address);

    /**
     * 将自己的 count 个 Token 转给 addressTo 地址
     *
     * @param addressTo 接收 Token 的地址
     * @param count     转让的 Token 个数
     *
     * @return 成功与否
     */
    boolean transfer(String addressTo, BigDecimal count);

    /**
     * 批准 addressSpender 账户从自己的账户转移 count 个 Token。
     *
     * @param addressSpender 账户地址
     * @param count          Token个数
     *
     * @return 成功与否
     */
    boolean approve(String addressSpender, BigDecimal count);

    /**
     * 与approve搭配使用，approve批准之后，调用本函数来转移token。
     *
     * @param addressFrom 转账来源账户地址
     * @param addressTo   转账去处账户地址
     * @param count       转账个数
     *
     * @return 成功与否
     */
    boolean transferFrom(String addressFrom, String addressTo, BigDecimal count);

    /**
     * 返回 addressSpender 还能提取token的个数。
     * <p>
     * 账户A有1000个 Token，想允许B账户随意调用100个 Token。
     * A账户按照以下形式调用approve函数approve(B,100)。
     * 当B账户想用这100个 Token 中的10个 Token 给C账户时，则调用transferFrom(A, C, 10)。
     * 这时调用allowance(A, B)可以查看B账户还能够调用A账户多少个token。
     *
     * @param addressOwner   账户A
     * @param addressSpender 账户B
     *
     * @return 能提取token的个数
     */
    BigDecimal allowance(String addressOwner, String addressSpender);

}
