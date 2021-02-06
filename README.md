# MinIO 定时下载

### 配置如下
```
# minio 用户名
minio.minio-root-user=minioadmin
# minio 密码
minio.minio-root-password=minioadmin
# minio 访问地址
minio.minio-url=http://XXX.XXX.XXX.XX:XXXX
# minio bucket名
minio.minio-bucket=
# 文件下载位置(Linux/Mac OS 使用'/' windows使用'/'或者'\\')
minio.file-storage-path=
```

### 配置定时任务
```MinioDownloadTimer.downloadTimedTask()为定时任务, 通过该方法的@Scheduled注解进行配置```