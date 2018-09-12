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

    /** 心跳包-0x00 */
    HEART("心跳包", (byte) 0x00),
    /** 区块包-0x51 */
    BLOCK("区块包", (byte) 0x51);

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
