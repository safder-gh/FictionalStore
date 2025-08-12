package com.fs.product_service.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "STATIC-CONTENT-SERVICE")
public interface ContentFeignClient {

    @PostMapping(value = "/api/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, Object> uploadFile(@RequestPart("file") MultipartFile file,
                                   @RequestParam("public") boolean isPublic);
}
