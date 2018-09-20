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
import cn.aberic.bother.entity.proto.node.NodeAssistProto;
import cn.aberic.bother.tools.Constant;
import com.google.gson.Gson;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * 当前协助节点对象
 * <p>
 * 作者：Aberic on 2018/09/17 16:58
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
public class NodeAssist implements BeanProtoFormat {

    /** 当前竞选节点下的节点集合 */
    private List<NodeBase> nodeBases;

    /** 新增节点 */
    public void add(NodeBase nodeBase) {
        if (null == nodeBases) {
            nodeBases = new LinkedList<>();
        }
        nodeBases.add(nodeBase);
    }

    /** 获取下标节点 */
    public NodeBase get(int index) {
        return nodeBases.get(index);
    }

    /** 移除下标节点 */
    public void remove(int index) {
        nodeBases.remove(index);
    }

    /** 移除节点对象 */
    public void remove(NodeBase nodeBase) {
        nodeBases.remove(nodeBase);
    }

    /**
     * 获取当前竞选节点下节点集合的长度
     *
     * @return 节点集合的长度
     */
    public int size() {
        return nodeBases.size();
    }

    /** 节点排序，前三个节点位置不变，后续节点重新排序 */
    public void sort() {
        if (nodeBases.size() <= 4) {
            return;
        }
        List<NodeBase> nodeBasesTmp = new LinkedList<NodeBase>() {{
            add(nodeBases.get(0));
            add(nodeBases.get(1));
            add(nodeBases.get(2));
        }};
        nodeBases.remove(0);
        nodeBases.remove(0);
        nodeBases.remove(0);
        nodeBases.sort((n1, n2) ->
                (int) ((n1.getCpu() - n2.getCpu()) * Constant.NODE_ELECTION_CPU + (n1.getFreeMemory() - n2.getFreeMemory()) * Constant.NODE_ELECTION_MEMORY));
        nodeBases.addAll(0, nodeBasesTmp);
    }

    @Override
    public byte[] bean2ProtoByteArray() {
        NodeAssistProto.NodeAssist.Builder builder = NodeAssistProto.NodeAssist.newBuilder();
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
    public <M extends GeneratedMessageV3> NodeAssist proto2Bean(M m) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(m);
        return new Gson().fromJson(jsonObject, NodeAssist.class);
    }

    @Override
    public NodeAssist protoByteArray2Bean(byte[] bytes) throws InvalidProtocolBufferException {
        return proto2Bean(NodeAssistProto.NodeAssist.parseFrom(bytes));
    }

}
