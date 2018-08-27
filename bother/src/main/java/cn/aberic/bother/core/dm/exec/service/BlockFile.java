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

package cn.aberic.bother.core.dm.exec.service;

import cn.aberic.bother.common.Common;
import cn.aberic.bother.common.ThreadTroublePool;
import cn.aberic.bother.core.dm.block.Block;
import cn.aberic.bother.core.dm.block.BlockInfo;
import cn.aberic.bother.core.dm.block.Transaction;
import cn.aberic.bother.core.dm.call.CallableSearchBlock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 区块文件本地读写——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/27 11:24
 * 邮箱：abericyang@gmail.com
 */
public interface BlockFile extends FileService<Block> {

    /**
     * 创建并存储区块文件，如已存在且大小超过24M，则覆盖，否则下一行追加更新
     *
     * @param block 区块对象
     */
    @Override
    default BlockInfo createOrUpdate(Block block) {
        BlockInfo blockInfo = new BlockInfo();
        int height = 0, line = 1;
        String currentDataHash;
        String jsonStringBlock;
        // 获取最新写入的区块文件
        File blockFile = getLastFile();
        try {
            // 如果最新写入的区块文件为null，则从0开始重新写入
            if (null == blockFile) {
                // 定义新的区块文件
                blockFile = createFirstFile();
                block.getHeader().setHeight(height); // 第一区块高度为0
                block.getHeader().setPreviousDataHash("0"); // 第一区块上一hash为0
                currentDataHash = block.calculateHash();
                block.getHeader().setCurrentDataHash(currentDataHash); // 第一区块当前hash
                jsonStringBlock = JSON.toJSONString(block);
                wirteFisrtLine(blockFile, jsonStringBlock);
            } else {
                // 获取当前区块文件中的总行数，其值即为上一区块的行数
                line = getFileLineCountIfBigCharLine(blockFile);
                // 获取上一区块
                Block preBlock = getFromFileByLine(blockFile, line);
                // 获取上一区块高度，计算并赋值当前区块高度
                height = preBlock.getHeader().getHeight() + 1;
                block.getHeader().setHeight(preBlock.getHeader().getHeight() + 1);
                // 为当前区块赋值上一区块hash
                block.getHeader().setPreviousDataHash(preBlock.getHeader().getCurrentDataHash());
                // 为当前区块赋值hash
                currentDataHash = block.calculateHash();
                block.getHeader().setCurrentDataHash(currentDataHash);
                // 重新生成待写入JSON String内容
                jsonStringBlock = JSON.toJSONString(block);
                // 计算该内容的字节长度
                long blockSize = jsonStringBlock.getBytes().length;
                // 如果区块文件和待写入对象之和已经大于或等于24MB，则开辟新区块文件写入区块对象
                if (blockFile.length() + blockSize >= 24 * 1000 * 1000) {
                    System.out.println(String.format("block file size great than 24MB, now size = %s", blockFile.length()));
                    blockFile = getNextFileByCurrentFile(blockFile);
                    System.out.println(String.format("next block file name = %s", blockFile.getName()));
                    wirteFisrtLine(blockFile, jsonStringBlock);
                } else {
                    wirteAppendLine(blockFile, jsonStringBlock);
                }
            }
            List<String> transactionHashs = new ArrayList<>();
            for (Transaction transaction: block.getBody().getTransactions()) {
                transactionHashs.add(transaction.getHash());
            }
            blockInfo.setHeight(height);
            blockInfo.setBlockHash(currentDataHash);
            blockInfo.setNum(getNumByFileName(blockFile.getName()));
            blockInfo.setLine(line + 1);
            blockInfo.setTransactionHashs(transactionHashs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blockInfo;
    }

    default int getHeight() {
        // 获取最新写入的区块文件
        File blockFile = getLastFile();
        // 如果最新写入的区块文件为null，则从0开始重新写入
        if (null == blockFile) {
            return 0;
        } else {
            // 获取当前区块文件中的总行数，其值即为上一区块的行数
            int lineCount = getFileLineCountIfBigCharLine(blockFile);
            // 获取上一区块
            Block preBlock = getFromFileByLine(blockFile, lineCount);
            // 返回上一区块高度
            return preBlock.getHeader().getHeight();
        }
    }

    /**
     * 获取区块文件中指定行号的区块对象
     *
     * @param file 区块文件
     * @param line 区块在区块文件中的行号
     * @return 区块对象
     */
    @Override
    default Block getFromFileByLine(File file, int line) {
        Block[] blocks = new Block[]{null};
        try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
            // 计算文件行数
            int linePosition = 0;
            while (it.hasNext()) {
                linePosition++;
                String lineString = it.nextLine();
                // System.out.println(String.format("linePosition = %s", linePosition));
                if (linePosition == line) {
                    if (StringUtils.isNotEmpty(lineString)) {
                        blocks[0] = JSON.parseObject(lineString, new TypeReference<Block>() {});
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blocks[0];
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param height 区块高度
     * @return 区块对象
     */
    default Block getBlockByHeight(int height) {
        List<Future<Block>> futures = new ArrayList<>();
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(Common.BLOCK_FILE_DIR));
        files.forEach(file -> {
            if (StringUtils.containsIgnoreCase(file.getName(), Common.BLOCK_FILE_START)) {
                Future<Block> future = ThreadTroublePool.obtain().submitFixed(new CallableSearchBlock(height, file, getNumByFileName(file.getName())));
                futures.add(future);
//                try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
//                    while (it.hasNext()) {
//                        String line = it.nextLine();
//                        if (StringUtils.isNotEmpty(line)) {
//                            Block block = JSON.parseObject(line, new TypeReference<Block>() {});
//                            if (block.getHeader().getHeight() == height) {
//                                blocks[0] = block;
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        for (Future<Block> blockFuture : futures) {
            Block block = null;
            try {
                block = blockFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (null != block) {
                return block;
            }
        }
        return null;
    }

}
