package org.nuist.myblog.util;

import java.util.UUID;

public class UUIDUtil {
    /**
     * 生成一个随机的UUID字符串
     *
     * @return 随机UUID字符串
     */
    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成一个随机的UUID字符串，并去掉其中的连字符
     *
     * @return 去掉连字符的随机UUID字符串
     */
    public static String generateRandomUUIDWithoutHyphens() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 根据名称生成一个基于SHA-1哈希算法的UUID字符串
     *
     * @param name 名称
     * @return 基于名称的UUID字符串
     */
    public static String generateNameBasedUUID(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes()).toString();
    }
}
