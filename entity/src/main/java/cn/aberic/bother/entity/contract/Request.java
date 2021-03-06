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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 智能合约请求对象
 * <p>
 * 作者：Aberic on 2018/9/2 19:57
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Request {

    /** 智能合约key */
    @JSONField(name = "k")
    private String key;
    /** 智能合约value */
    @JSONField(name = "v")
    private String value;
    /** 智能合约 json value */
    @JSONField(name = "j")
    private JSONObject jsonValue;
    /** RSA 私钥加密事务，请求不加密，处理时加密 */
    private String encryption;

    /** Token hash */
    @JSONField(name = "t")
    private String tokenHash;
    /** 处理该事务的账户地址 */
    @JSONField(name = "a")
    private String address;

    /** 处理该事务的账户 ECC 私钥 */
    @JSONField(serialize = false)
    private String priECCKey;

}
