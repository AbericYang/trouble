package cn.aberic.bother.io.message;

import cn.aberic.bother.contract.common.ContractVerify;
import cn.aberic.bother.entity.block.BlockOut;
import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.entity.node.NodeBase;
import cn.aberic.bother.entity.node.NodeHash;
import cn.aberic.bother.io.OutBlock;
import cn.aberic.bother.tools.HttpTool;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 应答区块消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/21 10:42
 * <p>
 * 邮箱：abericyang@gmail.com
 */
interface IMsgReceiveBlockService extends IMsgRequestService {

    /** 竞选节点或协助节点接收出块区块协议 */
    default void blockOut(Channel channel, String address, MessageData msgData) throws IOException {
        log().debug("竞选节点或协助节点接收出块区块协议，执行区块同步操作");
        // 得到出块对象
        BlockOut blockOut = new BlockOut().protoByteArray2Bean(msgData.getBytes());
        // 得到当前待出区块所属智能合约Hash
        String contractHash = blockOut.getBlock().getHeader().getHash();
        // 判断是否为当前Hash合约的竞选节点之一
        if (Node.obtain().isElectionNode(contractHash)) { // 如果是竞选节点
            // 判断当前发送消息的地址是否为当前Hash合约竞选节点集合的Leader
            if (StringUtils.equals(address, Node.obtain().getNodeElectionMap().get(contractHash).obtainLeaderAddress())) { // 发消息节点是竞选节点集合的Leader
                // 得到合约执行验证对象
                ContractVerify contractVerify = new ContractVerify(contractHash);
                // 将通过验证的新交易集合重置进待出块对象
                blockOut.resetTransactions(contractVerify.verifyTransactions(blockOut.getBlock().getBody().getTransactions()));
                // 该竞选节点协助节点同步区块
                new OutBlock(contractHash).syncAssistNode(blockOut);
                // 处理当前Hash合约竞选节点集合中Leader节点的变更业务
                blockOutLeaderChange(channel, address, contractHash);
            }
            // 关闭此链接
            shutdown();
        } else if (Node.obtain().isAssistNode(contractHash)) { // 本节点是否为指定Hash合约竞选节点之一的协助节点
            // 判断发消息节点是否为当前Hash合约的竞选节点
            if (StringUtils.equals(address, Node.obtain().getElectionAddress(contractHash))) { // 如果是竞选节点
                // 该协助节点下属节点同步区块
                new OutBlock(contractHash).syncNode(blockOut);
            } else { // 发送消息节点不是竞选节点
                // 关闭此链接
                shutdown();
            }
        }
    }

    /**
     * 竞选节点或协助节点接收出块区块协议协助方法——处理当前Hash合约竞选节点集合中Leader节点的变更业务<p>
     * 当前Hash合约的竞选节点集合中的Leader已经将新的区块进行广播，接下来需要变更当前Leader节点<p>
     * 由当前Hash合约竞选节点集合中的第二顺位节点替代成为新的Leader节点
     *
     * @param address      当前Hash合约竞选节点集合的Leader的地址
     * @param contractHash 当前合约Hash
     */
    default void blockOutLeaderChange(Channel channel, String address, String contractHash) throws IOException {
        // 判断自身是否为竞选节点集合中下一Leader节点
        if (Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().get(1).getTimestamp() == Node.obtain().getNodeBase().getTimestamp()) {
            // 向竞选节点集合中的Leader节点发送post请求询问其协助节点地址
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contractHash", contractHash);
            String leaderAssistNodeAddress = HttpTool.postNode(address, jsonObject.toJSONString());
            // 获取当前竞选节点集合Leader节点
            NodeBase oldLeaderNode = Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().get(0);
            // 将当前Hash合约竞选节点集合中的Leader节点移除
            Node.obtain().getNodeElectionMap().get(contractHash).removeLeader();
            if (StringUtils.isEmpty(leaderAssistNodeAddress)) { // 如果没有获取到当前Leader节点的协助节点地址
                // 判断当前竞选节点集合是否满员
                if (Node.obtain().getNodeElectionMap().get(contractHash).full()) { // 如果满员
                    // 获取当前竞选节点集合中下属子节点总数最少的竞选节点地址
                    String addressIdlest = Node.obtain().getNodeElectionMap().get(contractHash).getIdlest();
                    // 告知新的接入地址当前Hash合约下其它竞选节点地址
                    send(channel, ProtocolStatus.JOIN_ASK_ELECTION, addressIdlest);
                } else { // 如果不满员
                    // 将原竞选节点集合Leader节点置入竞选节点集合末尾继续参与轮流出块，新增竞选节点
                    Node.obtain().add(contractHash, oldLeaderNode);
                    // 当前Hash合约竞选节点集合内部广播新节点加入
                    Node.obtain().getNodeElectionMap().get(contractHash).getNodeBases().forEach(
                            nodeBase -> send(nodeBase.getAddress(), ProtocolStatus.JOIN_NEW_ELECTION, new NodeHash(contractHash, oldLeaderNode)));
                    // 将自身在当前竞选节点集合中的信息push给当前加入节点
                    send(channel, ProtocolStatus.JOIN_AS_ELECTION, Node.obtain().getNodeElectionMap().get(contractHash));
                }
            } else {
                // 向竞选节点集合中的Leader节点的协助节点发送加入竞选节点集合消息
                send(leaderAssistNodeAddress, ProtocolStatus.JOIN_AS_ELECTION, Node.obtain().getNodeElectionMap().get(contractHash));
            }
            // 执行出块操作
            new OutBlock(contractHash).publish();
        }
    }

    /** 普通节点接收出块区块协议 */
    default void blockNodeSync(String address, MessageData msgData) throws InvalidProtocolBufferException {
        log().debug("普通节点接收出块区块协议，执行区块同步操作");
        // 得到出块对象
        BlockOut blockOut = new BlockOut().protoByteArray2Bean(msgData.getBytes());
        // 得到当前待出区块所属智能合约Hash
        String contractHash = blockOut.getBlock().getHeader().getHash();
        // 判断是否为当前Hash合约的协助节点
        if (StringUtils.equals(address, Node.obtain().getAssistAddress(contractHash))) { // 如果是协助节点
            // 普通节点同步区块
            new OutBlock(contractHash).sync(blockOut);
        }
    }

}
