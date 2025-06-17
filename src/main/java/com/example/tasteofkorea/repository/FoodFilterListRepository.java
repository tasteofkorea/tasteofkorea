package com.example.tasteofkorea.repository;

import com.example.tasteofkorea.entity.FoodFilterListEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodFilterListRepository extends JpaRepository<FoodFilterListEntity, Long> {
    List<FoodFilterListEntity> findByFoodId(Long foodId);


}
