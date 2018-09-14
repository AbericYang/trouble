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
import cn.aberic.bother.entity.proto.consensus.JoinNodeProto;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 加入节点对象
 * <p>
 * 即推送或请求至当前组内告知这个待加入的节点和加入级别
 * 如果是楼内加入，则当前楼加入该节点
 * 如果是社区内家去，则当前社区小组加入该节点
 * <p>
 * 作者：Aberic on 2018/09/14 14:26
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
@ToString
public class JoinNode implements BeanProtoFormat {

    /** 节点地址 */
    private String address;
    /** 加入节点所在级别 */
    private JoinLevel level;

    public JoinNode(String address, JoinLevel level) {
        this.address = address;
        this.level = level;
    }

    /**
     * JoinNode 对象转成 {@link cn.aberic.bother.entity.proto.consensus.JoinNodeProto.JoinNode} 字节流
     *
     * @return proto 字节流
     */
    @Override
    public byte[] bean2ProtoByteArray() {
        JoinNodeProto.JoinNode.Builder builder = JoinNodeProto.JoinNode.newBuilder();
        String joinNodeJsonFormat = this.toJsonString();
        log.debug("joinNodeJsonFormat = {}", joinNodeJsonFormat);
        try {
            JsonFormat.parser().merge(joinNodeJsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return builder.build().toByteArray();
    }

}
