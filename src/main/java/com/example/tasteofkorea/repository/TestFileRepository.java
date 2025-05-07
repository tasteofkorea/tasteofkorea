package com.example.tasteofkorea.repository;

import com.example.tasteofkorea.entity.TestFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TestFileRepository extends JpaRepository<TestFileEntity, Integer> {
    Optional<TestFileEntity> findTopByOrderByIdDesc(); // 가장 최근 저장된 파일
}
