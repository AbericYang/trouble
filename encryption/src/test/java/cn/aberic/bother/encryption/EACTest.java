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

package cn.aberic.bother.encryption;

import cn.aberic.bother.tools.SystemOut;
import com.google.common.hash.Hashing;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 作者：Aberic on 2018/08/29 10:29
 * 邮箱：abericyang@gmail.com
 */
public class EACTest {

    public static void main(String[] args) {
        md5();
    }

    private static void md5() {
        String str = "123456";
        try {
            SecretKey MD5_KEY = new SecretKeySpec("secret key".getBytes("UTF-8"), "HmacMD5");
            SystemOut.println("hmacMd5 -----> " + str + " = " + Hashing.hmacMd5(MD5_KEY).hashString(str, Charset.forName("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SystemOut.println("MD532   -----> " + str + " = " + MD5.md532(str));
        SystemOut.println("MD516   -----> " + str + " = " + MD5.md516(str));
    }

}
