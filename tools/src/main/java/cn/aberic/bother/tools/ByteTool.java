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

package cn.aberic.bother.tools;

import java.util.List;

/**
 * 作者：Aberic on 2018/09/11 10:13
 * 邮箱：abericyang@gmail.com
 */
public class ByteTool {

    public static byte[] intToBytes(int num) {
        byte[] b = new byte[4];
        b[0] = (byte) (num >> 24);
        b[1] = (byte) (num >> 16);
        b[2] = (byte) (num >> 8);
        b[3] = (byte) num;
        return b;
    }

    /**
     * 字节数组转 UInt16 无符号整型16位
     *
     * @param bytes 字节数组
     * @return UInt16 无符号整型16位
     */
    public static int bytesToInt(byte[] bytes) {
        return (bytes[0] & 0xff) << 24
                | (bytes[1] & 0xff) << 16
                | (bytes[2] & 0xff) << 8
                | (bytes[3] & 0xff);
    }

    public static byte[] listToBytes(List<Byte> byteList) {
        byte[] bytes = new byte[byteList.size()];
        int length = byteList.size();
        for (int i = 0; i < length; i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }

    /**
     * 根据数据体内容生成对应校验码
     * 数据体内容所有字节的异或和<p>
     * 例如：数据体内容为0x41 0x42 0x43，则校验码为0x41 ^ 0x42 ^ 0x43 = 0x40
     *
     * @param bytes 数据体内容
     * @return 校验码
     */
    public static byte verifyCode(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return (byte) 0x00;
        }
        int rs = 0;
        for (byte b : bytes) {
            rs = rs ^ b;
        }
        return (byte) rs;
    }

}
