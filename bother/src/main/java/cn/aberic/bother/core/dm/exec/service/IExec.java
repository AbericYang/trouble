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

import cn.aberic.bother.core.dm.block.Block;
import cn.aberic.bother.core.dm.block.BlockInfo;
import cn.aberic.bother.core.dm.block.FileComponent;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 文件本地读写接口——数据操作层-data manipulation
 * <p>
 * 该接口服务提供了区块文件{@link cn.aberic.bother.core.dm.block.Block}
 * 及区块索引文件{@link BlockInfo}对象的
 * 基本操作方案
 * <p>
 * 作者：Aberic on 2018/08/27 12:13
 * 邮箱：abericyang@gmail.com
 */
public interface IExec<T> {

    /** 获取文件对应组件属性 */
    FileComponent getFileStatus();

    /**
     * 根据{@link T}对象创建或更新后存储{@link T}文件
     *
     * @param t {@link T}对象
     *
     * @return 成功与否
     */
    BlockInfo createOrUpdate(T t);

    /**
     * 获取指定文件编号的文件中指定行号的{@link T}对象
     *
     * @param num  文件编号
     * @param line 区块文件中的行号
     *
     * @return {@link T}对象
     */
    default T getByNumAndLine(int num, int line) {
        return getFromFileByLine(getFileByNum(num), line);
    }

    /**
     * 获取文件中指定行号的{@link T}对象
     *
     * @param file 文件
     * @param line 在区块文件中的行号
     *
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
                                ts[0] = (T) JSON.parseObject(lineString, new TypeReference<Block>() {});
                                break;
                            case T_TYPE_BLOCK_INDEX:
                                ts[0] = (T) JSON.parseObject(lineString, new TypeReference<BlockInfo>() {});
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
     *
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
        Iterable<File> files = Files.fileTraverser().breadthFirst(new File(getFileStatus().getDir()));
        final int[] lastFileNum = {-1};
        files.forEach(file -> {
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
        // 定义新的文件
        File file = new File(String.format("%s/%s0%s", getFileStatus().getDir(), getFileStatus().getStart(), getFileStatus().getEnd()));
        // 创建新文件的父目录
        Files.createParentDirs(file);
        // 创建新文件
        Preconditions.checkArgument(file.createNewFile(), "file can't be created");
        return file;
    }

    /**
     * 根据{@link T}文件名获取当前文件编号
     *
     * @param fileName {@link T}文件名
     *
     * @return 文件编号
     */
    default int getNumByFileName(String fileName) {
        return Ints.tryParse(fileName.substring(getFileStatus().getStart().length(), fileName.lastIndexOf(getFileStatus().getEnd())));
    }

    /**
     * 根据{@link T}文件编号获取当前文件名，编号从0开始计算
     *
     * @param num {@link T}文件编号
     *
     * @return 文件名
     */
    default String getFileNameByNum(int num) {
        return String.format("%s%s%s", getFileStatus().getStart(), num, getFileStatus().getEnd());
    }

    /**
     * 根据{@link T}文件编号读取文件，如果文件不存在，则直接返回新创建的{@link T}文件
     *
     * @param num 当前待读取{@link T}文件编号
     *
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
     *
     * @return 文件总行数
     */
    default int getFileLineCountIfBigCharLine(int num) {
        return getFileLineCountIfBigCharLine(getFileByNum(num));
    }

    /**
     * 获取指定文件编号文件中的总行数，适合单行内容较多较长的情况
     *
     * @param num 文件编号
     *
     * @return 文件总行数
     */
    default int getFileLineCountIfLittleCharLine(int num) {
        return getFileLineCountIfLittleCharLine(getFileByNum(num));
    }

    /**
     * 获取文件中的总行数，适合单行内容较多较长的情况
     *
     * @param file 文件
     *
     * @return 文件总行数
     */
    default int getFileLineCountIfBigCharLine(File file) {
        int cnt;
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(file));
            while (reader.readLine() != null) {
            }
            cnt = reader.getLineNumber();
        } catch (Exception ex) {
            cnt = -1;
            ex.printStackTrace();
        } finally {
            try {
                assert reader != null;
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cnt;
    }

    /**
     * 获取文件中的总行数，适合单行内容较少的情况
     *
     * @param file 文件
     *
     * @return 文件总行数
     */
    default int getFileLineCountIfLittleCharLine(File file) {
        int cnt = 0;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] c = new byte[1024];
            int readChars;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++cnt;
                    }
                }
            }
        } catch (Exception ex) {
            cnt = -1;
            ex.printStackTrace();
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cnt;
    }

    /**
     * 向空文件中写入第一行
     *
     * @param file   文件
     * @param string 写入内容
     */
    default void writeFirstLine(File file, String string) throws IOException {
        Files.asCharSink(file, Charset.forName("UTF-8"), FileWriteMode.APPEND).write(string);
    }

    /**
     * 文件内换行并在新行中追加内容
     *
     * @param file   文件
     * @param string 追加内容
     */
    default void writeAppendLine(File file, String string) throws IOException {
        Files.asCharSink(file, Charset.forName("UTF-8"), FileWriteMode.APPEND).write(String.format("\r\n%s", string));
    }

}
