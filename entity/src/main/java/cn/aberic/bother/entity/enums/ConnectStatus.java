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
 * 当前连接状态
 * <p>
 * 作者：Aberic on 2018/9/12 21:16
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Getter
public enum ConnectStatus {

    /** 未连接任何圈子 */
    NONE("未连接任何圈子", 0),
    /** 圈子中的Leader节点 */
    LEADER("圈子中的Leader节点", 1),
    /** 圈子中的Follow节点 */
    FOLLOW("圈子中的Follow节点", 2),
    /** 当前圈子Leader节点选举中 */
    ELECTION("当前圈子Leader节点选举中", 3);

    /** 状态描述 */
    private String brief;
    /** 状态码 */
    private int code;

    /**
     * 当前共识状态
     *
     * @param brief 状态描述
     * @param code  状态码
     */
    ConnectStatus(String brief, int code) {
        this.brief = brief;
        this.code = code;
    }

}
