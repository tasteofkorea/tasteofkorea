package com.example.tasteofkorea.controller;

import com.example.tasteofkorea.dto.FilterCriteria;
import com.example.tasteofkorea.dto.FilterRecipeList;
import com.example.tasteofkorea.dto.FilteredAllergyInfo;
import com.example.tasteofkorea.dto.RecipeDTO;
import com.example.tasteofkorea.entity.FilterEntity;
import com.example.tasteofkorea.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
@Tag(name = "음식 API", description = "한식 이미지 분석 및 정보 제공 API")

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodController {


    private final FastApiService fastApiService;
    private final RecipeService recipeService;
    private final FoodFilterService foodFilterService;

    // FastAPI와 연결하여 이미지 예측
    @Operation(summary = "이미지 예측", description = "FastAPI 서버로부터 음식 종류 예측")
    @PostMapping("/predict")
    public ResponseEntity<Map<String, Object>> predictFood(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = fastApiService.predict(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 예외를 콘솔에 출력하여 더 자세한 정보를 얻을 수 있습니다.
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to process the image"));
        }
    }



    // 특정 음식 조회
    @Operation(summary = "음식 정보 조회", description = "음식 ID로 한식 정보를 가져옴")
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable("id") Long id) {
        RecipeDTO recipe = recipeService.getRecipeById(id);
        if (recipe != null) {
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
    }


    @Operation(summary = "필터링 음식추천", description = "알러지 체크후 추천할때 로그까지 포함해서 로그 수 많은순으로 리스트 출력")
    @GetMapping("/filter")
    public FilterRecipeList getSafeFoods(@RequestParam("filter")  List<Integer> filterIndex,@RequestParam(value = "page",defaultValue = "1") int page,@RequestParam("size")int size) {
        return foodFilterService.getSafeFoods(filterIndex, PageRequest.of(page-1,size));
    }

    @Operation(summary = "음식의 알레르기 성분 조회", description = "특정 음식 ID로 알레르기 성분 목록 조회")
    @GetMapping("/{id}/filter")
    public FilteredAllergyInfo getFoodFilter(@PathVariable("id") Long id) {
        FilterCriteria criteria = foodFilterService.getFilterCriteriaByFoodId(id);
        return foodFilterService.getFilteredAllergyInfo(id, criteria);
    }




}
