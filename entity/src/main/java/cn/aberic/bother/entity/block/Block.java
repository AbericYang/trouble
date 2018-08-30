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

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

import java.nio.charset.Charset;

/**
 * 区块对象——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/8/20 21:21
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
public class Block {

    /** 区块头部信息 */
    @JSONField(name="h")
    private BlockHeader header;
    /** 区块数据体 */
    @JSONField(name="b")
    private BlockBody body;

    public Block(BlockHeader header, BlockBody body) {
        this.header = header;
        this.body = body;
    }

    /** 得到当前区块hash */
    public String calculateHash() {
        return Hashing.sha256().hashString(String.format("%s%s%s%s",
                header.getPreviousDataHash(),
                header.getConsentNodeCount(),
                Long.toString(header.getTimestamp()),
                body.bodyString()), Charset.forName("UTF-8")).toString();
    }

    public String toJsonString() {
        return new JSONObject(this).toString();
    }

}
