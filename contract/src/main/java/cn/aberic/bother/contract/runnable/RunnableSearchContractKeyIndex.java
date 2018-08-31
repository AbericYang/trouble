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

package cn.aberic.bother.contract.runnable;

import cn.aberic.bother.contract.exec.service.IContractDataFileExec;
import cn.aberic.bother.entity.contract.ContractInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 作者：Aberic on 2018/08/31 15:25
 * 邮箱：abericyang@gmail.com
 */
public class RunnableSearchContractKeyIndex implements Runnable {

    public interface SearchContractKeyIndexListener {
        void find(String string);
    }

    private IContractDataFileExec contractDataFileExec;
    private String key;
    private File file;
    private int contractDataIndexFile;
    private SearchContractKeyIndexListener listener;

    public RunnableSearchContractKeyIndex(IContractDataFileExec contractDataFileExec, String key, File file, int blockFileNum, SearchContractKeyIndexListener lintener) {
        this.contractDataFileExec = contractDataFileExec;
        this.key = key;
        this.file = file;
        this.contractDataIndexFile = blockFileNum;
        this.listener = lintener;
    }

    @Override
    public void run() {
        try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
            boolean found = false;
            while (it.hasNext()) {
                String lineString = it.nextLine();
                ContractInfo contractInfo = JSON.parseObject(lineString, new TypeReference<ContractInfo>() {});
                if (null != contractInfo && StringUtils.equalsIgnoreCase(contractInfo.getKey(), key)) {
                    System.out.println("找到file，contract-data-index-file-num = " + contractDataIndexFile);
                    found = true;
                    listener.find(contractDataFileExec.getByNumAndLine(contractInfo.getNum(), contractInfo.getLine()));
                    break;
                }
            }
            if (!found) {
                System.out.println("未找到file，contract-data-index-file-num = " + contractDataIndexFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
