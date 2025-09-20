package com.xiaoxin.iam.common.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 加密工具类
 * 提供常用的加密解密方法
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class CryptoUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private CryptoUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 常量定义 ====================

    /**
     * MD5算法
     */
    public static final String MD5 = "MD5";

    /**
     * SHA-1算法
     */
    public static final String SHA1 = "SHA-1";

    /**
     * SHA-256算法
     */
    public static final String SHA256 = "SHA-256";

    /**
     * SHA-512算法
     */
    public static final String SHA512 = "SHA-512";

    /**
     * AES算法
     */
    public static final String AES = "AES";

    /**
     * DES算法
     */
    public static final String DES = "DES";

    /**
     * 3DES算法
     */
    public static final String DES3 = "DESede";

    /**
     * RSA算法
     */
    public static final String RSA = "RSA";


    /**
     * 默认AES密钥长度
     */
    private static final int DEFAULT_AES_KEY_LENGTH = 128;

    /**
     * 默认DES密钥长度
     */
    private static final int DEFAULT_DES_KEY_LENGTH = 56;

    // ==================== MD5加密 ====================

    /**
     * MD5加密
     */
    public static String md5(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * MD5加密（带盐值）
     */
    public static String md5WithSalt(String input, String salt) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(salt)) {
            return null;
        }
        return md5(input + salt);
    }

    /**
     * MD5加密（多次加密）
     */
    public static String md5Multiple(String input, int times) {
        if (StringUtils.isBlank(input) || times <= 0) {
            return null;
        }
        String result = input;
        for (int i = 0; i < times; i++) {
            result = md5(result);
        }
        return result;
    }

    // ==================== SHA加密 ====================

    /**
     * SHA-1加密
     */
    public static String sha1(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(SHA1);
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1加密失败", e);
        }
    }

    /**
     * SHA-256加密
     */
    public static String sha256(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(SHA256);
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256加密失败", e);
        }
    }

    /**
     * SHA-512加密
     */
    public static String sha512(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(SHA512);
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512加密失败", e);
        }
    }

    /**
     * SHA加密（带盐值）
     */
    public static String shaWithSalt(String input, String salt, String algorithm) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(salt) || StringUtils.isBlank(algorithm)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hash = md.digest((input + salt).getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA加密失败", e);
        }
    }

    // ==================== AES加密 ====================

    /**
     * 生成AES密钥
     */
    public static String generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            keyGenerator.init(DEFAULT_AES_KEY_LENGTH);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }

    /**
     * AES加密
     */
    public static String aesEncrypt(String input, String key) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * AES解密
     */
    public static String aesDecrypt(String encrypted, String key) {
        if (StringUtils.isBlank(encrypted) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    // ==================== DES加密 ====================

    /**
     * 生成DES密钥
     */
    public static String generateDESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
            keyGenerator.init(DEFAULT_DES_KEY_LENGTH);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成DES密钥失败", e);
        }
    }

    /**
     * DES加密
     */
    public static String desEncrypt(String input, String key) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), DES);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("DES加密失败", e);
        }
    }

    /**
     * DES解密
     */
    public static String desDecrypt(String encrypted, String key) {
        if (StringUtils.isBlank(encrypted) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), DES);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("DES解密失败", e);
        }
    }

    // ==================== 3DES加密 ====================

    /**
     * 生成3DES密钥
     */
    public static String generate3DESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(DES3);
            keyGenerator.init(168); // 3DES密钥长度为168位
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成3DES密钥失败", e);
        }
    }

    /**
     * 3DES加密
     */
    public static String des3Encrypt(String input, String key) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), DES3);
            Cipher cipher = Cipher.getInstance(DES3);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("3DES加密失败", e);
        }
    }

    /**
     * 3DES解密
     */
    public static String des3Decrypt(String encrypted, String key) {
        if (StringUtils.isBlank(encrypted) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), DES3);
            Cipher cipher = Cipher.getInstance(DES3);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("3DES解密失败", e);
        }
    }

    // ==================== Base64编码 ====================

    /**
     * Base64编码
     */
    public static String base64Encode(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64编码
     */
    public static String base64Encode(byte[] input) {
        if (input == null || input.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(input);
    }

    /**
     * Base64解码
     */
    public static String base64Decode(String encoded) {
        if (StringUtils.isBlank(encoded)) {
            return null;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Base64解码失败", e);
        }
    }

    /**
     * Base64解码
     */
    public static byte[] base64DecodeToBytes(String encoded) {
        if (StringUtils.isBlank(encoded)) {
            return null;
        }
        try {
            return Base64.getDecoder().decode(encoded);
        } catch (Exception e) {
            throw new RuntimeException("Base64解码失败", e);
        }
    }

    // ==================== 密码生成 ====================

    /**
     * 生成随机密码
     */
    public static String generatePassword(int length) {
        if (length <= 0) {
            return null;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    /**
     * 生成随机密码（只包含字母和数字）
     */
    public static String generateAlphanumericPassword(int length) {
        if (length <= 0) {
            return null;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    /**
     * 生成随机密码（只包含数字）
     */
    public static String generateNumericPassword(int length) {
        if (length <= 0) {
            return null;
        }
        String chars = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    // ==================== 盐值生成 ====================

    /**
     * 生成随机盐值
     */
    public static String generateSalt(int length) {
        if (length <= 0) {
            return null;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < length; i++) {
            salt.append(chars.charAt(random.nextInt(chars.length())));
        }
        return salt.toString();
    }

    /**
     * 生成随机盐值（只包含字母和数字）
     */
    public static String generateAlphanumericSalt(int length) {
        if (length <= 0) {
            return null;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < length; i++) {
            salt.append(chars.charAt(random.nextInt(chars.length())));
        }
        return salt.toString();
    }

    // ==================== 密码验证 ====================

    /**
     * 验证密码
     */
    public static boolean verifyPassword(String input, String hashedPassword, String salt, String algorithm) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(hashedPassword) || 
            StringUtils.isBlank(salt) || StringUtils.isBlank(algorithm)) {
            return false;
        }
        String hashedInput = hashWithSalt(input, salt, algorithm);
        return hashedInput != null && hashedInput.equals(hashedPassword);
    }

    /**
     * 验证MD5密码
     */
    public static boolean verifyMD5Password(String input, String hashedPassword, String salt) {
        return verifyPassword(input, hashedPassword, salt, MD5);
    }

    /**
     * 验证SHA-256密码
     */
    public static boolean verifySHA256Password(String input, String hashedPassword, String salt) {
        return verifyPassword(input, hashedPassword, salt, SHA256);
    }

    // ==================== 哈希计算 ====================

    /**
     * 计算哈希值
     */
    public static String hash(String input, String algorithm) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(algorithm)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("哈希计算失败", e);
        }
    }

    /**
     * 计算带盐值的哈希值
     */
    public static String hashWithSalt(String input, String salt, String algorithm) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(salt) || StringUtils.isBlank(algorithm)) {
            return null;
        }
        return hash(input + salt, algorithm);
    }

    /**
     * 计算文件哈希值
     */
    public static String hashFile(String filePath, String algorithm) {
        if (StringUtils.isBlank(filePath) || StringUtils.isBlank(algorithm)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] fileBytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath));
            byte[] hash = md.digest(fileBytes);
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("文件哈希计算失败", e);
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 字节数组转十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * 十六进制字符串转字节数组
     */
    public static byte[] hexToBytes(String hex) {
        if (StringUtils.isBlank(hex)) {
            return null;
        }
        int len = hex.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("十六进制字符串长度必须为偶数");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 生成UUID
     */
    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * 生成UUID（不含连字符）
     */
    public static String generateUUIDWithoutHyphens() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成随机字符串
     */
    public static String generateRandomString(int length) {
        if (length <= 0) {
            return null;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }

    /**
     * 生成随机数字字符串
     */
    public static String generateRandomNumberString(int length) {
        if (length <= 0) {
            return null;
        }
        String chars = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }

    /**
     * 生成随机字母字符串
     */
    public static String generateRandomLetterString(int length) {
        if (length <= 0) {
            return null;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }
}
