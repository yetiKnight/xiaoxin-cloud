package com.xiaoxin.iam.common.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * IP工具类
 * 提供IP地址获取相关功能
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class IpUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private IpUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 获取客户端真实IP地址
     *
     * @param request HTTP请求
     * @return 客户端IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }

        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }

    /**
     * 判断是否为内网IP
     *
     * @param ip IP地址
     * @return 是否为内网IP
     */
    public static boolean isInternalIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }

        // 内网IP地址段
        return ip.startsWith("10.") || 
               ip.startsWith("172.") || 
               ip.startsWith("192.168.") ||
               "127.0.0.1".equals(ip) ||
               "localhost".equals(ip);
    }

    /**
     * 判断IP地址是否有效
     *
     * @param ip IP地址
     * @return 是否有效
     */
    public static boolean isValidIp(String ip) {
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }

        // 简单的IP地址格式验证
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        try {
            for (String part : parts) {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
