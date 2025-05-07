package com.example.tasteofkorea.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "restaurant")
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

    @ManyToOne
    @JoinColumn(name = "test_file_entity_id") // restaurant 테이블에 image_id 컬럼 생김
    private TestFileEntity image;

    // 메뉴 관련 필드들
    @ManyToOne
    @JoinColumn(name = "food_id")
    private RecipeEntity recipe;  // 레시피 참조 (한식 메뉴)

    private int price;  // 메뉴 가격
}
