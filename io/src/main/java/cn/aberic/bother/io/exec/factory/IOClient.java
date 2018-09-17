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

package cn.aberic.bother.io.exec.factory;

import cn.aberic.bother.entity.io.MessageData;

/**
 * IO 客户端操作接口
 * <p>
 * 作者：Aberic on 2018/9/10 00:53
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public interface IOClient extends IOExec {

    /***/
    void doConnect();

    /** 发送请求 */
    void send(MessageData msgData);

    /**
     * 是否关闭远程连接
     * <p>
     * 当{@link cn.aberic.bother.io.handler.EchoClientHandler}远程连接关闭或终止后，以此判断是否重新修复连接
     *
     * @return 与否
     */
    boolean isShutdown();

}
