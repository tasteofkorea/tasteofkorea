package com.example.tasteofkorea.service;

import com.example.tasteofkorea.dto.RestaurantDTO;
import com.example.tasteofkorea.entity.RecipeEntity;
import com.example.tasteofkorea.entity.RestaurantEntity;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.repository.RecipeRepository;
import com.example.tasteofkorea.repository.RestaurantRepository;
import com.example.tasteofkorea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository; // ✅ 추가
    private final S3Service s3Service;

    // Update
    public RestaurantDTO updateRestaurant(RestaurantDTO metadataJson, String username) throws IOException {
        RestaurantEntity restaurant = restaurantRepository.findById(metadataJson.getId())
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));

        // 권한 체크
        if (!restaurant.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 기존 데이터 업데이트
        restaurant.setEnglishName(metadataJson.getEnglishName());
        restaurant.setLatitude(metadataJson.getLatitude());
        restaurant.setLongitude(metadataJson.getLongitude());
        restaurant.setPrice(metadataJson.getPrice());


        // 레시피 업데이트
        if (metadataJson.getRecipeId() != null) {
            RecipeEntity recipe = recipeRepository.findById(metadataJson.getRecipeId())
                    .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
            restaurant.setRecipe(recipe);
        }

        // 새 이미지가 있을 경우 업로드 후 DB에 저장
        if (metadataJson.getNewFile() != null && !metadataJson.getNewFile().isEmpty()) {
            String newImageUrl = s3Service.upload(metadataJson.getNewFile());  // 새 이미지 업로드
            restaurant.setFile(newImageUrl);  // DB에 새로운 이미지 URL 저장
        }

        // DB 저장
        restaurant = restaurantRepository.save(restaurant);

        return RestaurantDTO.toDTO(restaurant);  // DTO로 반환
    }


    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantDTO::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteRestaurant(Long id, String username) {
        RestaurantEntity restaurant = restaurantRepository.findByIdAndUserName(id,username)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));

        restaurantRepository.delete(restaurant);
    }

    public List<RestaurantDTO> getRestaurantsByUser(String userName) {
        return restaurantRepository.findAllByUserName(userName).stream()
                .map(RestaurantDTO::toDTO)
                .collect(Collectors.toList());
    }

    public List<RestaurantDTO> getRestaurantsByRecipeId(Long recipeId) {
        return Optional.ofNullable(restaurantRepository.findAllByRecipeId(recipeId))
                .orElse(Collections.emptyList())  // null이면 빈 리스트로 대체
                .stream()
                .map(RestaurantDTO::toDTO)
                .collect(Collectors.toList());
    }


    public void createRestaurant(RestaurantDTO metadataJson, String username) {
        // 레시피 설정
        if (metadataJson.getRecipeId() != null) {
            RecipeEntity recipe = recipeRepository.findById(metadataJson.getRecipeId())
                    .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
            User user = userRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException());
            restaurantRepository.save(new RestaurantEntity(user,recipe,metadataJson));
        }
    }

    // 특정 식당 조회
    public RestaurantDTO getRestaurantById(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));

        return RestaurantDTO.toDTO(restaurant);
    }


    public RestaurantDTO getRestaurantByIdAndUsername(Long id, String username) {
        RestaurantEntity entity = restaurantRepository.findByIdAndUserName(id, username)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없거나 접근 권한이 없습니다."));
        return RestaurantDTO.toDTO(entity);
    }


    public void addMenuToRestaurant(Long existingRestaurantId, Long recipeId, int price, String file, String username) {
        // 기존 식당 정보 가져오기
        RestaurantEntity existing = restaurantRepository.findById(existingRestaurantId)
                .orElseThrow(() -> new RuntimeException("기존 식당을 찾을 수 없습니다."));

        // 사용자 인증 확인
        if (!existing.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        // 레시피 가져오기
        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));

        // 새로운 식당(메뉴) 객체 생성
        RestaurantEntity newRestaurant = new RestaurantEntity();
        newRestaurant.setOwner(existing.getOwner());
        newRestaurant.setEnglishName(existing.getEnglishName());
        newRestaurant.setLatitude(existing.getLatitude());
        newRestaurant.setLongitude(existing.getLongitude());
        newRestaurant.setRecipe(recipe);
        newRestaurant.setPrice(price);
        newRestaurant.setFile(file);  // URL 직접 주입

        // DB 저장
        restaurantRepository.save(newRestaurant);
    }

    public RestaurantEntity findByEnglishNameAndRecipeId(String englishName, Long recipeId) {
        return restaurantRepository.findByEnglishNameAndRecipeId(englishName, recipeId)
                .orElse(null);
    }




}
