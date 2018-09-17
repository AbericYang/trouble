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

package cn.aberic.bother.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 验证通用工具
 * <p>
 * 作者：Aberic on 2018/8/31 00:26
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public class VerifyTool {

    public static JSONObject verifyJSON(JSONObject jsonObject, String result) {
        try {
            JSONObject object = JSON.parseObject(result);
            jsonObject.put("data", object);
            return jsonObject;
        } catch (Exception ignored) {
        }
        try {
            JSONArray array = JSON.parseArray(result);
            jsonObject.put("data", array);
            return jsonObject;
        } catch (Exception ignored) {
        }
        jsonObject.put("data", result);
        return jsonObject;
    }

    public static JSONObject verifyJSON(String key, String value) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject object = JSON.parseObject(value);
            jsonObject.put(key, object);
            return jsonObject;
        } catch (Exception ignored) {
        }
        try {
            JSONArray array = JSON.parseArray(value);
            jsonObject.put(key, array);
            return jsonObject;
        } catch (Exception ignored) {
        }
        jsonObject.put(key, value);
        return jsonObject;
    }

}
