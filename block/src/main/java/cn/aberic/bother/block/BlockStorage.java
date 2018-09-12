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
import cn.aberic.bother.entity.consensus.ConsensusStatus;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.storage.FileComponent;
import cn.aberic.bother.tools.exception.SearchDataNotFoundException;
import cn.aberic.bother.tools.exception.SearchDataTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * 存储区块——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/24 11:27
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class BlockStorage extends BlockAS implements IDataExec {

    /** 主动发起共识操作对象 */
    private Proactive proactive;
    private BlockAcquire acquire;

    public BlockStorage(String contractHash) {
        super(contractHash);
        acquire = new BlockAcquire(contractHash);
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
     * 根据投票后，作为非打包区块节点的操作方法
     * <p>
     * 同步指定智能合约账本的区块文件
     *
     * @param block 待同步区块对象
     */
    public BlockInfo snyc(Block block) {
//        // 获取当前待存储区块高度
//        int height = block.getHeader().getHeight();
//        // 根据高度查询是否已存在本地区块对象
//        Block blockFromFile = null;
//        try {
//            blockFromFile = getBlockIndexExec().getByHeight(height);
//        } catch (SearchDataNotFoundException | SearchDataTimeoutException ignored) {
//        }
//        if (null == blockFromFile) { // 如果不存在，则执行存储操作
//            if (checkBlockVerify(block)) {
//                return save(block);
//            }
//        } else { // 如果存在，则进入下一步判断两者区块有效性
//            checkVerify(height, block, blockFromFile);
//        }
        return save(block);
    }

    /**
     * 根据投票后，作为区块打包节点的操作方法
     * <p>
     * 打包并存储指定智能合约账本的区块文件
     *
     * @param block 待存储区块对象
     */
    private BlockInfo save(Block block) {
        BlockInfo blockInfo = getBlockExec().createOrUpdate(block);
        String blockIndex = String.format("%s,%s,%s,%s", blockInfo.getNum(), blockInfo.getLine(), blockInfo.getBlockHash(), blockInfo.getHeight());
        getBlockIndexExec().createOrUpdate(blockIndex);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s,%s", blockInfo.getNum(), blockInfo.getLine()));
        for (String s : blockInfo.getTransactionHashList()) {
            sb.append(",").append(s);
        }
        getBlockTransactionIndexExec().createOrUpdate(sb.toString());
        return blockInfo;
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
            } catch (SearchDataNotFoundException e) {
                e.printStackTrace();
            } catch (SearchDataTimeoutException e) {
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
