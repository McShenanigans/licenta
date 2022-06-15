package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.dto.IngredientCategoryDto;

import java.util.List;
import java.util.Optional;

public interface IngredientCategoryService {
    List<IngredientCategoryDto> getAll();
    Optional<IngredientCategoryDto> getById(Long id);
    void add(IngredientCategoryDto category);
    void update(IngredientCategoryDto category);
    void delete(Long id);
}
