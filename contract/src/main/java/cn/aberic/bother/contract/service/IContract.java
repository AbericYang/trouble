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

package cn.aberic.bother.contract.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 智能合约常规操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/08/29 16:40
 * 邮箱：abericyang@gmail.com
 */
public interface IContract {

    int SUCCESS = 200;

    /**
     * 智能合约初始化方法，相同版本合约只能初始化一次，重复初始化无效。
     * 初始化操作执行完成后会返回当前智能合约唯一hash，该hash值是用于提供给其它节点安装本合约使用。
     *
     * @return 智能合约唯一hash
     */
    String init(IContractBlockExec exec);

    String invoke(IContractBlockExec exec);

    String query(IContractBlockExec exec);

    default String response(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS);
        jsonObject.put("data", result);
        return jsonObject.toString();
    }

    default String response(int result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS);
        jsonObject.put("data", result);
        return jsonObject.toString();
    }

    default String response(Object obj) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS);
        jsonObject.put("data", obj);
        return jsonObject.toString();
    }



}
