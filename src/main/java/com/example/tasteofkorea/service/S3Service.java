package com.example.tasteofkorea.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucketName}")
	private String bucket;

	public String upload(MultipartFile multipartFile) throws IOException {
		String fileName = UUID.randomUUID() + ".jpg";  // 무조건 .jpg로 저장

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("image/jpeg");
		metadata.setContentLength(multipartFile.getSize());

		amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);

		return amazonS3.getUrl(bucket, fileName).toString();  // S3 URL 반환
	}
}
