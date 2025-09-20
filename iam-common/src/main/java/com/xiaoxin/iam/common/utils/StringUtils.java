package com.xiaoxin.iam.common.utils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 * 提供常用的字符串操作方法
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class StringUtils {

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    /**
     * 空格字符串
     */
    public static final String SPACE = " ";

    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 连字符
     */
    public static final String HYPHEN = "-";

    /**
     * 点号
     */
    public static final String DOT = ".";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 分号
     */
    public static final String SEMICOLON = ";";

    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * 换行符
     */
    public static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 私有构造函数，防止实例化
     */
    private StringUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 空值判断 ====================

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空（包括空白字符）
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否不为空（包括空白字符）
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断字符串是否为空或null
     */
    public static boolean isEmptyOrNull(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否不为空且不为null
     */
    public static boolean isNotEmptyAndNotNull(String str) {
        return str != null && str.length() > 0;
    }

    // ==================== 字符串处理 ====================

    /**
     * 获取字符串长度
     */
    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 去除字符串两端空白
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 去除字符串左端空白
     */
    public static String trimLeft(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        int st = 0;
        while ((st < len) && (str.charAt(st) <= ' ')) {
            st++;
        }
        return (st > 0) ? str.substring(st) : str;
    }

    /**
     * 去除字符串右端空白
     */
    public static String trimRight(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        while ((len > 0) && (str.charAt(len - 1) <= ' ')) {
            len--;
        }
        return (len < str.length()) ? str.substring(0, len) : str;
    }

    /**
     * 去除字符串所有空白
     */
    public static String trimAll(String str) {
        return str == null ? null : str.replaceAll("\\s+", "");
    }

    /**
     * 去除字符串中的指定字符
     */
    public static String remove(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        return str.replace(remove, EMPTY);
    }

    /**
     * 去除字符串中的指定字符（忽略大小写）
     */
    public static String removeIgnoreCase(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        return str.replaceAll("(?i)" + Pattern.quote(remove), EMPTY);
    }

    /**
     * 去除字符串中的空白字符
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", EMPTY);
    }

    // ==================== 字符串比较 ====================

    /**
     * 比较两个字符串是否相等
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 比较两个字符串是否相等（忽略大小写）
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == str2) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 比较两个字符串的大小
     */
    public static int compare(String str1, String str2) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return -1;
        }
        if (str2 == null) {
            return 1;
        }
        return str1.compareTo(str2);
    }

    /**
     * 比较两个字符串的大小（忽略大小写）
     */
    public static int compareIgnoreCase(String str1, String str2) {
        if (str1 == str2) {
            return 0;
        }
        if (str1 == null) {
            return -1;
        }
        if (str2 == null) {
            return 1;
        }
        return str1.compareToIgnoreCase(str2);
    }

    // ==================== 字符串查找 ====================

    /**
     * 判断字符串是否包含指定子串
     */
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.contains(searchStr);
    }

    /**
     * 判断字符串是否包含指定子串（忽略大小写）
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }

    /**
     * 查找子串在字符串中的位置
     */
    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr);
    }

    /**
     * 查找子串在字符串中的位置（从指定位置开始）
     */
    public static int indexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr, startPos);
    }

    /**
     * 查找子串在字符串中最后出现的位置
     */
    public static int lastIndexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr);
    }

    /**
     * 查找子串在字符串中最后出现的位置（从指定位置开始）
     */
    public static int lastIndexOf(String str, String searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr, startPos);
    }

    // ==================== 字符串截取 ====================

    /**
     * 截取字符串
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return EMPTY;
        }
        return str.substring(start);
    }

    /**
     * 截取字符串
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return EMPTY;
        }
        if (start < 0) {
            start = 0;
        }
        if (start == end) {
            return EMPTY;
        }
        return str.substring(start, end);
    }

    /**
     * 截取字符串（从左开始）
     */
    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    /**
     * 截取字符串（从右开始）
     */
    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    /**
     * 截取字符串（从中间开始）
     */
    public static String mid(String str, int pos, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0 || pos > str.length()) {
            return EMPTY;
        }
        if (pos < 0) {
            pos = 0;
        }
        if (str.length() <= (pos + len)) {
            return str.substring(pos);
        }
        return str.substring(pos, pos + len);
    }

    // ==================== 字符串替换 ====================

    /**
     * 替换字符串中的指定子串
     */
    public static String replace(String str, String searchStr, String replacement) {
        if (isEmpty(str) || isEmpty(searchStr)) {
            return str;
        }
        if (replacement == null) {
            replacement = EMPTY;
        }
        return str.replace(searchStr, replacement);
    }

    /**
     * 替换字符串中的指定子串（忽略大小写）
     */
    public static String replaceIgnoreCase(String str, String searchStr, String replacement) {
        if (isEmpty(str) || isEmpty(searchStr)) {
            return str;
        }
        if (replacement == null) {
            replacement = EMPTY;
        }
        return str.replaceAll("(?i)" + Pattern.quote(searchStr), replacement);
    }

    /**
     * 替换字符串中的第一个匹配项
     */
    public static String replaceFirst(String str, String searchStr, String replacement) {
        if (isEmpty(str) || isEmpty(searchStr)) {
            return str;
        }
        if (replacement == null) {
            replacement = EMPTY;
        }
        return str.replaceFirst(Pattern.quote(searchStr), replacement);
    }

    /**
     * 替换字符串中的第一个匹配项（忽略大小写）
     */
    public static String replaceFirstIgnoreCase(String str, String searchStr, String replacement) {
        if (isEmpty(str) || isEmpty(searchStr)) {
            return str;
        }
        if (replacement == null) {
            replacement = EMPTY;
        }
        return str.replaceFirst("(?i)" + Pattern.quote(searchStr), replacement);
    }

    // ==================== 字符串分割 ====================

    /**
     * 分割字符串
     */
    public static String[] split(String str, String separator) {
        if (isEmpty(str)) {
            return new String[0];
        }
        if (isEmpty(separator)) {
            return new String[]{str};
        }
        return str.split(Pattern.quote(separator));
    }

    /**
     * 分割字符串（限制分割次数）
     */
    public static String[] split(String str, String separator, int limit) {
        if (isEmpty(str)) {
            return new String[0];
        }
        if (isEmpty(separator)) {
            return new String[]{str};
        }
        return str.split(Pattern.quote(separator), limit);
    }

    /**
     * 分割字符串为List
     */
    public static List<String> splitToList(String str, String separator) {
        if (isEmpty(str)) {
            return new ArrayList<>();
        }
        if (isEmpty(separator)) {
            return Arrays.asList(str);
        }
        return Arrays.asList(str.split(Pattern.quote(separator)));
    }

    /**
     * 分割字符串为Set
     */
    public static Set<String> splitToSet(String str, String separator) {
        if (isEmpty(str)) {
            return new HashSet<>();
        }
        if (isEmpty(separator)) {
            return new HashSet<>(Arrays.asList(str));
        }
        return new HashSet<>(Arrays.asList(str.split(Pattern.quote(separator))));
    }

    // ==================== 字符串连接 ====================

    /**
     * 连接字符串数组
     */
    public static String join(String[] array, String separator) {
        if (array == null || array.length == 0) {
            return EMPTY;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * 连接字符串集合
     */
    public static String join(Collection<String> collection, String separator) {
        if (collection == null || collection.isEmpty()) {
            return EMPTY;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(separator));
    }

    /**
     * 连接字符串集合（过滤空值）
     */
    public static String joinSkipNull(Collection<String> collection, String separator) {
        if (collection == null || collection.isEmpty()) {
            return EMPTY;
        }
        if (separator == null) {
            separator = EMPTY;
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.joining(separator));
    }

    // ==================== 字符串填充 ====================

    /**
     * 左填充字符串
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen >= size) {
            return str;
        }
        return repeat(padChar, size - strLen) + str;
    }

    /**
     * 左填充字符串
     */
    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = SPACE;
        }
        int strLen = str.length();
        if (strLen >= size) {
            return str;
        }
        int padLen = size - strLen;
        int padStrLen = padStr.length();
        if (padStrLen == 1) {
            return leftPad(str, size, padStr.charAt(0));
        }
        if (padLen == padStrLen) {
            return padStr + str;
        } else if (padLen < padStrLen) {
            return padStr.substring(0, padLen) + str;
        } else {
            char[] padding = new char[padLen];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < padLen; i++) {
                padding[i] = padChars[i % padStrLen];
            }
            return new String(padding) + str;
        }
    }

    /**
     * 右填充字符串
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen >= size) {
            return str;
        }
        return str + repeat(padChar, size - strLen);
    }

    /**
     * 右填充字符串
     */
    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = SPACE;
        }
        int strLen = str.length();
        if (strLen >= size) {
            return str;
        }
        int padLen = size - strLen;
        int padStrLen = padStr.length();
        if (padStrLen == 1) {
            return rightPad(str, size, padStr.charAt(0));
        }
        if (padLen == padStrLen) {
            return str + padStr;
        } else if (padLen < padStrLen) {
            return str + padStr.substring(0, padLen);
        } else {
            char[] padding = new char[padLen];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < padLen; i++) {
                padding[i] = padChars[i % padStrLen];
            }
            return str + new String(padding);
        }
    }

    /**
     * 重复字符串
     */
    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        if (repeat == 1) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repeat; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 重复字符
     */
    public static String repeat(char ch, int repeat) {
        if (repeat <= 0) {
            return EMPTY;
        }
        char[] buf = new char[repeat];
        for (int i = 0; i < repeat; i++) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    // ==================== 字符串转换 ====================

    /**
     * 转换为小写
     */
    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    /**
     * 转换为大写
     */
    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 驼峰转下划线
     */
    public static String camelToUnderline(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * 下划线转驼峰
     */
    public static String underlineToCamel(String str) {
        if (isEmpty(str)) {
            return str;
        }
        String[] parts = str.split("_");
        if (parts.length == 1) {
            return parts[0];
        }
        StringBuilder result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            result.append(capitalize(parts[i]));
        }
        return result.toString();
    }

    /**
     * 驼峰转连字符
     */
    public static String camelToHyphen(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }

    /**
     * 连字符转驼峰
     */
    public static String hyphenToCamel(String str) {
        if (isEmpty(str)) {
            return str;
        }
        String[] parts = str.split("-");
        if (parts.length == 1) {
            return parts[0];
        }
        StringBuilder result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            result.append(capitalize(parts[i]));
        }
        return result.toString();
    }

    // ==================== 字符串验证 ====================

    /**
     * 判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为整数
     */
    public static boolean isInteger(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为长整数
     */
    public static boolean isLong(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为邮箱格式
     */
    public static boolean isEmail(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, str);
    }

    /**
     * 判断字符串是否为手机号格式
     */
    public static boolean isPhone(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String phoneRegex = "^1[3-9]\\d{9}$";
        return Pattern.matches(phoneRegex, str);
    }

    /**
     * 判断字符串是否为身份证号格式
     */
    public static boolean isIdCard(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String idCardRegex = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        return Pattern.matches(idCardRegex, str);
    }

    /**
     * 判断字符串是否为URL格式
     */
    public static boolean isUrl(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String urlRegex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return Pattern.matches(urlRegex, str);
    }

    // ==================== 字符串统计 ====================

    /**
     * 统计字符串中指定字符的个数
     */
    public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    /**
     * 统计字符串中指定字符的个数
     */
    public static int countMatches(String str, char ch) {
        if (isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }

    /**
     * 统计字符串长度（按字符计算）
     */
    public static int getLength(String str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 统计字符串长度（按字节计算）
     */
    public static int byteLength(String str) {
        return str == null ? 0 : str.getBytes().length;
    }

    // ==================== 字符串反转 ====================

    /**
     * 反转字符串
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 反转字符串（按单词）
     */
    public static String reverseWords(String str) {
        if (isEmpty(str)) {
            return str;
        }
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            if (i < words.length - 1) {
                result.append(" ");
            }
            result.append(words[i]);
        }
        return result.toString();
    }

    // ==================== 其他工具方法 ====================

    /**
     * 获取字符串的默认值
     */
    public static String defaultIfEmpty(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    /**
     * 获取字符串的默认值
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    /**
     * 获取字符串的默认值
     */
    public static String defaultIfNull(String str, String defaultStr) {
        return str == null ? defaultStr : str;
    }

    /**
     * 去除字符串中的HTML标签
     */
    public static String stripHtml(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("<[^>]+>", "");
    }

    /**
     * 去除字符串中的特殊字符
     */
    public static String stripSpecialChars(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "");
    }

    /**
     * 去除字符串中的数字
     */
    public static String stripNumbers(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\d", "");
    }

    /**
     * 去除字符串中的字母
     */
    public static String stripLetters(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("[a-zA-Z]", "");
    }

    /**
     * 去除字符串中的中文
     */
    public static String stripChinese(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("[\\u4e00-\\u9fa5]", "");
    }

    /**
     * 获取字符串的哈希值
     */
    public static int hashCode(String str) {
        return str == null ? 0 : str.hashCode();
    }

    /**
     * 获取字符串的哈希值（忽略大小写）
     */
    public static int hashCodeIgnoreCase(String str) {
        return str == null ? 0 : str.toLowerCase().hashCode();
    }
}
