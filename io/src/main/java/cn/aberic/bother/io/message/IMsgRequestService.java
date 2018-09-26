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

package cn.aberic.bother.io.message;

import cn.aberic.bother.entity.enums.ProtocolStatus;
import io.netty.channel.Channel;
import org.slf4j.Logger;

/**
 * 请求消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/12 14:12
 * <p>
 * 邮箱：abericyang@gmail.com
 */
interface IMsgRequestService extends IMsgRequestSendService, IMsgRequestPushService, IMsgConnectTest {

    Logger log();

    /** 关闭当前通道且禁止长连接 */
    void shutdown();

    /**
     * 在当前channel下发送心跳包
     *
     * @param channel 当前通道
     */
    default void sendHeartBeat(Channel channel) {
        push(channel, ProtocolStatus.HEART);
    }

    /**
     * 请求当前竞选节点Leader保持心跳协议
     *
     * @param address      指定地址
     * @param contractHash 合约Hash
     */
    default void sendElectionToLeaderHeartBeatKeepAsk(String address, String contractHash) {
        send(address, ProtocolStatus.ELECTION_TO_LEADER_HEART_KEEP_ASK, contractHash);
    }

    /**
     * 请求当前协助节点保持心跳协议
     *
     * @param address      指定地址
     * @param contractHash 合约Hash
     */
    default void sendElectionToAssistHeartKeepAsk(String address, String contractHash) {
        send(address, ProtocolStatus.ELECTION_TO_ASSIST_HEART_KEEP_ASK, contractHash);
    }

    /**
     * 在当前channel下发送保持心跳包
     *
     * @param channel 当前通道
     */
    default void pushKeep(Channel channel) {
        push(channel, ProtocolStatus.KEEP);
    }

}
