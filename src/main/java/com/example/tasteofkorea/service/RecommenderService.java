//package com.example.tasteofkorea.service;
//
//import com.example.tasteofkorea.dto.RecipeDTO;
//import com.example.tasteofkorea.entity.RecipeEntity;
//import com.example.tasteofkorea.repository.RecipeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class RecommenderService {
//
//    @Autowired //수정
//    private RecipeRepository recipeRepository;
//
//    // 코사인 유사도 계산
//    /*
//    private double cosineSimilarity(Map<String, Integer> userPreferences, List<Integer> recipeScores) {
//        double dotProduct = 0.0;
//        double normA = 0.0;
//        double normB = 0.0;
//        String[] keys = {"spicy", "sour", "salty", "oily", "bigun"};
//
//        for (int i = 0; i < keys.length; i++) {
//            int userPref = userPreferences.getOrDefault(keys[i], 0);
//            int recipeScore = recipeScores.get(i);
//            dotProduct += userPref * recipeScore;
//            normA += Math.pow(userPref, 2);
//            normB += Math.pow(recipeScore, 2);
//        }
//
//        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
//    }
//
//    // 추천 시스템
//    public List<RecipeDTO> recommendFood(Map<String, Integer> userPreferences) {
//        // 사용자 선호도에서 비건 여부를 확인
//        int userIsVegan = userPreferences.getOrDefault("bigun", 0);
//
//        // 모든 레시피 가져오기
//        List<RecipeEntity> allRecipes = recipeRepository.findAll();
//        List<RecipeDTO> recommendedRecipes = new ArrayList<>();
//
//        for (RecipeEntity recipe : allRecipes) {
//            // 비건인 경우, 비건이 아닌 음식을 제외
//            if (userIsVegan == 1 && recipe.getBigun() == 0) {
//                continue; // 비건이 아니면 추천하지 않음
//            }
//
//            // `spicy`, `sour`, `salty`, `oily` 항목이 4 이상일 경우, 해당 값이 1 또는 2인 레시피 제외
//            if ((userPreferences.get("spicy") >= 4 && recipe.getSpicy() <= 2) ||
//                    (userPreferences.get("sour") >= 4 && recipe.getSour() <= 2) ||
//                    (userPreferences.get("salty") >= 4 && recipe.getSalty() <= 2) ||
//                    (userPreferences.get("oily") >= 4 && recipe.getOily() <= 2)) {
//                continue;  // 해당 항목이 4 이상인 경우 1 또는 2인 레시피는 제외
//            }
//
//            // 레시피의 속성 값 (spicy, sour, salty, oily, bigun)
//            List<Integer> recipeScores = Arrays.asList(
//                    recipe.getSpicy(),
//                    recipe.getSour(),
//                    recipe.getSalty(),
//                    recipe.getOily(),
//                    recipe.getBigun()
//            );
//
//            // 코사인 유사도 계산
//            double similarity = cosineSimilarity(userPreferences, recipeScores);
//
//            // 유사도가 0보다 큰 경우에만 추천
//            if (similarity > 0) {
//                RecipeDTO recipeDTO = new RecipeDTO();
//                recipeDTO.setId(recipe.getId());
//                recipeDTO.setKoreanName(recipe.getKoreanName());
//                recipeDTO.setRomanizedName(recipe.getRomanizedName());
//                recipeDTO.setEnglishName(recipe.getEnglishName());
//                recipeDTO.setCategory(recipe.getCategory());
//                recipeDTO.setMadeWith(recipe.getMadeWith());
//                recipeDTO.setImgLink(recipe.getImgLink());
//                recipeDTO.setRecipeLink(recipe.getRecipeLink());
//                recipeDTO.setSpicy(recipe.getSpicy());
//                recipeDTO.setSour(recipe.getSour());
//                recipeDTO.setSalty(recipe.getSalty());
//                recipeDTO.setOily(recipe.getOily());
//                recipeDTO.setBigun(recipe.getBigun());
//                recipeDTO.setCalories(recipe.getCalories());
//
//                recommendedRecipes.add(recipeDTO);
//            }
//        }
//
//        // 추천된 레시피를 유사도에 따라 내림차순 정렬하고, 상위 5개만 반환
//        recommendedRecipes.sort((r1, r2) -> {
//            double similarity1 = cosineSimilarity(userPreferences, Arrays.asList(r1.getSpicy(), r1.getSour(), r1.getSalty(), r1.getOily(), r1.getBigun()));
//            double similarity2 = cosineSimilarity(userPreferences, Arrays.asList(r2.getSpicy(), r2.getSour(), r2.getSalty(), r2.getOily(), r2.getBigun()));
//            return Double.compare(similarity2, similarity1);  // 내림차순으로 정렬
//        });
//
//        return recommendedRecipes.stream().limit(5).collect(Collectors.toList());  // 상위 5개의 추천 결과 반환
//    }*/
//}
