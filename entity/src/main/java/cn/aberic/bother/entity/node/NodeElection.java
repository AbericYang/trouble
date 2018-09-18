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
import cn.aberic.bother.entity.proto.node.NodeElectionProto;
import cn.aberic.bother.tools.Constant;
import com.google.gson.Gson;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 当前节点竞选对象
 * <p>
 * 作者：Aberic on 2018/09/17 16:59
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
public class NodeElection implements BeanProtoFormat {

    /** 当前节点竞选对象所属智能合约Hash */
    private String contractHash;
    /** 当前Hash合约选举节点基本信息集合 <= 50 */
    private List<NodeBase> nodeBases;
    /** Hash合约从其它竞选节点获取其两个子节点的而组成的备用节点集合 <= 100 */
    private List<String> addresses;

    /**
     * 当前Hash合约选举节点基本信息集合添加新节点
     *
     * @param nodeBase 节点基本信息
     *
     * @return 成功与否
     */
    public boolean add(NodeBase nodeBase) {
        if (!addresses.contains(nodeBase.getAddress())) {
            if (addresses.size() < Constant.NODE_BACK_COUNT) {
                addresses.add(nodeBase.getAddress());
            } else {
                addresses.remove(0);
                addresses.add(nodeBase.getAddress());
            }
        }
        for (NodeBase node : nodeBases) {
            if (StringUtils.equals(node.getAddress(), nodeBase.getAddress())) {
                return true;
            }
        }
        if (nodeBases.size() < Constant.NODE_ELECTION_COUNT) {
            nodeBases.add(nodeBase);
            return true;
        }
        return false;
    }

    /**
     * 删除当前Hash合约选举节点基本信息集合中指定节点地址的节点
     *
     * @param address 节点地址
     */
    public void remove(String address) {
        //使用迭代器的remove()方法删除元素
        nodeBases.removeIf(nodeBase -> StringUtils.equals(nodeBase.getAddress(), address));
        addresses.remove(address);
    }

    /**
     * 判断当前Hash合约下竞选节点总数量是否充足
     *
     * @return 与否
     */
    public boolean full() {
        return nodeBases.size() >= Constant.NODE_ELECTION_COUNT;
    }

    @Override
    public byte[] bean2ProtoByteArray() {
        NodeElectionProto.NodeElection.Builder builder = NodeElectionProto.NodeElection.newBuilder();
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
    public <M extends GeneratedMessageV3> NodeElection proto2Bean(M m) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(m);
        return new Gson().fromJson(jsonObject, NodeElection.class);
    }

    @Override
    public NodeElection protoByteArray2Bean(byte[] bytes) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(NodeElectionProto.NodeElection.parseFrom(bytes));
        return new Gson().fromJson(jsonObject, NodeElection.class);
    }

}
