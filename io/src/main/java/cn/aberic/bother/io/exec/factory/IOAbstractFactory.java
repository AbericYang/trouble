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

import cn.aberic.bother.entity.io.Remote;
import cn.aberic.bother.io.IOContext;

/**
 * IO 对象获取抽象工厂
 * <p>
 * 作者：Aberic on 2018/9/10 00:55
 * 邮箱：abericyang@gmail.com
 */
public abstract class IOAbstractFactory implements IOFactory {

    protected abstract IOClient createClient(Remote address) throws Exception;

    @Override
    public IOExec getOrCreate(Remote remote) throws Exception {
        if (isClient()) {
            IOClient ioClient = IOContext.obtain().ioClientGet(remote.getAddress());
            if (null == ioClient) {
                ioClient = createClient(remote);
            }
            return ioClient;
        } else {
            IOServer ioServer = IOContext.obtain().ioServerGet(remote.getAddress());
            if (null == ioServer) {
                createClient(remote);
            }
            return null;
        }
    }
}
