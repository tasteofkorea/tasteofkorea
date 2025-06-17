package com.example.tasteofkorea.repository;

import com.example.tasteofkorea.entity.FilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<FilterEntity, Long> {
    // 필요한 경우 커스텀 쿼리 메서드 추가 가능
}
