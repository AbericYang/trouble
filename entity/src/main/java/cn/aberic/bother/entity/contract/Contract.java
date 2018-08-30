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

package cn.aberic.bother.entity.contract;

import cn.aberic.bother.entity.BeanJsonField;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.Date;

/**
 * 智能合约对象-smart contract
 * <p>
 * 作者：Aberic on 2018/08/29 17:24
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Contract implements BeanJsonField {

    /** 名称 */
    @JSONField(name = "c")
    private String name;
    /** 版本名称 */
    @JSONField(name = "v")
    private String versionName;
    /** 版本号 */
    @JSONField(name = "vc")
    private int versionCode;
    /** 初始化时间戳 */
    @JSONField(name = "t")
    private long timestamp;
    /** 内容摘要 */
    @JSONField(name = "b")
    private String brief;
    /** 安装路径 */
    @JSONField(name = "d")
    private String dir;
    /**
     * 智能合约唯一hash。
     * <p>
     * 该hash值必须被妥善保管。
     * <p>
     * 该智能合约今后的升级等操作均依赖此hash值。
     * <p>
     * 此hash不会重复，如果出现重复，则表示有误操作或有人恶意攻击该合约
     */
    @JSONField(name = "h")
    private String hash;

    public Contract(String name, String versionName, int versionCode, String brief) {
        this.name = name;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.timestamp = new Date().getTime();
        this.brief = brief;
    }

}
