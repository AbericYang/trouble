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

package cn.aberic.bother.io.client;

import cn.aberic.bother.entity.EntityTest;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.io.Remote;
import cn.aberic.bother.entity.proto.BlockProto;
import cn.aberic.bother.io.IOContext;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

/**
 * 作者：Aberic on 2018/9/9 19:42
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class ClientTest {

    public static void main(String[] args) throws Exception {

        Remote remote = new Remote();
        remote.setAddress("127.0.0.1");
        remote.setPort(63715);
        IOContext.obtain().startClient(remote);

        MessageData msgData = new MessageData((byte) 0x79, EntityTest.getBlockBytes());
        IOContext.obtain().sendByIOClient("127.0.0.1", msgData);
    }

}
