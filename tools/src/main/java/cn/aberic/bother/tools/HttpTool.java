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

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 作者：Aberic on 2018/09/21 14:01
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public class HttpTool {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient();

    /**
     * 向指定地址的节点发起post请求
     *
     * @param ipAddress 节点地址
     * @param json      请求json
     */
    public static String postNode(String ipAddress, String json) throws IOException {
        return post(String.format("%s:19022", ipAddress), json);
    }

    /**
     * 普通post请求
     *
     * @param url  请求url
     * @param json 请求json
     *
     * @return 请求返回字符串
     */
    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body() != null ? response.body().string() : null;
    }

    /**
     * 普通get请求
     *
     * @param url 请求url
     *
     * @return 请求返回字符串
     */
    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body() != null ? response.body().string() : null;
    }

    /**
     * 向指定地址的节点发起请求连接测试
     *
     * @param ipAddress 节点ip
     * @param port      节点端口号
     */
    public static String get(String ipAddress, int port) throws IOException {
        Request request = new Request.Builder()
                .url(String.format("http://%s:%s", ipAddress, port))
                .build();
        Response response = client.newCall(request).execute();
        return response.body() != null ? response.body().string() : null;
    }

    /**
     * 向指定地址的节点发起请求连接测试
     *
     * @param ipAddress 节点地址
     *
     * @return 请求成功与否
     */

    public static boolean nodeTestSuccess(String ipAddress) {
        client.newBuilder().connectTimeout(2, TimeUnit.SECONDS).readTimeout(2, TimeUnit.SECONDS).writeTimeout(2, TimeUnit.SECONDS).build();
        try {
            return StringUtils.equals(get(ipAddress, 19022), "success");
        } catch (IOException e) {
            return false;
        }
    }

}
