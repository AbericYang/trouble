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

package cn.aberic.bother.controller;

import cn.aberic.bother.contract.SystemContract;
import cn.aberic.bother.contract.exec.SystemContractExec;
import cn.aberic.bother.entity.contract.Request;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：Aberic on 2018/9/1 22:40
 * 邮箱：abericyang@gmail.com
 */
@CrossOrigin
@RestController
@RequestMapping("contract")
public class ContractController {

    @PostMapping(value = "invoke")
    public String invoke(@RequestBody Request request) {
        SystemContract contract = new SystemContract();
        SystemContractExec contractExec = new SystemContractExec();
        contractExec.setRequest(request);
        String result = contract.invoke(contractExec);
        contractExec.sendTransaction();
        return result;
    }

    @PostMapping(value = "query")
    public String query(@RequestBody Request request) {
        SystemContract contract = new SystemContract();
        SystemContractExec contractExec = new SystemContractExec();
        contractExec.setRequest(request);
        return contract.query(contractExec);
    }

}
