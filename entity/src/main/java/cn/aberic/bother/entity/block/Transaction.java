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

package cn.aberic.bother.entity.block;

import cn.aberic.bother.encryption.MD5;
import cn.aberic.bother.entity.enums.TransactionStatus;
import cn.aberic.bother.tools.DateTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 事务/交易/业务对象——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/8/20 21:28
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
public class Transaction {

    /** 发起方 */
    @JSONField(name="c")
    private String creator;
    /** 发起方签名 */
    @JSONField(name="s")
    private String sign;
    /** 交易读写集 */
    @JSONField(name="rw")
    private List<RWSet> rwSets;
    /** 交易时间戳 */
    @JSONField(name="t")
    private Long timestamp;
    /**
     * 交易hash
     * <p>
     * 为creator、sign、JSON.toJSONString(readSet)、JSON.toJSONString(writeSet)及timestamp拼接后md5
     */
    @JSONField(name="h")
    private String hash;
    /** 交易状态 */
    @JSONField(name="ts")
    private int transactionStatusCode = TransactionStatus.SUCCESS.getCode();
    /** 交易错误信息 */
    @JSONField(name="e")
    private String errorMessage;
    /**交易索引hash*/
    @JSONField(serialize=false)
    private String dataStorageHash; // 序列化时不写入
    /**交易时间戳转字符串——yyyy/MM/dd HH:mm:ss*/
    @JSONField(serialize=false)
    private String time; // 序列化时不写入

    public Transaction build() {
        hash = Hashing.sha256().hashString(String.format("%s%s%s%s",
                creator, sign, JSON.toJSONString(rwSets), timestamp), Charset.forName("UTF-8")).toString();
        dataStorageHash = MD5.md516(hash);
        return this;
    }

    public String getTime() {
        return DateTool.timestampToString(timestamp, "yyyy/MM/dd HH:mm:ss");
    }
}
