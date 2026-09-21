package com.res.examservice.controller;

import com.res.examservice.dto.RequestPoint;
import com.res.examservice.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/send")
@RequiredArgsConstructor
public class KafkaController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<String> createPoint(@RequestBody RequestPoint requestPoint) {
        examService.createNewPoint(requestPoint);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tạo điểm thành công và đã phát sinh sự kiện");
    }
}
