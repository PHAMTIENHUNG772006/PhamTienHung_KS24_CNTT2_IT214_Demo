package com.res.emailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamPointDto {
    private Long id;
    private String email;
    private Float point;
    private LocalDate createdAt;
}