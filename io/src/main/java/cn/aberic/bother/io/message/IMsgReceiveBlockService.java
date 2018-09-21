package cn.aberic.bother.io.message;

import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.block.BlockOut;
import cn.aberic.bother.entity.io.MessageData;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 应答区块消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/21 10:42
 * <p>
 * 邮箱：abericyang@gmail.com
 */
interface IMsgReceiveBlockService extends IMsgRequestService {

    /** 接收出块区块协议 */
    default void blockOut(MessageData msgData) throws InvalidProtocolBufferException {
        log().debug("接收区块协议，执行区块同步操作");
        BlockOut blockOut = new BlockOut().protoByteArray2Bean(msgData.getBytes());
        String contractHash = blockOut.getBlock().getHeader().getHash();
        try {
            Block block = new Block().protoByteArray2Bean(msgData.getBytes());
            log().debug("block = {}", block.toJsonString());
            // TODO: 2018/9/12 验证并存储
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

}
