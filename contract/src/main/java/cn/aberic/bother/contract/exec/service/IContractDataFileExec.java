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

import cn.aberic.bother.block.exec.service.IInit;
import cn.aberic.bother.entity.contract.Contract;
import cn.aberic.bother.storage.Common;
import cn.aberic.bother.storage.FileComponent;
import cn.aberic.bother.storage.IFile;
import cn.aberic.bother.tools.VerifyTool;
import com.alibaba.fastjson.JSONObject;

import java.io.File;

/**
 * 系统级智能合约数据文件对象操作接口-smart contract
 * <p>
 * 作者：Aberic on 2018/08/31 11:28
 * 邮箱：abericyang@gmail.com
 */
public interface IContractDataFileExec extends IInit, IFile<Contract> {

    /**
     * 将根据旧版hash所指定智能合约数据文件夹重命名为新版hash
     *
     * @param contractOldHash 旧版hash
     */
    default void renameContractFile(String contractOldHash) {
        // 旧版hash所在文件夹路径
        File file = new File(FileComponent.getContractDataFileComponent(contractOldHash).getDir());
        // 将原文件夹更改为新版hash
        file.renameTo(new File(getFileStatus().getDir()));
    }

    /**
     * 根据传入字符串 {@param value} 存入一个key为 {@param key} 的json。
     * <p>
     * 如果 {@param forcedString} 不为true
     * <p>
     * 那么 {@param value} 会被验证是否为json对象或json array对象
     * <p>
     * 如果是json对象，则转成json对象后作为json value的值
     * <p>
     * 如果是json array对象，则转成json array对象后作为json value的值
     * <p>
     * 如果不属于json类型，则直接将此字符串作为json value的值。
     * <p>
     * 如果 {@param forcedString} 为true
     * <p>
     * 则直接将 {@param value} 作为json value的值。
     *
     * @param key   json key
     * @param value 传入字符串
     * @param force 是否强制将value不做处理直接存入
     */
    default void put(String key, String value, boolean force) {
        JSONObject jsonObject;
        if (!force) {
            jsonObject = VerifyTool.verifyJSON(key, value);
        } else {
            jsonObject = new JSONObject();
            jsonObject.put(key, value);
        }

    }

    /**
     * 根据传入对象 {@param obj} 存入一个key为 {@param key} 的json。
     *
     * @param key json key
     * @param obj 传入字符串
     */
    default void put(String key, Object obj) {
    }

}