package com.example.tasteofkorea.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FilterRecipeList {
	private List<RecipeDTO> recipeDtoList;
	private int page;
	private int size;
	private int totalPage;

}
