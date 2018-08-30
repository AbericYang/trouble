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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期公共方法
 * <p>
 * 作者：Aberic on 2018/08/30 13:55
 * 邮箱：abericyang@gmail.com
 */
public class DateTool {

    /**
     * 获取当前时间
     *
     * @param format (yyyy年MM月dd日 HH时mm分ss秒|yyyy年MM月dd日
     *               HH时mm分ss秒|yyyyMMddHHmmss|HH:mm:ss|yyyy年MM月dd日 等)
     * @return 当前时间格式
     */
    public static String getCurrent(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(new Date());
    }

    /**
     * 将字符串转换为日期
     *
     * @param dateStr 实际日期字符串
     * @param format  指定日期字符串格式
     * @return date
     */
    public static Date str2Date(String dateStr, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.parse(dateStr);
    }

    /**
     * 将日期字符串格从当前格式格式化位指定格式
     *
     * @param dateStr      日期字符串，如2016-08-08 08:08
     * @param formatBefore 当前格式，如yyyy-MM-dd HH:mm
     * @param formatAfter  指定格式，如yyyy/MM/dd HH:MM
     * @return 返回重新格式化后的日期字符串
     */
    public static String strDateFormat(String dateStr, String formatBefore, String formatAfter) throws Exception {
        return date2Str(str2Date(dateStr, formatBefore), formatAfter);
    }

    /**
     * 将日期转换为字符串
     *
     * @param date   date日期
     * @param format 日期格式
     * @return 日期字符串
     */
    public static String date2Str(Date date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 将时间戳按照指定格式转换为日期字符串
     *
     * @param timestamp 时间戳
     * @param format    指定格式
     * @return 日期字符串
     */
    public static String timestampToString(long timestamp, String format) {
        return date2Str(new Date(timestamp), format);
    }

}
