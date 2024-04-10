package com.example.backend.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket.bucket-name}")
    private String bucketName;

    private final AmazonS3 s3Client;
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    public String uploadFile(MultipartFile multipartFile, String folderName) throws IOException {

        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("MultipartFile이 null이거나 비어 있습니다.");
        }

        String originalFileName = multipartFile.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalStateException("파일 이름이 null입니다.");
        }

        File file = convertMultiPartFileToFile(multipartFile);
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + fileExtension;

        String key = folderName + fileName;
        s3Client.putObject(new PutObjectRequest(bucketName, key, file));

        boolean deleteSuccess = file.delete();
        if (!deleteSuccess) {
            logger.warn("임시 파일을 삭제할 수 없습니다: {}", file.getAbsolutePath());
        }

        return key;
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    private File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("MultipartFile이 null이거나 비어 있습니다.");
        }

        String originalFileName = multipartFile.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalStateException("파일 이름이 null입니다.");
        }

        File file = new File(originalFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}
