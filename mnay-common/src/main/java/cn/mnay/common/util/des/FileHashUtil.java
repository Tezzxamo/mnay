package cn.mnay.common.util.des;

import cn.hutool.core.util.StrUtil;
import cn.mnay.common.enums.error.CodeEnum;
import cn.mnay.common.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 计算文件hash
 */
public class FileHashUtil {
    /**
     * 通过文件实际路径获取文件hash
     *
     * @param fileName 文件绝对路径
     * @param hashType hash类型，SHA1/SHA256/SHA512/MD5，默认为SHA256
     * @return 文件64位hash值
     */
    public static String getFileHashByRealPath(String fileName, String hashType) {
        if (StrUtil.isBlank(fileName)) {
            return null;
        }
        if (StrUtil.isBlank(hashType)) {
            hashType = "SHA-256";
        }
        try (InputStream fis = Files.newInputStream(Paths.get(fileName))) {
            byte[] buffer = new byte[1024];
            MessageDigest md5 = MessageDigest.getInstance(hashType);
            for (int numRead; (numRead = fis.read(buffer)) > 0; ) {
                md5.update(buffer, 0, numRead);
            }
            return toHexString(md5.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new BusinessException(CodeEnum.FILE_HASH_ERROR, "请求特定加密算法但在环境中不可用或IO异常");
        }
    }

    /**
     * 通过MultipartFile文件形式获取文件hash
     *
     * @param file     文件
     * @param hashType hash类型，SHA-1/SHA-256/SHA-512/MD5，默认为SHA256
     * @return 文件hash
     */
    public static String getFileHashByFile(MultipartFile file, String hashType) {
        if (file.isEmpty()) {
            return null;
        }
        if (StrUtil.isBlank(hashType)) {
            hashType = "SHA-256";
        }
        try (InputStream fis = file.getInputStream()) {
            byte[] buffer = new byte[1024];
            MessageDigest md5 = MessageDigest.getInstance(hashType);
            for (int numRead; (numRead = fis.read(buffer)) > 0; ) {
                md5.update(buffer, 0, numRead);
            }
            return toHexString(md5.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new BusinessException(CodeEnum.FILE_HASH_ERROR, "请求特定加密算法但在环境中不可用或IO异常");
        }
    }

    /**
     * 获取字符串的hash
     *
     * @param strSrc   原始字符串
     * @param hashType hash类型，默认SHA256
     * @return 字符串hash
     */
    public static String getStringHash(String strSrc, String hashType) {
        if (null == strSrc || strSrc.trim().isEmpty()) {
            return null;
        }
        if (null == hashType || hashType.trim().isEmpty()) {
            hashType = "SHA-256";
        }
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            md.update(strSrc.getBytes());
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException(CodeEnum.FILE_HASH_ERROR, "请求特定加密算法但在环境中不可用");
        }
    }

    /**
     * byte数组转16进制字符串
     *
     * @param src byte数组
     * @return 16进制字符串
     */
    private static String toHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String getStringHash(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            return toHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException(CodeEnum.FILE_HASH_ERROR, "请求特定加密算法但在环境中不可用");
        }
    }

}