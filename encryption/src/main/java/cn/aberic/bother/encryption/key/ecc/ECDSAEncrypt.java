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

package cn.aberic.bother.encryption.key.ecc;

import cn.aberic.bother.encryption.key.exec.KeyExec;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * ECDSA（椭圆加密算法）加解密工具类
 * <p>
 * 作者：Aberic on 2018/09/03 16:04
 * 邮箱：abericyang@gmail.com
 */
public class ECDSAEncrypt {

    /**
     * 通过公钥字符串获取公钥
     *
     * @param keyStr 公钥字符串信息
     * @return 公钥
     */
    private ECPublicKey getPublicKeyByStr(String keyStr) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(keyStr));
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
            return (ECPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法异常");
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效X509EncodedKeySpec异常错误");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("无此加密代理");
        }
    }

    /**
     * 通过私钥字符串获取公钥
     *
     * @param keyStr 私钥字符串信息
     * @return 私钥
     */
    private ECPrivateKey getPrivateKeyByStr(String keyStr) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(keyStr));
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
            return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法异常");
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效PKCS8EncodedKeySpec异常错误");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("无此加密代理");
        }
    }

    /**
     * 使用私钥文件来执行对info内容的解密操作
     *
     * @param filePath 私钥文件所在路径（如：xx/xx/privateECDSA.key）
     * @param info     待解密内容
     * @return 解密后字符串
     */
    public String decryptPriFile(String filePath, String info) {
        return decryptPriStr(KeyExec.obtain().getStringByFile(filePath), info);
    }

    /**
     * 使用私钥字符串信息来执行对info内容的解密操作
     *
     * @param keyStr 私钥字符串信息
     * @param info   待解密内容
     * @return 解密后字符串
     */
    public String decryptPriStr(String keyStr, String info) {
        byte[] infoBytes = Base64.decodeBase64(info);
        if (keyStr.isEmpty()) {
            throw new RuntimeException("加密公钥不能为空");
        }
        ECPrivateKey privateKey = getPrivateKeyByStr(keyStr);
        try {
            // ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(privateKey.getS(), privateKey.getParams());
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(infoBytes));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("加密公钥非法,请检查");
        } catch (BadPaddingException e) {
            throw new RuntimeException("加密内容数据已损坏");
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("加密内容长度非法");
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("无此加密填充类型");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无此加密算法");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("无此加密代理");
        }
    }

    /**
     * 使用公钥文件来执行对info内容的加解密操作
     *
     * @param filePath 公钥文件所在路径（如：xx/xx/publicECDSA.key）
     * @param info     待加解密内容
     * @return 加解密后字符串
     */
    public String encryptByFile(String filePath, String info) {
        return encryptByStr(KeyExec.obtain().getStringByFile(filePath), info);
    }

    /**
     * 使用公钥字符串信息来执行对info内容的加密操作
     *
     * @param keyStr 公钥字符串信息
     * @param info   待加密内容
     * @return 加密后字符串
     */
    public String encryptByStr(String keyStr, String info) {
        byte[] infoBytes = info.getBytes();
        if (keyStr.isEmpty()) {
            throw new RuntimeException("加密公钥不能为空");
        }
        ECPublicKey publicKey = getPublicKeyByStr(keyStr);
        try {
            // ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(publicKey.getW(), publicKey.getParams());
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64String(cipher.doFinal(infoBytes));
        } catch (InvalidKeyException e) {
            throw new RuntimeException("加密公钥非法,请检查");
        } catch (BadPaddingException e) {
            throw new RuntimeException("加密内容数据已损坏");
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("加密内容长度非法");
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("无此加密填充类型");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无此加密算法");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("无此加密代理");
        }
    }

}
