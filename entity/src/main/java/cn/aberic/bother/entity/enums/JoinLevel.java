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
 *
 */

package cn.aberic.bother.entity.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 请求加入节点当前请求级别
 * <p>
 * 当在IMsgJoinService中处理joinFeedbackExec业务，
 * <p>
 * 即应答加入新节点消息业务处理接口做出请求加入应答时，根据请求节点循环请求楼进行加入时，
 * <p>
 * 已经给过当前反馈的事务将不再返回当前事务。
 * <p>
 * 例如：请求加入节点请求级别为社区，此时应答服务返回了当前社区所处集合以及社区项城县城Leader节点
 * <p>
 * 请求节点此时需要遍历社区下所有楼节点信息，此时楼节点将不再返回任何内容，除非允许其加入，避免无限循环
 * <p>
 * 作者：Aberic on 2018/9/13 20:22
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Getter
public enum JoinLevel {

    /** 孤岛 */
    LONELY("孤岛", -1),
    /** 楼 */
    TOWER("楼", 0),
    /** 社区 */
    COMMUNITY("社区", 1),
    /** 县城 */
    COUNTY("县城", 2),
    /** 市 */
    CITY("市", 3),
    /** 省 */
    PROVINCE("省", 4),
    /** 楼 */
    TOWER_NO_EXEC("楼，无需处理", 5),
    /** 社区 */
    COMMUNITY_NO_EXEC("社区，无需处理", 6),
    /** 县城 */
    COUNTY_NO_EXEC("县城，无需处理", 7),
    /** 市 */
    CITY_NO_EXEC("市，无需处理", 8),
    /** 省 */
    PROVINCE_NO_EXEC("省，无需处理", 9);

    /** 别名描述 */
    private String alias;
    /** 级别码 */
    private int level;

    /**
     * 当前交易状态
     *
     * @param alias 别名描述
     * @param level 级别码
     */
    JoinLevel(String alias, int level) {
        this.alias = alias;
        this.level = level;
    }

    public static JoinLevel get(String alias) {
        JoinLevel[] levels = values();
        for (JoinLevel level : levels) {
            if (StringUtils.equals(level.alias, alias)) {
                return level;
            }
        }
        return null;
    }

}
