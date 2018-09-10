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

import cn.aberic.bother.entity.account.AccountUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：Aberic on 2018/9/10 20:45
 * 邮箱：abericyang@gmail.com
 */
public class MsgPackTest {


    public static void main(String[] args) {
        System.out.println("=====对象转换bytes=====");
        AccountUser person = new AccountUser("Jecced", "18", "true");
        byte[] bytes = MsgPackTool.toBytes(person);
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));

        System.out.println("=====bytes转对象=====");
        AccountUser value = MsgPackTool.toObject(bytes, AccountUser.class);
        System.out.println(value);

        System.out.println("=====集合转换bytes=====");
        List<AccountUser> list = new ArrayList<>();
        list.add(new AccountUser("Jecced", "18", "true"));
        list.add(new AccountUser("Ruby", "3", "false"));
        bytes = MsgPackTool.toBytes(list);
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));

        System.out.println("=====bytes转换集合=====");
        List<AccountUser> list2 = MsgPackTool.toList(bytes, AccountUser.class);
        System.out.println(list2);
    }

}
