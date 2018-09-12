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

package cn.aberic.bother.entity.io;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据传输消息体对象
 * <p>
 * 作者：Aberic on 2018/09/11 11:28
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
@ToString
public class MessageData {

    /** 与服务器约定协议号，以解析数据，如0x00、0x01等 */
    private byte protocolId;
    /** 数据长度 */
    private int length;
    /** 数据请求 ID */
    private int dataId;
    /** 数据对象字节数组 */
    private byte[] bytes;

    public MessageData() {
    }

    public MessageData(byte protocolId, byte[] bytes) {
        this.protocolId = protocolId;
        this.bytes = bytes;
    }

}
