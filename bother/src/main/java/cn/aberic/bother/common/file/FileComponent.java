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

package cn.aberic.bother.common.file;

import cn.aberic.bother.common.Common;
import cn.aberic.bother.core.dm.entity.Block;
import cn.aberic.bother.core.dm.entity.BlockInfo;
import cn.aberic.bother.core.sc.entity.Contract;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 区块及区块索引文件可操作组件——公共方法包
 * <p>
 * 作者：Aberic on 2018/08/27 14:31
 * 邮箱：abericyang@gmail.com
 */
@Getter
public class FileComponent {

    @Getter
    public enum TType {

        /** 区块对象类型 */
        T_TYPE_BLOCK(Block.class),
        /** 区块索引对象类型 */
        T_TYPE_BLOCK_INDEX(BlockInfo.class),
        /** 智能合约对象类型 */
        T_TYPE_CONTRACT(Contract .class);

        /** 交易结果码 */
        private Class aClass;

        /**
         * 当前交易状态
         *
         * @param aClass 交易结果码
         */
        TType(Class aClass) {
            this.aClass = aClass;
        }
    }

    /** 文件固定格式开头字符串 */
    private String start;
    /** 文件固定格式结尾字符串 */
    private String end;
    /** 文件所存储目录 */
    private String dir;
    /** JSON格式化解析类型枚举 */
    private TType tType;

    /**
     * 当前文件组件
     *
     * @param start 文件固定格式开头字符串
     * @param end   文件固定格式结尾字符串
     * @param dir   文件所存储目录
     */
    private FileComponent(String start, String end, String dir, TType tType) {
        this.start = start;
        this.end = end;
        this.dir = dir;
        this.tType = tType;
    }

    /** 获取默认区块文件可操作状态 */
    public static FileComponent getBlockFileComponentDefault() {
        return new FileComponent(
                Common.BLOCK_FILE_START,
                Common.BLOCK_FILE_END,
                Common.BLOCK_FILE_DIR,
                TType.T_TYPE_BLOCK);
    }

    /** 获取默认区块索引文件可操作状态 */
    public static FileComponent getBlockIndexFileComponentDefault() {
        return new FileComponent(
                Common.BLOCK_INDEX_START,
                Common.BLOCK_INDEX_END,
                Common.BLOCK_INDEX_DIR,
                TType.T_TYPE_BLOCK_INDEX);
    }

    /** 获取默认区块索引文件可操作状态 */
    public static FileComponent getBlockTransactionIndexFileComponentDefault() {
        return new FileComponent(
                Common.BLOCK_TRANSACTION_INDEX_START,
                Common.BLOCK_TRANSACTION_INDEX_END,
                Common.BLOCK_TRANSACTION_INDEX_DIR,
                TType.T_TYPE_BLOCK_INDEX);
    }

    /** 获取默认智能合约文件可操作状态 */
    public static FileComponent getContractFileComponentDefault() {
        return new FileComponent(
                Common.CONTRACT_FILE_START,
                Common.CONTRACT_FILE_END,
                Common.CONTRACT_FILE_DIR,
                TType.T_TYPE_CONTRACT);
    }

    /**
     * 获取指定智能合约hash的区块文件可操作状态
     *
     * @param contractHash 智能合约hash
     */
    public static FileComponent getBlockFileComponent(String contractHash) {
        if (StringUtils.isEmpty(contractHash)) {
            throw new NullPointerException("smart contract hash can't be empty");
        }
        return new FileComponent(
                Common.BLOCK_FILE_START,
                Common.BLOCK_FILE_END,
                getCustomDirByContractHash(Common.BLOCK_FILE_CUSTOM_DIR, contractHash),
                TType.T_TYPE_BLOCK);
    }

    /**
     * 获取指定智能合约hash的区块索引文件可操作状态
     *
     * @param contractHash 智能合约hash
     */
    public static FileComponent getBlockIndexFileComponent(String contractHash) {
        if (StringUtils.isEmpty(contractHash)) {
            throw new NullPointerException("smart contract hash can't be empty");
        }
        return new FileComponent(
                Common.BLOCK_INDEX_START,
                Common.BLOCK_INDEX_END,
                getCustomDirByContractHash(Common.BLOCK_INDEX_CUSTOM_DIR, contractHash),
                TType.T_TYPE_BLOCK_INDEX);
    }

    /**
     * 获取指定智能合约hash的区块索引文件可操作状态
     *
     * @param contractHash 智能合约hash
     */
    public static FileComponent getBlockTransactionIndexFileComponent(String contractHash) {
        if (StringUtils.isEmpty(contractHash)) {
            throw new NullPointerException("smart contract hash can't be empty");
        }
        return new FileComponent(
                Common.BLOCK_TRANSACTION_INDEX_START,
                Common.BLOCK_TRANSACTION_INDEX_END,
                getCustomDirByContractHash(Common.BLOCK_TRANSACTION_INDEX_CUSTOM_DIR, contractHash),
                TType.T_TYPE_BLOCK_INDEX);
    }

    /**
     * 获取指定智能合约hash的智能合约文件可操作状态
     *
     * @param contractHash 智能合约hash
     */
    public static FileComponent getContractFileComponent(String contractHash) {
        if (StringUtils.isEmpty(contractHash)) {
            throw new NullPointerException("smart contract hash can't be empty");
        }
        return new FileComponent(
                Common.CONTRACT_FILE_START,
                Common.CONTRACT_FILE_END,
                getCustomDirByContractHash(Common.CONTRACT_FILE_CUSTOM_DIR, contractHash),
                TType.T_TYPE_CONTRACT);
    }

    /**
     * 根据待操作文件固定目录及智能合约hash得到当前文件操作目录
     *
     * @param dir          文件固定目录
     * @param contractHash 智能合约hash
     *
     * @return 当前文件操作目录
     */
    private static String getCustomDirByContractHash(String dir, String contractHash) {
        return String.format("%s/%s", dir, contractHash);
    }

}
