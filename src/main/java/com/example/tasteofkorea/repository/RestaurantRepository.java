//package com.example.tasteofkorea.repository;
//
//import com.example.tasteofkorea.entity.RestaurantEntity;
//import com.example.tasteofkorea.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//@Repository
//
//public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
//    // 기본적인 CRUD 연산을 제공
//    List<RestaurantEntity> findAllByOwner(User owner);
//
//    List<RestaurantEntity> findAllByRecipeId(Long recipeId);
//
//}
////