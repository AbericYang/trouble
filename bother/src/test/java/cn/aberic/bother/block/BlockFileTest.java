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

package cn.aberic.bother.block;

import cn.aberic.bother.entity.EntityTest;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.DeflaterTool;
import cn.aberic.bother.tools.FileTool;
import cn.aberic.bother.tools.exception.SearchDataNotFoundException;
import cn.aberic.bother.tools.exception.SearchDataTimeoutException;
import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * 作者：Aberic on 2018/08/24 12:24
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class BlockFileTest {

    public static void main(String[] args) throws IOException {
//        writeBlock();
//        blockTest();
        fileSize();
    }

    private static void blockTest() throws SearchDataNotFoundException, SearchDataTimeoutException {
        log.debug("================= entity file test start =================");
        BlockAcquire acquire = new BlockAcquire(Constant.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);

        long time = System.currentTimeMillis();
        int count = acquire.getFileCount();
        log.debug("处理时长 = {} | getBlockFileCount = {}", (System.currentTimeMillis() - time), count);


        int height = acquire.getHeight();
        log.debug("处理时长 = {} | getHeight = {}", (System.currentTimeMillis() - time), height);

        time = System.currentTimeMillis();
        Block block = acquire.getBlockByHeight(45848);
        log.debug("处理时长 = {} | getBlockByHeight = {}", (System.currentTimeMillis() - time), block.toJsonString());
//
//        time = System.currentTimeMillis();
//        block = acquire.getBlockByHash("8552d5d383d84f3aa34338fb1edd2542e8d031111a46c8b55d6b0744c9a503e1");
//        log.debug("处理时长 = {} | getBlockByHash = {}", (System.currentTimeMillis() - time), block.toJsonString());
//
//        time = System.currentTimeMillis();
//        block = acquire.getBlockByTransactionHash("c3f0b966bcce948f3059129e8f524426c81002c683091e0f7028c8109c4454d5");
//        log.debug("处理时长 = {} | getBlockByTransactionHash = {}", (System.currentTimeMillis() - time), block.toJsonString());
//
//        log.debug("================= entity file test end =================");
    }

    private static void writeBlock() {
        BlockStorage blockStorage = new BlockStorage(Constant.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH);
        for (int blockCount = 45849; blockCount < 5000000; blockCount++) {
            log.debug("================= blockCount = {} =================", blockCount);
            blockStorage.sync(EntityTest.createBlock(blockCount));
        }
    }

    private static void fileSize() throws IOException {
        String fileJsonPath = "/Users/Aberic/Documents/tmp/file/json.data";
        String fileProtoPath = "/Users/Aberic/Documents/tmp/file/proto.data";
        Block block = EntityTest.createBlock(10000);
        FileTool.createFirstFile(fileJsonPath);
        FileTool.createFirstFile(fileProtoPath);
        FileTool.writeFirstLine(new File(fileJsonPath), DeflaterTool.compress(JSON.toJSONString(block)));
        Files.write(EntityTest.getBlockBytes(), new File(fileProtoPath));
    }

}
