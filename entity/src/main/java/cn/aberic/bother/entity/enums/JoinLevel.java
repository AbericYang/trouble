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

package cn.aberic.bother.entity.enums;

import lombok.Getter;

/**
 * 请求加入节点当前请求级别
 * <p>
 * 作者：Aberic on 2018/9/13 20:22
 * 邮箱：abericyang@gmail.com
 */
@Getter
public enum JoinLevel {

    /** 孤岛 */
    LONEY("孤岛", 0),
    /** 楼 */
    TOWER("楼", 1),
    /** 社区 */
    COMMUNITY("社区", 2),
    /** 县城 */
    COUNTY("县城", 3),
    /** 市 */
    CITY("市", 4),
    /** 省 */
    PROVINCE("省", 5);

    /** 别名描述 */
    private String alias;
    /** 级别码 */
    private int level;

    /**
     * 当前交易状态
     *
     * @param alias 别名描述
     * @param level 级别码
     */
    JoinLevel(String alias, int level) {
        this.alias = alias;
        this.level = level;
    }

}
