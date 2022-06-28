package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.RecipeIngredientQuantity;
import licenta.andreibalinth.backend.entities.embeddingKeys.RecipeQuantityKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeToIngredientRepository extends JpaRepository<RecipeIngredientQuantity, RecipeQuantityKey> {
    List<RecipeIngredientQuantity> findAllByRecipe_Id(Long recipeId);
    Optional<RecipeIngredientQuantity> findAllByRecipe_IdAndIngredient_Id(Long recipeId, Long ingredientId);
}
