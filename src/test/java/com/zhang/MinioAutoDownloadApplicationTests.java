package com.zhang;

import com.zhang.service.MinioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MinioAutoDownloadApplicationTests {

    @Autowired
    private MinioService minioService;

    @Test
    void downloadTest() {
        minioService.download();
    }

}
