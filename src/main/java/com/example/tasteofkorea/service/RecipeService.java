package com.example.tasteofkorea.service;

import com.example.tasteofkorea.dto.RecipeDTO;
import com.example.tasteofkorea.entity.FoodLogEntity;
import com.example.tasteofkorea.entity.RecipeEntity;
import com.example.tasteofkorea.repository.FoodLogRepository;
import com.example.tasteofkorea.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FoodLogRepository foodLogRepository;

    // 특정 음식 조회 + 로그 기록
    public RecipeDTO getRecipeById(Long id) {
        Optional<RecipeEntity> recipeEntityOpt = recipeRepository.findById(id);
        if (recipeEntityOpt.isPresent()) {
            RecipeEntity recipeEntity = recipeEntityOpt.get();

            // 접근 로그 저장
            FoodLogEntity log = new FoodLogEntity();
            log.setFood(recipeEntity);
            log.setAccessTime(LocalDateTime.now());
            foodLogRepository.save(log);

            return convertToDTO(recipeEntity);
        } else {
            return null; // 또는 예외 처리
        }
    }

    private RecipeDTO convertToDTO(RecipeEntity entity) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(entity.getId());
        dto.setKoreanName(entity.getKoreanName());
        dto.setEnglishName(entity.getEnglishName());
        dto.setPronunciation(entity.getPronunciation());
        dto.setInformation(entity.getInformation());
        dto.setRecipeLink(entity.getRecipeLink());
        dto.setEatLink(entity.getEatLink());
        dto.setRecipeSource(entity.getRecipeSource());
        dto.setEatingSource(entity.getEatingSource());
        dto.setImageLink(entity.getImageLink());
        dto.setImageSource(entity.getImageSource());
        return dto;
    }
}
