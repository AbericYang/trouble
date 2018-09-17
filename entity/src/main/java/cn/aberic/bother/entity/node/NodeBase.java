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

package cn.aberic.bother.entity.node;

import cn.aberic.bother.entity.BeanProtoFormat;
import cn.aberic.bother.entity.proto.node.NodeBaseProto;
import cn.aberic.bother.tools.Constant;
import com.google.gson.Gson;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点基类对象
 * <p>
 * 作者：Aberic on 2018/09/17 14:45
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
public class NodeBase implements BeanProtoFormat {

    /** 创建时间戳/节点 ID */
    private long timestamp;
    /** 节点地址 */
    private String address;
    /** 节点绑定账户地址 */
    private String accountAddress;
    /** CPU 个数 */
    private int cpu;
    /** 总内存 */
    private long totalMemory;
    /** 节点执行合约的Hash列表 */
    private List<String> hashes;

    public NodeBase() {
        hashes = new ArrayList<>();
        hashes.add(Constant.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        accountAddress = Constant.TOKEN_DEFAULT_SECOND_HASH;
        Runtime r = Runtime.getRuntime();
        cpu = r.availableProcessors();
        totalMemory = r.totalMemory();
    }

    @Override
    public byte[] bean2ProtoByteArray() {
        NodeBaseProto.NodeBase.Builder builder = NodeBaseProto.NodeBase.newBuilder();
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
    public <M extends GeneratedMessageV3> NodeBase proto2Bean(M m) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(m);
        return new Gson().fromJson(jsonObject, NodeBase.class);
    }

    @Override
    public NodeBase protoByteArray2Bean(byte[] bytes) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(NodeBaseProto.NodeBase.parseFrom(bytes));
        return new Gson().fromJson(jsonObject, NodeBase.class);
    }

}
