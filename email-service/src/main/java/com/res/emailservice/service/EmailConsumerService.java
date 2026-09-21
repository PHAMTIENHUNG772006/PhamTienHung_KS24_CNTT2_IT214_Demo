package com.res.emailservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.res.emailservice.dto.DataEvent;
import com.res.emailservice.dto.ExamPointDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "exam-point-topic", groupId = "email-group")
    public void listenExamPoint(DataEvent<?> rawEvent) {
        log.info("Email-service đã nhận event: {}", rawEvent);

        ExamPointDto pointData = objectMapper.convertValue(rawEvent.getData(), ExamPointDto.class);

        sendEmail(pointData.getEmail(), pointData.getPoint());

        DataEvent<String> emailSentEvent = new DataEvent<>(
                pointData.getId(),
                "EMAIL_NOTIFICATION_SENT",
                "Đã gửi email điểm số thành công tới: " + pointData.getEmail(),
                LocalDate.now()
        );

        kafkaTemplate.send("email-notification-topic", String.valueOf(pointData.getId()), emailSentEvent)
                .whenComplete((res, ex) -> {
                    if (ex == null) {
                        log.info("Đã phát sinh tiếp sự kiện gửi mail thành công lên topic [email-notification-topic]");
                    } else {
                        log.error("Lỗi khi bắn event thông báo gửi mail", ex);
                    }
                });
    }

    private void sendEmail(String toEmail, Float point) {
        log.info(">>> ĐANG GỬI MAIL ĐẾN: {} | Điểm thi của bạn là: {} <<<", toEmail, point);
    }
}