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
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.tools.MsgPackTool;

import java.util.List;

/**
 * 请求消息业务最基本/默认处理接口
 * <p>
 * 作者：Aberic on 2018/9/14 21:38
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public interface IMsgRequestSendService {

    /**
     * 发送连接请求
     *
     * @param address 请求地址
     * @param status  请求协议
     * @param bytes   请求内容字节数组
     */
    default void send(String address, ProtocolStatus status, byte[] bytes) {
        IOContext.obtain().send(address, new MessageData(status, bytes));
    }

    /**
     * 发送连接请求
     *
     * @param address 请求地址
     * @param status  请求协议
     * @param string  请求字符串内容
     */
    default void send(String address, ProtocolStatus status, String string) {
        send(address, status, MsgPackTool.string2Bytes(string));
    }

    /**
     * 发送连接请求
     *
     * @param address    请求地址
     * @param status     请求协议
     * @param stringList 请求字符串集合内容
     */
    default void send(String address, ProtocolStatus status, List<String> stringList) {
        send(address, status, MsgPackTool.list2Bytes(stringList));
    }

    /**
     * 发送连接请求
     *
     * @param address 请求地址
     * @param status  请求协议
     * @param t       请求对象——继承BeanProtoFormat的对象
     */
    default <T extends BeanProtoFormat> void send(String address, ProtocolStatus status, T t) {
        IOContext.obtain().send(address, new MessageData(status, t.bean2ProtoByteArray()));
    }

}
