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
import cn.aberic.bother.entity.proto.block.BlockOutProto;
import com.google.gson.Gson;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 出块辅助对象
 * <p>
 * 作者：Aberic on 2018/09/20 16:57
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
@ToString
public class BlockOut implements BeanProtoFormat {

    /** 原始区块对象 */
    private Block block;
    /** 区块在区块文件中的基本信息 */
    private BlockInfo blockInfo;

    public BlockOut() {
    }

    public BlockOut(Block block, BlockInfo blockInfo) {
        this.block = block;
        this.blockInfo = blockInfo;
    }

    /**
     * 重置交易集合
     *
     * @param transactions 新交易集合
     */
    public void resetTransactions(List<Transaction> transactions) {
        block.getBody().setTransactions(transactions);
    }

    @Override
    public byte[] bean2ProtoByteArray() {
        BlockOutProto.BlockOut.Builder builder = BlockOutProto.BlockOut.newBuilder();
        String jsonFormat = this.toJsonString();
        log.debug("jsonFormat = {}", jsonFormat);
        try {
            JsonFormat.parser().merge(jsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return builder.build().toByteArray();
    }

    @Override
    public <M extends GeneratedMessageV3> BlockOut proto2Bean(M m) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(m);
        return new Gson().fromJson(jsonObject, BlockOut.class);
    }

    @Override
    public BlockOut protoByteArray2Bean(byte[] bytes) throws InvalidProtocolBufferException {
        return proto2Bean(BlockOutProto.BlockOut.parseFrom(bytes));
    }

}
