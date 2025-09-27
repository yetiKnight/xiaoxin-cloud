/*
 * Copyright 2024 xiaoxin. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoxin.iam.starter.web.util;

import com.xiaoxin.iam.starter.web.config.WebProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传工具类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadUtil {

    private final WebProperties webProperties;

    /**
     * 上传文件
     */
    public String uploadFile(MultipartFile file) throws IOException {
        WebProperties.FileUpload fileUpload = webProperties.getFileUpload();
        
        // 验证文件
        validateFile(file);
        
        // 生成文件名
        String fileName = generateFileName(file.getOriginalFilename());
        
        // 创建上传目录
        Path uploadDir = createUploadDirectory();
        
        // 保存文件
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        log.info("文件上传成功: {}", filePath);
        
        return filePath.toString();
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        WebProperties.FileUpload fileUpload = webProperties.getFileUpload();
        
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 验证文件大小
        if (fileUpload.isEnableSizeValidation()) {
            long maxSize = parseSize(fileUpload.getMaxFileSize());
            if (file.getSize() > maxSize) {
                throw new IllegalArgumentException("文件大小超过限制: " + fileUpload.getMaxFileSize());
            }
        }
        
        // 验证文件类型
        if (fileUpload.isEnableTypeValidation()) {
            String contentType = file.getContentType();
            if (contentType == null || !fileUpload.getAllowedTypes().contains(contentType)) {
                throw new IllegalArgumentException("不支持的文件类型: " + contentType);
            }
        }
        
        // 验证文件扩展名
        if (fileUpload.isEnableNameValidation()) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalArgumentException("文件名不能为空");
            }
            
            String extension = getFileExtension(originalFilename);
            if (!fileUpload.getAllowedExtensions().contains(extension.toLowerCase())) {
                throw new IllegalArgumentException("不支持的文件扩展名: " + extension);
            }
        }
    }

    /**
     * 生成文件名
     */
    private String generateFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        
        return timestamp + "_" + uuid + "." + extension;
    }

    /**
     * 创建上传目录
     */
    private Path createUploadDirectory() throws IOException {
        WebProperties.FileUpload fileUpload = webProperties.getFileUpload();
        Path uploadDir = Paths.get(fileUpload.getUploadPath());
        
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
            log.info("创建上传目录: {}", uploadDir);
        }
        
        return uploadDir;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        
        return filename.substring(lastDotIndex + 1);
    }

    /**
     * 解析文件大小
     */
    private long parseSize(String size) {
        if (size == null || size.isEmpty()) {
            return 0;
        }
        
        size = size.trim().toUpperCase();
        
        if (size.endsWith("KB")) {
            return Long.parseLong(size.substring(0, size.length() - 2)) * 1024;
        } else if (size.endsWith("MB")) {
            return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024;
        } else if (size.endsWith("GB")) {
            return Long.parseLong(size.substring(0, size.length() - 2)) * 1024 * 1024 * 1024;
        } else {
            return Long.parseLong(size);
        }
    }

    /**
     * 删除文件
     */
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                log.info("文件删除成功: {}", filePath);
            }
            return deleted;
        } catch (IOException e) {
            log.error("文件删除失败: {}", filePath, e);
            return false;
        }
    }

    /**
     * 检查文件是否存在
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 获取文件大小
     */
    public long getFileSize(String filePath) {
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            log.error("获取文件大小失败: {}", filePath, e);
            return 0;
        }
    }
}
