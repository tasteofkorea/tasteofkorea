package com.example.tasteofkorea.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "food_filter_list")
@Getter
@Setter
public class FoodFilterListEntity {

    @Id
    private Long id;

    // food_id -> food.id 외래키 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_food"))
    private RecipeEntity food;

    // filter_id -> filter.id 외래키 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filter_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_filter"))
    private FilterEntity filter;
}
