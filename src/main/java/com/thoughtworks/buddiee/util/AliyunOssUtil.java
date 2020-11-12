package com.thoughtworks.buddiee.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.thoughtworks.buddiee.config.AliyunOssConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class AliyunOssUtil {

    private final AliyunOssConfig aliyunOssConfig;

    public AliyunOssUtil(AliyunOssConfig aliyunOssConfig) {
        this.aliyunOssConfig = aliyunOssConfig;
    }

    private static AliyunOssUtil aliyunOssUtil;

    @PostConstruct
    public void init() {
        aliyunOssUtil = this;
    }

    private static OSS getOSSClient(){
        return new OSSClientBuilder().build(
                aliyunOssUtil.aliyunOssConfig.getEndpoint(),
                aliyunOssUtil.aliyunOssConfig.getAccessKeyId(),
                aliyunOssUtil.aliyunOssConfig.getAccessKeySecret());
    }

    public String uploadFile(MultipartFile file, String key) throws IOException {
        OSS ossClient = getOSSClient();
        ossClient.putObject(aliyunOssUtil.aliyunOssConfig.getBucketName(), key , file.getInputStream());
        ossClient.shutdown();
        return "http://"+aliyunOssUtil.aliyunOssConfig.getBucketName()+"."
                +aliyunOssUtil.aliyunOssConfig.getEndpoint()+"/" + key;
    }

    public void deleteFile(String key ){
        OSS ossClient = getOSSClient();
        ossClient.deleteObject(aliyunOssUtil.aliyunOssConfig.getBucketName(), key);
        ossClient.shutdown();
    }

    public String uploadBase64FileToAliyunOss(String base64String) throws IOException {
        MultipartFile imageFile = Base64Converter.converter(base64String);
        return this.uploadFile(imageFile, imageFile.getOriginalFilename());
    }




}
