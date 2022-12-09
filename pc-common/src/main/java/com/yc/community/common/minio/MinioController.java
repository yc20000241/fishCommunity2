package com.yc.community.common.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/common/minio")
public class MinioController {

    @Autowired
    private MinioUtil minioUtil;


    @PostMapping("/upload")
    public List<String> upload(@RequestParam(name = "file") MultipartFile[] multipartFile, @RequestParam(name = "bucketName") String bucketName) {
        return minioUtil.upload(multipartFile, bucketName);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String fileName, @RequestParam(name = "bucketName") String bucketName) {
        return minioUtil.download(fileName, bucketName);
    }


}
