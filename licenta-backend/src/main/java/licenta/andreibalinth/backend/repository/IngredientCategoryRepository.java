package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.IngredientCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategoryEntity, Long> {
}
