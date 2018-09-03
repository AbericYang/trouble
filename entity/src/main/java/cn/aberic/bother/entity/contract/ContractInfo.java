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

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * 智能合约数据key在智能合约数据文件中的基本信息——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/31 14:11
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class ContractInfo {

    /** 智能合约数据key */
    @JSONField(name = "k")
    private String key;
    /** 智能合约数据所在智能合约数据key编号 */
    @JSONField(name = "n")
    private int num;
    /** 智能合约数据所在智能合约数据key中的行号 */
    @JSONField(name = "l")
    private int line;

    public ContractInfo(String key, int num, int line) {
        this.key = key;
        this.num = num;
        this.line = line;
    }
}
