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

package cn.aberic.bother.token;

import cn.aberic.bother.contract.exec.ERC20Token;
import cn.aberic.bother.contract.exec.SystemContractExec;
import cn.aberic.bother.contract.exec.service.ISystemContractExec;
import lombok.extern.slf4j.Slf4j;

/**
 * 作者：Aberic on 2018/9/5 22:12
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class TokenTest {

    public static void main(String[] args) {
        ERC20Token erc20Token = getERC20Token();
        log.debug("token name = {}", erc20Token.name());
        log.debug("token symbol = {}", erc20Token.symbol());
        log.debug("token decimals = {}", erc20Token.decimals());
        log.debug("token totalSupply = {}", erc20Token.totalSupply());
    }

    private static ERC20Token getERC20Token() {
        ISystemContractExec systemContractExec = new SystemContractExec();
        return new ERC20Token("3d14dadf5c6f37e94f67efde76591a1eea37c032b1b2025a5b8b9b3a167d6cf3", systemContractExec);
    }

}
