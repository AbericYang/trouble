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

package cn.aberic.bother.contract;

import cn.aberic.bother.contract.exec.service.ISystemContract;
import cn.aberic.bother.contract.exec.service.ISystemContractExec;
import cn.aberic.bother.entity.contract.Contract;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 系统应用智能合约-app support
 * <p>
 * 作者：Aberic on 2018/8/29 20:49
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class SystemContract implements ISystemContract {

    @Override
    public String invoke(ISystemContractExec exec) {
        exec.put("haha", "hehe4");
        Contract test = new Contract("Aberic", "1", 18, "2");
        exec.put("test", JSON.toJSONString(test));
        return exec.response(exec.getContract());
    }

    @Override
    public String query(ISystemContractExec exec) {
        log.debug("o = {}", exec.get("haha"));
        List<String> strings = exec.getHistory("haha");
        strings.forEach(o1 -> {
            log.debug("oh = {}", o1);
        });
        Contract test = JSON.parseObject(exec.get("test"), new TypeReference<Contract>() {});
        log.debug("test = {}", test.toJsonString());
        return "haha";
    }

}
