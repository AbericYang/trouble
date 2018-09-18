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

import cn.aberic.bother.entity.io.MessageData;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 应答选举消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/14 14:08
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public interface IMsgElectionService extends IMsgRequestService {

    /**
     * 应答选举消息业务处理方案，由{@link IMsgReceiveService}继承并启用该方案
     *
     * @param address 当前指定通道的连接地址
     * @param msgData 协议消息对象
     */
    default void election(String address, MessageData msgData) throws InvalidProtocolBufferException {
        switch (msgData.getProtocol()) {
            case ELECTION: // 接收到发起选举协议
                break;
        }
    }

    default void electionExec(String address, MessageData msgData) {
    }

}