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

package cn.aberic.bother.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 作者：Aberic on 2018/9/17 22:20
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class MapListString {

    List<String> stringList;

    /**
     * 返回当前对象所属字符串集合长度
     *
     * @return 长度
     */
    public int size() {
        return stringList.size();
    }

    /** 新增节点 */
    public void add(String string) {
        stringList.add(string);
    }

    /** 批量新增 */
    public void addAll(List<String> strings) {
        stringList.addAll(strings);
    }

    /** 获取下标节点 */
    public String get(int index) {
        return stringList.get(index);
    }

    /** 移除下标节点 */
    public void remove(int index) {
        stringList.remove(index);
    }

    /** 移除节点对象 */
    public void remove(String string) {
        stringList.remove(string);
    }

    public List<String> subList(int fromIndex, int toIndex) {
        return stringList.subList(fromIndex, toIndex);
    }

}
