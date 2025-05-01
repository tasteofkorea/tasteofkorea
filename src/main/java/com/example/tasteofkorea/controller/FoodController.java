package com.example.tasteofkorea.controller;

import com.example.tasteofkorea.dto.FilterCriteria;
import com.example.tasteofkorea.dto.RecipeDTO;
import com.example.tasteofkorea.entity.RecipeEntity;
import com.example.tasteofkorea.entity.TestFileEntity;
import com.example.tasteofkorea.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
@Tag(name = "음식 API", description = "한식 이미지 분석 및 정보 제공 API")
@CrossOrigin(origins = {"http://localhost:5173","https://taste-of-korea-fe.vercel.app/"}) // React 앱의 주소

@RestController
@RequiredArgsConstructor  // Lombok을 사용하여 생성자 자동 생성
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FastApiService fastApiService;

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

    @Autowired
    private RecipeService recipeService;

    // 특정 음식 조회
    @Operation(summary = "음식 정보 조회", description = "음식 ID로 한식 정보를 가져옴")
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable("id") int id) {
        RecipeDTO recipe = recipeService.getRecipeById(id);
        if (recipe != null) {
            return ResponseEntity.ok(recipe);
        } else {
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
    }

    private final TestFileService testService;

    @Operation(summary = "이미지 파일 업로드", description = "이미지 파일을 S3에 업로드")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            testService.saveFile(file);
            return ResponseEntity.ok("파일 업로드 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("업로드 실패: " + e.getMessage());
        }
    }


    @Operation(summary = "이미지 정보 조회", description = "ID를 기준으로 이미지 정보를 조회")
    @GetMapping("/file/{id}")
    public ResponseEntity<TestFileEntity> getFileInfo(@PathVariable("id") int id) {
        try {
            TestFileEntity file = testService.getFile(id);
            return ResponseEntity.ok(file);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    private final FoodFilterService foodFilterService;

    @Operation(summary = "필터링 음식추천", description = "알러지 체크후 추천할때 로그까지 포함해서 로그 수 많은순으로 리스트 출력")
    @PostMapping("/filter")
    public List<RecipeDTO> getSafeFoods(@RequestBody FilterCriteria filterCriteria) {
        return foodFilterService.getSafeFoods(filterCriteria);
    }




    /*@Autowired
    private RecommenderService recommenderService;
    // Recommend food items based on user preferences (in the format of a map)
    @PostMapping("/recommend")
    public ResponseEntity<List<RecipeDTO>> recommendFood(@RequestBody Map<String, Integer> userPreferences) {
        List<RecipeDTO> recommendedRecipes = recommenderService.recommendFood(userPreferences);
        return ResponseEntity.ok(recommendedRecipes);
    }*/


}
