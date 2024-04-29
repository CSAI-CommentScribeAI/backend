package com.example.backend.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.backend.exception.aws.FileUploadFailedException;
import com.example.backend.exception.aws.MalformedFileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket.bucket-name}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3 amazonS3;

    public String uploadReviewImage(String review, MultipartFile multipartFile){
        String folder = "store/" + review + "/review";
        return uploadImage("reivew",multipartFile);
    }
    public String uploadMenuImage(String menu, MultipartFile multipartFile) {
        String folder = "store/" + menu + "/menu";
        return uploadImage(folder, multipartFile);
    }

    public String uploadStoreImage(String storeName, MultipartFile multipartFile) {
        String folder = "store/" + storeName + "/storelogo";
        return uploadImage(folder, multipartFile);
    }

    private String uploadImage(String folder, MultipartFile multipartFile) {
        String url = "https://s3." + region + ".amazonaws.com/" + bucket + "/";

        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, folder + "/" + fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new FileUploadFailedException();
        }

        return url + folder + "/" + fileName;
    }

    public void deleteImage(String folder, String fileName) {
        amazonS3.deleteObject(bucket, folder + "/" + fileName);
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new MalformedFileException("잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}
