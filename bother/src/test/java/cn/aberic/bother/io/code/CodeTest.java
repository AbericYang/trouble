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

package cn.aberic.bother.io.code;

import cn.aberic.bother.entity.EntityTest;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.io.Remote;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * 作者：Aberic on 2018/09/11 10:51
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class CodeTest {

    public static void main(String[] args) {

        Remote remote = new Remote();
        remote.setAddress("1");
        remote.setTimeOut(2);

        TroubleCodeTest codeTest = new TroubleCodeTest();
        byte[] bytes = codeTest.get(EntityTest.createBlock(10));
        List<Byte> receiveBytesList = new LinkedList<>();
        for (byte b : bytes) {
            // 将接收到的字节流加入接收队列
            receiveBytesList.add(b);
        }
        MessageData messageData = new MessageData();
        // messageData.setProtocolId((byte) 0x02);
        messageData = codeTest.parse(messageData, receiveBytesList);
        log.debug(codeTest.parse(messageData, receiveBytesList).toString());
        // Block block = messageData.getObject(Block.class);
        // log.debug(block.toString());
    }

    public static class TroubleCodeTest implements TroubleCode {

        @Override
        public Logger log() {
            return log;
        }

        public byte[] get(Block block) {
            MessageData messageData = new MessageData(ProtocolStatus.BLOCK_OUT, EntityTest.getBlockOutBytes());
            messageData.setDataId(createDataId());
            return createData(messageData.getBytes());
        }

        public MessageData parse(MessageData messageData, List<Byte> receiveBytesList) {
            return analysis(messageData, receiveBytesList);
        }

    }
}
