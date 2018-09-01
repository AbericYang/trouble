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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 作者：Aberic on 2018/9/1 17:10
 * 邮箱：abericyang@gmail.com
 */
public class CallableSearchContractKeyIndexList implements Callable<List<String>> {

    private IContractDataFileExec contractDataFileExec;
    private String key;
    private File file;

    public CallableSearchContractKeyIndexList(IContractDataFileExec contractDataFileExec, String key, File file) {
        this.contractDataFileExec = contractDataFileExec;
        this.key = key;
        this.file = file;
    }

    @Override
    public List<String> call() {
        List<String> list = new ArrayList<>();
        try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
            while (it.hasNext()) {
                String lineString = it.nextLine();
                ContractInfo contractInfo = JSON.parseObject(lineString, new TypeReference<ContractInfo>() {});
                if (null != contractInfo && StringUtils.equalsIgnoreCase(contractInfo.getKey(), key)) {
                    list.add(contractDataFileExec.getByNumAndLine(contractInfo.getNum(), contractInfo.getLine()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
