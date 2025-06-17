package com.example.tasteofkorea.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class FastApiService {

    @Autowired
    private RestTemplate restTemplate;

    // FastAPI 서버 URL
    private static final String FASTAPI_URL = "http://localhost:8000/predict/";

    public Map<String, Object> predict(MultipartFile file) throws IOException {
        // Multipart 파일을 전송하기 위한 준비
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 파일을 byte[]로 변환하여 전송
        byte[] fileBytes = file.getBytes();
        body.add("file", new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // HTTP 요청을 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // FastAPI 서버로 POST 요청
        try {
            System.out.println("Sending request to FastAPI...");
            ResponseEntity<Map> response = restTemplate.exchange(
                    FASTAPI_URL,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );
            System.out.println("Received response from FastAPI.");
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.err.println("Error while calling FastAPI: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error calling FastAPI service", e);
        }
    }
}