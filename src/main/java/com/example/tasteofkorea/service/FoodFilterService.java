package com.example.tasteofkorea.service;

import com.example.tasteofkorea.dto.FilterRecipeList;
import com.example.tasteofkorea.dto.RecipeDTO;
import com.example.tasteofkorea.entity.FoodFilterListEntity;
import com.example.tasteofkorea.entity.RecipeEntity;
import com.example.tasteofkorea.repository.*;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FoodFilterService {

    private final RecipeRepository recipeRepository;

    public FilterRecipeList getSafeFoods(List<Integer> filterCriteria, Pageable pageable) {
        Page<RecipeEntity> recipeEntityList=  recipeRepository.findAllByFilter(filterCriteria,pageable);
        List<RecipeDTO> recipeDtoList =  recipeEntityList.getContent().stream().map(r -> r.toDto()).toList();
        return new FilterRecipeList(recipeDtoList, pageable.getPageNumber()+1,
            pageable.getPageSize(), recipeEntityList.getTotalPages());
    }
}
