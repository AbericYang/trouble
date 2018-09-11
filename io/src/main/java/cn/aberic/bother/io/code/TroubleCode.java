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

import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.tools.ByteTool;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者：Aberic on 2018/9/10 22:51
 * 邮箱：abericyang@gmail.com
 */
interface TroubleCode {

    AtomicInteger dataId = new AtomicInteger(0);

    Logger log();

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
     * @return 数据包头
     */
    default byte[] createHeader() {
        byte[] resultBytes = new byte[2]; // 数据包头 byte[] 数组结果集

        resultBytes[0] = 0x56; // 0x56（固定值）
        resultBytes[1] = 0x33; // 0x33（固定值）

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
     * 生成心跳数据包
     *
     * @return 心跳数据包
     */
    default byte[] createHeart() {
        try {
            byte[] tempBytes; // 临时 byte 数据数组
            List<Byte> resultList = new LinkedList<>(); // 心跳数据包 List<Byte> 集合结果集

            tempBytes = createHeader(); // 创建包头
            for (byte b : tempBytes) { // 结果集加入包头
                resultList.add(b);
            }
            // （心跳包为空包）数据体长度（UInt16 无符号整型16位）是指数据体内容的字节长度
            // 计算数据体内容长度
            tempBytes = ByteTool.intToBytes(0);
            resultList.add(tempBytes[3]);
            resultList.add(tempBytes[2]);
            resultList.add(tempBytes[1]);
            resultList.add(tempBytes[0]);

            /* 校验码，数据体内容所有字节的异或和
               例如：数据体内容为0x41 0x42 0x43，则校验码为0x41 ^ 0x42 ^ 0x43 = 0x40 */
            resultList.add((byte) 0x00);

            resultList.add(createEnd()); // 包尾

            return ByteTool.listToBytes(resultList);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * 发送消息数据包
     *
     * @param bytes 数据对象字节数组，来自{@link MessageData}
     * @return 消息数据包
     */
    default byte[] createData(byte[] bytes) {
        try {
            byte[] tempBytes;  // 数据包临时 byte 数据数组
            List<Byte> resultList = new LinkedList<>(); // 消息数据包 List<Byte> 集合结果集

            tempBytes = createHeader(); // 创建包头
            for (byte b : tempBytes) { // 结果集加入包头
                resultList.add(b);
            }

            // 计算数据体内容长度
            tempBytes = ByteTool.intToBytes(bytes.length);
            resultList.add(tempBytes[3]);
            resultList.add(tempBytes[2]);
            resultList.add(tempBytes[1]);
            resultList.add(tempBytes[0]);

            for (byte b : bytes) {
                resultList.add(b);// 将数据体内容分段加入数据包
            }

            resultList.add(ByteTool.verifyCode(bytes)); // 生成校验码

            resultList.add(createEnd()); // 包尾

            return ByteTool.listToBytes(resultList);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    default MessageData analysis(MessageData messageData, List<Byte> receiveBytesList) {
        // 数据包头2、数据体长度4、校验码1、包尾1，总长度不得少于8
        if (receiveBytesList.size() < 8) {
            log().warn("数据长度不正确");
            return null;
        }
        log().debug("接收包：{}", Arrays.toString(receiveBytesList.toArray()));
        // 确保数据包第1个字节为0x56，第2个字节为0x33
        if (receiveBytesList.get(0) != 0x56 || receiveBytesList.get(1) != 0x33) {
            receiveBytesList.remove(0);
            receiveBytesList.remove(0);
            log().warn("数据包第1、2字节不正确");
            return analysis(messageData, receiveBytesList);
        }
        // 数据体长度（UInt16 无符号整型16位）是指数据体内容的字节长度
        int dataBodyContentLength = ByteTool.bytesToInt(new byte[]{receiveBytesList.get(5), receiveBytesList.get(4), receiveBytesList.get(3), receiveBytesList.get(2)});
        // 数据体长度（UInt16 无符号整型16位）是指数据体内容的字节长度
        log().debug("数据体长度：{}", dataBodyContentLength);
        // 如果数据体内容加固定长度11的数量大于当前已接收到的字节流集合长度，则表明当前数据体内容不符合数据包格式
        if (8 + dataBodyContentLength > receiveBytesList.size()) {
            log().warn("数据体内容不符合数据包格式");
            return null;
        }
        // 确保数据包最后1个字节为0x13
        if (receiveBytesList.get(7 + dataBodyContentLength) != 0x13) {
            log().warn("数据包最后1个字节不正确");
            for (int i = 0; i < 7 + dataBodyContentLength; i++) {
                receiveBytesList.remove(0);
            }
            return analysis(messageData, receiveBytesList);
        }
        // 数据体字节数组
        byte[] dataBodyContent = new byte[0];
        if (dataBodyContentLength > 0) {
            dataBodyContent = ByteTool.listToBytes(receiveBytesList.subList(6, 6 + dataBodyContentLength));
        }
        // 确保数据体校验码正确
        if (receiveBytesList.get(6 + dataBodyContentLength) != ByteTool.verifyCode(dataBodyContent)) {
            log().warn("数据体校验码不正确");
            for (int i = 0; i < 6 + dataBodyContentLength; i++) {
                receiveBytesList.remove(0);
            }
            return analysis(messageData, receiveBytesList);
        }
        messageData.setBytes(dataBodyContent);
        return messageData;
    }

}
