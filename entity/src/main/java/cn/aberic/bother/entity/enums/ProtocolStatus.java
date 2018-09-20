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

package cn.aberic.bother.entity.enums;

import lombok.Getter;

/**
 * 传输协议
 * <p>
 * 作者：Aberic on 2018/09/12 16:57
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Getter
public enum ProtocolStatus {

    /** 心跳协议-0x00 */
    HEART("心跳协议", (byte) 0x00),
    /** 保持心跳协议-0x01 */
    KEEP("心跳协议", (byte) 0x01),
    /** 关闭心跳协议-0x02 */
    CLOSE("关闭心跳协议", (byte) 0x02),
    /** 加入新节点协议-0x20 */
    JOIN("加入新节点协议", (byte) 0x20),
    /** 告知新的接入节点当前Hash合约的竞选节点地址-0x21 */
    JOIN_ASK_ELECTION("告知新的接入节点当前Hash合约的竞选节点地址", (byte) 0x21),
    /** 告知新的接入节点准许加入，且为当前Hash合约的竞选节点-0x22 */
    JOIN_AS_ELECTION("告知新的接入节点准许加入，且为当前Hash合约的竞选节点", (byte) 0x22),
    /** 告知新的接入节点准许加入，且为当前Hash合约竞选节点的协助节点-0x23 */
    JOIN_AS_ASSIST("告知新的接入节点准许加入，且为当前Hash合约竞选节点的协助节点", (byte) 0x23),
    /** 告知当前Hash合约的竞选节点有新的协助节点加入-0x24 */
    JOIN_NEW_ASSIST("告知当前Hash合约的竞选节点有新的协助节点加入", (byte) 0x24),
    /** 告知当前Hash合约的竞选节点有新的竞选节点加入-0x25 */
    JOIN_NEW_ELECTION("告知当前Hash合约的竞选节点有新的竞选节点加入", (byte) 0x25),
    /** 告知新的接入节点当前Hash合约的竞选节点的协助节点-0x26 */
    JOIN_TO_ASSIST("告知新的接入节点当前Hash合约的竞选节点的协助节点", (byte) 0x26),
    /** 告知新的接入节点当前Hash合约的基本信息并要求跟随自己-0x27 */
    JOIN_FOLLOW_ME("告知新的接入节点当前Hash合约的基本信息并要求跟随自己", (byte) 0x27),
    /** 告知当前Hash合约的协助节点已经加入该协助节点信息-0x28 */
    JOIN_FOLLOW_U("告知当前Hash合约的协助节点已经加入该协助节点信息", (byte) 0x28),
    /** 告知当前Hash合约的竞选节点更新其下属子节点总数-0x29 */
    JOIN_RESULT_TO_UPGRADE_NODE_COUNT("告知当前Hash合约的竞选节点更新其下属子节点总数", (byte) 0x29),
    /** 发起选举协议-0x40 */
    ELECTION("发起选举协议", (byte) 0x40),
    /** 请求当前竞选节点Leader保持心跳协议-0x41 */
    ELECTION_TO_LEADER_HEART_KEEP_ASK("请求当前竞选节点Leader保持心跳协议", (byte) 0x41),
    /** 告知请求长连接节点当前Hash合约竞选节点集合Leader发生变更，并返回一个可以尝试再次发起请求长连接的节点地址-0x42 */
    ELECTION_TO_LEADER_HEART_KEEP_ASK_CHANGE("告知请求长连接节点当前Hash合约竞选节点集合Leader发生变更", (byte) 0x42),
    /** 告知当前Hash合约的竞选节点集合更新其下属子节点总数-0x43 */
    ELECTION_UPGRADE_NODE_COUNT("告知当前Hash合约的竞选节点集合更新其下属子节点总数", (byte) 0x43),
    /** 交易协议-0x60 */
    TRANSACTION("交易协议", (byte) 0x60),
    /** 同步交易协议-0x61 */
    TRANSACTION_SYNC("同步交易协议", (byte) 0x61),
    /** 区块协议-0x80 */
    BLOCK("区块协议", (byte) 0x80);

    /** 协议描述 */
    private String brief;
    /** 协议码 */
    private byte b;

    /**
     * 当前交易状态
     *
     * @param brief 协议描述
     * @param b     协议码
     */
    ProtocolStatus(String brief, byte b) {
        this.brief = brief;
        this.b = b;
    }

    public static ProtocolStatus get(byte b) {
        ProtocolStatus[] protocolStatuses = values();
        for (ProtocolStatus protocolStatus : protocolStatuses) {
            if (protocolStatus.b == b) {
                return protocolStatus;
            }
        }
        return null;
    }

}
