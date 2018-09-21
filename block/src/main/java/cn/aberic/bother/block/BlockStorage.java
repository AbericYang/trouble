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

import cn.aberic.bother.block.exec.service.IDataExec;
import cn.aberic.bother.consensus.exec.Proactive;
import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.block.BlockInfo;
import cn.aberic.bother.entity.block.BlockOut;
import cn.aberic.bother.entity.enums.ConsensusStatus;
import cn.aberic.bother.storage.FileComponent;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.DeflaterTool;
import cn.aberic.bother.tools.FileTool;
import cn.aberic.bother.tools.exception.SearchDataNotFoundException;
import cn.aberic.bother.tools.exception.SearchDataTimeoutException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * 区块存储操作对象——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/24 11:27
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class BlockStorage extends BlockAS implements IDataExec {

    /** 主动发起共识操作对象 */
    private Proactive proactive;

    public BlockStorage(String contractHash) {
        super(contractHash);
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public FileComponent getFileStatus() {
        if (StringUtils.equals(getStorageHash(), Constant.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH)) {
            return FileComponent.getContractDataIndexFileComponentDefault();
        }
        return FileComponent.getContractDataIndexFileComponent(getStorageHash());
    }

    @Override
    public String getStorageHash() {
        return contractHash;
    }

    /**
     * 打包出块
     * <p>
     * 仅出块操作，不将区块存储本地
     *
     * @param block 待存储区块对象
     */
    public BlockOut packOut(Block block) {
        // 区块不应该被直接存入，而是被获取到之后再执行存入操作，同时将获取到的区块广播出去
        return getBlockExec().createOrUpdate(block);
    }

    /**
     * 同步已出快的区块对象到本地
     * <p>
     * 同步指定智能合约账本的区块文件
     *
     * @param blockOut 待同步区块出块对象
     */
    public void sync(BlockOut blockOut) {
        // 区块是否可正常写入
        boolean success = true;
        Block block = blockOut.getBlock();
        BlockInfo blockInfo = blockOut.getBlockInfo();
        // 区块对象被保存本地文件中的压缩字符串
        String compressJsonString;
        // 获取最新写入的区块文件
        File blockFile = getLastFile();
        try {
            // 如果最新写入的区块文件为null，则从0开始重新写入
            if (null == blockFile) {
                // 如果得到区块文件高度不为0，即最新区块文件应该存在，则说明本地数据同步尚未完成
                if (0 != block.getHeader().getHeight()) {
                    // TODO: 2018/9/20 同步数据
                    success = false;
                    log.debug("继续同步数据操作");
                } else {
                    // 定义新的区块文件
                    blockFile = createFirstFile();
                    FileTool.writeFirstLine(blockFile, DeflaterTool.compress(JSON.toJSONString(block)));
                }
            } else {
                // 获取当前区块文件中的总行数，其值即为上一区块的行数
                int line = FileTool.getFileLineCount(blockFile);
                // 获取上一区块
                Block preBlock = getBlockExec().getFromFileByLine(blockFile, line);
                // 如果上一区块高度+1不等于待同步区块高度，则说明本地数据同步尚未完成
                if (preBlock.getHeader().getHeight() + 1 != block.getHeader().getHeight()) {
                    // TODO: 2018/9/20 同步数据
                    success = false;
                    log.debug("继续同步数据操作");
                } else {
                    // 重新生成待写入JSON String内容
                    compressJsonString = DeflaterTool.compress(JSON.toJSONString(block));
                    // 计算该内容的字节长度
                    long blockSize = compressJsonString.getBytes().length;
                    // 如果区块文件和待写入对象之和已经大于或等于 64 MB，则开辟新区块文件写入区块对象
                    if (blockFile.length() + blockSize >= 64 * 1000 * 1000) {
                        blockFile = getNextFileByCurrentFile(blockFile);
                        FileTool.writeFirstLine(blockFile, compressJsonString);
                    } else {
                        FileTool.writeAppendLine(blockFile, compressJsonString);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {
            String blockIndex = String.format("%s,%s,%s,%s", blockInfo.getNum(), blockInfo.getLine(), blockInfo.getBlockHash(), blockInfo.getHeight());
            getBlockIndexExec().createOrUpdate(blockIndex);
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s,%s", blockInfo.getNum(), blockInfo.getLine()));
            for (String s : blockInfo.getTransactionHashList()) {
                sb.append(",").append(s);
            }
            getBlockTransactionIndexExec().createOrUpdate(sb.toString());
        }
    }

    /**
     * 检查两个区块有效性
     *
     * @param height        待存储区块对象高度
     * @param block         待存储区块对象
     * @param blockFromFile 本地已存在区块文件中获取的区块对象
     * @return 区块存储结果
     */
    private boolean checkVerify(int height, Block block, Block blockFromFile) {
        // 比较两者上一区块hash值是否匹配
        if (StringUtils.equalsIgnoreCase(
                block.getHeader().getPreviousDataHash(),
                blockFromFile.getHeader().getPreviousDataHash())) {
            // 比较两者当前区块hash值是否匹配，如果匹配则表示已存在，返回true
            if (StringUtils.equalsIgnoreCase(
                    block.getHeader().getCurrentDataHash(),
                    blockFromFile.getHeader().getCurrentDataHash())) {
                return true;
            } else { // 两者当前区块hash值不匹配，<--主动发起共识-->，共识级别为中间区块冲突
                proactive().verifyBlock(height, ConsensusStatus.BLOCK_CLASH_IN_MIDDLE);
                return false;
            }
        } else { // 如果两者上一区块hash不匹配，则根据上一区块的hash值继续判断两者的有效性
            int lastHeight = height - 1;
            // 如果当前区块即初始区块（极低概率事件，除非恶意节点故意串联），<--主动发起共识-->，共识级别为初始区块冲突
            if (lastHeight < 0) {
                proactive().verifyBlock(height, ConsensusStatus.BLOCK_CLASH_IN_FIRST);
                return false;
            }
            Block blockFromPreFile = null;
            try {
                blockFromPreFile = getBlockIndexExec().getByHeight(height - 1);
            } catch (SearchDataNotFoundException | SearchDataTimeoutException e) {
                e.printStackTrace();
            }
            // 如果待同步区块上一hash与上一区块的当前hash相同
            if (StringUtils.equalsIgnoreCase(
                    block.getHeader().getPreviousDataHash(),
                    blockFromPreFile.getHeader().getCurrentDataHash())) {
                return getBlockExec().createOrUpdate(block) != null;
            } else if (StringUtils.equalsIgnoreCase( // 如果本地已存在区块上一hash与上一区块的当前hash相同
                    blockFromFile.getHeader().getPreviousDataHash(),
                    blockFromPreFile.getHeader().getCurrentDataHash())) {
                return true;
            } else { // 两者上一区块hash与本地上一区块当前hash都不一致，则表示本地区块有被篡改可能，<--主动发起共识-->，共识级别为相同账本下中间区块冲突
                proactive().verifyBlock(height, ConsensusStatus.BLOCK_TAMPERING_IN_MIDDLE);
                return false;
            }
        }
    }

    /** 获取主动发起共识操作对象 */
    private Proactive proactive() {
        if (null == proactive) {
            proactive = new Proactive();
        }
        return proactive;
    }

}
