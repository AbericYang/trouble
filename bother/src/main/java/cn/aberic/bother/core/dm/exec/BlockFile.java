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
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
     * 创建并存储区块文件，如已存在，则覆盖
     *
     * @param height 当前待存储区块高度
     * @param block  区块对象
     *
     * @return 成功与否
     */
    boolean create(int height, Block block) {
        File blockFile = new File(String.format("%s/block_file_%s.block", Common.BLOCK_FILE_DIR, height));
        try {
            if (!blockFile.getParentFile().exists() && !blockFile.getParentFile().mkdirs()) {
                return false;
            }
            if (!blockFile.exists() && !blockFile.createNewFile()) {
                return false;
            }
            FileOutputStream fos = new FileOutputStream(blockFile);
            byte[] bytes = JSON.toJSONString(block).getBytes();
            fos.write(bytes);
            fos.flush();
        } catch (IOException e) {
            log.error(String.format("block file create failed, %s", e.getMessage()));
            return false;
        }
        return true;
    }

    /**
     * 根据区块高度读取区块对象
     *
     * @param height 当前待读取区块高度
     *
     * @return 区块对象
     */
    Block read(int height) {
        File blockFile = new File(String.format("%s/block_file_%s.block", Common.BLOCK_FILE_DIR, height));
        try {
            if (!blockFile.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(blockFile);
            int length;
            byte[] bytes = new byte[1024];
            StringBuilder read = new StringBuilder();
            while ((length = fis.read(bytes)) != -1) {
                read.append(new String(bytes, 0, length));
            }
            fis.close();
            return JSON.parseObject(read.toString(), new TypeReference<Block>() {});
        } catch (IOException e) {
            log.error(String.format("block file read failed, %s", e.getMessage()));
        }
        return null;
    }

    private void createTransactionIndexFile() {

    }

}
