package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.RecipeTimeTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeTimeTagRepository extends JpaRepository<RecipeTimeTagEntity, Long> {
}
