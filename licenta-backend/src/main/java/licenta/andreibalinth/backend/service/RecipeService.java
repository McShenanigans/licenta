package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.dto.RecipeEntityDto;
import licenta.andreibalinth.backend.entities.dto.UserToRecipeDto;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<RecipeEntityDto> getAll();
    List<UserToRecipeDto> getAllForUser(Long userId);
    List<RecipeEntityDto> getAllPublicRecipesFromOtherUsers(Long userId);
    Optional<RecipeEntityDto> getById(Long id);
    Optional<UserToRecipeDto> getByIdForUser(Long userId, Long recipeId);
    void add(RecipeEntityDto recipe);
    void addForUser(Long userId, UserToRecipeDto dto);
    void update(RecipeEntityDto recipe);
    void createConnectionBetweenUserAndRecipe(Long userId, Long recipeId);
    void delete(Long id);
    void deleteForUser(Long userId, Long recipeId);
}
