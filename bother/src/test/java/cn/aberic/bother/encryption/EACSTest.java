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

package cn.aberic.bother.encryption;

import com.google.common.base.Splitter;
import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.spongycastle.asn1.pkcs.PrivateKeyInfo;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.cert.CertException;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.cert.X509v3CertificateBuilder;
import org.spongycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spongycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.spongycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.openssl.PEMKeyPair;
import org.spongycastle.openssl.PEMParser;
import org.spongycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.spongycastle.openssl.jcajce.JcaPEMWriter;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.ContentVerifierProvider;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.spongycastle.pkcs.PKCS10CertificationRequest;
import org.spongycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.spongycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemWriter;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.Enumeration;

/**
 * 作者：Aberic on 2018/09/25 10:58
 * <p>
 * 邮箱：abericyang@gmail.com
 */
public class EACSTest {
    private static final String TEST_PWD = "1234";

    public static void main(String[] args) throws Exception {
        BouncyCastleProvider prov = new org.spongycastle.jce.provider.BouncyCastleProvider();
        Security.addProvider(prov);
        // 查看支持的Provider和Algorithm
        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider: " + provider.getName() + " version: " + provider.getVersion());
            for (Provider.Service service : provider.getServices()) {
                System.out.printf("  Type : %-30s  Algorithm: %-30s\n", service.getType(), service.getAlgorithm());
            }
        }
        System.out.println("------------------------------------------------------------------------------------------\n\n");

        //生成ECDSA 公私钥
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA", prov.getName());
        generator.initialize(ecSpec, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();

        // extract the encoded private key, this is an unencrypted PKCS#8 private key
        byte[] encodedPrivateKey = privateKey.getEncoded();

        //输出private key(非BouncyCastle API)
        String pem = generatePem("PRIVATE KEY", encodedPrivateKey);
        System.out.println("pem:\n" + pem);
        checkRecover(privateKey, pem);

        //输出private key(BouncyCastle API)
        pem = privateKeyToPEM(privateKey);
        System.out.println("pem:\n" + pem);
        checkRecover(privateKey, pem);

        // 输出加密private key(BouncyCastle API)
        // We must use a PasswordBasedEncryption algorithm in order to encrypt the private key, you may use any common algorithm supported by openssl,
        // you can check them in the openssl documentation http://www.openssl.org/docs/apps/pkcs8.html
        String pbeAlg = "PBEWITHSHA1ANDDESEDE";
        int count = 1 << 10;// hash iteration count
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[128];
        random.nextBytes(salt);
        // Create PBE parameter set
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(TEST_PWD.toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(pbeAlg);
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        Cipher pbeCipher = Cipher.getInstance(pbeAlg);
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        // Encrypt the encoded Private Key with the PBE key
        byte[] ciphertext = pbeCipher.doFinal(encodedPrivateKey);
        // Now construct PKCS #8 EncryptedPrivateKeyInfo object
        AlgorithmParameters algparms = AlgorithmParameters.getInstance(pbeAlg);
        algparms.init(pbeParamSpec);
        EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, ciphertext);
        // and here we have it! a DER encoded PKCS#8 encrypted key!
        byte[] encryptedPkcs8 = encinfo.getEncoded();
        pem = generatePem("ENCRYPTED PRIVATE KEY", encryptedPkcs8);
        System.out.println("pem encrypted:\n" + pem);
        checkRecover(privateKey, pem);

        System.out.println("------------------------------------------------------------------------------------------");
        //生成ECDSA csr，并签名
        generatePKCS10CSRAndSign(publicKey, privateKey);
    }

    private static void checkRecover(PrivateKey privateKey, String pem) throws IOException {
        PrivateKey recoveredKey = privateKeyFromPEM(pem);
        System.out.println("privateKey=" + privateKey);
        System.out.println("privateKey.getAlgorithm()=" + privateKey.getAlgorithm());
        System.out.println("\nrecoveredKey=" + privateKey);
        System.out.println("recoveredKey.getAlgorithm()=" + recoveredKey.getAlgorithm());
        System.out.println();
        if (privateKey.equals(recoveredKey)) {
            System.out.println("Key recovery ok");
        } else {
            System.err.println("Private key recovery failed");
        }
        if (privateKey.getAlgorithm().equals(recoveredKey.getAlgorithm())) {
            System.out.println("Key algorithm ok");
        } else {
            System.err.println("Key algorithms do not match");
        }
        System.out.println("\n\n\n");
    }

