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

package cn.aberic.bother.account.exec.service;

import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.tools.DeflaterTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 账户文件本地读写接口
 * <p>
 * 作者：Aberic on 2018/9/3 19:37
 * 邮箱：abericyang@gmail.com
 */
public interface IAccountExec extends IFile<Account> {

    /** 创建或更新账户信息 */
    default void createOrUpdate(Account account) {
        cou(JSON.toJSONString(account), true);
    }

    /**
     * 根据账户地址获取账户对象
     *
     * @param address 账户地址
     * @return 账户对象
     */
    default Account getByAddress(String address) {
        return getAccount(address, getFileStatus().getDir());
    }

    /**
     * 根据公网账户地址获取公网账户对象
     *
     * @param address 公网账户地址
     * @return 公网账户对象
     */
    default Account getPubByAddress(String address) {
        return getAccount(address, Common.TOKEN_DEFAULT_SYSTEM_HASH);
    }


    /**
     * 根据账户地址及账户所属 Token 路径获取账户对象
     *
     * @param address  账户地址
     * @param filePath 账户所属 Token 路径
     * @return 账户对象
     */
    default Account getAccount(String address, String filePath) {
        Account[] accounts = new Account[]{null};
        Files.fileTraverser().breadthFirst(new File(filePath)).forEach(file -> {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
                    while (it.hasNext()) {
                        String lineString = it.nextLine();
                        if (StringUtils.isEmpty(lineString)) {
                            continue;
                        }
                        Account account = JSON.parseObject(DeflaterTool.uncompress(lineString), new TypeReference<Account>() {});
                        if (StringUtils.equals(account.getAddress(), address)) {
                            accounts[0] = account;
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return accounts[0];
    }

}
