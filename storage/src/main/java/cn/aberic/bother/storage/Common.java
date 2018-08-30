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

package cn.aberic.bother.storage;

/**
 * 常量——公共方法包
 * <p>
 * 作者：Aberic on 2018/8/20 20:24
 * 邮箱：abericyang@gmail.com
 */
public class Common {

    public final static String BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH = "d11d842aca8ee9b82ea1634791f98e83";

    /** 默认区块文件前缀 */
    public final static String BLOCK_FILE_START = "block_file_";
    /** 默认区块文件后缀 */
    public final static String BLOCK_FILE_END = ".block";
    /** 默认区块文件存储路径 测试/生产 */
    public final static String BLOCK_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/bf";
    // public final static String BLOCK_FILE_DIR = "/data/trouble/blockfile";
    /** 自定义区块文件存储路径 测试/生产 */
    public final static String BLOCK_FILE_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/bfs";
    // public final static String BLOCK_FILE_CUSTOM_DIR = "/data/trouble/blockfile";

    /** 默认区块存储索引文件前缀 */
    public final static String BLOCK_INDEX_START = "block_index_file_";
    /** 默认区块存储索引文件后缀 */
    public final static String BLOCK_INDEX_END = ".index";
    /** 默认区块存储索引文件路径 测试/生产 */
    public final static String BLOCK_INDEX_DIR = "/Users/Aberic/Documents/tmp/trouble/bif";
    // public final static String BLOCK_INDEX_DIR = "/data/trouble/blocktxindex";
    /** 自定义区块存储索引文件路径 测试/生产 */
    public final static String BLOCK_INDEX_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/bifs";
    // public final static String BLOCK_INDEX_CUSTOM_DIR = "/data/trouble/blocktxindex";

    /** 默认区块交易索引文件前缀 */
    public final static String BLOCK_TRANSACTION_INDEX_START = "block_transaction_index_file_";
    /** 默认区块交易索引文件后缀 */
    public final static String BLOCK_TRANSACTION_INDEX_END = ".index";
    /** 默认区块交易索引文件路径 测试/生产 */
    public final static String BLOCK_TRANSACTION_INDEX_DIR = "/Users/Aberic/Documents/tmp/trouble/btif";
    // public final static String BLOCK_INDEX_DIR = "/data/trouble/btif";
    /** 自定义区块交易索引文件路径 测试/生产 */
    public final static String BLOCK_TRANSACTION_INDEX_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/btifs";
    // public final static String BLOCK_INDEX_CUSTOM_DIR = "/data/trouble/btifs";

    /** 默认区块文件前缀 */
    public final static String CONTRACT_FILE_START = "contract_file_";
    /** 默认区块文件后缀 */
    public final static String CONTRACT_FILE_END = ".contract";
    /** 默认区块文件存储路径 测试/生产 */
    public final static String CONTRACT_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/c";
    // public final static String BLOCK_FILE_DIR = "/data/trouble/blockfile";
    /** 自定义区块文件存储路径 测试/生产 */
    public final static String CONTRACT_FILE_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/cs";
    // public final static String BLOCK_FILE_CUSTOM_DIR = "/data/trouble/blockfile";

}
