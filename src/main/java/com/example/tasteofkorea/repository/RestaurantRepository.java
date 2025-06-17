package com.example.tasteofkorea.repository;

import com.example.tasteofkorea.entity.RestaurantEntity;
import com.example.tasteofkorea.entity.User;
import io.micrometer.common.KeyValues;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Repository

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    List<RestaurantEntity> findAllByRecipeId(Long recipeId);

    @Query("select r FROM RestaurantEntity r where r.id  = :id and r.owner.username =:username")
    Optional<RestaurantEntity> findByIdAndUserName(@Param("id") Long id,@Param("userName") String username);

    @Query("select r FROM RestaurantEntity r where r.owner.username =:username")
    List<RestaurantEntity> findAllByUserName(@Param("userName") String userName);
}
//