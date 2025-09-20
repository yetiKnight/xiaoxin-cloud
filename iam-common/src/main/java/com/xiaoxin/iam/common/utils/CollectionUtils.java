package com.xiaoxin.iam.common.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合工具类
 * 提供常用的集合操作方法
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class CollectionUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private CollectionUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 空值判断 ====================

    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否不为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断Map是否不为空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断数组是否为空
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否不为空
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    // ==================== 集合创建 ====================

    /**
     * 创建空List
     */
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    /**
     * 创建空Set
     */
    public static <T> Set<T> emptySet() {
        return new HashSet<>();
    }

    /**
     * 创建空Map
     */
    public static <K, V> Map<K, V> emptyMap() {
        return new HashMap<>();
    }

    /**
     * 创建List
     */
    @SafeVarargs
    public static <T> List<T> listOf(T... elements) {
        if (elements == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(elements);
    }

    /**
     * 创建Set
     */
    @SafeVarargs
    public static <T> Set<T> setOf(T... elements) {
        if (elements == null) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(elements));
    }

    /**
     * 创建Map
     */
    public static <K, V> Map<K, V> mapOf(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * 创建Map
     */
    public static <K, V> Map<K, V> mapOf(K key1, V value1, K key2, V value2) {
        Map<K, V> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    /**
     * 创建Map
     */
    public static <K, V> Map<K, V> mapOf(K key1, V value1, K key2, V value2, K key3, V value3) {
        Map<K, V> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        return map;
    }

    // ==================== 集合大小 ====================

    /**
     * 获取集合大小
     */
    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * 获取Map大小
     */
    public static int size(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    /**
     * 获取数组长度
     */
    public static int length(Object[] array) {
        return array == null ? 0 : array.length;
    }

    // ==================== 集合转换 ====================

    /**
     * 数组转List
     */
    public static <T> List<T> arrayToList(T[] array) {
        if (array == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(array);
    }

    /**
     * 数组转Set
     */
    public static <T> Set<T> arrayToSet(T[] array) {
        if (array == null) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(array));
    }

    /**
     * List转Set
     */
    public static <T> Set<T> listToSet(List<T> list) {
        if (list == null) {
            return new HashSet<>();
        }
        return new HashSet<>(list);
    }

    /**
     * Set转List
     */
    public static <T> List<T> setToList(Set<T> set) {
        if (set == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(set);
    }

    /**
     * Collection转List
     */
    public static <T> List<T> collectionToList(Collection<T> collection) {
        if (collection == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(collection);
    }

    /**
     * Collection转Set
     */
    public static <T> Set<T> collectionToSet(Collection<T> collection) {
        if (collection == null) {
            return new HashSet<>();
        }
        return new HashSet<>(collection);
    }

    // ==================== 集合过滤 ====================

    /**
     * 过滤集合
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * 过滤集合
     */
    public static <T> Set<T> filter(Set<T> set, Predicate<T> predicate) {
        if (isEmpty(set)) {
            return new HashSet<>();
        }
        return set.stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    /**
     * 过滤Map
     */
    public static <K, V> Map<K, V> filter(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
        if (isEmpty(map)) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .filter(predicate)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // ==================== 集合映射 ====================

    /**
     * 映射集合
     */
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * 映射集合
     */
    public static <T, R> Set<R> map(Set<T> set, Function<T, R> mapper) {
        if (isEmpty(set)) {
            return new HashSet<>();
        }
        return set.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

    /**
     * 映射Map的键
     */
    public static <K, V, R> Map<R, V> mapKeys(Map<K, V> map, Function<K, R> keyMapper) {
        if (isEmpty(map)) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> keyMapper.apply(entry.getKey()),
                        Map.Entry::getValue
                ));
    }

    /**
     * 映射Map的值
     */
    public static <K, V, R> Map<K, R> mapValues(Map<K, V> map, Function<V, R> valueMapper) {
        if (isEmpty(map)) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> valueMapper.apply(entry.getValue())
                ));
    }

    // ==================== 集合查找 ====================

    /**
     * 查找第一个匹配的元素
     */
    public static <T> T findFirst(List<T> list, Predicate<T> predicate) {
        if (isEmpty(list)) {
            return null;
        }
        return list.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * 查找第一个匹配的元素
     */
    public static <T> T findFirst(Set<T> set, Predicate<T> predicate) {
        if (isEmpty(set)) {
            return null;
        }
        return set.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * 查找所有匹配的元素
     */
    public static <T> List<T> findAll(List<T> list, Predicate<T> predicate) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * 查找所有匹配的元素
     */
    public static <T> Set<T> findAll(Set<T> set, Predicate<T> predicate) {
        if (isEmpty(set)) {
            return new HashSet<>();
        }
        return set.stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    /**
     * 查找Map中的值
     */
    public static <K, V> V findValue(Map<K, V> map, K key) {
        if (isEmpty(map)) {
            return null;
        }
        return map.get(key);
    }

    /**
     * 查找Map中的值（带默认值）
     */
    public static <K, V> V findValue(Map<K, V> map, K key, V defaultValue) {
        if (isEmpty(map)) {
            return defaultValue;
        }
        return map.getOrDefault(key, defaultValue);
    }

    // ==================== 集合排序 ====================

    /**
     * 排序List
     */
    public static <T extends Comparable<T>> List<T> sort(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * 排序List（自定义比较器）
     */
    public static <T> List<T> sort(List<T> list, Comparator<T> comparator) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * 排序Set
     */
    public static <T extends Comparable<T>> List<T> sort(Set<T> set) {
        if (isEmpty(set)) {
            return new ArrayList<>();
        }
        return set.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * 排序Set（自定义比较器）
     */
    public static <T> List<T> sort(Set<T> set, Comparator<T> comparator) {
        if (isEmpty(set)) {
            return new ArrayList<>();
        }
        return set.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    // ==================== 集合去重 ====================

    /**
     * 去重List
     */
    public static <T> List<T> distinct(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 去重Set
     */
    public static <T> Set<T> distinct(Set<T> set) {
        if (isEmpty(set)) {
            return new HashSet<>();
        }
        return new HashSet<>(set);
    }

    /**
     * 去重数组
     */
    public static <T> List<T> distinct(T[] array) {
        if (isEmpty(array)) {
            return new ArrayList<>();
        }
        return Arrays.stream(array)
                .distinct()
                .collect(Collectors.toList());
    }

    // ==================== 集合合并 ====================

    /**
     * 合并List
     */
    public static <T> List<T> merge(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();
        if (isNotEmpty(list1)) {
            result.addAll(list1);
        }
        if (isNotEmpty(list2)) {
            result.addAll(list2);
        }
        return result;
    }

    /**
     * 合并Set
     */
    public static <T> Set<T> merge(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>();
        if (isNotEmpty(set1)) {
            result.addAll(set1);
        }
        if (isNotEmpty(set2)) {
            result.addAll(set2);
        }
        return result;
    }

    /**
     * 合并Map
     */
    public static <K, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2) {
        Map<K, V> result = new HashMap<>();
        if (isNotEmpty(map1)) {
            result.putAll(map1);
        }
        if (isNotEmpty(map2)) {
            result.putAll(map2);
        }
        return result;
    }

    /**
     * 合并多个List
     */
    @SafeVarargs
    public static <T> List<T> mergeAll(List<T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<T> list : lists) {
            if (isNotEmpty(list)) {
                result.addAll(list);
            }
        }
        return result;
    }

    /**
     * 合并多个Set
     */
    @SafeVarargs
    public static <T> Set<T> mergeAll(Set<T>... sets) {
        Set<T> result = new HashSet<>();
        for (Set<T> set : sets) {
            if (isNotEmpty(set)) {
                result.addAll(set);
            }
        }
        return result;
    }

    // ==================== 集合交集 ====================

    /**
     * 计算两个List的交集
     */
    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        if (isEmpty(list1) || isEmpty(list2)) {
            return new ArrayList<>();
        }
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }

    /**
     * 计算两个Set的交集
     */
    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        if (isEmpty(set1) || isEmpty(set2)) {
            return new HashSet<>();
        }
        return set1.stream()
                .filter(set2::contains)
                .collect(Collectors.toSet());
    }

    // ==================== 集合差集 ====================

    /**
     * 计算两个List的差集
     */
    public static <T> List<T> difference(List<T> list1, List<T> list2) {
        if (isEmpty(list1)) {
            return new ArrayList<>();
        }
        if (isEmpty(list2)) {
            return new ArrayList<>(list1);
        }
        return list1.stream()
                .filter(item -> !list2.contains(item))
                .collect(Collectors.toList());
    }

    /**
     * 计算两个Set的差集
     */
    public static <T> Set<T> difference(Set<T> set1, Set<T> set2) {
        if (isEmpty(set1)) {
            return new HashSet<>();
        }
        if (isEmpty(set2)) {
            return new HashSet<>(set1);
        }
        return set1.stream()
                .filter(item -> !set2.contains(item))
                .collect(Collectors.toSet());
    }

    // ==================== 集合包含 ====================

    /**
     * 判断集合是否包含指定元素
     */
    public static <T> boolean contains(Collection<T> collection, T element) {
        return collection != null && collection.contains(element);
    }

    /**
     * 判断集合是否包含所有指定元素
     */
    public static <T> boolean containsAll(Collection<T> collection, Collection<T> elements) {
        return collection != null && elements != null && collection.containsAll(elements);
    }

    /**
     * 判断集合是否包含任意指定元素
     */
    public static <T> boolean containsAny(Collection<T> collection, Collection<T> elements) {
        if (collection == null || elements == null) {
            return false;
        }
        return elements.stream().anyMatch(collection::contains);
    }

    // ==================== 集合分组 ====================

    /**
     * 按指定字段分组
     */
    public static <T, K> Map<K, List<T>> groupBy(List<T> list, Function<T, K> classifier) {
        if (isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .collect(Collectors.groupingBy(classifier));
    }

    /**
     * 按指定字段分组
     */
    public static <T, K> Map<K, Set<T>> groupByToSet(List<T> list, Function<T, K> classifier) {
        if (isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .collect(Collectors.groupingBy(classifier, Collectors.toSet()));
    }

    // ==================== 集合统计 ====================

    /**
     * 统计集合大小
     */
    public static int count(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * 统计满足条件的元素个数
     */
    public static <T> long count(List<T> list, Predicate<T> predicate) {
        if (isEmpty(list)) {
            return 0;
        }
        return list.stream()
                .filter(predicate)
                .count();
    }

    /**
     * 统计满足条件的元素个数
     */
    public static <T> long count(Set<T> set, Predicate<T> predicate) {
        if (isEmpty(set)) {
            return 0;
        }
        return set.stream()
                .filter(predicate)
                .count();
    }

    // ==================== 集合切片 ====================

    /**
     * 获取集合的子集
     */
    public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > list.size()) {
            toIndex = list.size();
        }
        if (fromIndex >= toIndex) {
            return new ArrayList<>();
        }
        return list.subList(fromIndex, toIndex);
    }

    /**
     * 获取集合的前N个元素
     */
    public static <T> List<T> take(List<T> list, int n) {
        if (isEmpty(list) || n <= 0) {
            return new ArrayList<>();
        }
        return list.stream()
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * 获取集合的后N个元素
     */
    public static <T> List<T> takeLast(List<T> list, int n) {
        if (isEmpty(list) || n <= 0) {
            return new ArrayList<>();
        }
        int size = list.size();
        if (n >= size) {
            return new ArrayList<>(list);
        }
        return list.subList(size - n, size);
    }

    // ==================== 集合随机 ====================

    /**
     * 随机获取集合中的一个元素
     */
    public static <T> T random(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    /**
     * 随机获取集合中的一个元素
     */
    public static <T> T random(Set<T> set) {
        if (isEmpty(set)) {
            return null;
        }
        List<T> list = new ArrayList<>(set);
        return random(list);
    }

    /**
     * 随机获取集合中的N个元素
     */
    public static <T> List<T> random(List<T> list, int n) {
        if (isEmpty(list) || n <= 0) {
            return new ArrayList<>();
        }
        if (n >= list.size()) {
            return new ArrayList<>(list);
        }
        List<T> result = new ArrayList<>();
        List<T> temp = new ArrayList<>(list);
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int index = random.nextInt(temp.size());
            result.add(temp.remove(index));
        }
        return result;
    }

    // ==================== 集合反转 ====================

    /**
     * 反转List
     */
    public static <T> List<T> reverse(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>(list);
        Collections.reverse(result);
        return result;
    }

    // ==================== 集合去空 ====================

    /**
     * 去除List中的null元素
     */
    public static <T> List<T> removeNulls(List<T> list) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 去除Set中的null元素
     */
    public static <T> Set<T> removeNulls(Set<T> set) {
        if (isEmpty(set)) {
            return new HashSet<>();
        }
        return set.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * 去除Map中的null值
     */
    public static <K, V> Map<K, V> removeNullValues(Map<K, V> map) {
        if (isEmpty(map)) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // ==================== 集合转换 ====================

    /**
     * 将集合转换为字符串
     */
    public static <T> String toString(Collection<T> collection, String separator) {
        if (isEmpty(collection)) {
            return "";
        }
        return collection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(separator));
    }

    /**
     * 将集合转换为字符串（默认分隔符为逗号）
     */
    public static <T> String toString(Collection<T> collection) {
        return toString(collection, ",");
    }

    /**
     * 将Map转换为字符串
     */
    public static <K, V> String toString(Map<K, V> map, String keyValueSeparator, String entrySeparator) {
        if (isEmpty(map)) {
            return "";
        }
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + keyValueSeparator + entry.getValue())
                .collect(Collectors.joining(entrySeparator));
    }

    /**
     * 将Map转换为字符串（默认分隔符）
     */
    public static <K, V> String toString(Map<K, V> map) {
        return toString(map, "=", ",");
    }
}
