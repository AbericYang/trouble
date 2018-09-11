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

package cn.aberic.bother.entity.contract;

import cn.aberic.bother.entity.BeanJsonField;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 账户
 * 作者：Aberic on 2018/9/2 16:55
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
public class Account implements BeanJsonField {

    /** 账户唯一地址码 */
    @JSONField(name = "a")
    private String address;
    /** 账户余额 */
    @JSONField(name = "c")
    private BigDecimal count;
    /** 账户信息详情 {@link AccountInfo}，经由ECC加密 */
    @JSONField(name = "i")
    private String jsonAccountInfoString;
    /** 账户 RSA 公钥 */
    @JSONField(name = "r")
    private String pubRSAKey;
    /** 账户 ECC 公钥 */
    @JSONField(name = "e")
    private String pubECCKey;
    /** 账户创建时间戳 */
    @JSONField(name = "t")
    private long timestamp;

}
