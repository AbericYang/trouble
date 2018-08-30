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

package cn.aberic.bother.block.exec;

import cn.aberic.bother.block.exec.service.IBlockExec;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.storage.FileComponent;
import org.apache.commons.lang3.StringUtils;

/**
 * 区块文件本地读写——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/24 11:44
 * 邮箱：abericyang@gmail.com
 */
public class BlockExec extends Init implements IBlockExec {

    /**
     * 根据智能合约hash值操作区块文件；
     * 在智能合约被安装的时候就根据合约内容计算该合约hash；
     * 并以此hash匹配所有安装该合约的节点且同步数据
     *
     * @param contractHash 智能合约hash值
     */
    BlockExec(String contractHash) {
        super(contractHash);
    }

    @Override
    public FileComponent getFileStatus() {
        if (StringUtils.equals(getContractHash(), Common.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH)) {
            return FileComponent.getBlockFileComponentDefault();
        }
        return FileComponent.getBlockFileComponent(getContractHash());
    }

}
