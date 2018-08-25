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
import cn.aberic.bother.core.dm.block.Block;
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
     * 创建并存储区块文件，如已存在且大小超过64M，则覆盖，否则追加行
     *
     * @param block 区块对象
     *
     * @return 成功与否
     */
    boolean createOrWrite(Block block) {
        // 获取最新写入的区块文件
        File blockFile = getLastBlockFile();
        try {
            // 如果最新写入的区块文件为null，则从0开始重新写入
            if (null == blockFile) {
                // 定义新的区块文件
                blockFile = new File(String.format("%s/block_file_0.block", Common.BLOCK_FILE_DIR));
                // 创建新区块文件的父目录
                Files.createParentDirs(blockFile);
                // 创建新区块文件
                Preconditions.checkArgument(blockFile.createNewFile(), "block file can't be created");
                // 如果区块文件和待写入对象之和已经大于或等于64MB，则开辟新区块文件写入区块对象
            } else if (blockFile.length() >= 64 * 1024 * 1024) {
                blockFile = getNextBlockFile(blockFile);
            }
            // 向区块文件中追加写入区块对象并换行
            Files.asCharSink(blockFile, Charset.forName("UTF-8"), FileWriteMode.APPEND).write(String.format("%s\r\n", JSON.toJSONString(block)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 根据区块高度获取区块对象
     *
     * @param height 区块高度
     *
     * @return 区块对象
     */
    Block getBlockByHeight(int height) {
        Block[] blocks = {null};
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(Common.BLOCK_FILE_DIR));
        files.forEach(file -> {
            if (!StringUtils.equalsIgnoreCase(file.getName(), "blockfile")) {
                try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
                    while (it.hasNext()) {
                        String line = it.nextLine();
                        if (StringUtils.isNotEmpty(line)) {
                            Block block = JSON.parseObject(line, new TypeReference<Block>() {});
                            if (block.getHeader().getHeight() == height) {
                                blocks[0] = block;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return blocks[0];
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
            if (!StringUtils.equalsIgnoreCase(file.getName(), "blockfile")) {
                String fileName = file.getName();
                int fileNum = getBlockNum(fileName);
                if (lastBlockFileNum[0] < fileNum) {
                    lastBlockFileNum[0] = fileNum;
                    lastBlockFile[0] = file;
                }
            }
        });
        log.debug(String.format("last file number is %s", lastBlockFileNum[0]));
        System.out.println(String.format("last file number is %s", lastBlockFileNum[0]));
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
        log.debug(String.format("file name is %s", blockFileName));
        System.out.println(String.format("file name is %s", blockFileName));

        String fileNumStr = blockFileName.substring(11, blockFileName.lastIndexOf(".block"));
        log.debug(String.format("file number is %s", fileNumStr));
        System.out.println(String.format("file number is %s", fileNumStr));

        return Ints.tryParse(fileNumStr);
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
        return String.format("block_file_%s.block", num);
    }

    private void createTransactionIndexFile() {

    }

}