    private static String privateKeyToPEM(PrivateKey privateKey) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
        pemWriter.writeObject(privateKey);
        pemWriter.close();
        return new String(bos.toByteArray());
    }

    private static PrivateKey privateKeyFromPEM(String der) throws IOException {
        StringReader reader = new StringReader(der);
        PEMParser pemParser = new PEMParser(reader);
        try {
            Object o = pemParser.readObject();
            if (o == null) {
                throw new IOException("Not an OpenSSL key");
            }
            if (o instanceof PEMKeyPair) {
                KeyPair kp = new JcaPEMKeyConverter().setProvider("SC").getKeyPair((PEMKeyPair) o);
                return kp.getPrivate();
            } else if (o instanceof PrivateKeyInfo) {
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(new BouncyCastleProvider());
                return converter.getPrivateKey((PrivateKeyInfo) o);
            } else if (o instanceof PKCS8EncryptedPrivateKeyInfo) {
                PKCS8EncryptedPrivateKeyInfo eki = (PKCS8EncryptedPrivateKeyInfo) o;
                return privateKeyFromEncryptedPEM(eki.getEncoded());
            }
            throw new IOException("Not an OpenSSL key" + o);
        } finally {
            pemParser.close();
        }
    }

    private static PrivateKey privateKeyFromEncryptedPEM(byte[] keyBytes) throws IOException {
        EncryptedPrivateKeyInfo encryptPKInfo = new EncryptedPrivateKeyInfo(keyBytes);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(encryptPKInfo.getAlgName());
            PBEKeySpec pbeKeySpec = new PBEKeySpec(TEST_PWD.toCharArray());
            SecretKeyFactory secFac = SecretKeyFactory.getInstance(encryptPKInfo.getAlgName());
            Key pbeKey = secFac.generateSecret(pbeKeySpec);
            AlgorithmParameters algParams = encryptPKInfo.getAlgParameters();
            cipher.init(Cipher.DECRYPT_MODE, pbeKey, algParams);
            KeySpec pkcs8KeySpec = encryptPKInfo.getKeySpec(cipher);
            KeyFactory kf = KeyFactory.getInstance("ECDSA");
            return kf.generatePrivate(pkcs8KeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String generatePem(String type, byte[] encoded) {
        String code = "-----BEGIN " + type + "-----\n";
        String codenew = new String(Base64.encode((encoded)));
        String myOutput = "";
        for (String substring : Splitter.fixedLength(64).split(codenew)) {
            myOutput += substring + "\n";
        }
        code += myOutput.substring(0, myOutput.length() - 1);
        code += "\n-----END " + type + "-----";
        return new String(code.getBytes());
    }

    private static String csrToPEM(PKCS10CertificationRequest csr) throws IOException {
        PemObject pemObject = new PemObject("NEW CERTIFICATE REQUEST", csr.getEncoded());
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        return stringWriter.toString();
    }

    private static byte[] generatePKCS10CSRAndSign(PublicKey publicKey, PrivateKey privateKey) throws Exception {
        // CSR
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                new X500Principal("C=CN, ST=SC, O=CD, OU=MC, CN=test, emailAddress=xx@xx.com"), publicKey);
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withECDSA");
        ContentSigner signer = csBuilder.build(privateKey);
        PKCS10CertificationRequest csr = p10Builder.build(signer);

        String pem = csrToPEM(csr);
        System.out.println("csr:\n" + pem);
        System.out.println(csr.getSubjectPublicKeyInfo().getPublicKeyData());

        //加载ca公私钥
        PEMParser pemParser = new PEMParser(new StringReader("-----BEGIN EC PRIVATE KEY-----\n" +
                "MHQCAQEEIFLDMLtsZrm1WlqMo/VZ0mWM/hX+rYpQaYdeMvkJA4F1oAcGBSuBBAAK\n" +
                "oUQDQgAEVVrLKeTmtkXqDK1lrRi2/AngLSHSERQFscZzOED09W9zYxA/vqx5uxI8\n" +
                "XpN5PQ/ekvAPJAqkbfN7msw4f5U5KQ==\n" +
                "-----END EC PRIVATE KEY-----\n"));
        Object object = pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(new BouncyCastleProvider());
        PrivateKey caPrivateKey = null;
        if (object instanceof PrivateKeyInfo) {
            System.out.println("ca privateKey=\n" + converter.getPrivateKey((PrivateKeyInfo) object));
            caPrivateKey = converter.getPrivateKey((PrivateKeyInfo) object);
        } else if (object instanceof org.spongycastle.openssl.PEMKeyPair) {
            PEMKeyPair pemKeyPair = (PEMKeyPair) object;
            PrivateKeyInfo pki = pemKeyPair.getPrivateKeyInfo();
            System.out.println("ca privateKey=\n" + converter.getPrivateKey(pki));
            caPrivateKey = converter.getPrivateKey(pki);
        }
        pemParser = new PEMParser(new StringReader("-----BEGIN CERTIFICATE-----\n" +
                "MIICYzCCAgigAwIBAgIJALVT13T2E0X2MAoGCCqGSM49BAMCMIGNMQswCQYDVQQG\n" +
                "EwJDTjEQMA4GA1UECBMHQmVpSmluZzEQMA4GA1UEBxMHQmVpSmluZzETMBEGA1UE\n" +
                "ChMKY2xvdWRtaW5kczESMBAGA1UECxMJYXV0aGNoYWluMQ0wCwYDVQQDEwRkYXRh\n" +
                "MSIwIAYJKoZIhvcNAQkBFhNkYXRhQGNsb3VkbWluZHMuY29tMCAXDTE3MDIxMDAz\n" +
                "MjU1NFoYDzMwMTYwNjEzMDMyNTU0WjCBjTELMAkGA1UEBhMCQ04xEDAOBgNVBAgT\n" +
                "B0JlaUppbmcxEDAOBgNVBAcTB0JlaUppbmcxEzARBgNVBAoTCmNsb3VkbWluZHMx\n" +
                "EjAQBgNVBAsTCWF1dGhjaGFpbjENMAsGA1UEAxMEZGF0YTEiMCAGCSqGSIb3DQEJ\n" +
                "ARYTZGF0YUBjbG91ZG1pbmRzLmNvbTBWMBAGByqGSM49AgEGBSuBBAAKA0IABFVa\n" +
                "yynk5rZF6gytZa0YtvwJ4C0h0hEUBbHGczhA9PVvc2MQP76sebsSPF6TeT0P3pLw\n" +
                "DyQKpG3ze5rMOH+VOSmjUDBOMB0GA1UdDgQWBBQgbt/oOfrxgKp91uCWDLrOi5qb\n" +
                "TzAfBgNVHSMEGDAWgBQgbt/oOfrxgKp91uCWDLrOi5qbTzAMBgNVHRMEBTADAQH/\n" +
                "MAoGCCqGSM49BAMCA0kAMEYCIQDqeOeAbg5mvB7kl483aiRSlVvEUokaaZllnKKQ\n" +
                "auXZewIhAKtE1BtMJZzPjDrQE3+tJI69eSE+nTTH7cUvvIJFTvCd\n" +
                "-----END CERTIFICATE-----"));
        object = pemParser.readObject();
        X509CertificateHolder xh = (X509CertificateHolder) object;
        X509Certificate caCert = new JcaX509CertificateConverter().setProvider("SC") .getCertificate(xh);
        PublicKey caPublicKey = converter.getPublicKey(xh.getSubjectPublicKeyInfo());
        System.out.println("ca publicKey=\n" + caPublicKey);

        Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000);
        signCertificateRequest(caCert, caPublicKey, caPrivateKey, csr, startDate, endDate, publicKey);
        return csr.getEncoded();
    }

    private static String certificateToPEM(X509Certificate certificate) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
        pemWriter.writeObject(certificate);
        pemWriter.close();
        return new String(bos.toByteArray());
    }

    private static X509Certificate signCertificateRequest(X509Certificate caCert, PublicKey caPublicKey, PrivateKey caPrivateKey, PKCS10CertificationRequest csr,
                                                          Date notBefore, Date notAfter, PublicKey csrPublicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, CertificateException, IOException, OperatorCreationException {
        JcaPKCS10CertificationRequest jcaRequest = new JcaPKCS10CertificationRequest(csr);
        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(caCert,
                BigInteger.valueOf(System.currentTimeMillis()), notBefore, notAfter, jcaRequest.getSubject(), jcaRequest.getPublicKey());

        JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
        certificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(caCert))
                .addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(jcaRequest.getPublicKey()))
                .addExtension(Extension.basicConstraints, true, new BasicConstraints(false))
                .addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature))
                .addExtension(Extension.extendedKeyUsage, true, new ExtendedKeyUsage(KeyPurposeId.id_kp_clientAuth));

        // add pkcs extensions
        org.spongycastle.asn1.pkcs.Attribute[] attributes = csr.getAttributes();
        for (org.spongycastle.asn1.pkcs.Attribute attr : attributes) {
            // process extension request
            if (attr.getAttrType().equals(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest)) {
                Extensions extensions = Extensions.getInstance(attr.getAttrValues().getObjectAt(0));
                Enumeration e = extensions.oids();
                while (e.hasMoreElements()) {
                    ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) e.nextElement();
                    Extension ext = extensions.getExtension(oid);
                    certificateBuilder.addExtension(oid, ext.isCritical(), ext.getParsedValue());
                }
            }
        }

        ContentSigner signer = null;
        if (caPrivateKey instanceof org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey) {
            signer = new JcaContentSignerBuilder("SHA256withECDSA").setProvider("SC").build(caPrivateKey);
        } else {
            signer = new JcaContentSignerBuilder("SHA256withRSA").setProvider("SC").build(caPrivateKey);
        }
        X509CertificateHolder certHolder = certificateBuilder.build(signer);
        X509Certificate c = new JcaX509CertificateConverter().setProvider("SC").getCertificate(certHolder);

        System.out.println("\n" + certificateToPEM(c));
        System.out.println("-----------------------\n" + c.getPublicKey());
        System.out.println(csrPublicKey);
        System.out.println(isEqual(c.getPublicKey(), csrPublicKey));

        ContentVerifierProvider contentVerifierProvider = new JcaContentVerifierProviderBuilder()
                .setProvider("SC").build(caPublicKey);
        try {
            if (!certHolder.isSignatureValid(contentVerifierProvider)) {
                System.err.println("signature invalid");
            } else {
                System.out.println("isSignatureValid");
            }
        } catch (CertException e) {
            System.err.println("CertException");
            e.printStackTrace();
        }
        return c;
    }

    private static boolean isEqual(PublicKey publicKey1, PublicKey publicKey2) {
        byte[] pub1 = publicKey1.getEncoded();
        byte[] pub2 = publicKey2.getEncoded();
        if (pub1.length != pub2.length) {
            return false;
        }
        for (int i = 0; i < pub1.length; i++) {
            if (pub1[i] != pub2[i]) {
                return false;
            }
        }
        return true;
    }

    //签名demo
    private static X509Certificate sign(X500Principal subject, PublicKey pubKey,
                                        X500Principal issuer, PrivateKey caKey,
                                        Date begin, Date ends)
            throws GeneralSecurityException, OperatorCreationException {
        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(issuer,
                BigInteger.valueOf(System.currentTimeMillis()), begin, ends, subject, pubKey);
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withECDSA").build(caKey);//.setProvider("SC").build(caKey);
        X509CertificateHolder certHolder = certificateBuilder.build(signer);
        X509Certificate c = new JcaX509CertificateConverter().getCertificate(certHolder);//.setProvider("SC").getCertificate(certHolder);
        return c;
    }
}
