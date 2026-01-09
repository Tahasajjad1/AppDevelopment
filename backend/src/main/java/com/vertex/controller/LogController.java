package com.vertex.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/logs")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
public class LogController {

    // Matches the name "AngularLogger" in logback-spring.xml
    private static final Logger angularLogger = LoggerFactory.getLogger("AngularLogger");

    @PostMapping("/client")
    public void receiveClientLog(@RequestBody Map<String, String> logData) {
        String level = logData.getOrDefault("level", "INFO");
        String message = logData.getOrDefault("message", "No message");
        String timestamp = logData.getOrDefault("timestamp", "");

        // Write to angular.log
        angularLogger.info("[{}] {}", level, message);
    }
}