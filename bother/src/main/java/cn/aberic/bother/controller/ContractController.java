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

import cn.aberic.bother.contract.exec.PublicContractExec;
import cn.aberic.bother.contract.system.PublicContract;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.response.Response;
import cn.aberic.bother.tools.thread.ThreadTroublePool;
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
        PublicContract contract = new PublicContract();
        PublicContractExec exec = new PublicContractExec();
        exec.setRequest(request);
        Response response = contract.invoke(exec);
        if (response.isSend()) {
            new ThreadTroublePool().submit(exec::sendTransaction);
        }
        return response.getResultResponse();
    }

    @PostMapping(value = "query")
    public String query(@RequestBody Request request) {
        PublicContract contract = new PublicContract();
        PublicContractExec contractExec = new PublicContractExec();
        contractExec.setRequest(request);
        return contract.query(contractExec).getResultResponse();
    }

}
