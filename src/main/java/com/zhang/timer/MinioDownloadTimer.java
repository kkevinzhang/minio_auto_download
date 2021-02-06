package com.zhang.timer;

import com.zhang.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class MinioDownloadTimer {

    @Autowired
    private MinioService minioService;

    @Scheduled(cron = "0 0 0 * * ? ")
    public void downloadTimedTask(){
        minioService.download();
    }
}
