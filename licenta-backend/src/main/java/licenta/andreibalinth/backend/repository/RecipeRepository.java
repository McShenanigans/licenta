package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
}
