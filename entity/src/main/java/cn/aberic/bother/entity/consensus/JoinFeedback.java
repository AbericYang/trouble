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

package cn.aberic.bother.entity.consensus;

import cn.aberic.bother.entity.BeanProtoFormat;
import cn.aberic.bother.entity.enums.JoinLevel;
import cn.aberic.bother.entity.proto.consensus.JoinFeedbackProto;
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
 * 告知新的接入节点反馈协议对象
 * 可参考{@link cn.aberic.bother.entity.enums.ProtocolStatus}JOIN_FEEDBACK协议
 * <p>
 * 作者：Aberic on 2018/09/14 10:11
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
@ToString
public class JoinFeedback implements BeanProtoFormat {

    /** 请求加入节点当前请求级别 */
    private JoinLevel level;
    /** 返回上一层Leader地址，如楼、社区、县城、市、省以及国等 */
    private String address;
    /**
     * 返回上一层Leader当前所在域的下级Leader集合
     * 如 {@link #address} 返回的是社区，则本集合为该社区下的每栋楼的Leader集合
     */
    private List<String> addresses;

    /**
     * JoinFeedback 对象转成 {@link cn.aberic.bother.entity.proto.consensus.JoinFeedbackProto.JoinFeedback} 字节流
     *
     * @return proto 字节流
     */
    @Override
    public byte[] bean2ProtoByteArray() {
        JoinFeedbackProto.JoinFeedback.Builder builder = JoinFeedbackProto.JoinFeedback.newBuilder();
        String joinJsonFormat = this.toJsonString();
        log.debug("joinJsonFormat = {}", joinJsonFormat);
        try {
            JsonFormat.parser().merge(joinJsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return builder.build().toByteArray();
    }

    @Override
    public <M extends GeneratedMessageV3> JoinFeedback proto2Bean(M m) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(m);
        return new Gson().fromJson(jsonObject, JoinFeedback.class);
    }

    @Override
    public JoinFeedback protoByteArray2Bean(byte[] bytes) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(JoinFeedbackProto.JoinFeedback.parseFrom(bytes));
        return new Gson().fromJson(jsonObject, JoinFeedback.class);
    }

}
