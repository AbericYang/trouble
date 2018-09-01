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

package cn.aberic.bother.block.exec.service;

import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.block.BlockInfo;
import cn.aberic.bother.entity.block.Transaction;
import cn.aberic.bother.tools.DeflaterTool;
import cn.aberic.bother.tools.FileTool;
import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 区块文件本地读写接口——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/27 11:24
 * 邮箱：abericyang@gmail.com
 */
public interface IBlockExec extends IExec<Block> {

    /**
     * 创建并存储区块文件，如已存在且大小超过24M，则覆盖，否则下一行追加更新
     *
     * @param block 区块对象
     */
    @Override
    default BlockInfo createOrUpdate(Block block) {
        BlockInfo blockInfo = new BlockInfo();
        int height = 0, line = 0;
        String currentDataHash;
        String compressJsonString;
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
                compressJsonString = DeflaterTool.compress(JSON.toJSONString(block));
                FileTool.writeFirstLine(blockFile, compressJsonString);
            } else {
                // 获取当前区块文件中的总行数，其值即为上一区块的行数
                line = FileTool.getFileLineCountIfBigCharLine(blockFile);
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
                compressJsonString = DeflaterTool.compress(JSON.toJSONString(block));
                // 计算该内容的字节长度
                long blockSize = compressJsonString.getBytes().length;
                // 如果区块文件和待写入对象之和已经大于或等于 256 MB，则开辟新区块文件写入区块对象
                if (blockFile.length() + blockSize >= 256 * 1000 * 1000) {
                    blockFile = getNextFileByCurrentFile(blockFile);
                    FileTool.writeFirstLine(blockFile, compressJsonString);
                } else {
                    FileTool.writeAppendLine(blockFile, compressJsonString);
                }
            }
            List<String> transactionHashList = new ArrayList<>();
            for (Transaction transaction: block.getBody().getTransactions()) {
                transactionHashList.add(transaction.getDataStorageHash());
            }
            blockInfo.setHeight(height);
            blockInfo.setBlockHash(block.getHeader().getDataStorageHash());
            blockInfo.setNum(getNumByFileName(blockFile.getName()));
            blockInfo.setLine(line + 1);
            blockInfo.setTransactionHashList(transactionHashList);
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
            int lineCount = FileTool.getFileLineCountIfBigCharLine(blockFile);
            // 获取上一区块
            Block preBlock = getFromFileByLine(blockFile, lineCount);
            // 返回上一区块高度
            return preBlock.getHeader().getHeight();
        }
    }

}
