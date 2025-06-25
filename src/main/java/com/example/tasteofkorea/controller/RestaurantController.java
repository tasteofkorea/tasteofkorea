package com.example.tasteofkorea.controller;

import com.example.tasteofkorea.annotation.LoginUser;
import com.example.tasteofkorea.dto.RestaurantDTO;
import com.example.tasteofkorea.entity.RestaurantEntity;
import com.example.tasteofkorea.entity.User;
import com.example.tasteofkorea.jwt.JwtUtil;
import com.example.tasteofkorea.service.RestaurantService;
import com.example.tasteofkorea.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {

	private final RestaurantService restaurantService;
	private final ObjectMapper objectMapper;
	private final JwtUtil jwtUtil;
	private final S3Service s3Service;

	// 공통 로직: "Bearer " 제거
	private String extractToken(String bearerToken) {
		return bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
	}

	@PostMapping("/update/image")
	public ResponseEntity<String> updateRestaurantImage(
			@RequestHeader("Authorization") String bearerToken,
			@RequestParam("file") MultipartFile file) {
		try {
			String token = extractToken(bearerToken);

			if (jwtUtil.validateToken(token)) {
				if (file.isEmpty()) {
					return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
				}
				return ResponseEntity.ok(s3Service.upload(file));
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
			}
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "식당 등록", description = "식당 등록 (JSON 문자열 전송, 이미지 URL 포함)")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> createRestaurant(
			@RequestHeader("Authorization") String bearerToken,
			@RequestParam("metadata") String metadataJson) {  // 👉 JSON 문자열로 받음
		try {
			String token = extractToken(bearerToken);
			String username = jwtUtil.getUsernameFromToken(token);

			// JSON 문자열 → DTO
			RestaurantDTO metadata = objectMapper.readValue(metadataJson, RestaurantDTO.class);

			// 서비스 호출
			restaurantService.createRestaurant(metadata, username);

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
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> updateRestaurant(
			@PathVariable(name = "id") Long id,  // ✅ 여기에 name 명시
			@RequestHeader("Authorization") String bearerToken,
			@RequestParam("metadata") String metadataJson,
			@RequestParam(value = "newFile", required = false) MultipartFile newFile) {

		try {
			String token = extractToken(bearerToken);
			String username = jwtUtil.getUsernameFromToken(token);

			// JSON 문자열 -> DTO
			RestaurantDTO metadata = objectMapper.readValue(metadataJson, RestaurantDTO.class);

			// ❗ newFile을 DTO에 주입
			metadata.setNewFile(newFile);

			// 기존 이미지 삭제 (이미 있는 이미지 URL 기준)
			if (metadata.getFile() != null && !metadata.getFile().isEmpty()) {
				s3Service.deleteFile(metadata.getFile());
			}

			// 서비스 로직 실행
			restaurantService.updateRestaurant(metadata, username);

			return ResponseEntity.ok("식당 수정 성공");
		} catch (Exception e) {
			return ResponseEntity.status(400).body("수정 실패: " + e.getMessage());
		}
	}






	@Operation(summary = "식당 삭제", description = "식당 삭제 (로그인 필요)")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRestaurant(
			@PathVariable("id") Long id,  // ✅ 필수
			@RequestHeader("Authorization") String bearerToken) {  // @RequestBody 삭제
		try {
			String token = extractToken(bearerToken);
			String username = jwtUtil.getUsernameFromToken(token);

			// 식당 정보 가져오기
			RestaurantDTO restaurant = restaurantService.getRestaurantById(id);

			// S3에서 이미지 삭제
			if (restaurant.getFile() != null && !restaurant.getFile().isEmpty()) {
				s3Service.deleteFile(restaurant.getFile());  // S3에서 기존 이미지 삭제
			}

			// 식당 삭제
			restaurantService.deleteRestaurant(id, username);
			return ResponseEntity.ok("식당 삭제 성공");
		} catch (Exception e) {
			return ResponseEntity.status(400).body("삭제 실패: " + e.getMessage());
		}
	}


	@Operation(summary = "내가 등록한 식당 조회", description = "로그인한 사용자가 등록한 식당 리스트를 반환합니다.")
	@GetMapping("/my")
	public ResponseEntity<List<RestaurantDTO>> getMyRestaurants(
			@RequestHeader("Authorization") String bearerToken) {
		String token = extractToken(bearerToken);
		String username = jwtUtil.getUsernameFromToken(token);
		List<RestaurantDTO> myRestaurants = restaurantService.getRestaurantsByUser(username);
		return ResponseEntity.ok(myRestaurants);
	}

	@Operation(summary = "레시피 ID로 식당 조회", description = "특정 recipeId에 해당하는 식당을 조회합니다.")
	@GetMapping("/by-recipe")
	public ResponseEntity<List<RestaurantDTO>> getRestaurantsByRecipe(@RequestParam(name = "recipeId") Long recipeId) {
		List<RestaurantDTO> result = restaurantService.getRestaurantsByRecipeId(recipeId);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RestaurantDTO> getMyRestaurantById(
			@PathVariable(name = "id") Long id,  // ✅ name 속성 추가
			@RequestHeader(name = "Authorization") String bearerToken  // ✅ 이것도 명시 추천
	) {
		String token = extractToken(bearerToken);
		String username = jwtUtil.getUsernameFromToken(token);
		RestaurantDTO restaurantDTO = restaurantService.getRestaurantByIdAndUsername(id, username);
		return ResponseEntity.ok(restaurantDTO);
	}


	@Operation(summary = "메뉴 주문", description = "식당 이름과 메뉴 ID로 주문")
	@PostMapping("/order")
	public ResponseEntity<String> orderMenuByName(
			@RequestParam(name = "restaurantName") String restaurantName,
			@RequestParam(name = "recipeId") Long recipeId
	) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String kioskUrl = "https://tastekiosk.site/api/order";

			// ✅ restaurantService가 Optional 반환 안 하는 경우
			RestaurantEntity restaurant = restaurantService.findByEnglishNameAndRecipeId(restaurantName, recipeId);
			if (restaurant == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 식당/메뉴 조합을 찾을 수 없습니다.");
			}

			Map<String, Object> order = new HashMap<>();
			order.put("restaurantId", restaurant.getId());     // ID를 전달
			order.put("restaurantName", restaurantName);       // 이름도 함께 전달 (Node용)
			order.put("menuId", recipeId);

			restTemplate.postForEntity(kioskUrl, order, String.class);
			return ResponseEntity.ok("주문 완료");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류: " + e.getMessage());
		}
	}





}
