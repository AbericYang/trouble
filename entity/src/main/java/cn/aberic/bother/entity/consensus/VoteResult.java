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

package cn.aberic.bother.entity.consensus;

import cn.aberic.bother.entity.BeanProtoFormat;
import cn.aberic.bother.entity.enums.JoinLevel;
import cn.aberic.bother.entity.proto.consensus.VoteResultProto;
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
 * 选举结果
 * <p>
 * 作者：Aberic on 2018/9/15 20:55
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
@ToString
public class VoteResult implements BeanProtoFormat {

    /** 选举结果所在级别/组 */
    private JoinLevel level;
    /** 选举结果地址 */
    private List<String> addresses;

    /**
     * VoteResult 对象转成 {@link cn.aberic.bother.entity.proto.consensus.VoteResultProto.VoteResult} 字节流
     *
     * @return proto 字节流
     */
    @Override
    public byte[] bean2ProtoByteArray() {
        VoteResultProto.VoteResult.Builder builder = VoteResultProto.VoteResult.newBuilder();
        String voteResultJsonFormat = this.toJsonString();
        log.debug("voteResultJsonFormat = {}", voteResultJsonFormat);
        try {
            JsonFormat.parser().merge(voteResultJsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return builder.build().toByteArray();
    }

    @Override
    public <M extends GeneratedMessageV3> VoteResult proto2Bean(M m) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(m);
        return new Gson().fromJson(jsonObject, VoteResult.class);
    }

    @Override
    public VoteResult protoByteArray2Bean(byte[] bytes) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(VoteResultProto.VoteResult.parseFrom(bytes));
        return new Gson().fromJson(jsonObject, VoteResult.class);
    }

}
