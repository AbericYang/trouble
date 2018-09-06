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

/**
 * 带 Token 的智能合约常规操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/09/06 13:55
 * 邮箱：abericyang@gmail.com
 */
public interface ITokenContract {

    /**
     * 智能合约初始化方法，相同版本合约只能初始化一次，重复初始化无效。
     * 初始化操作执行完成后会返回当前智能合约唯一hash，该hash值是用于提供给其它节点安装本合约使用。
     *
     * @param exec       智能合约区块操作接口
     * @param erc20Token ERC20 接口
     * @return 智能合约唯一hash
     */
    String init(IContractExec exec, IERC20Token erc20Token);

    /**
     * 执行智能合约
     *
     * @param exec       智能合约区块操作接口
     * @param erc20Token ERC20 接口
     * @return 执行结果
     */
    String invoke(IContractExec exec, IERC20Token erc20Token);

    /**
     * 查询智能合约
     *
     * @param exec       智能合约区块操作接口
     * @param erc20Token ERC20 接口
     * @return 查询结果
     */
    String query(IContractExec exec, IERC20Token erc20Token);

}
