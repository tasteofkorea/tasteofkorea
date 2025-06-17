package com.example.tasteofkorea.repository;

import com.example.tasteofkorea.dto.RecipeDTO;
import com.example.tasteofkorea.entity.RecipeEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

	@Modifying
	@Query("UPDATE RecipeEntity r SET r.views = r.views + 1 WHERE r.id = :id")
	void plusView(@Param("id") Long id);

	@Query("select r FROM RecipeEntity r where r.id not in (select f.food.id from FoodFilterListEntity f where f.filter.id in :filterList) order by r.views desc")
	Page<RecipeEntity> findAllByFilter(@Param("filterList") List<Integer> filterCriteria, Pageable pageable);
}
