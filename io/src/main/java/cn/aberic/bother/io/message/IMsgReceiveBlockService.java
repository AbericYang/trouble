package cn.aberic.bother.io.message;

import cn.aberic.bother.contract.common.ContractVerify;
import cn.aberic.bother.entity.block.BlockOut;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.io.OutBlock;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.lang3.StringUtils;

/**
 * 应答区块消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/21 10:42
 * <p>
 * 邮箱：abericyang@gmail.com
 */
interface IMsgReceiveBlockService extends IMsgRequestService {

    /** 竞选节点或协助节点接收出块区块协议 */
    default void blockOut(String address, MessageData msgData) throws InvalidProtocolBufferException {
        log().debug("竞选节点或协助节点接收出块区块协议，执行区块同步操作");
        // 得到出块对象
        BlockOut blockOut = new BlockOut().protoByteArray2Bean(msgData.getBytes());
        // 得到当前待出区块所属智能合约Hash
        String contractHash = blockOut.getBlock().getHeader().getHash();
        // 判断是否为当前Hash合约的竞选节点之一
        if (Node.obtain().isElectionNode(contractHash)) { // 如果是竞选节点
            // 判断当前发送消息的地址是否为当前Hash合约竞选节点集合的Leader
            if (StringUtils.equals(address, Node.obtain().getNodeElectionMap().get(contractHash).obtainLeaderAddress())) { // 是竞选节点集合的Leader
                // 得到合约执行验证对象
                ContractVerify contractVerify = new ContractVerify(contractHash);
                // 将通过验证的新交易集合重置进待出块对象
                blockOut.resetTransactions(contractVerify.verifyTransactions(blockOut.getBlock().getBody().getTransactions()));
                // 该竞选节点协助节点同步区块
                new OutBlock(contractHash).syncAssistNode(blockOut);
                // TODO: 2018/9/20 需要退出当前竞选节点集合并加入到当前竞选节点子节点集合中，当前竞选节点由协助节点代替，且协助节点还得广播区块？还需要生成新的协助节点
            } else { // 发送消息节点不是竞选节点集合的Leader
                // 关闭此链接
                shutdown();
            }
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
