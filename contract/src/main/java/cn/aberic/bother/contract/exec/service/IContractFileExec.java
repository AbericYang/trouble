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

package cn.aberic.bother.contract.exec.service;

import cn.aberic.bother.entity.contract.Contract;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 智能合约文件对象操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/8/30 21:28
 * 邮箱：abericyang@gmail.com
 */
public interface IContractFileExec extends ISystemContractFileExec {

    /**
     * 安装或者升级智能合约。
     * <p>
     * 首先判断当前 {@link #getContractHash()} 是否为空，如果为空则表示当前操作需要实例化，该智能合约是被首次安装。
     * <p>
     * 安装智能合约需要获取上传路径，在
     * <p>
     * 如果 {@link #getContractHash()} 不为空，则表示直接安装并在安装完成后加入该智能合约创世节点。
     * <p>
     * 如果 {@link #getContractHash()} 不为空，还会判断本地合约是否有相同hash值得存在。
     * <p>
     * 如果有，则创建失败，如果没有，则返回当前合约的完整对象，包括提供给第三方进行安装部署的hash字段
     *
     * @param contract 智能合约
     *
     * @return 智能合约唯一hash
     */
    String init(Contract contract);

    /**
     * 获取当前智能合约对象
     *
     * @return 智能合约对象
     */
    default Contract getContract() {
        Contract[] contracts = new Contract[]{null};
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        for (File file : files) {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
                    while (it.hasNext()) {
                        String lineString = it.nextLine();
                        Contract lineContract = JSON.parseObject(lineString, new TypeReference<Contract>() {});
                        if (null == lineContract) {
                            continue;
                        }
                        if (StringUtils.equalsIgnoreCase(lineContract.getHash(), getContractHash())) {
                            contracts[0] = lineContract;
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != contracts[0]) {
                break;
            }
        }
        return contracts[0];
    }

}
