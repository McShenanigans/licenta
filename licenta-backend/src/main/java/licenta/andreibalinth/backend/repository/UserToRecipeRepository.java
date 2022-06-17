package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.UserToRecipeEntity;
import licenta.andreibalinth.backend.entities.embeddingKeys.UserToRecipeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserToRecipeRepository extends JpaRepository<UserToRecipeEntity, UserToRecipeKey> {
    List<UserToRecipeEntity> findAllByUser_Id(Long userId);
    Optional<UserToRecipeEntity> findByUser_IdAndRecipe_Id(Long userId, Long recipeId);
}
