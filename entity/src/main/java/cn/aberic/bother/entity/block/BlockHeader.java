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

import cn.aberic.bother.tools.DateTool;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 区块头部信息——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/8/23 21:43
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
public class BlockHeader {

    /** 区块高度 */
    @JSONField(name = "h")
    private int height;
    /** 当前区块hash */
    @JSONField(name = "c")
    private String currentDataHash;
    /** 上一区块hash */
    @JSONField(name = "p")
    private String previousDataHash;
    /** 当前区块生成时间戳 */
    @JSONField(name = "t")
    private long timestamp;
    /** 交易时间戳转字符串——yyyy/MM/dd HH:mm:ss */
    @JSONField(serialize = false)
    private String time; // 序列化时不写入

    private BlockHeader() {}

    public static BlockHeader newInstance() {
        return new BlockHeader();
    }

    public String getTime() {
        return DateTool.timestampToString(timestamp, "yyyy/MM/dd HH:mm:ss");
    }

    /**
     * 创建新区快头部构造
     *
     * @return 区快头部构造
     */
    public BlockHeader create() {
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    /**
     * 同步新区快头部构造
     *
     * @param height           区块高度
     * @param currentDataHash  当前区块hash
     * @param previousDataHash 上一区块hash
     * @param timestamp        当前区块生成时间戳
     */
    public BlockHeader sync(int height, String currentDataHash, String previousDataHash, long timestamp) {
        this.height = height;
        this.currentDataHash = currentDataHash;
        this.previousDataHash = previousDataHash;
        this.timestamp = timestamp;
        return this;
    }

}
