package org.sopt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("jpg", "jpeg", "png", "gif", "webp");

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public UploadUrlResponse generateUploadUrl(
            String fileName,
            String contentType,
            long fileSize
    ) {
        if (fileSize > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기는 10MB를 초과할 수 없어요.");
        }

        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("파일 확장자가 필요해요.");
        }

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식이에요. (jpg, jpeg, png, gif, webp만 가능)");
        }

        String key = "posts/" + UUID.randomUUID() + "_" + fileName;

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest
                .builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(req -> req
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                )
                .build();

        String uploadUrl = s3Presigner
                .presignPutObject(presignRequest)
                .url()
                .toString();

        return new UploadUrlResponse(uploadUrl, key);
    }

    public String generateDownloadUrl(String key) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest
                .builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(req -> req
                        .bucket(bucket)
                        .key(key)
                )
                .build();

        return s3Presigner
                .presignGetObject(presignRequest)
                .url()
                .toString();
    }

    public record UploadUrlResponse(
            String uploadUrl,
            String imageKey
    ) {
    }
}