package com.example.tasteofkorea.entity;

import com.example.tasteofkorea.dto.RecipeDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "food")
@Getter
@Setter
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "k_name", length = 255)
    private String koreanName;

    @Column(name = "e_name", length = 500)
    private String englishName;

    @Column(length = 255)
    private String pronunciation;

    @Column(length = 1000)
    private String information;

    @Column(name = "recipe_link", length = 500)
    private String recipeLink;

    @Column(name = "eat_link", length = 500)
    private String eatLink;

    @Column(name = "recipe_source", length = 255)
    private String recipeSource;

    @Column(name = "eating_source", length = 255)
    private String eatingSource;

    @Column(name = "img_link", length = 500)
    private String imageLink;

    @Column(name = "image_source", length = 500)
    private String imageSource;

    @Column(name = "view")
    private Long views;

    public RecipeDTO toDto() {
        return RecipeDTO.builder()
            .id(this.getId())
            .koreanName(this.getKoreanName())
            .englishName(this.getEnglishName())
            .pronunciation(this.getPronunciation())
            .information(this.getInformation())
            .recipeLink(this.getRecipeLink())
            .eatLink(this.getEatLink())
            .recipeSource(this.getRecipeSource())
            .eatingSource(this.getEatingSource())
            .imageLink(this.getImageLink())
            .imageSource(this.getImageSource())
            .build();
    }

}