package com.aosom.aosombase.jwt;


import com.aosom.aosombase.entity.UserDto;
import org.apache.commons.codec.binary.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {

//    public void testVerifyToken() throws Exception {
//        User user = new User();
//        String token = createToken(user);
//        System.out.println(token);
//
//        JwtClaims jwtClaims = verifyToken(token);
//        System.out.println(jwtClaims.getClaimValue("username"));
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jwtClaims.getIssuedAt().getValueInMillis()));
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jwtClaims.getExpirationTime().getValueInMillis()));
//    }

    /**
     * 生成jwt,SHA256加密
     * @return
     * @throws IOException
     */
    public static String createToken(UserDto user) throws IOException {
        PrivateKey privateKey = getPrivateKey(getPrivateKeyString());
        final JwtClaims claims = new JwtClaims();
        Map<String, Object> claimMap = new HashMap<String, Object>();
        claimMap.put("uid", user.getUid());
        claimMap.put("email", user.getEmail());
        claimMap.put("mobile", user.getMobile());
        claimMap.put("nickname", user.getNickname());
        claims.setClaim("userclaim", claimMap);
        claims.setSubject("usertoken");
        claims.setAudience("aosom");//用于验证签名是否合法，验证方必须包含这些内容才验证通过
        claims.setExpirationTimeMinutesInTheFuture(60*24*30);
        claims.setIssuedAtToNow();

        // Generate the payload
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        jws.setPayload(claims.toJson());
        jws.setKeyIdHeaderValue(UUID.randomUUID().toString());

        // Sign using the private key
        jws.setKey(privateKey);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            return null;
        }
    }

    /**
     * 验证jwt
     * @param token
     * @return
     * @throws Exception
     */
    public static JwtClaims verifyToken(String token) throws Exception {

        try {
            PublicKey publicKey = getPublicKey(getPEMPublicKeyString());

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setVerificationKey(publicKey)
                    .setExpectedAudience("aosom")//用于验证签名是否合法，可以设置多个，且可设置必须存在项，如果jwt中不包含这些内容则不通过
                    .build();

            return jwtConsumer.processToClaims(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPrivateKeyString() throws IOException {
        //    生成方法：安装openssl,执行     openssl genrsa -out private.pem 2048
        FileInputStream fis = new FileInputStream("C:/Users/pc-a/.ssh/private.pem");
        byte[] data =new byte[2000];
        int len =fis.read(data);///返回实际读取的字节数
        String str =new String(data,0,len,"UTF-8");//将实际读取的字节数转换为字符串
//        System.out.print(str);
        fis.close();
        return str;
//        return IOUtils.toString(new FileInputStream("C:/Users/pc-a/.ssh/private.pem"));

    }

    public static String getPEMPublicKeyString() throws IOException {
        //    导出公钥方法：生成私钥(private.pem)后,执行    openssl rsa -in private.pem -outform PEM -pubout -out public.pem
        FileInputStream fis = new FileInputStream("C:/Users/pc-a/.ssh/public.pem");
        byte[] data =new byte[2000];
        int len =fis.read(data);///返回实际读取的字节数
        String str =new String(data,0,len,"UTF-8");//将实际读取的字节数转换为字符串
//        System.out.print(str);
        fis.close();
        return str;
    }

    /**
     * 获取PublicKey对象
     * @param publicKeyBase64
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getPublicKey(String publicKeyBase64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pem = publicKeyBase64
                .replaceAll("\\-*BEGIN.*KEY\\-*", "")
                .replaceAll("\\-*END.*KEY\\-*", "");
        Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pem));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
//        System.out.println(publicKey);
        return publicKey;
    }

    /**
     * 获取PrivateKey对象
     * @param privateKeyBase64
     * @return
     */
    private static  PrivateKey getPrivateKey(String privateKeyBase64) {
        String privKeyPEM = privateKeyBase64
                .replaceAll("\\-*BEGIN.*KEY\\-*", "")
                .replaceAll("\\-*END.*KEY\\-*", "");

        // Base64 decode the data
        byte[] encoded = Base64.decodeBase64(privKeyPEM);

        try {
            DerInputStream derReader = new DerInputStream(encoded);
            DerValue[] seq = derReader.getSequence(0);

            if (seq.length < 9) {
                throw new GeneralSecurityException("Could not read private key");
            }

            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger primeP = seq[4].getBigInteger();
            BigInteger primeQ = seq[5].getBigInteger();
            BigInteger expP = seq[6].getBigInteger();
            BigInteger expQ = seq[7].getBigInteger();
            BigInteger crtCoeff = seq[8].getBigInteger();

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp,
                    primeP, primeQ, expP, expQ, crtCoeff);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(keySpec);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
