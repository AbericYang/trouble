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

package cn.aberic.bother.contract.exec.service;

import cn.aberic.bother.entity.contract.Contract;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.storage.IInit;

/**
 * 系统级智能合约文件对象操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/8/30 23:33
 * 邮箱：abericyang@gmail.com
 */
public interface IPublicContractFileExec extends IInit, IFile<Contract> {

    /**
     * 获取当前智能合约对象
     *
     * @return 智能合约对象
     */
    default Contract getContract() {
        Contract contract = new Contract("pub", "1.0", 1, "让连接更有价值！no trouble, no bother!");
        contract.setTimestamp(515260800000L);
        contract.setHash(Constant.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        contract.setDir("6c5ea876d5220135ee0c05d0f0840efe");
        return contract;
    }

}
