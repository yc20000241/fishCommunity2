package com.yc.community.common.minio;

import com.yc.community.common.exception.BusinessException;
import com.yc.community.common.exception.BusinessExceptionCode;
import com.yc.community.common.util.UUIDUtil;
import io.minio.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description： minio工具类
 *
 * @version：3.0
 */
@Component
public class MinioUtil {


    @Autowired
    private MinioClient minioClient;

    /**
     * description: 判断bucket是否存在，不存在则创建
     *
     * @return: void
     *
     */
    public void existBucket(String name) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * description: 上传文件
     *
     *
     *
     */
    public List<String> upload(MultipartFile file, String bucketName, String fileName) {
        if(file == null){
            ArrayList<String> list = new ArrayList<>();
            list.add("");
            return list;
        }
        existBucket(bucketName);

        List<String> names = new ArrayList<>();
//        for (MultipartFile file : multipartFile) {

            if(StringUtils.isEmpty(fileName)){
                fileName = file.getOriginalFilename();
                String[] split = fileName.split("\\.");
                fileName = file.getOriginalFilename();
                if (split.length > 1) {
                    fileName = split[0] + "_" + System.currentTimeMillis() + "." + split[1];
                } else {
                    fileName = fileName + System.currentTimeMillis();
                }
            }
            InputStream in = null;
            try {
                in = file.getInputStream();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(in, in.available(), -1)
                        .contentType(file.getContentType())
                        .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            names.add("/" + bucketName + "/" + fileName);
//        }
        return names;
    }

    /**
     * description: 下载文件
     *
     * @param fileName
     * @return: org.springframework.http.ResponseEntity<byte [ ]>
     *
     */
    public ResponseEntity<byte[]> download(String fileName, String bucketName) {
        ResponseEntity<byte[]> responseEntity = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            //封装返回值
            byte[] bytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            try {
                headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            headers.setContentLength(bytes.length);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setAccessControlExposeHeaders(Arrays.asList("*"));
            responseEntity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseEntity;
    }

    public String stringUpload(String str, String bucketName, String fileName){
        existBucket(bucketName);

        try{
            InputStream in = new ByteArrayInputStream(str.getBytes());
            if(StringUtils.isEmpty(fileName))
                fileName = UUIDUtil.getUUID() + ".txt";
            minioClient.putObject(PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(in, in.available(), -1)
                            .contentType("text/plain")
                            .build()
            );
        }catch (Exception e){
            System.out.println(e);
            throw new BusinessException(BusinessExceptionCode.ARTICLE_UPLOAD_FILE);
        }
        return fileName;
    }

    public String stringDownload(String fileName, String bucketName) {
        InputStream in = null;
        String result = null;
        try {
            in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
            result = IOUtils.toString(in);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
