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
import cn.aberic.bother.entity.MapListString;
import cn.aberic.bother.entity.proto.node.NodeProto;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.FileTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 节点对象<p>
 * 新节点A加入后，会将自己的节点基本信息{@link NodeBase}发送至某一已知节点M<p>
 * 如果M本身即为当前竞选节点之一，有如下两种情况
 * ①检查当前竞选节点集合是否满足{@link cn.aberic.bother.tools.Constant}ELECTION_COUNT数量，如果不满足，则将该节点当做竞选节点之一。如果满足，则进入第二种情况<p>
 * ②将自己的协助节点、当前竞选中的节点集合以及备用节点集合发回给A<p>
 * 如果M本身即为当前竞选节点都协助节点，则直接将A加入到当前竞选节点序列之下，告知A保持心跳并返回当前竞选节点，并允许A参与下一轮排序<p>
 * 如果M为普通节点，则M接收到A加入请求后，会将自己当前访问的竞选节点X发回给A<p>
 * A接收到当前可访问X后，会向X发送接入请求<p>
 * X收到A的接入请求后首先判断自己是否依旧是当前竞选节点之一，如果不是，则重复M的操作<p>
 * 如果X目前还是当前竞选节点之一，则将自己的协助节点Y、当前竞选中的节点集合以及备用节点集合发回给A<p>
 * A收到X的推送后保存并向Y发送加入请求，Y收到A的加入请求后将A加入到当前竞选节点序列之下，告知A保持心跳并返回当前竞选节点，并允许A参与下一轮排序
 * <p>
 * 作者：Aberic on 2018/09/17 14:05
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
public class Node implements BeanProtoFormat {

    private static Node instance;

    /** 当前节点的基本信息 */
    private NodeBase nodeBase;
    /** 当前Hash访问的竞选中节点 */
    private Map<String, String> addressElectionMap;
    /**
     * 当前Hash访问的竞选节点指定协助节点<p>
     * 协助节点将辅助竞选节点管理竞选节点下的子节点<p>
     * 即用于处理除了竞选工作以外的所有事务<p>
     * 如：内部排序、节点加减管理等
     */
    private Map<String, NodeBase> nodeBaseAssistMap;
    /** 当前Hash竞选中节点集合 <= 50 */
    private Map<String, MapListString> addressElectionsMap;
    /** 当前Hash已知备用节点集合/随机节点数 <= 100 */
    private Map<String, MapListString> addressMap;

    /** 当前Hash协助节点对象 */
    private Map<String, NodeAssist> nodeAssistMap;
    /** 当前Hash节点竞选对象 */
    private Map<String, NodeElection> nodeElectionMap;

    public static Node obtain() {
        if (null == instance) {
            synchronized (Node.class) {
                if (null == instance) {
                    instance = getNode();
                }
            }
        }
        return instance;
    }

    private static Node getNode() {
        Node node;
        String nodeJsonString = null;
        try {
            nodeJsonString = FileTool.getStringFromPath(Constant.NODE_FILE);
        } catch (IOException e) {
            FileTool.createFile(Constant.NODE_FILE);
        }
        if (StringUtils.isEmpty(nodeJsonString)) {
            node = new Node();
        } else {
            node = JSON.parseObject(nodeJsonString, new TypeReference<Node>() {});
        }
        return node;
    }

    private Node() {
        nodeBase = new NodeBase();
        addressElectionMap = new HashMap<>();
        nodeBaseAssistMap = new HashMap<>();
        addressElectionsMap = new HashMap<>();
        addressMap = new HashMap<>();
        nodeAssistMap = new HashMap<>();
        nodeElectionMap = new HashMap<>();
    }

    /**
     * 本节点是否为当前Hash合约竞选节点集合中的Leader
     *
     * @param contractHash 合约Hash
     * @return 与否
     */
    public boolean isElectionNode(String contractHash) {
        if (nodeElectionMap.size() > 0 && nodeElectionMap.containsKey(contractHash)) {
            return nodeElectionMap.get(contractHash).getNodeBases().get(0).getTimestamp() == nodeBase.getTimestamp();
        }
        return false;
    }

    /**
     * 本节点是否为当前Hash合约竞选节点之一的辅助节点
     *
     * @param contractHash 合约Hash
     * @return 与否
     */
    public boolean isAssistNode(String contractHash) {
        if (nodeBaseAssistMap.size() > 0 && nodeBaseAssistMap.containsKey(contractHash)) {
            return nodeBaseAssistMap.get(contractHash).getTimestamp() == nodeBase.getTimestamp();
        }
        return false;
    }

    /**
     * 当前Hash合约选举节点基本信息集合添加新节点
     *
     * @param nodeBase 节点基本信息
     * @return 成功与否
     */
    public boolean add(String contractHash, NodeBase nodeBase) {
        if (addressElectionsMap.get(contractHash).getStringList().size() < 50) {
            addressElectionsMap.get(contractHash).getStringList().add(nodeBase.getAddress());
            if (addressMap.get(contractHash).getStringList().size() < 100) {
                addressMap.get(contractHash).getStringList().add(nodeBase.getAddress());
            } else {
                addressMap.get(contractHash).getStringList().remove(0);
                addressMap.get(contractHash).getStringList().add(nodeBase.getAddress());
            }
            return nodeElectionMap.get(contractHash).add(nodeBase);
        }
        return false;
    }

    @Override
    public byte[] bean2ProtoByteArray() {
        NodeProto.Node.Builder builder = NodeProto.Node.newBuilder();
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
    public <M extends GeneratedMessageV3> Node proto2Bean(M m) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(m);
        instance = new Gson().fromJson(jsonObject, Node.class);
        FileTool.write(Constant.NODE_FILE, JSON.toJSONString(instance));
        return this;
    }

    @Override
    public Node protoByteArray2Bean(byte[] bytes) throws InvalidProtocolBufferException {
        return proto2Bean(NodeProto.Node.parseFrom(bytes));
    }

}
