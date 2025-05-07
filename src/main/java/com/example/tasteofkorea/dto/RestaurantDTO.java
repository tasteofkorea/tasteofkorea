package com.example.tasteofkorea.dto;

import com.example.tasteofkorea.entity.RestaurantEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor // ✅ Jackson 역직렬화를 위해 필요
public class RestaurantDTO {
    private Long id;
    private String englishName;
    private double latitude;
    private double longitude;
    private String ownerName;
    private String imageUrl;
    private Long recipeId;
    private int price;
    // Constructor to map data from
    // ✅ 기본 생성자 추가

    public RestaurantDTO(Long id, String englishName, double latitude, double longitude,
                         String ownerName, String imageUrl, Long recipeId, int price) {
        this.id = id;
        this.englishName = englishName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ownerName = ownerName;
        this.imageUrl = imageUrl;
        this.recipeId = recipeId;
        this.price = price;
    }

    // static 메서드로 변경하여 클래스에서 바로 호출할 수 있도록 함
    public static RestaurantDTO toDTO(RestaurantEntity restaurantEntity) {
        return new RestaurantDTO(
                restaurantEntity.getId(),
                restaurantEntity.getEnglishName(),
                restaurantEntity.getLatitude(),
                restaurantEntity.getLongitude(),
                restaurantEntity.getOwner().getName(),  // User의 이름
                restaurantEntity.getImage().getImgUrl(),  // 이미지 URL
                restaurantEntity.getRecipe().getId(),
                restaurantEntity.getPrice()
        );
    }

}
