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

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：Aberic on 2018/09/13 11:50
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class MsgPackTest {

    public static void main(String[] args) throws IOException {
        log.debug("==============================================================================================================");
        log.debug("=====字符串转换bytes=====");
        String string = "字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes";
        log.debug("=====string====={}", string);
        byte[] bytes = MsgPackTool.string2Bytes(string);
        log.debug("=====bytes.length====={}", bytes.length);
        log.debug("=====Arrays.toString(bytes)====={}", Arrays.toString(bytes));

        log.debug("==============================================================================================================");
        log.debug("=====bytes转字符串=====");
        String string2 = MsgPackTool.bytes2String(bytes);
        log.debug("=====string2====={}", string2);

        log.debug("==============================================================================================================");
        log.debug("=====字符串集合转换bytes=====");
        List<String> list = new ArrayList<>();
        list.add("字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes字符串转换bytes");
        list.add("bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合bytes转换字符串集合");
        log.debug("=====list====={}", list);
        bytes = MsgPackTool.list2Bytes(list);
        log.debug("=====bytes.length====={}", bytes.length);
        log.debug("=====Arrays.toString(bytes)====={}", Arrays.toString(bytes));

        log.debug("==============================================================================================================");
        log.debug("=====bytes转换字符串集合=====");
        List<String> list2 = MsgPackTool.bytes2List(bytes);
        log.debug("=====list2====={}", list2);
    }

}
