package com.example.tasteofkorea.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class FilteredAllergyInfo {
    private Long foodId;
    private Map<String, Long> allergyIngredients;
}
