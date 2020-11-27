package com.thoughtworks.buddiee.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.thoughtworks.buddiee.config.AliyunOssConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AliyunOssUtil {

    private final AliyunOssConfig aliyunOssConfig;

    private OSS getOSSClient(){
        return new OSSClientBuilder().build(
                aliyunOssConfig.getEndpoint(),
                aliyunOssConfig.getAccessKeyId(),
                aliyunOssConfig.getAccessKeySecret());
    }

    public String uploadFile(MultipartFile file, String key) throws IOException {
        OSS ossClient = getOSSClient();
        ossClient.putObject(aliyunOssConfig.getBucketName(), key , file.getInputStream());
        ossClient.shutdown();
        return "http://"+aliyunOssConfig.getBucketName()+"."
                +aliyunOssConfig.getEndpoint()+"/" + key;
    }

    public void deleteFile(String key){
        OSS ossClient = getOSSClient();
        ossClient.deleteObject(aliyunOssConfig.getBucketName(), key);
        ossClient.shutdown();
    }

    public String uploadBase64FileToAliyunOss(String dir, String base64String) throws IOException {
        MultipartFile imageFile = Base64Converter.converter(base64String);
        return this.uploadFile(imageFile, "buddiee/" + dir + imageFile.getOriginalFilename());
    }




}
