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
 *
 */

package cn.aberic.bother.io.message;

import cn.aberic.bother.entity.BeanProtoFormat;
import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.tools.MsgPackTool;
import io.netty.channel.Channel;

import java.util.List;

/**
 * 作者：Aberic on 2018/9/14 21:39
 * <p>
 * 邮箱：abericyang@gmail.com
 */
interface IMsgRequestPushService {

    /**
     * 推送空消息
     *
     * @param channel 推送消息通道
     * @param status  推送协议
     */
    default void push(Channel channel, ProtocolStatus status) {
        MessageData msgData = new MessageData(status, null);
        channel.writeAndFlush(msgData);
    }

    /**
     * 推送消息
     *
     * @param channel 推送消息通道
     * @param status  推送协议
     * @param bytes   推送内容字节数组
     */
    default void push(Channel channel, ProtocolStatus status, byte[] bytes) {
        MessageData msgData = new MessageData(status, bytes);
        channel.writeAndFlush(msgData);
    }

    /**
     * 推送消息
     *
     * @param channel 推送消息通道
     * @param status  推送协议
     * @param string  推送字符串内容
     */
    default void push(Channel channel, ProtocolStatus status, String string) {
        push(channel, status, MsgPackTool.string2Bytes(string));
    }

    /**
     * 推送消息
     *
     * @param channel    推送消息通道
     * @param status     推送协议
     * @param stringList 推送字符串集合内容
     */
    default void push(Channel channel, ProtocolStatus status, List<String> stringList) {
        push(channel, status, MsgPackTool.list2Bytes(stringList));
    }

    /**
     * 推送消息
     *
     * @param channel 推送消息通道
     * @param status  推送协议
     * @param t       继承BeanProtoFormat的对象
     */
    default <T extends BeanProtoFormat> void push(Channel channel, ProtocolStatus status, T t) {
        push(channel, status, t.bean2ProtoByteArray());
    }

}
