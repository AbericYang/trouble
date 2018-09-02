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

package cn.aberic.bother.tools;

import com.google.common.base.Preconditions;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * 文件工具类——公共方法包
 * <p>
 * 作者：Aberic on 2018/8/29 21:31
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class FileTool {

    /**
     * 创建第一个隶属指定目录下的文件
     *
     * @param filePath 文件完整路径及文件名
     * @return 创建的文件
     */
    public static File createFirstFile(String filePath) throws IOException {
        // 定义新的文件
        File file = new File(filePath);
        // 创建新文件的父目录
        Files.createParentDirs(file);
        // 创建新文件
        Preconditions.checkArgument(file.createNewFile(), "file can't be created");
        return file;
    }

    /**
     * 向空文件中写入第一行
     *
     * @param file   文件
     * @param string 写入内容
     */
    public static void writeFirstLine(File file, String string) throws IOException {
        Files.asCharSink(file, Charset.forName("UTF-8"), FileWriteMode.APPEND).write(string);
    }

    /**
     * 文件内换行并在新行中追加内容
     *
     * @param file   文件
     * @param string 追加内容
     */
    public static void writeAppendLine(File file, String string) throws IOException {
        Files.asCharSink(file, Charset.forName("UTF-8"), FileWriteMode.APPEND).write(String.format("\r\n%s", string));
    }

    /**
     * 获取文件中的总行数，适合单行内容较多较长的情况
     *
     * @param file 文件
     * @return 文件总行数
     */
    public static int getFileLineCount(File file) {
        long time = new Date().getTime();
        int lines = 0;
        long fileLength = file.length();
        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));){
            lineNumberReader.skip(fileLength);
            lines = lineNumberReader.getLineNumber() + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("getFileLineCount new 处理时长 = {}", new Date().getTime() - time);
        return lines;
    }

    /**
     * 获取文件MD5的值
     *
     * @param file 文件
     * @return MD5值
     */
    public static String getMD5(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            // 获取一个文件MD5的方法，参数为该文件的输入流
            return DigestUtils.md5Hex(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
