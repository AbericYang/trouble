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

import cn.aberic.bother.entity.BeanProtoFormat;
import cn.aberic.bother.entity.proto.block.BlockProto;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.hash.Hashing;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 区块对象——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/8/20 21:21
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
@ToString
public class Block implements BeanProtoFormat {

    /** 区块头部信息 */
    @JSONField(name = "h")
    private BlockHeader header;
    /** 区块数据体 */
    @JSONField(name = "b")
    private BlockBody body;

    public Block(BlockHeader header, BlockBody body) {
        this.header = header;
        this.body = body;
    }

    /** 得到当前区块hash */
    public String calculateHash() {
        return Hashing.sha256().hashString(String.format("%s%s%s",
                header.getPreviousDataHash(),
                Long.toString(header.getTimestamp()),
                body.bodyString()), Charset.forName("UTF-8")).toString();
    }

    /**
     * Block 对象转成 {@link cn.aberic.bother.entity.proto.block.BlockProto.Block} 字节流
     *
     * @return proto 字节流
     */
    @Override
    public byte[] bean2ProtoByteArray() {
        BlockProto.Block.Builder builder = BlockProto.Block.newBuilder();
        String blockJsonFormat = this.toJsonString();
        log.debug("blockJsonFormat = {}", blockJsonFormat);
        try {
            JsonFormat.parser().merge(blockJsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return builder.build().toByteArray();
    }

}
