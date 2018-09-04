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

package cn.aberic.bother.encryption.key.rsa;

import cn.aberic.bother.encryption.key.exec.KeyExec;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA签名验证工具类
 * <p>
 * 作者：Aberic on 2018/09/03 16:12
 * 邮箱：abericyang@gmail.com
 */
public class RSASignature {

    /** 签名算法 */
    private final String SIGN_ALGORITHMS = "SHA256WithRSA";

    /**
     * RSA私钥签名字符串
     *
     * @param info     待签名字符串
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @return 签名值
     */
    public String signByFile(String info, String filePath) {
        return signByStr(info, KeyExec.obtain().getStringByFile(filePath));
    }

    /**
     * RSA私钥签名字符串
     *
     * @param info     待签名字符串
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @param encode   编码
     * @return 签名值
     */
    public String signByFile(String info, String filePath, @Nullable String encode) {
        return signByStr(info, KeyExec.obtain().getStringByFile(filePath), encode);
    }

    /**
     * RSA私钥签名字符串
     *
     * @param info          待签名字符串
     * @param privateKeyStr 私钥字符串内容
     * @return 签名值
     */
    public String signByStr(String info, String privateKeyStr) {
        return signByStr(info, privateKeyStr, null);
    }

    /**
     * RSA私钥签名字符串
     *
     * @param info          待签名字符串
     * @param privateKeyStr 私钥字符串内容
     * @param encode        编码
     * @return 签名值
     */
    public String signByStr(String info, String privateKeyStr, @Nullable String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(privateKey);
            if (encode == null) {
                signature.update(info.getBytes());
            } else {
                signature.update(info.getBytes(encode));
            }
            byte[] signed = signature.sign();
            return Base64.encodeBase64String(signed);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA公钥签名校验
     *
     * @param info     待签名字符串
     * @param sign     签名值
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @return 校验结果
     */
    public boolean verifyByFile(String info, String sign, String filePath) {
        return verifyByStr(info, sign, KeyExec.obtain().getStringByFile(filePath));
    }

    /**
     * RSA公钥签名校验
     *
     * @param info     待签名字符串
     * @param sign     签名值
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @return 校验结果
     */
    public boolean verifyByFile(String info, String sign, String filePath, @Nullable String encode) {
        return verifyByStr(info, sign, KeyExec.obtain().getStringByFile(filePath), encode);
    }

    /**
     * RSA公钥签名校验
     *
     * @param info         待签名字符串
     * @param sign         签名值
     * @param publicKeyStr 公钥字符串内容
     * @return 校验结果
     */
    public boolean verifyByStr(String info, String sign, String publicKeyStr) {
        return verifyByStr(info, sign, publicKeyStr, null);
    }

    /**
     * RSA公钥签名校验
     *
     * @param info         待签名字符串
     * @param sign         签名值
     * @param publicKeyStr 公钥字符串内容
     * @param encode       编码
     * @return 校验结果
     */
    public boolean verifyByStr(String info, String sign, String publicKeyStr, @Nullable String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(publicKey);
            if (encode == null) {
                signature.update(info.getBytes());
            } else {
                signature.update(info.getBytes(encode));
            }
            return signature.verify(Base64.decodeBase64(sign));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

}
