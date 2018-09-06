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

package cn.aberic.bother.contract;

import cn.aberic.bother.contract.exec.ContractExec;
import cn.aberic.bother.contract.exec.PublicContractExec;
import cn.aberic.bother.contract.system.PublicContract;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.tools.exception.ContractParamException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Scanner;

/**
 * 作者：Aberic on 2018/8/29 19:57
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class SCTest {

    public static void main(String[] args) {
        systemContract();
//         simpleContract();
//        input();
        // scanner();
    }

    private static void systemContract() throws ContractParamException {
        PublicContract contract = new PublicContract();
        PublicContractExec contractExec = new PublicContractExec();
        Request request = new Request();
        request.setKey("test1");
        request.setValue("value1");
        contractExec.setRequest(request);
        log.debug("system contract invoke result = {}", contract.invoke(contractExec));
        log.debug("system contract query  result = {}", contract.query(contractExec));
    }

    private static void simpleContract() {
        SimpleContract contract = new SimpleContract();
        log.debug("simple contract test result = {}", contract.init(new ContractExec(new File(""), "")));
    }

    private static void input() {
        Request request = new Request();
        for (int i = 1; i < 1000000; i++) {
            request.setKey("key" + i);
            request.setValue("value" + i);
            request.setValue(JSON.toJSONString(request));
            PublicContract contract = new PublicContract();
            PublicContractExec contractExec = new PublicContractExec();
            contractExec.setRequest(request);
            String result = contract.invoke(contractExec);
            log.debug("result = {}", result);
            contractExec.sendTransaction();
        }
    }

    private static void scanner() {
        System.out.print("请输入:");
        String showInfo = new Scanner(System.in).next();
        System.out.println("\n*************\n");
        System.out.println(showInfo);
    }

}
