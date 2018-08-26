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

package cn.aberic.bother.core.dm.exec;

import cn.aberic.bother.common.Common;
import cn.aberic.bother.common.ThreadTroublePool;
import cn.aberic.bother.core.dm.block.Block;
import cn.aberic.bother.core.dm.call.CallableSearchBlock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 区块文件本地读写——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/08/24 11:44
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
class BlockFile {

    volatile private static BlockFile instance;

    static BlockFile obtain() {
        if (null == instance) {
            synchronized (BlockFile.class) {
                if (null == instance) {
                    instance = new BlockFile();
                }
            }
        }
        return instance;
    }

    /**
     * 创建并存储区块文件，如已存在且大小超过64M，则覆盖，否则下一行追加更新
     *
     * @param block 区块对象
     *
     * @return 成功与否
     */
    boolean createOrUpdate(Block block) {
        String jsonStringBlock = JSON.toJSONString(block);
        long blockSize = jsonStringBlock.getBytes().length;
        // 获取最新写入的区块文件
        File blockFile = getLastBlockFile();
        try {
            // 如果最新写入的区块文件为null，则从0开始重新写入
            if (null == blockFile) {
                // 定义新的区块文件
                blockFile = new File(String.format("%s/%s0.block", Common.BLOCK_FILE_DIR, Common.BLOCK_FILE_NAME_START));
                // 创建新区块文件的父目录
                Files.createParentDirs(blockFile);
                // 创建新区块文件
                Preconditions.checkArgument(blockFile.createNewFile(), "block file can't be created");
                // 如果区块文件和待写入对象之和已经大于或等于64MB，则开辟新区块文件写入区块对象
            } else if (blockFile.length() + blockSize >= 64 * 1000 * 1000) {
                log.debug(String.format("block file size great than 64MB, now size = %s", blockFile.length()));
                System.out.println(String.format("block file size great than 64MB, now size = %s", blockFile.length()));
                blockFile = getNextBlockFile(blockFile);
                log.debug(String.format("next block file name = %s", blockFile.getName()));
                System.out.println(String.format("next block file name = %s", blockFile.getName()));
            }
            // 向区块文件中追加写入区块对象并换行
            Files.asCharSink(blockFile, Charset.forName("UTF-8"), FileWriteMode.APPEND).write(String.format("%s\r\n", jsonStringBlock));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void createOrUpdateBlockIndexByTransaction() {

    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param height 区块高度
     *
     * @return 区块对象
     */
    Block getBlockByHeight(int height) {
        List<Future<Block>> futures = new ArrayList<>();
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(Common.BLOCK_FILE_DIR));
        files.forEach(file -> {
            if (StringUtils.containsIgnoreCase(file.getName(), Common.BLOCK_FILE_NAME_START)) {
                Future<Block> future = ThreadTroublePool.obtain().submitFixed(new CallableSearchBlock(height, file, getBlockNum(file.getName())));
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
        for (Future<Block> blockFuture: futures) {
            Block block = null;
            try {
                block = blockFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                log.error(String.format("blockFuture.get() block failed as reason : %s", e.getMessage()));
            }
            if (null != block) {
                return block;
            }
        }
        return null;
    }

    private Block d(int height, File file, int blockFileNum) throws ExecutionException, InterruptedException {
        CallableSearchBlock searchBlock = new CallableSearchBlock(height, file, blockFileNum);
        FutureTask<Block> task = new FutureTask<>(searchBlock);
        new Thread(task).start();
        return task.get();
    }

    /**
     * 获取本地区块文件个数
     *
     * @return 区块文件个数
     */
    int getBlockFileCount() {
        int count = 0;
        for (File ignored : Files.fileTraverser().breadthFirst(new File(Common.BLOCK_FILE_DIR))) {
            count++;
        }
        return count;
    }

    /**
     * 根据当前区块文件获取下一区块文件，如果下一区块不存在，则直接返回新创建的下一区块文件
     *
     * @param blockFile 当前区块文件
     *
     * @return 下一区块文件
     */
    private File getNextBlockFile(File blockFile) {
        String blockFileName = blockFile.getName();
        int fileNum = getBlockNum(blockFileName);
        int nextFileNum = fileNum + 1;
        return getBlockFile(nextFileNum);
    }

    /** 获取最新写入的区块文件 */
    private File getLastBlockFile() {
        final File[] lastBlockFile = {null};
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(Common.BLOCK_FILE_DIR));
        final int[] lastBlockFileNum = {-1};
        files.forEach(file -> {
            if (StringUtils.containsIgnoreCase(file.getName(), Common.BLOCK_FILE_NAME_START)) {
                String fileName = file.getName();
                int fileNum = getBlockNum(fileName);
                if (lastBlockFileNum[0] < fileNum) {
                    lastBlockFileNum[0] = fileNum;
                    lastBlockFile[0] = file;
                }
            }
        });
        return lastBlockFile[0];
    }

    /**
     * 根据区块文件名获取当前区块编号
     *
     * @param blockFileName 区块文件名
     *
     * @return 区块编号
     */
    private int getBlockNum(String blockFileName) {
        return Ints.tryParse(blockFileName.substring(11, blockFileName.lastIndexOf(".block")));
    }

    /**
     * 根据区块文件编号读取区块集合
     *
     * @param num 当前待读取区块文件编号
     *
     * @return 区块集合
     */
    List<Block> readBlocksFromFile(int num) {
        File blockFile = getBlockFile(num);
        List<Block> blocks = new ArrayList<>();
        try {
            // String result = Files.asCharSource(blockFile, Charset.forName("UTF-8")).read();
            // return JSON.parseObject(result, new TypeReference<Block>() {});
            try (LineIterator it = FileUtils.lineIterator(blockFile, "UTF-8")) {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if (StringUtils.isNotEmpty(line)) {
                        blocks.add(JSON.parseObject(line, new TypeReference<Block>() {}));
                    }
                }
            }
        } catch (IOException e) {
            log.error(String.format("block file read failed, %s", e.getMessage()));
        }
        return blocks;
    }

    /**
     * 根据区块文件编号读取区块文件，如果区块不存在，则直接返回新创建的区块文件
     *
     * @param num 当前待读取区块文件编号
     *
     * @return 区块对象
     */
    private File getBlockFile(int num) {
        File blockFile = new File(String.format("%s/%s", Common.BLOCK_FILE_DIR, getBlockFileNameByNum(num)));
        if (!blockFile.exists()) {
            // 创建新区块文件
            try {
                Preconditions.checkArgument(blockFile.createNewFile(), "block file can't be created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return blockFile;
    }

    /**
     * 根据区块文件编号获取当前区块文件名，编号从0开始计算
     *
     * @param num 区块文件编号
     *
     * @return 区块文件名
     */
    private String getBlockFileNameByNum(int num) {
        return String.format("%s%s.block", Common.BLOCK_FILE_NAME_START, num);
    }

}
