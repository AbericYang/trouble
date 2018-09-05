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

package cn.aberic.bother.token.exec.service;

import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.token.runnable.RunnableSearchToken;
import cn.aberic.bother.tools.ITimeOut;
import cn.aberic.bother.tools.thread.ThreadTroublePool;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Token 文件本地读写接口
 * <p>
 * 作者：Aberic on 2018/9/3 20:45
 * 邮箱：abericyang@gmail.com
 */
public interface ITokenExec extends IFile<Token>, ITimeOut {

    /**
     * 创建或更新 Token 信息
     *
     * @param tokenStr {@link Token} 字符串
     */
    default void createOrUpdate(String tokenStr) {
        cou(tokenStr);
    }

    /**
     * 根据 Token hash 获取 Token 对象
     *
     * @param tokenHash Token hash
     *
     * @return Token 对象
     */
    default Token getByHash(String tokenHash) {
        Token[] tokens = new Token[]{null};
        int fileCount = getFileCount();
        AtomicInteger count = new AtomicInteger(0);
        ThreadTroublePool troublePool = new ThreadTroublePool();
        boolean found = false;
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        for (File file : files) {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                troublePool.submit(new RunnableSearchToken(tokenHash, file, getNumByFileName(file.getName()), token -> {
                    if (null != token) {
                        tokens[0] = token;
                        troublePool.shutdown();
                    }
                    count.getAndIncrement();
                }));
            }
            if (null != tokens[0]) {
                break;
            }
        }
        long time = new Date().getTime();
        while (!found) {
            found = checkTimeOut(fileCount, count, tokens[0], time, 300000);
        }
        troublePool.shutdown();
        return tokens[0];
    }

}
