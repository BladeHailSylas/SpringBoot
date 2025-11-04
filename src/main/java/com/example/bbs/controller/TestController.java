package com.example.bbs.controller;

import com.example.bbs.entity.TestEntity;
import com.example.bbs.repository.TestRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {

    private final TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/db-test")
    public String testConnection() {
        // 단순 DB insert → select → 반환
        TestEntity entity = new TestEntity("DB 연결 성공!");
        testRepository.save(entity);

        List<TestEntity> all = testRepository.findAll();
        return "✅ DB 연결 성공! 현재 총 데이터 수: " + all.size();
    }
}