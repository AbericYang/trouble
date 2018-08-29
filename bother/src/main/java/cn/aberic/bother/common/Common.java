/*
 * MIT License
 *
 * Copyright (c) 2018. Aberic Yang
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

package cn.aberic.bother.common;

/**
 * 公共方法包
 * <p>
 * 作者：Aberic on 2018/8/20 20:24
 * 邮箱：abericyang@gmail.com
 */
public class Common {

    /** RocksDB存储路径 测试/生产 */
    public final static String ROCKS_DB_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/rocksdb";
    // public final static String ROCKS_DB_FILE_DIR = "/data/trouble/rocksdb";

    public final static String BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH = "default_system_contract_hash";

    /** 默认区块文件前缀 */
    public final static String BLOCK_FILE_START = "block_file_";
    /** 默认区块文件后缀 */
    public final static String BLOCK_FILE_END = ".block";
    /** 默认区块文件存储路径 测试/生产 */
    public final static String BLOCK_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/new/bf";
    // public final static String BLOCK_FILE_DIR = "/data/trouble/blockfile";
    /** 自定义合约区块文件存储路径 测试/生产 */
    public final static String BLOCK_FILE_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/new/bfs";
    // public final static String BLOCK_FILE_CUSTOM_DIR = "/data/trouble/blockfile";

    /** 默认区块索引文件前缀 */
    public final static String BLOCK_INDEX_START = "block_index_file_";
    /** 默认区块索引文件后缀 */
    public final static String BLOCK_INDEX_END = ".index";
    /** 默认区块文件存储索引路径 测试/生产 */
    public final static String BLOCK_INDEX_DIR = "/Users/Aberic/Documents/tmp/trouble/new/bif";
    // public final static String BLOCK_INDEX_DIR = "/data/trouble/blocktxindex";
    /** 自定义合约区块文件存储索引路径 测试/生产 */
    public final static String BLOCK_INDEX_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/new/bifs";
    // public final static String BLOCK_INDEX_CUSTOM_DIR = "/data/trouble/blocktxindex";

    /** 默认区块交易索引文件前缀 */
    public final static String BLOCK_TRANSACTION_INDEX_START = "block_transaction_index_file_";
    /** 默认区块交易索引文件后缀 */
    public final static String BLOCK_TRANSACTION_INDEX_END = ".index";
    /** 默认区块文件中交易存储索引路径 测试/生产 */
    public final static String BLOCK_TRANSACTION_INDEX_DIR = "/Users/Aberic/Documents/tmp/trouble/new/btif";
    // public final static String BLOCK_INDEX_DIR = "/data/trouble/btif";
    /** 自定义合约区块文件中交易存储索引路径 测试/生产 */
    public final static String BLOCK_TRANSACTION_INDEX_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/new/btifs";
    // public final static String BLOCK_INDEX_CUSTOM_DIR = "/data/trouble/btifs";

}
