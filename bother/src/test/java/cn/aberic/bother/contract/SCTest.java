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
import cn.aberic.bother.contract.exec.SystemContractExec;
import cn.aberic.bother.tools.exception.ContractParamException;
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
        // scanner();
    }

    private static void systemContract() throws ContractParamException {
        SystemContract contract = new SystemContract();
        log.debug("system contract invoke result = {}", contract.invoke(new SystemContractExec()));
        log.debug("system contract query  result = {}", contract.query(new SystemContractExec()));
        log.debug("system contract query  result = {}", contract.query(new SystemContractExec()));
    }

    private static void simpleContract() {
        SimpleContract contract = new SimpleContract();
        log.debug("simple contract test result = {}", contract.init(new ContractExec(new File(""), "")));
    }

    private static void scanner() {
        System.out.print("请输入:");
        String showInfo = new Scanner(System.in).next();
        System.out.println("\n*************\n");
        System.out.println(showInfo);
    }

}
