package com.res.updateexamevent.repository;

import com.res.updateexamevent.entity.ExamPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamPointRepository extends JpaRepository<ExamPoint, Long> {
}