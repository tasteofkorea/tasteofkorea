package com.example.tasteofkorea.dto;


import com.example.tasteofkorea.entity.RecipeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RecipeDTO {
    private Long id;
    private String koreanName;
    private String englishName;
    private String pronunciation;
    private String information;
    private String recipeLink;
    private String eatLink;
    private String recipeSource;
    private String eatingSource;
    private String imageLink;
    private String imageSource;


}
