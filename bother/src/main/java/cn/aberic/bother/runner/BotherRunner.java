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

package cn.aberic.bother.runner;

import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.tools.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.hyperic.jni.ArchNotSupportedException;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.*;

/**
 * 启动操作
 * <p>
 * 作者：Aberic on 2018/09/07 09:47
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Component
public class BotherRunner implements ApplicationRunner {

    @Value("${socket.ip}")
    private String ipAddress;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initDir();
        initSigar();
        Node.obtain().initAddress(Constant.BLOCK_DEFAULT_SYSTEM_CONTRACT_HASH, ipAddress);
        System.out.println();
        System.out.println(" _____   _   _   ____    ");
        System.out.println("| ____| | \\ | | |  _ \\   ");
        System.out.println("|  _|   |  \\| | | | | |  ");
        System.out.println("| |___  | |\\  | | |_| |  ");
        System.out.println("|_____| |_| \\_| |____/   ");
        System.out.println();
        System.out.println("============================================================================ ");
        System.out.println();
        System.out.println("================================= read logs ================================ ");
    }

    /** 创建 db 目录 */
    private void initDir() {
        File file = new File(Constant.CONTRACT_DATA_ROCKS_DB_FILE_DIR);
        if (file.exists()) {
            return;
        }
        file.mkdirs();
    }

    /** 初始化sigar的配置文件 */
    private void initSigar() throws IOException {
        SigarLoader loader = new SigarLoader(Sigar.class);
        String lib = null;

        try {
            lib = loader.getLibraryName();
        } catch (ArchNotSupportedException var7) {
            log.error(var7.getMessage(), var7);
        }
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:/sigar/" + lib);
        if (resource.exists()) {
            InputStream is = resource.getInputStream();
            File tempDir = FileUtils.getTempDirectory();
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(tempDir, lib), false));
            StreamUtils.copy(is, os);
            is.close();
            os.close();
            System.setProperty("org.hyperic.sigar.path", tempDir.getCanonicalPath());
        }
    }

}
