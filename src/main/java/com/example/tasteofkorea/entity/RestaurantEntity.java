package com.example.tasteofkorea.entity;

import com.example.tasteofkorea.dto.RestaurantDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "restaurant")
@NoArgsConstructor
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String englishName;
    private double latitude;
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User owner;


    private String file;
    // 메뉴 관련 필드들
    @ManyToOne
    @JoinColumn(name = "food_id")
    private RecipeEntity recipe;  // 레시피 참조 (한식 메뉴)

    private int price;  // 메뉴 가격

    public RestaurantEntity(User owner, RecipeEntity recipe, RestaurantDTO restaurantDTO) {
        this.owner = owner;
        this.recipe = recipe;
        this.price = restaurantDTO.getPrice();
        this.englishName = restaurantDTO.getEnglishName();
        this.latitude = restaurantDTO.getLatitude();
        this.longitude = restaurantDTO.getLongitude();
    }
}
