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
    public final static String TOKEN_DEFAULT_SYSTEM_HASH = "67539efa4534b4214d3bba07e2b4d4b3812d9fe25c3f014e04aeb9fa604b418e";

    /** 默认区块文件前缀 */
    public final static String BLOCK_FILE_START = "block_file_";
    /** 默认区块文件后缀 */
    public final static String BLOCK_FILE_END = ".block";
    /** 默认区块文件存储路径 测试/生产 */
    public final static String BLOCK_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/block/bf";
    // public final static String BLOCK_FILE_DIR = "/data/trouble/block/bf";
    /** 自定义区块文件存储路径 测试/生产 */
    public final static String BLOCK_FILE_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/block/bfs";
    // public final static String BLOCK_FILE_CUSTOM_DIR = "/data/trouble/block/bfs";

    /** 默认区块存储索引文件前缀 */
    public final static String BLOCK_INDEX_START = "block_index_file_";
    /** 默认区块存储索引文件后缀 */
    public final static String BLOCK_INDEX_END = ".index";
    /** 默认区块存储索引文件路径 测试/生产 */
    public final static String BLOCK_INDEX_DIR = "/Users/Aberic/Documents/tmp/trouble/block/bif";
    // public final static String BLOCK_INDEX_DIR = "/data/trouble/block/bif";
    /** 自定义区块存储索引文件路径 测试/生产 */
    public final static String BLOCK_INDEX_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/block/bifs";
    // public final static String BLOCK_INDEX_CUSTOM_DIR = "/data/trouble/block/bifs";

    /** 默认区块交易索引文件前缀 */
    public final static String BLOCK_TRANSACTION_INDEX_START = "block_transaction_index_file_";
    /** 默认区块交易索引文件后缀 */
    public final static String BLOCK_TRANSACTION_INDEX_END = ".index";
    /** 默认区块交易索引文件路径 测试/生产 */
    public final static String BLOCK_TRANSACTION_INDEX_DIR = "/Users/Aberic/Documents/tmp/trouble/block/btif";
    // public final static String BLOCK_TRANSACTION_INDEX_DIR = "/data/trouble/block/btif";
    /** 自定义区块交易索引文件路径 测试/生产 */
    public final static String BLOCK_TRANSACTION_INDEX_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/block/btifs";
    // public final static String BLOCK_TRANSACTION_INDEX_CUSTOM_DIR = "/data/trouble/btifs";

    /** 默认合约文件前缀 */
    public final static String CONTRACT_FILE_START = "contract_file_";
    /** 默认合约文件后缀 */
    public final static String CONTRACT_FILE_END = ".contract";
    /** 默认合约文件存储路径 测试/生产 */
    public final static String CONTRACT_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/block/cf";
    // public final static String CONTRACT_FILE_DIR = "/data/trouble/cf";
    /** 自定义合约文件存储路径 测试/生产 */
    public final static String CONTRACT_FILE_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/block/cfs";
    // public final static String CONTRACT_FILE_CUSTOM_DIR = "/data/trouble/cfs";

    /** 默认合约数据索引文件前缀 */
    public final static String CONTRACT_DATA_INDEX_FILE_START = "data_index_file_";
    /** 默认合约数据索引文件后缀 */
    public final static String CONTRACT_DATA_INDEX_FILE_END = ".data";
    /** 默认合约数据索引文件存储路径 测试/生产 */
    public final static String CONTRACT_DATA_INDEX_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/block/cdif";
    // public final static String CONTRACT_DATA_INDEX_FILE_DIR = "/data/trouble/cdif";
    /** 自定义合约数据索引文件存储路径 测试/生产 */
    public final static String CONTRACT_DATA_INDEX_FILE_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/block/cdifs";
    // public final static String CONTRACT_DATA_INDEX_FILE_CUSTOM_DIR = "/data/trouble/cdifs";

    /** 默认LevelDB合约数据文件存储路径 测试/生产 */
    public final static String CONTRACT_DATA_LEVEL_DB_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/db/cdfleveldb";
    // public final static String CONTRACT_DATA_INDEX_FILE_DIR = "/data/trouble/cdfleveldb";
    /** 自定义LevelDB合约数据文件存储路径 测试/生产 */
    public final static String CONTRACT_DATA_LEVEL_DB_FILE_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/db/cdfsleveldb";
    // public final static String CONTRACT_DATA_INDEX_FILE_CUSTOM_DIR = "/data/trouble/cdfsleveldb";

    /** 默认RocksDB合约数据文件存储路径 测试/生产 */
    public final static String CONTRACT_DATA_ROCKS_DB_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/db/cdfrocksdb";
    // public final static String CONTRACT_DATA_INDEX_FILE_DIR = "/data/trouble/cdflevecdfrocksdbldb";
    /** 自定义RocksDB合约数据文件存储路径 测试/生产 */
    public final static String CONTRACT_DATA_ROCKS_DB_FILE_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/db/cdfsrocksdb";
    // public final static String CONTRACT_DATA_INDEX_FILE_CUSTOM_DIR = "/data/trouble/cdfsrocksdb";

    /** 默认账户文件前缀 */
    public final static String ACCOUNT_FILE_START = "account_file_";
    /** 默认账户文件后缀 */
    public final static String ACCOUNT_FILE_END = ".account";
    /** 默认账户文件存储路径 测试/生产 */
    public final static String ACCOUNT_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/af";
    // public final static String ACCOUNT_FILE_DIR = "/data/trouble/af";
    /** 自定义账户文件存储路径 测试/生产 */
    public final static String ACCOUNT_FILE_CUSTOM_DIR = "/Users/Aberic/Documents/tmp/trouble/afs";
    // public final static String ACCOUNT_FILE_CUSTOM_DIR = "/data/trouble/afs";

    /** 默认账户文件前缀 */
    public final static String TOKEN_FILE_START = "token_file_";
    /** 默认账户文件后缀 */
    public final static String TOKEN_FILE_END = ".token";
    /** 默认账户文件存储路径 测试/生产 */
    public final static String TOKEN_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/tf";
    // public final static String TOKEN_FILE_DIR = "/data/trouble/tf";
    /** 默认账户文件临时存储路径 测试/生产 */
    public final static String TOKEN_TMP_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/tftmp";
    // public final static String TOKEN_FILE_DIR = "/data/trouble/tf";


    /** Key键值对保存路径 测试/生产 */
    public final static String KEY_FILE_DIR = "/Users/Aberic/Documents/tmp/trouble/key";
    // public final static String KEY_FILE_DIR = "/data/trouble/key";

}
