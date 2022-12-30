package com.yc.community.common.minio;

import com.yc.community.common.response.CommonResponse;
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
    public CommonResponse upload(@RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                 @RequestParam(name = "bucketName") String bucketName,
                                 @RequestParam(name = "fileName", required = false) String fileName) {
        List<String> upload = minioUtil.upload(multipartFile, bucketName, fileName);
        return CommonResponse.OKBuilder.data(upload).msg("上传成功").build();
    }

    @PostMapping("/stringUpload")
    public CommonResponse stringUpload(@RequestParam(name = "str") String str,
                                       @RequestParam(name = "bucketName") String bucketName,
                                       @RequestParam(name = "fileName", required = false) String fileName) {
        String fileName1 = minioUtil.stringUpload(str, bucketName, fileName);
        return CommonResponse.OKBuilder.data(fileName1).msg("上传成功").build();
    }

    @GetMapping("/download")
    public CommonResponse download(@RequestParam String fileName, @RequestParam(name = "bucketName") String bucketName) {
        ResponseEntity<byte[]> download = minioUtil.download(fileName, bucketName);
        return CommonResponse.OKBuilder.data(download).build();
    }

    @GetMapping("/stringDownload")
    public CommonResponse stringDownload(@RequestParam String fileName, @RequestParam(name = "bucketName") String bucketName) {
        String s = minioUtil.stringDownload(fileName, bucketName);
        return CommonResponse.OKBuilder.data(s).build();
    }
}
