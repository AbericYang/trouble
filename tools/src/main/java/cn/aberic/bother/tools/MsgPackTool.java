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

package cn.aberic.bother.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Aberic on 2018/9/10 19:15
 * 邮箱：abericyang@gmail.com
 */
public class MsgPackTool {

    private static ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());

    public static <T> byte[] toBytes(T obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(byte[] bytes, Class<T> clazz) {
        T value = null;
        try {
            value = mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static <T> JavaType list(Class<?> clazz) {
        return mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
    }

    public static <T> List<T> toList(byte[] bytes, Class<T> clazz) {
        List<T> list = null;
        try {
            list = mapper.readValue(bytes, list(clazz));
        } catch (IOException e) {
            list = new ArrayList<>();
            e.printStackTrace();
        }
        return list;
    }

}
