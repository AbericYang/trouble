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

import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.block.BlockInfo;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.contract.Contract;
import cn.aberic.bother.entity.contract.ContractInfo;
import cn.aberic.bother.entity.token.Token;
import cn.aberic.bother.tools.DeflaterTool;
import cn.aberic.bother.tools.FileTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 存储文件公共接口——公共方法包
 * <p>
 * 作者：Aberic on 2018/8/29 22:18
 * 邮箱：abericyang@gmail.com
 */
public interface IFile<T> {

    /** 获取文件对应组件属性 */
    FileComponent getFileStatus();

    /**
     * 获取指定文件编号的文件中指定行号的{@link T}对象
     *
     * @param num  文件编号
     * @param line 区块文件中的行号
     * @return {@link T}对象
     */
    default T getByNumAndLine(int num, int line) {
        long time = new Date().getTime();
        T t = getFromFileByLine(getFileByNum(num), line);
        System.out.println("getByNumAndLine time = " + (new Date().getTime() - time));
        return t;
    }

    /**
     * 获取文件中指定行号的{@link T}对象
     *
     * @param file 文件
     * @param line 在区块文件中的行号
     * @return {@link T}对象
     */
    @SuppressWarnings("unchecked")
    default T getFromFileByLine(File file, int line) {
        T[] ts = (T[]) new Object[]{null};
        try (LineIterator it = FileUtils.lineIterator(file, "UTF-8")) {
            // 计算文件行数
            int linePosition = 0;
            while (it.hasNext()) {
                linePosition++;
                String lineString = it.nextLine();
                // System.out.println(String.format("linePosition = %s", linePosition));
                if (linePosition == line) {
                    if (StringUtils.isNotEmpty(lineString)) {
                        switch (getFileStatus().getTType()) {
                            case T_TYPE_BLOCK:
                                ts[0] = (T) JSON.parseObject(DeflaterTool.uncompress(lineString), new TypeReference<Block>() {});
                                break;
                            case T_TYPE_BLOCK_INDEX:
                                ts[0] = (T) JSON.parseObject(DeflaterTool.uncompress(lineString), new TypeReference<BlockInfo>() {});
                                break;
                            case T_TYPE_CONTRACT:
                                ts[0] = (T) JSON.parseObject(DeflaterTool.uncompress(lineString), new TypeReference<Contract>() {});
                                break;
                            case T_TYPE_CONTRACT_INDEX_DATA:
                                ts[0] = (T) JSON.parseObject(DeflaterTool.uncompress(lineString), new TypeReference<ContractInfo>() {});
                                break;
                            case T_TYPE_ACCOUNT:
                                ts[0] = (T) JSON.parseObject(DeflaterTool.uncompress(lineString), new TypeReference<Account>() {});
                                break;
                            case T_TYPE_TOKEN:
                                ts[0] = (T) JSON.parseObject(DeflaterTool.uncompress(lineString), new TypeReference<Token>() {});
                                break;
                        }
                    }
                    if (null != ts[0]) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ts[0];
    }

    /**
     * 根据当前区块文件获取下一区块文件，如果下一区块不存在，则直接返回新创建的下一区块文件
     *
     * @param file 当前区块文件
     * @return 下一区块文件
     */
    default File getNextFileByCurrentFile(File file) {
        String fileName = file.getName();
        int fileNum = getNumByFileName(fileName);
        int nextFileNum = fileNum + 1;
        return getFileByNum(nextFileNum);
    }

    /** 获取最新写入的文件 */
    default File getLastFile() {
        final File[] lastFile = {null};
        final int[] lastFileNum = {-1};
        Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir())).forEach(file -> {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                String fileName = file.getName();
                int fileNum = getNumByFileName(fileName);
                if (lastFileNum[0] < fileNum) {
                    lastFileNum[0] = fileNum;
                    lastFile[0] = file;
                }
            }
        });
        return lastFile[0];
    }

    /**
     * 创建第一个隶属指定目录下的文件
     *
     * @return 创建的文件
     */
    default File createFirstFile() throws IOException {
        return FileTool.createFirstFile(String.format("%s/%s0%s", getFileStatus().getDir(), getFileStatus().getStart(), getFileStatus().getEnd()));
    }

    /**
     * 根据{@link T}文件名获取当前文件编号
     *
     * @param fileName {@link T}文件名
     * @return 文件编号
     */
    default int getNumByFileName(String fileName) {
        return Ints.tryParse(fileName.substring(getFileStatus().getStart().length(), fileName.lastIndexOf(getFileStatus().getEnd())));
    }

    /**
     * 根据{@link T}文件编号获取当前文件名，编号从0开始计算
     *
     * @param num {@link T}文件编号
     * @return 文件名
     */
    default String getFileNameByNum(int num) {
        return String.format("%s%s%s", getFileStatus().getStart(), num, getFileStatus().getEnd());
    }

    /**
     * 根据{@link T}文件编号读取文件，如果文件不存在，则直接返回新创建的{@link T}文件
     *
     * @param num 当前待读取{@link T}文件编号
     * @return {@link T}文件
     */
    default File getFileByNum(int num) {
        File file = new File(String.format("%s/%s", getFileStatus().getDir(), getFileNameByNum(num)));
        if (!file.exists()) {
            // 创建新区块文件
            try {
                Preconditions.checkArgument(file.createNewFile(), "file can't be created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取本地目录下文件个数
     *
     * @return 文件个数
     */
    default int getFileCount() {
        int count = 0;
        for (File file : Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()))) {
            if (StringUtils.startsWith(file.getName(), getFileStatus().getStart())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取指定文件编号文件中的总行数，适合单行内容较多较长的情况
     *
     * @param num 文件编号
     * @return 文件总行数
     */
    default int getFileLineCount(int num) {
        return FileTool.getFileLineCount(getFileByNum(num));
    }

    /**
     * 创建或更新对象信息进入文件
     * <p>
     * 此方法与实现本接口的类中的createOrUpdate方法相同。
     * <p>
     * 此方法只是对通用行为进行了一次封装
     *
     * @param result   对象字符串信息
     * @param compress 是否压缩字符串信息
     */
    default void cou(String result, boolean compress) {
        File freshFile = getLastFile();
        try {
            if (compress) {
                result = DeflaterTool.compress(result);
            }
            // 如果最新写入的文件为null，则从0开始重新写入
            if (null == freshFile) {
                // 定义新的文件
                freshFile = createFirstFile();
                FileTool.writeFirstLine(freshFile, result);
            } else {
                // 计算该内容的字节长度
                long accountSize = result.getBytes().length;
                // 如果文件和待写入对象之和已经大于或等于 256 MB，则开辟新文件写入对象
                if (freshFile.length() + accountSize >= 256 * 1000 * 1000) {
                    freshFile = getNextFileByCurrentFile(freshFile);
                    FileTool.writeFirstLine(freshFile, result);
                } else {
                    FileTool.writeAppendLine(freshFile, result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
