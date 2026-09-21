package com.res.examservice.service;

import com.res.examservice.dto.DataEvent;
import com.res.examservice.dto.ExamPoint;
import com.res.examservice.dto.RequestPoint;
import com.res.examservice.repository.ExamPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamPointRepository examPointRepository;
    private final KafkaTemplate<String, DataEvent<ExamPoint>> kafkaTemplate;

    public static final String TOPIC_EXAM_POINT = "exam-point-topic";

    @Transactional
    public ExamPoint createNewPoint(RequestPoint requestPoint) {
        ExamPoint examPoint = ExamPoint.builder()
                .email(requestPoint.getEmail())
                .point(requestPoint.getPoint())
                .createdAt(LocalDate.now())
                .build();

        ExamPoint savedPoint = examPointRepository.save(examPoint);
        log.info("Đã lưu điểm vào CSDL với ID: {}", savedPoint.getId());

        DataEvent<ExamPoint> event = DataEvent.<ExamPoint>builder()
                .id(savedPoint.getId())
                .content("NEW_EXAM_POINT_CREATED")
                .data(savedPoint)
                .time(LocalDate.now())
                .build();

        kafkaTemplate.send(TOPIC_EXAM_POINT, String.valueOf(savedPoint.getId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Lỗi khi bắn event điểm thi ID: {}", savedPoint.getId(), ex);
                    } else {
                        log.info("Đã gửi event thành công tới topic [{}], offset [{}]",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().offset());
                    }
                });

        return savedPoint;
    }
}