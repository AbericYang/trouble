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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MsgPack 序列化工具，非对象类型由 MsgPack 来执行序列化，对象类型走 protobuf
 * <p>
 * 作者：Aberic on 2018/09/13 11:36
 * 邮箱：abericyang@gmail.com
 */
public class MsgPackTool {

    private static ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());

    /**
     * 字符串序列化byte数组
     *
     * @param str 字符串
     * @return byte数组
     */
    public static byte[] string2Bytes(String str) {
        try {
            return mapper.writeValueAsBytes(str);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * byte数组反序列化成字符串
     *
     * @param bytes byte数组
     * @return 字符串
     */
    public static String bytes2String(byte[] bytes) {
        try {
            return mapper.readValue(bytes, String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串集合序列化byte数组
     *
     * @param stringList 字符串集合
     * @return byte数组
     */
    public static byte[] list2Bytes(List<String> stringList) {
        try {
            return new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(stringList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * byte数组反序列化成字符串
     *
     * @param bytes byte数组
     * @return 字符串集合
     */
    public static List<String> bytes2List(byte[] bytes) {
        try {
            return mapper.readValue(bytes, mapper.getTypeFactory().constructParametricType(ArrayList.class, String.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
