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

package cn.aberic.bother.entity;

import cn.aberic.bother.tools.VerifyTool;
import com.alibaba.fastjson.JSONObject;

/**
 * 返回内容通用接口
 * <p>
 * 作者：Aberic on 2018/08/30 14:08
 * 邮箱：abericyang@gmail.com
 */
public interface IResponse {

    /** 返回的成功码 */
    int SUCCESS_CODE = 200;

    /**
     * 通用且简单点成功返回，仅需接收方知道此次请求状态即可。
     * <p>
     * 返回格式是一个json字符串，此json有两个key，分别是code和data。
     * <p>
     * code默认值是 {@link #SUCCESS_CODE} ，data默认返回success字符串
     *
     * @return 返回 success json字符串
     */
    default String response() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS_CODE);
        jsonObject.put("data", "success");
        return jsonObject.toString();
    }

    /**
     * 根据传入字符串 {@param result} 返回一个json字符串，此json有两个key，分别是code和data。
     * <p>
     * code默认值是 {@link #SUCCESS_CODE}。
     * <p>
     * data会验证 {@param result} 是否为json对象或json array对象
     * <p>
     * 如果是json对象，则转成json对象后作为data的值
     * <p>
     * 如果是json array对象，则转成json array对象后作为data的值
     * <p>
     * 如果不属于json类型，则直接将此字符串作为data的值。
     *
     * @param result 传入字符串
     * @return json字符串
     */
    default String response(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS_CODE);
        return VerifyTool.verifyJSON(jsonObject, result).toString();
    }

    /**
     * 根据传入对象 {@param obj} 返回一个json字符串，此json有两个key，分别是code和data。
     * <p>
     * code默认值是 {@link #SUCCESS_CODE}。
     * <p>
     * {@param obj} 直接作为data的值。
     *
     * @param obj 传入对象
     * @return json字符串
     */
    default String response(Object obj) {
        if (obj instanceof BeanJsonField) {
            return responseBean((BeanJsonField) obj);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS_CODE);
        jsonObject.put("data", obj);
        return jsonObject.toString();
    }

    /**
     * 根据传入实现了 {@link BeanJsonField} 接口的对象 {@param bean} 返回一个json字符串
     * <p>
     * 此json有两个key，分别是code和data。
     * <p>
     * code默认值是 {@link #SUCCESS_CODE}。
     * <p>
     * {@param bean} 在调用接口方法toJsonString后直接作为data的值。
     *
     * @param bean 实现 BeanJsonField 接口的对象
     * @return json字符串
     */
    default String responseBean(BeanJsonField bean) {
        return response(bean.toJsonString());
    }

}
