package com.res.updateexamevent.service;

import com.res.updateexamevent.dto.DataEvent;
import com.res.updateexamevent.repository.ExamPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateExamSentConsumer {

    private final ExamPointRepository examPointRepository;

    @Transactional
    @KafkaListener(topics = "email-notification-topic", groupId = "update-exam-group")
    public void handleEmailSentEvent(DataEvent<?> event) {
        log.info("Nhận sự kiện email đã gửi xong: {}", event);

        Long examPointId = event.getId();

        examPointRepository.findById(examPointId).ifPresentOrElse(
                examPoint -> {
                    examPoint.setStatus("SENT");
                    examPointRepository.save(examPoint);
                    log.info("Cập nhật thành công trạng thái SENT cho bản ghi điểm ID: {}", examPointId);
                },
                () -> log.error("Không tìm thấy bản ghi điểm với ID: {}", examPointId)
        );
    }
}