package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.RecipeTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeTagRepository extends JpaRepository<RecipeTagEntity, Long> {
}
