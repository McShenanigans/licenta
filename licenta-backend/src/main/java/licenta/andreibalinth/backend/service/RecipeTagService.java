package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.RecipeTagEntity;
import licenta.andreibalinth.backend.entities.dto.RecipeTagEntityDto;

import java.util.List;
import java.util.Optional;

public interface RecipeTagService{
    List<RecipeTagEntityDto> getAll();
    List<RecipeTagEntityDto> getAllByRecipeId(Long recipeId);
    Optional<RecipeTagEntityDto> getById(Long id);
    void add(RecipeTagEntityDto tag);
    void update(RecipeTagEntityDto tag);
    void delete(Long id);
}
