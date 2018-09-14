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
 * 邮箱：abericyang@gmail.com
 */
@Getter
public enum ProtocolStatus {

    /** 心跳协议-0x00 */
    HEART("心跳协议", (byte) 0x00),
    /** 保持心跳协议-0x01 */
    KEEP("心跳协议", (byte) 0x01),
    /** 关闭心跳协议-0x02 */
    BYE("关闭心跳协议", (byte) 0x02),
    /** 应答协议-0x03 */
    OK("应答协议", (byte) 0x03),
    /**
     * 加入新节点协议
     * follow节点收到新节点加入通知后，发送此协议告知leader节点有新节点加入请求
     * leader节点直接处理该协议-0x04
     */
    JOIN("加入新节点协议", (byte) 0x04),
    /** 告知新的接入地址可加入协议-0x05 */
    JOIN_ACCEPT("告知新的接入地址可加入协议", (byte) 0x05),
    /** 告知新的接入节点反馈协议-0x06 */
    JOIN_FEEDBACK("告知新的接入节点反馈协议", (byte) 0x06),
    /** 由leader节点发出新增小组节点协议-0x07 */
    ADD_NODE("新增小组节点协议", (byte) 0x07),
    /** 由leader节点发出更新小组节点集合协议-0x08 */
    UPGRADE_NODE("更新小组节点集合协议", (byte) 0x08),
    /** 当前没有可加入小组，自建小组并参与小组间选举协议-0x09 */
    CREATE_GROUP("当前没有可加入小组", (byte) 0x09),
    /** 区块协议-0x51 */
    BLOCK("区块协议", (byte) 0x51);

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
