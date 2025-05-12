package com.example.tasteofkorea.dto;

import com.example.tasteofkorea.entity.RestaurantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor // ✅ Jackson 역직렬화를 위해 필요
public class RestaurantDTO {
    private Long id;
    private String englishName;
    private double latitude;
    private double longitude;
    private String ownerName;
    private Long recipeId;
    private int price;


    // static 메서드로 변경하여 클래스에서 바로 호출할 수 있도록 함
    public static RestaurantDTO toDTO(RestaurantEntity restaurantEntity) {
        return new RestaurantDTO(
                restaurantEntity.getId(),
                restaurantEntity.getEnglishName(),
                restaurantEntity.getLatitude(),
                restaurantEntity.getLongitude(),
                restaurantEntity.getOwner().getName(),
                restaurantEntity.getRecipe().getId(),
                restaurantEntity.getPrice()
        );
    }

}
