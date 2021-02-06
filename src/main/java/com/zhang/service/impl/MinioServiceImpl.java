package com.zhang.service.impl;

import com.zhang.model.MinioProperties;
import com.zhang.service.MinioService;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhang
 */
@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioProperties minioProperties;

    @Override
    public void download() {
        InputStream inputStream = null;
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getMinioUrl())
                    .credentials(minioProperties.getMinioRootUser(), minioProperties.getMinioRootPassword())
                    .build();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isEmpty(minioProperties.getMinioBucket())) {
                List<Bucket> bucketList = minioClient.listBuckets();
                if(bucketList.isEmpty()){
                    throw new RuntimeException("没有配置bucket！");
                }
                for (Bucket bucket : bucketList) {
                    Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket.name()).recursive(true).build());
                    for (Result<Item> result : results) {
                        Item item = result.get();
                        inputStream =
                                minioClient.getObject(GetObjectArgs.builder().bucket(bucket.name()).object(item.objectName()).build());

                        File file = new File(String.format("%s%s%s%s", minioProperties.getFileStoragePath(), bucket.name(), File.separator, item.objectName()));
                        if (file.exists()) {
                            continue;
                        } else {
                            System.out.println("当前时间: " + dateTimeFormatter.format(LocalDateTime.now()) + " -- 文件大小: " + item.size() + " -- 文件地址: " + item.objectName() + " -- 上传时间: " + item.lastModified());
                        }
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }

                        IOUtils.copy(inputStream, new FileOutputStream(file));
                    }
                }
            } else {
                boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getMinioBucket()).build());
                if (isExist) {
                    Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(minioProperties.getMinioBucket()).recursive(true).build());
                    for (Result<Item> result : results) {
                        Item item = result.get();
                        inputStream =
                                minioClient.getObject(GetObjectArgs.builder().bucket(minioProperties.getMinioBucket()).object(item.objectName()).build());

                        File file = new File(String.format("%s%s%s%s", minioProperties.getFileStoragePath(), minioProperties.getMinioBucket(), File.separator, item.objectName()));
                        if (file.exists()){
                            break;
                        }else{
                            System.out.println("当前时间: " + dateTimeFormatter.format(LocalDateTime.now()) + " -- 文件大小: " + item.size() + " -- 文件地址: " + item.objectName() + " -- 上传时间: " + item.lastModified());
                        }
                        if(!file.getParentFile().exists()){
                            file.getParentFile().mkdirs();
                        }

                        IOUtils.copy(inputStream, new FileOutputStream(file));
                    }
                } else {
                    System.out.println("bucket" + minioProperties.getMinioBucket() + "不存在!");
                }
            }
        } catch (Exception e) {
            System.out.println("minio文件下载失败！");
            System.out.println(e.getMessage());
        }
    }
}
