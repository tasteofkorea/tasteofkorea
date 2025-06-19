package com.example.tasteofkorea.dto;

import com.example.tasteofkorea.entity.RestaurantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@Builder
@Setter  // 이 어노테이션을 추가하여 setter 메서드 자동 생성
@NoArgsConstructor // ✅ Jackson 역직렬화를 위해 필요
public class RestaurantDTO {
    private Long id;
    private String englishName;
    private double latitude;
    private double longitude;
    private String ownerName;
    private String file;
    private Long recipeId;
    private int price;
    private MultipartFile newFile;  // 새 이미지 파일 추가 (이미지 수정 시 사용)

    // DTO -> Entity 변환 메서드
    public static RestaurantDTO toDTO(RestaurantEntity restaurantEntity) {
        return RestaurantDTO.builder()
                .id(restaurantEntity.getId())
                .englishName(restaurantEntity.getEnglishName())
                .latitude(restaurantEntity.getLatitude())
                .longitude(restaurantEntity.getLongitude())
                .ownerName(restaurantEntity.getOwner().getUsername())
                .file(restaurantEntity.getFile())
                .recipeId(restaurantEntity.getRecipe() != null ? restaurantEntity.getRecipe().getId() : null)
                .price(restaurantEntity.getPrice())
                .build();
    }

}