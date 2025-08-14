package com.fs.api_gateway.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "auth-service")
public interface AuthFeignClient {

    @PostMapping("/api/auth/validate")
    Map<String, Object> validateToken(@RequestBody Map<String, String> request);
}
