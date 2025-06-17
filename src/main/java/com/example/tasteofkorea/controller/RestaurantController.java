package com.example.tasteofkorea.controller;

import com.example.tasteofkorea.annotation.LoginUser;
import com.example.tasteofkorea.dto.RestaurantDTO;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.jwt.JwtUtil;
import com.example.tasteofkorea.service.RestaurantService;
import com.example.tasteofkorea.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {

	private final RestaurantService restaurantService;
	private final ObjectMapper objectMapper;
	private final JwtUtil jwtUtil;
	private final S3Service s3Service;

	@PostMapping("/update/image")
	public ResponseEntity<String> updateRestaurantImage(
		@RequestHeader("Authorization") String bearerToken,
		@RequestParam("file") MultipartFile file) {
		try {
			if(jwtUtil.validateToken(bearerToken)){

				if (file.isEmpty()) {
					return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
				}
				return ResponseEntity.ok(s3Service.upload(file));
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");

			}
		}catch (IOException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}


	@Operation(summary = "식당 등록", description = "식당 등록 (JSON + 이미지)")
	@PostMapping
	public ResponseEntity<String> createRestaurant(
		@RequestHeader("Authorization") String bearerToken,
		@RequestParam("metadata") RestaurantDTO metadataJson,
		@RequestParam(value = "file", required = false) MultipartFile file
	) {
		try {
			String username = jwtUtil.getUsernameFromBearer(bearerToken);
			restaurantService.createRestaurant(metadataJson,username);
			return ResponseEntity.ok("식당 등록 성공");
		} catch (Exception e) {
			return ResponseEntity.status(400).body("등록 실패: " + e.getMessage());
		}
	}

	@Operation(summary = "식당 전체 조회", description = "모든 식당을 조회합니다. (로그인 불필요)")
	@GetMapping
	public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
		return ResponseEntity.ok(restaurantService.getAllRestaurants());
	}

	@Operation(summary = "식당 수정", description = "식당 정보 수정 (JSON + 이미지)")
	@PutMapping("/{id}")
	public ResponseEntity<String> updateRestaurant(
		@PathVariable Long id,
		@RequestHeader("Authorization") String bearerToken,
		@RequestParam("metadata") RestaurantDTO metadataJson) {
		String username = jwtUtil.getUsernameFromBearer(bearerToken);
		try {
			restaurantService.updateRestaurant(
				metadataJson,username
			);
			return ResponseEntity.ok("식당 수정 성공");
		} catch (Exception e) {
			return ResponseEntity.status(400).body("수정 실패: " + e.getMessage());
		}
	}

	@Operation(summary = "식당 삭제", description = "식당 삭제 (로그인 필요)")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRestaurant(@PathVariable Long id,
		@RequestHeader("Authorization") String bearerToken) {
		String username = jwtUtil.getUsernameFromBearer(bearerToken);
		try {
			restaurantService.deleteRestaurant(id, username);
			return ResponseEntity.ok("식당 삭제 성공");
		} catch (Exception e) {
			return ResponseEntity.status(400).body("삭제 실패: " + e.getMessage());
		}
	}


	@Operation(summary = "내가 등록한 식당 조회", description = "로그인한 사용자가 등록한 식당 리스트를 반환합니다.")
	@GetMapping("/my")
	public ResponseEntity<List<RestaurantDTO>> getMyRestaurants(@RequestHeader("Authorization") String bearerToken) {
		String username = jwtUtil.getUsernameFromBearer(bearerToken);
		List<RestaurantDTO> myRestaurants = restaurantService.getRestaurantsByUser(username);
		return ResponseEntity.ok(myRestaurants);
	}

	@Operation(summary = "레시피 ID로 식당 조회", description = "특정 recipeId에 해당하는 식당을 조회합니다.")
	@GetMapping("/by-recipe")
	public ResponseEntity<List<RestaurantDTO>> getRestaurantsByRecipe(@RequestParam Long recipeId) {
		List<RestaurantDTO> result = restaurantService.getRestaurantsByRecipeId(recipeId);
		return ResponseEntity.ok(result);
	}
}
