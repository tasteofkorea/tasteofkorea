//package com.example.tasteofkorea.service;
//
//import com.example.tasteofkorea.dto.RestaurantDTO;
//import com.example.tasteofkorea.entity.RecipeEntity;
//import com.example.tasteofkorea.entity.RestaurantEntity;
//import com.example.tasteofkorea.entity.TestFileEntity;
//import com.example.tasteofkorea.entity.User;
//import com.example.tasteofkorea.repository.RecipeRepository;
//import com.example.tasteofkorea.repository.RestaurantRepository;
//import com.example.tasteofkorea.repository.TestFileRepository;
//import com.example.tasteofkorea.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class RestaurantService {
//
//    private final RestaurantRepository restaurantRepository;
//    private final RecipeRepository recipeRepository;
//    private final UserRepository userRepository; // ✅ 추가
//
//    // Create
//    public RestaurantDTO createRestaurant(String englishName, double latitude, double longitude,
//                                          int price, Long recipeId, MultipartFile file, User loginUser) throws IOException {
//        // ✅ User를 DB에서 다시 조회해 영속 상태로 만든다.
//        User user = userRepository.findById(loginUser.getId())
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        RestaurantEntity restaurant = new RestaurantEntity();
//        restaurant.setEnglishName(englishName);
//        restaurant.setLatitude(latitude);
//        restaurant.setLongitude(longitude);
//        restaurant.setPrice(price);
//        restaurant.setOwner(user); // 반드시 영속 상태 User
//
//        // 레시피 설정
//        if (recipeId != null) {
//            RecipeEntity recipe = recipeRepository.findById(recipeId)
//                    .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
//            restaurant.setRecipe(recipe);
//        }
//
//        restaurant = restaurantRepository.save(restaurant);
//
//        return RestaurantDTO.toDTO(restaurant);
//    }
//
//    // Update
//    public RestaurantDTO updateRestaurant(Long id, String englishName, double latitude, double longitude,
//                                          int price, Long recipeId, MultipartFile file, User loginUser) throws IOException {
//        RestaurantEntity restaurant = restaurantRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));
//
//        if (!restaurant.getOwner().getId().equals(loginUser.getId())) {
//            throw new RuntimeException("수정 권한이 없습니다.");
//        }
//
//        restaurant.setEnglishName(englishName);
//        restaurant.setLatitude(latitude);
//        restaurant.setLongitude(longitude);
//        restaurant.setPrice(price);
//
//        if (recipeId != null) {
//            RecipeEntity recipe = recipeRepository.findById(recipeId)
//                    .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
//            restaurant.setRecipe(recipe);
//        }
//
//        if (file != null && !file.isEmpty()) {
//            testFileService.saveFile(file);
//
//            TestFileEntity uploadedImage = testFileRepository.findAll().stream()
//                    .filter(fileEntity -> fileEntity.getImgUrl() != null)
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("이미지 파일을 찾을 수 없습니다."));
//
//            restaurant.setImage(uploadedImage);
//        }
//
//        restaurant = restaurantRepository.save(restaurant);
//
//        return RestaurantDTO.toDTO(restaurant);
//    }
//
//    public List<RestaurantDTO> getAllRestaurants() {
//        return restaurantRepository.findAll().stream()
//                .map(RestaurantDTO::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    public void deleteRestaurant(Long id, User loginUser) {
//        RestaurantEntity restaurant = restaurantRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));
//
//        if (!restaurant.getOwner().getId().equals(loginUser.getId())) {
//            throw new RuntimeException("삭제 권한이 없습니다.");
//        }
//
//        if (restaurant.getImage() != null) {
//            testFileRepository.delete(restaurant.getImage());
//        }
//
//        restaurantRepository.delete(restaurant);
//    }
//
//    public List<RestaurantDTO> getRestaurantsByUser(User user) {
//        return restaurantRepository.findAllByOwner(user).stream()
//                .map(RestaurantDTO::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    public List<RestaurantDTO> getRestaurantsByRecipeId(Long recipeId) {
//        return restaurantRepository.findAllByRecipeId(recipeId).stream()
//                .map(RestaurantDTO::toDTO)
//                .collect(Collectors.toList());
//    }
//}
