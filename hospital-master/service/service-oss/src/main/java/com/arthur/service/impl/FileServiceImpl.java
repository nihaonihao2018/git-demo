package com.arthur.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.arthur.config.ConstantOssPropertiesUtils;
import com.arthur.service.FileService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @authur arthur
 * @desc
 */
@Service(value = "fileService")
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file) {
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
        String bucket = ConstantOssPropertiesUtils.BUCKET;
        String ednpoint = ConstantOssPropertiesUtils.EDNPOINT;
        String secrect = ConstantOssPropertiesUtils.SECRECT;
        try {
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            String uuString = UUID.randomUUID().toString().replaceAll("-", "");
            filename=uuString+filename;
            String dateTimeString = new DateTime().toString("yyyy/MM/dd");
            filename=dateTimeString+"/"+filename;

            OSS ossClient = new OSSClientBuilder().build(ednpoint, accessKeyId, secrect);

            ossClient.putObject(bucket, filename, inputStream);
            ossClient.shutdown();

            String url="https://"+bucket+"."+ednpoint+"/"+filename;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
