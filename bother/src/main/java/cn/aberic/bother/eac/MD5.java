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

package cn.aberic.bother.eac;

import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

/**
 * MD5工具类
 * <p>
 * 作者：Aberic on 2018/08/29 10:44
 * 邮箱：abericyang@gmail.com
 */
public class MD5 {

    public static String md516(String str) {
        return md516(str, Charset.forName("UTF-8"));
    }

    public static String md516(String str, Charset charset) {
        return md532(str, charset).substring(8, 24);
    }

    public static String md532(String str) {
        return md532(str, Charset.forName("UTF-8"));
    }

    public static String md532(String str, Charset charset) {
        return Hashing.md5().hashString(str, charset).toString();
    }

}
