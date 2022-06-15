package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
}
