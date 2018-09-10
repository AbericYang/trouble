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

package cn.aberic.bother.io.code;

import cn.aberic.bother.tools.MsgPackTool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者：Aberic on 2018/9/10 22:51
 * 邮箱：abericyang@gmail.com
 */
interface TroubleCode {

    AtomicInteger dataId = new AtomicInteger(0);

    /**
     * 生成数据包ID
     * 数据包 id（UInt16 无符号整型16位）
     * 采用累加的方式，应答数据的数据包ID与发送的数据包ID相同
     * 数值范围：0至65535（超出范围清0）
     *
     * @return 数据包ID
     */
    default int createDataId() {
        dataId.getAndIncrement();
        if (dataId.get() > 65535) {
            dataId.set(0);
        }
        return dataId.get();
    }

    /**
     * 生成数据包头（标准数据包格式，详见各字段注释）
     *
     * @param protocolId 与服务器约定协议号，以解析数据，如0x00、0x01等
     *
     * @return 数据包头
     */
    default byte[] createHeader(byte protocolId) {
        byte[] tempBytes; // 临时 byte 数据数组
        byte[] resultBytes = new byte[5]; // 数据包头 byte[] 数组结果集

        resultBytes[0] = 0x56; // 0x56（固定值）
        resultBytes[1] = 0x33; // 0x33（固定值）

        resultBytes[2] = protocolId;

        // 数据包 id 转 byte[]
        tempBytes = intToBytes(createDataId());
        resultBytes[3] = tempBytes[3];
        resultBytes[4] = tempBytes[2];

        return resultBytes;
    }

    /**
     * 生成数据包尾
     *
     * @return 数据包尾
     */
    default byte createEnd() {
        return 0x13; // 0x13（固定值）
    }

    /**
     * 发送消息数据包
     *
     * @param protocolId 与服务器约定协议号，以解析数据 0x01、0x02 等
     *                   采用累加的方式，应答数据的数据包ID与发送的数据包ID相同
     *                   数值范围：0至65535（超出范围清0）
     * @param msg        消息内容
     *
     * @return 消息数据包
     */
    default byte[] createData(byte protocolId, String msg) {
        try {
            byte[] tempBytes;  // 数据包临时 byte 数据数组
            List<Byte> resultList = new LinkedList<>(); // 消息数据包 List<Byte> 集合结果集

            tempBytes = createHeader(protocolId); // 创建包头
            for (byte b : tempBytes) { // 结果集加入包头
                resultList.add(b);
            }

            List<Byte> resultDataList = new LinkedList<>(); // 消息数据包所属数据体 List<Byte> 集合结果集
            byte[] tempDataBytes;  // 数据体临时 byte 数据数组

            tempBytes = msg.getBytes("UTF-16LE");
            // 将数据体长度【（UInt16 无符号整型16位）是指数据体内容的字节长度】加入数据包且在具体数据体之前
            tempDataBytes = shortToBytes((short) tempBytes.length);
            resultDataList.add(tempDataBytes[1]);
            resultDataList.add(tempDataBytes[0]);
            for (byte b : tempBytes) { // 将数据体加入数据包
                resultDataList.add(b);
            }

            // 数据体内容 转 byte[]
            byte[] dataBodyBytes = listToBytes(resultDataList);

            // 计算数据体内容长度
            tempBytes = intToBytes(dataBodyBytes.length);
            resultList.add(tempBytes[3]); // 数据体长度
            resultList.add(tempBytes[2]); // 数据体长度

            for (byte b : dataBodyBytes) {
                resultList.add(b);// 将数据体内容分段加入数据包
            }

            resultList.add(verifyCode(dataBodyBytes)); // 生成校验码

            resultList.add(createEnd()); // 包尾

            return listToBytes(resultList);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * 发送消息数据包
     *
     * @param protocolId 与服务器约定协议号，以解析数据 0x01、0x02 等
     *                   采用累加的方式，应答数据的数据包ID与发送的数据包ID相同
     *                   数值范围：0至65535（超出范围清0）
     * @param msg        消息内容
     *
     * @return 消息数据包
     */
    default <T> byte[] createData(byte protocolId, T obj) {
        try {
            byte[] tempBytes;  // 数据包临时 byte 数据数组
            List<Byte> resultList = new LinkedList<>(); // 消息数据包 List<Byte> 集合结果集

            tempBytes = createHeader(protocolId); // 创建包头
            for (byte b : tempBytes) { // 结果集加入包头
                resultList.add(b);
            }

            List<Byte> resultDataList = new LinkedList<>(); // 消息数据包所属数据体 List<Byte> 集合结果集
            byte[] tempDataBytes;  // 数据体临时 byte 数据数组

            tempBytes = MsgPackTool.toBytes(obj);
            // 将数据体长度【（UInt16 无符号整型16位）是指数据体内容的字节长度】加入数据包且在具体数据体之前
            tempDataBytes = shortToBytes((short) tempBytes.length);
            resultDataList.add(tempDataBytes[1]);
            resultDataList.add(tempDataBytes[0]);
            for (byte b : tempBytes) { // 将数据体加入数据包
                resultDataList.add(b);
            }

            // 数据体内容 转 byte[]
            byte[] dataBodyBytes = listToBytes(resultDataList);

            // 计算数据体内容长度
            tempBytes = intToBytes(dataBodyBytes.length);
            resultList.add(tempBytes[3]); // 数据体长度
            resultList.add(tempBytes[2]); // 数据体长度

            for (byte b : dataBodyBytes) {
                resultList.add(b);// 将数据体内容分段加入数据包
            }

            resultList.add(verifyCode(dataBodyBytes)); // 生成校验码

            resultList.add(createEnd()); // 包尾

            return listToBytes(resultList);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    default byte[] intToBytes(int num) {
        byte[] b = new byte[4];
        b[0] = (byte) (num >> 24);
        b[1] = (byte) (num >> 16);
        b[2] = (byte) (num >> 8);
        b[3] = (byte) num;
        return b;
    }

    default byte[] shortToBytes(short num) {
        byte[] b = new byte[2];
        b[0] = (byte) (num >> 8);
        b[1] = (byte) num;
        return b;
    }

    default byte[] listToBytes(List<Byte> byteList) {
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
     *
     * @return 校验码
     */
    default byte verifyCode(byte[] bytes) {
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
