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

package cn.aberic.bother.token.runnable;

import cn.aberic.bother.entity.token.Token;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 作者：Aberic on 2018/9/5 21:28
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class RunnableSearchToken implements Runnable {

    public interface SearchTokenListener {
        void find(Token token);
    }

    private String tokenHash;
    private File file;
    private int tokenFileNum;
    private SearchTokenListener listener;

    public RunnableSearchToken(String tokenHash, File file, int tokenFileNum, SearchTokenListener listener) {
        this.tokenHash = tokenHash;
        this.file = file;
        this.tokenFileNum = tokenFileNum;
        this.listener = listener;

    }

    @Override
    public void run() {
        try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
            boolean found = false;
            while (it.hasNext()) {
                Token token = null;
                String lineString = it.nextLine();
                if (StringUtils.isNotEmpty(lineString)) {
                    token = JSON.parseObject(lineString, new TypeReference<Token>() {});
                }
                if (null != token && StringUtils.equalsIgnoreCase(token.getHash(), tokenHash)) {
                    log.debug("找到file，token-file-num = {}", tokenFileNum);
                    found = true;
                    listener.find(token);
                    break;
                }
            }
            if (!found) {
                listener.find(null);
                log.debug("未找到file，block-hash-index-file-num = {}", tokenFileNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
