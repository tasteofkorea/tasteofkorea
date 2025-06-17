package com.example.tasteofkorea.repository;

import com.example.tasteofkorea.entity.FoodLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodLogRepository extends JpaRepository<FoodLogEntity, Long> {
    // 예: 최근 로그, 특정 음식에 대한 로그 조회 등은 여기에 메서드 추가 가능
    // 특정 음식의 로그 개수 조회
    int countByFoodId(Long foodId);  // foodId 타입을 Long으로 변경
}
