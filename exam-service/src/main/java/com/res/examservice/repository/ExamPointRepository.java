package com.res.examservice.repository;

import com.res.examservice.dto.ExamPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamPointRepository extends JpaRepository<ExamPoint,Long> {
}
