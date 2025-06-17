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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository; // ✅ 추가

    // Update
    public RestaurantDTO updateRestaurant(RestaurantDTO metadataJson,String username) throws IOException {
        RestaurantEntity restaurant = restaurantRepository.findById(metadataJson.getId())
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));

        if (!restaurant.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        restaurant.setEnglishName(metadataJson.getEnglishName());
        restaurant.setLatitude(metadataJson.getLatitude());
        restaurant.setLongitude(metadataJson.getLongitude());
        restaurant.setPrice(metadataJson.getPrice());

        if (metadataJson.getRecipeId() != null) {
            RecipeEntity recipe = recipeRepository.findById(metadataJson.getRecipeId() )
                    .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
            restaurant.setRecipe(recipe);
        }

        restaurant = restaurantRepository.save(restaurant);

        return RestaurantDTO.toDTO(restaurant);
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
        return restaurantRepository.findAllByRecipeId(recipeId).stream()
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
}
