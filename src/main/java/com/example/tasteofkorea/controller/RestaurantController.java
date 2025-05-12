//package com.example.tasteofkorea.controller;
//
//import com.example.tasteofkorea.annotation.LoginUser;
//import com.example.tasteofkorea.dto.RestaurantDTO;
//import com.example.tasteofkorea.entity.RestaurantEntity;
//import com.example.tasteofkorea.entity.User;
//import com.example.tasteofkorea.service.RestaurantService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:5173", "https://taste-of-korea-fe.vercel.app"})
//@RequestMapping("/restaurants")
//public class RestaurantController {
//
//    private final RestaurantService restaurantService;
//    private final ObjectMapper objectMapper;
//
//    @Operation(summary = "식당 등록", description = "식당 등록 (JSON + 이미지)")
//    @PostMapping
//    public ResponseEntity<String> createRestaurant(
//            @LoginUser User loginUser,
//            @RequestParam("metadata") String metadataJson,
//            @RequestParam(value = "file", required = false) MultipartFile file
//    ) {
//        try {
//            RestaurantDTO metadata = objectMapper.readValue(metadataJson, RestaurantDTO.class);
//            restaurantService.createRestaurant(
//                    metadata.getEnglishName(),
//                    metadata.getLatitude(),
//                    metadata.getLongitude(),
//                    metadata.getPrice(),
//                    metadata.getRecipeId(),
//                    file,
//                    loginUser
//            );
//            return ResponseEntity.ok("식당 등록 성공");
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body("등록 실패: " + e.getMessage());
//        }
//    }
//
//    @Operation(summary = "식당 전체 조회", description = "모든 식당을 조회합니다. (로그인 불필요)")
//    @GetMapping
//    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
//        return ResponseEntity.ok(restaurantService.getAllRestaurants());
//    }
//
//    @Operation(summary = "식당 수정", description = "식당 정보 수정 (JSON + 이미지)")
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateRestaurant(
//            @PathVariable Long id,
//            @LoginUser User loginUser,
//            @RequestParam("metadata") String metadataJson,
//            @RequestParam(value = "file", required = false) MultipartFile file
//    ) {
//        try {
//            RestaurantDTO metadata = objectMapper.readValue(metadataJson, RestaurantDTO.class);
//            restaurantService.updateRestaurant(
//                    id,
//                    metadata.getEnglishName(),
//                    metadata.getLatitude(),
//                    metadata.getLongitude(),
//                    metadata.getPrice(),
//                    metadata.getRecipeId(),
//                    file,
//                    loginUser
//            );
//            return ResponseEntity.ok("식당 수정 성공");
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body("수정 실패: " + e.getMessage());
//        }
//    }
//
//    @Operation(summary = "식당 삭제", description = "식당 삭제 (로그인 필요)")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id,
//                                                   @LoginUser User loginUser) {
//        try {
//            restaurantService.deleteRestaurant(id, loginUser);
//            return ResponseEntity.ok("식당 삭제 성공");
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body("삭제 실패: " + e.getMessage());
//        }
//    }
//
//    @Operation(summary = "내가 등록한 식당 조회", description = "로그인한 사용자가 등록한 식당 리스트를 반환합니다.")
//    @GetMapping("/my")
//    public ResponseEntity<List<RestaurantDTO>> getMyRestaurants(@LoginUser User loginUser) {
//        List<RestaurantDTO> myRestaurants = restaurantService.getRestaurantsByUser(loginUser);
//        return ResponseEntity.ok(myRestaurants);
//    }
//
//    @Operation(summary = "레시피 ID로 식당 조회", description = "특정 recipeId에 해당하는 식당을 조회합니다.")
//    @GetMapping("/by-recipe")
//    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByRecipe(@RequestParam Long recipeId) {
//        List<RestaurantDTO> result = restaurantService.getRestaurantsByRecipeId(recipeId);
//        return ResponseEntity.ok(result);
//    }
//}
