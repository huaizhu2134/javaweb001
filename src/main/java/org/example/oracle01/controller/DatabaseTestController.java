package org.example.oracle01.controller;

import org.example.oracle01.service.DatabaseTestService;
import org.example.oracle01.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class DatabaseTestController {

    @Autowired
    private DatabaseTestService databaseTestService;

    @GetMapping("/connection")
    public String testConnection() {
        return databaseTestService.testConnection();
    }

    @GetMapping("/create-tables")
    public String createTables() {
        return databaseTestService.createTables();
    }

    @GetMapping("/init-data")
    public String initTestData() {
        return databaseTestService.initTestData();
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        return Result.success(databaseTestService.getStatistics());
    }
}