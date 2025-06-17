package com.example.tasteofkorea.service;

import com.example.tasteofkorea.dto.RecipeDTO;
import com.example.tasteofkorea.entity.FoodLogEntity;
import com.example.tasteofkorea.entity.RecipeEntity;
import com.example.tasteofkorea.repository.FoodLogRepository;
import com.example.tasteofkorea.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    // 특정 음식 조회 + 로그 기록
    @Transactional
    public RecipeDTO getRecipeById(Long id) {
        Optional<RecipeEntity> recipeEntityOpt = recipeRepository.findById(id);
        if (recipeEntityOpt.isPresent()) {
            RecipeEntity recipeEntity = recipeEntityOpt.get();

            // 접근 로그 저장
//            FoodLogEntity log = FoodLogEntity.builder()
//                .id(null)
//                .accessTime(LocalDateTime.now())
//                .food(recipeEntity)
//                .build();
//
//            foodLogRepository.save(log);
            recipeRepository.plusView(id);
            return recipeEntity.toDto();
        } else {
            return null; // 또는 예외 처리
        }
    }


}
