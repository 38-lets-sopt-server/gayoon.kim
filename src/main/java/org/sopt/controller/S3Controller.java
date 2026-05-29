package org.sopt.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.service.S3Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/image-url")
    public S3Service.UploadUrlResponse generateImageUploadUrl(
            @RequestBody ImageUrlRequest request
    ) {
        return s3Service.generateUploadUrl(
                request.fileName(),
                request.contentType(),
                request.fileSize()
        );
    }

    public record ImageUrlRequest(
            String fileName,
            String contentType,
            long fileSize
    ) {
    }
}