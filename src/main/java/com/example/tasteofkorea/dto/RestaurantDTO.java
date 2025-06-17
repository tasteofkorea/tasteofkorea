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

    public static RestaurantDTO toDTO(RestaurantEntity restaurantEntity) {
        return RestaurantDTO.builder()
            .id(restaurantEntity.getId())
            .englishName(restaurantEntity.getEnglishName())
            .latitude(restaurantEntity.getLatitude())
            .longitude(restaurantEntity.getLongitude())
            .ownerName(restaurantEntity.getOwner().getUsername())
            .file(restaurantEntity.getFile())
            .recipeId(restaurantEntity.getRecipe().getId())
            .price(restaurantEntity.getPrice())
            .build();
    }

}
