package com.hrmf.user_service.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/test")
public class TestController {

    private List<String> names;

    @RequestMapping(value = "/ping", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, String> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "user-service");
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "user-service");
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }


}