package com.xiaoxin.iam.common.utils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 文件工具类
 * 提供常用的文件操作方法
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class FileUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private FileUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 文件存在性检查 ====================

    /**
     * 判断文件是否存在
     */
    public static boolean exists(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 判断文件是否存在
     */
    public static boolean exists(Path path) {
        return path != null && Files.exists(path);
    }

    /**
     * 判断文件是否存在
     */
    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    /**
     * 判断是否为文件
     */
    public static boolean isFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        return Files.isRegularFile(Paths.get(filePath));
    }

    /**
     * 判断是否为文件
     */
    public static boolean isFile(Path path) {
        return path != null && Files.isRegularFile(path);
    }

    /**
     * 判断是否为文件
     */
    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }

    /**
     * 判断是否为目录
     */
    public static boolean isDirectory(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            return false;
        }
        return Files.isDirectory(Paths.get(dirPath));
    }

    /**
     * 判断是否为目录
     */
    public static boolean isDirectory(Path path) {
        return path != null && Files.isDirectory(path);
    }

    /**
     * 判断是否为目录
     */
    public static boolean isDirectory(File file) {
        return file != null && file.isDirectory();
    }

    // ==================== 文件创建 ====================

    /**
     * 创建文件
     */
    public static boolean createFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                return true;
            }
            // 创建父目录
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 创建目录
     */
    public static boolean createDirectory(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            return false;
        }
        try {
            Path path = Paths.get(dirPath);
            if (Files.exists(path)) {
                return true;
            }
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 创建临时文件
     */
    public static File createTempFile(String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 创建临时文件
     */
    public static File createTempFile(String prefix, String suffix, File directory) {
        try {
            return File.createTempFile(prefix, suffix, directory);
        } catch (IOException e) {
            return null;
        }
    }

    // ==================== 文件删除 ====================

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(Path path) {
        if (path == null) {
            return false;
        }
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }
        return file.delete();
    }

    /**
     * 删除目录（递归删除）
     */
    public static boolean deleteDirectory(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            return false;
        }
        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                return true;
            }
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除目录（递归删除）
     */
    public static boolean deleteDirectory(Path path) {
        if (path == null) {
            return false;
        }
        try {
            if (!Files.exists(path)) {
                return true;
            }
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ==================== 文件复制 ====================

    /**
     * 复制文件
     */
    public static boolean copyFile(String sourcePath, String targetPath) {
        if (StringUtils.isBlank(sourcePath) || StringUtils.isBlank(targetPath)) {
            return false;
        }
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            if (!Files.exists(source)) {
                return false;
            }
            
            // 创建目标目录
            Path targetParent = target.getParent();
            if (targetParent != null && !Files.exists(targetParent)) {
                Files.createDirectories(targetParent);
            }
            
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 复制文件
     */
    public static boolean copyFile(Path source, Path target) {
        if (source == null || target == null) {
            return false;
        }
        try {
            if (!Files.exists(source)) {
                return false;
            }
            
            // 创建目标目录
            Path targetParent = target.getParent();
            if (targetParent != null && !Files.exists(targetParent)) {
                Files.createDirectories(targetParent);
            }
            
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 复制目录
     */
    public static boolean copyDirectory(String sourcePath, String targetPath) {
        if (StringUtils.isBlank(sourcePath) || StringUtils.isBlank(targetPath)) {
            return false;
        }
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            if (!Files.exists(source) || !Files.isDirectory(source)) {
                return false;
            }
            
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = target.resolve(source.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = target.resolve(source.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ==================== 文件移动 ====================

    /**
     * 移动文件
     */
    public static boolean moveFile(String sourcePath, String targetPath) {
        if (StringUtils.isBlank(sourcePath) || StringUtils.isBlank(targetPath)) {
            return false;
        }
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            if (!Files.exists(source)) {
                return false;
            }
            
            // 创建目标目录
            Path targetParent = target.getParent();
            if (targetParent != null && !Files.exists(targetParent)) {
                Files.createDirectories(targetParent);
            }
            
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 移动文件
     */
    public static boolean moveFile(Path source, Path target) {
        if (source == null || target == null) {
            return false;
        }
        try {
            if (!Files.exists(source)) {
                return false;
            }
            
            // 创建目标目录
            Path targetParent = target.getParent();
            if (targetParent != null && !Files.exists(targetParent)) {
                Files.createDirectories(targetParent);
            }
            
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ==================== 文件读取 ====================

    /**
     * 读取文件内容为字符串
     */
    public static String readFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取文件内容为字符串
     */
    public static String readFile(Path path) {
        if (path == null) {
            return null;
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取文件内容为字节数组
     */
    public static byte[] readFileBytes(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取文件内容为字节数组
     */
    public static byte[] readFileBytes(Path path) {
        if (path == null) {
            return null;
        }
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取文件内容为行列表
     */
    public static List<String> readLines(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * 读取文件内容为行列表
     */
    public static List<String> readLines(Path path) {
        if (path == null) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    // ==================== 文件写入 ====================

    /**
     * 写入字符串到文件
     */
    public static boolean writeFile(String filePath, String content) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Path path = Paths.get(filePath);
            // 创建父目录
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.writeString(path, content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 写入字符串到文件
     */
    public static boolean writeFile(Path path, String content) {
        if (path == null) {
            return false;
        }
        try {
            // 创建父目录
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.writeString(path, content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 写入字节数组到文件
     */
    public static boolean writeFile(String filePath, byte[] content) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Path path = Paths.get(filePath);
            // 创建父目录
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.write(path, content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 写入字节数组到文件
     */
    public static boolean writeFile(Path path, byte[] content) {
        if (path == null) {
            return false;
        }
        try {
            // 创建父目录
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.write(path, content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 写入行列表到文件
     */
    public static boolean writeLines(String filePath, List<String> lines) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Path path = Paths.get(filePath);
            // 创建父目录
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.write(path, lines);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 写入行列表到文件
     */
    public static boolean writeLines(Path path, List<String> lines) {
        if (path == null) {
            return false;
        }
        try {
            // 创建父目录
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.write(path, lines);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ==================== 文件追加 ====================

    /**
     * 追加字符串到文件
     */
    public static boolean appendFile(String filePath, String content) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Path path = Paths.get(filePath);
            // 创建父目录
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 追加字符串到文件
     */
    public static boolean appendFile(Path path, String content) {
        if (path == null) {
            return false;
        }
        try {
            // 创建父目录
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ==================== 文件信息 ====================

    /**
     * 获取文件大小
     */
    public static long getFileSize(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return 0;
        }
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * 获取文件大小
     */
    public static long getFileSize(Path path) {
        if (path == null) {
            return 0;
        }
        try {
            return Files.size(path);
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * 获取文件大小
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        return file.length();
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) {
            return null;
        }
        return filePath.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * 获取文件名（不含扩展名）
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        String fileName = getFileName(filePath);
        if (fileName == null) {
            return null;
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        int lastSeparatorIndex = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if (lastSeparatorIndex == -1) {
            return filePath;
        }
        return filePath.substring(lastSeparatorIndex + 1);
    }

    /**
     * 获取文件目录
     */
    public static String getFileDirectory(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        int lastSeparatorIndex = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if (lastSeparatorIndex == -1) {
            return null;
        }
        return filePath.substring(0, lastSeparatorIndex);
    }

    /**
     * 获取文件修改时间
     */
    public static LocalDateTime getLastModifiedTime(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        try {
            FileTime fileTime = Files.getLastModifiedTime(Paths.get(filePath));
            return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取文件修改时间
     */
    public static LocalDateTime getLastModifiedTime(Path path) {
        if (path == null) {
            return null;
        }
        try {
            FileTime fileTime = Files.getLastModifiedTime(path);
            return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
        } catch (IOException e) {
            return null;
        }
    }

    // ==================== 文件搜索 ====================

    /**
     * 搜索文件
     */
    public static List<String> searchFiles(String dirPath, String pattern) {
        if (StringUtils.isBlank(dirPath) || StringUtils.isBlank(pattern)) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        try {
            Path dir = Paths.get(dirPath);
            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                return result;
            }
            
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().matches(pattern)) {
                        result.add(file.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // 忽略异常
        }
        return result;
    }

    /**
     * 搜索文件（按扩展名）
     */
    public static List<String> searchFilesByExtension(String dirPath, String extension) {
        if (StringUtils.isBlank(dirPath) || StringUtils.isBlank(extension)) {
            return new ArrayList<>();
        }
        String pattern = ".*\\." + extension + "$";
        return searchFiles(dirPath, pattern);
    }

    /**
     * 搜索文件（按文件名）
     */
    public static List<String> searchFilesByName(String dirPath, String fileName) {
        if (StringUtils.isBlank(dirPath) || StringUtils.isBlank(fileName)) {
            return new ArrayList<>();
        }
        String pattern = ".*" + fileName + ".*";
        return searchFiles(dirPath, pattern);
    }

    // ==================== 目录操作 ====================

    /**
     * 列出目录下的所有文件
     */
    public static List<String> listFiles(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        try {
            Path dir = Paths.get(dirPath);
            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                return result;
            }
            
            try (Stream<Path> paths = Files.list(dir)) {
                paths.filter(Files::isRegularFile)
                     .forEach(path -> result.add(path.toString()));
            }
        } catch (IOException e) {
            // 忽略异常
        }
        return result;
    }

    /**
     * 列出目录下的所有子目录
     */
    public static List<String> listDirectories(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        try {
            Path dir = Paths.get(dirPath);
            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                return result;
            }
            
            try (Stream<Path> paths = Files.list(dir)) {
                paths.filter(Files::isDirectory)
                     .forEach(path -> result.add(path.toString()));
            }
        } catch (IOException e) {
            // 忽略异常
        }
        return result;
    }

    /**
     * 列出目录下的所有文件和子目录
     */
    public static List<String> listAll(String dirPath) {
        if (StringUtils.isBlank(dirPath)) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        try {
            Path dir = Paths.get(dirPath);
            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                return result;
            }
            
            try (Stream<Path> paths = Files.list(dir)) {
                paths.forEach(path -> result.add(path.toString()));
            }
        } catch (IOException e) {
            // 忽略异常
        }
        return result;
    }

    // ==================== 文件权限 ====================

    /**
     * 设置文件可读
     */
    public static boolean setReadable(String filePath, boolean readable) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return false;
            }
            return path.toFile().setReadable(readable);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置文件可写
     */
    public static boolean setWritable(String filePath, boolean writable) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return false;
            }
            return path.toFile().setWritable(writable);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置文件可执行
     */
    public static boolean setExecutable(String filePath, boolean executable) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return false;
            }
            return path.toFile().setExecutable(executable);
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 格式化文件大小
     */
    public static String formatFileSize(long size) {
        if (size < 0) {
            return "0 B";
        }
        
        String[] units = {"B", "KB", "MB", "GB", "TB", "PB"};
        int unitIndex = 0;
        double fileSize = size;
        
        while (fileSize >= 1024 && unitIndex < units.length - 1) {
            fileSize /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", fileSize, units[unitIndex]);
    }

    /**
     * 获取文件MIME类型
     */
    public static String getMimeType(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        try {
            return Files.probeContentType(Paths.get(filePath));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取文件MIME类型
     */
    public static String getMimeType(Path path) {
        if (path == null) {
            return null;
        }
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 检查文件是否为图片
     */
    public static boolean isImageFile(String filePath) {
        String mimeType = getMimeType(filePath);
        return mimeType != null && mimeType.startsWith("image/");
    }

    /**
     * 检查文件是否为文本文件
     */
    public static boolean isTextFile(String filePath) {
        String mimeType = getMimeType(filePath);
        return mimeType != null && mimeType.startsWith("text/");
    }
}
