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

package cn.aberic.bother.service.impl;

import cn.aberic.bother.contract.exec.PublicContractExec;
import cn.aberic.bother.contract.system.PublicContract;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.response.IResponse;
import cn.aberic.bother.entity.response.Response;
import cn.aberic.bother.service.BlockService;
import cn.aberic.bother.service.ContractService;
import cn.aberic.bother.tools.thread.ThreadTroublePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 作者：Aberic on 2018/09/12 12:11
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Service("contractService")
public class ContractServiceImpl implements ContractService {

    @Override
    public String invoke(Request request, BlockService blockService) {
        PublicContract contract = new PublicContract();
        PublicContractExec exec = new PublicContractExec();
        exec.setRequest(request);
        Response response = contract.invoke(exec);
        if (response.isSend()) {
            Block block = blockService.checkBlockVerify(exec);
            if (null != block) {
                new ThreadTroublePool().submit(() -> exec.sendTransaction(blockService.checkBlockVerify(exec)));
            } else {
                exec.response(IResponse.ResponseType.FAIL);
            }
        }
        return response.getResultResponse();
    }

    @Override
    public String query(Request request) {
        PublicContract contract = new PublicContract();
        PublicContractExec contractExec = new PublicContractExec();
        contractExec.setRequest(request);
        return contract.query(contractExec).getResultResponse();
    }

}
