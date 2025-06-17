package com.example.tasteofkorea.service;

import com.example.tasteofkorea.dto.FilterCriteria;
import com.example.tasteofkorea.dto.FilterRecipeList;
import com.example.tasteofkorea.dto.FilteredAllergyInfo;
import com.example.tasteofkorea.dto.RecipeDTO;
import com.example.tasteofkorea.entity.FilterEntity;
import com.example.tasteofkorea.entity.FoodFilterListEntity;
import com.example.tasteofkorea.entity.RecipeEntity;
import com.example.tasteofkorea.repository.*;
import java.lang.reflect.Field; // ← 요거!
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.*;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    private final FoodFilterListRepository foodFilterListRepository;

    public FilterCriteria getFilterCriteriaByFoodId(Long foodId) {
        List<FoodFilterListEntity> list = foodFilterListRepository.findByFoodId(foodId);

        FilterCriteria.FilterCriteriaBuilder builder = FilterCriteria.builder();

        for (FoodFilterListEntity entity : list) {
            FilterEntity f = entity.getFilter();
            if (f == null) continue;

            if (f.getEgg() != null) builder.egg(f.getEgg());
            if (f.getMilk() != null) builder.milk(f.getMilk());
            if (f.getBuckwheat() != null) builder.buckwheat(f.getBuckwheat());
            if (f.getPeanut() != null) builder.peanut(f.getPeanut());
            if (f.getSoybean() != null) builder.soybean(f.getSoybean());
            if (f.getWheat() != null) builder.wheat(f.getWheat());
            if (f.getFish() != null) builder.fish(f.getFish());
            if (f.getCrab() != null) builder.crab(f.getCrab());
            if (f.getShrimp() != null) builder.shrimp(f.getShrimp());
            if (f.getPork() != null) builder.pork(f.getPork());
            if (f.getPeach() != null) builder.peach(f.getPeach());
            if (f.getTomato() != null) builder.tomato(f.getTomato());
            if (f.getSulfites() != null) builder.sulfites(f.getSulfites());
            if (f.getWalnut() != null) builder.walnut(f.getWalnut());
            if (f.getChicken() != null) builder.chicken(f.getChicken());
            if (f.getBeef() != null) builder.beef(f.getBeef());
            if (f.getSquid() != null) builder.squid(f.getSquid());
            if (f.getBivalvesAndAbalone() != null) builder.bivalvesAndAbalone(f.getBivalvesAndAbalone());
            if (f.getPineNut() != null) builder.pineNut(f.getPineNut());
        }

        return builder.build();
    }

    public FilteredAllergyInfo getFilteredAllergyInfo(Long foodId, FilterCriteria criteria) {
        Map<String, Long> allergyMap = new HashMap<>();

        if (criteria.getEgg() != null) allergyMap.put("egg", criteria.getEgg());
        if (criteria.getMilk() != null) allergyMap.put("milk", criteria.getMilk());
        if (criteria.getBuckwheat() != null) allergyMap.put("buckwheat", criteria.getBuckwheat());
        if (criteria.getPeanut() != null) allergyMap.put("peanut", criteria.getPeanut());
        if (criteria.getSoybean() != null) allergyMap.put("soybean", criteria.getSoybean());
        if (criteria.getWheat() != null) allergyMap.put("wheat", criteria.getWheat());
        if (criteria.getFish() != null) allergyMap.put("fish", criteria.getFish());
        if (criteria.getCrab() != null) allergyMap.put("crab", criteria.getCrab());
        if (criteria.getShrimp() != null) allergyMap.put("shrimp", criteria.getShrimp());
        if (criteria.getPork() != null) allergyMap.put("pork", criteria.getPork());
        if (criteria.getPeach() != null) allergyMap.put("peach", criteria.getPeach());
        if (criteria.getTomato() != null) allergyMap.put("tomato", criteria.getTomato());
        if (criteria.getSulfites() != null) allergyMap.put("sulfites", criteria.getSulfites());
        if (criteria.getWalnut() != null) allergyMap.put("walnut", criteria.getWalnut());
        if (criteria.getChicken() != null) allergyMap.put("chicken", criteria.getChicken());
        if (criteria.getBeef() != null) allergyMap.put("beef", criteria.getBeef());
        if (criteria.getSquid() != null) allergyMap.put("squid", criteria.getSquid());
        if (criteria.getBivalvesAndAbalone() != null) allergyMap.put("bivalvesAndAbalone", criteria.getBivalvesAndAbalone());
        if (criteria.getPineNut() != null) allergyMap.put("pineNut", criteria.getPineNut());

        return FilteredAllergyInfo.builder()
                .foodId(foodId)
                .allergyIngredients(allergyMap)
                .build();
    }


}
