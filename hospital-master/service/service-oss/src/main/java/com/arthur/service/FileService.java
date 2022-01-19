package com.arthur.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @authur arthur
 * @desc
 */
public interface FileService {
    String upload(MultipartFile file);
}
