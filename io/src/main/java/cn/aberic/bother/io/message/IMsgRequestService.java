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

import cn.aberic.bother.entity.consensus.ConnectSelf;
import cn.aberic.bother.entity.consensus.GroupInfo;
import cn.aberic.bother.entity.consensus.JoinFeedback;
import cn.aberic.bother.entity.enums.JoinLevel;
import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.tools.MsgPackTool;
import io.netty.channel.Channel;
import org.slf4j.Logger;

/**
 * 请求消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/12 14:12
 * 邮箱：abericyang@gmail.com
 */
interface IMsgRequestService {

    Logger log();

    /**
     * 在当前channel下发送心跳包
     *
     * @param channel 当前通道
     */
    default void sendHeartBeat(Channel channel) {
        MessageData msgData = new MessageData(ProtocolStatus.HEART, null);
        channel.writeAndFlush(msgData);
    }

    /**
     * 向指定地址发送加入请求协议
     *
     * @param address 指定地址
     * @param level   请求级别
     */
    default void sendJoin(String address, JoinLevel level) {
        IOContext.obtain().send(address, new MessageData(ProtocolStatus.JOIN, MsgPackTool.string2Bytes(level.name())));
    }

    /**
     * 在当前channel下发送保持心跳包
     *
     * @param channel 当前通道
     */
    default void pushKeepHeartBeat(Channel channel) {
        MessageData msgData = new MessageData(ProtocolStatus.KEEP, null);
        channel.writeAndFlush(msgData);
    }

    /**
     * 在当前channel下发送应答协议包
     *
     * @param channel 当前通道
     */
    default void pushAnswerOK(Channel channel) {
        MessageData msgData = new MessageData(ProtocolStatus.OK, null);
        channel.writeAndFlush(msgData);
    }

    /**
     * 告知新的接入地址可加入协议
     *
     * @param channel 当前通道
     */
    default void pushJoinAccept(Channel channel) {
        MessageData msgData = new MessageData(ProtocolStatus.JOIN_ACCEPT, ConnectSelf.obtain().self2ProtoByteArray());
        channel.writeAndFlush(msgData);
    }

    /**
     * 告知新的接入节点反馈协议
     *
     * @param channel 当前通道
     */
    default void pushJoinFeedback(Channel channel, JoinLevel level, GroupInfo info) {
        JoinFeedback joinFeedback = new JoinFeedback();
        joinFeedback.setLevel(level);
        joinFeedback.setAddress(info.getLeaderAddress());
        joinFeedback.setAddresses(info.getAddresses());
        MessageData msgData = new MessageData(ProtocolStatus.JOIN_FEEDBACK, joinFeedback.join2ProtoByteArray());
        channel.writeAndFlush(msgData);
    }

    /**
     * 在当前channel下发送当前没有可加入小组，自建小组并参与小组间选举协议
     *
     * @param channel 当前通道
     */
    default void pushCreateGroup(Channel channel) {
        MessageData msgData = new MessageData(ProtocolStatus.CREATE_GROUP, null);
        channel.writeAndFlush(msgData);
    }

    /**
     * 广播新增小组节点协议
     *
     * @param address 新增小组节点地址
     */
    default void pushAddNode(String address) {
        MessageData msgData = new MessageData(ProtocolStatus.ADD_NODE, MsgPackTool.string2Bytes(address));
        IOContext.obtain().broadcast(msgData);
    }

}
