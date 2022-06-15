package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.RecipeEntity;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<RecipeEntity> getAll();
    Optional<RecipeEntity> getById(Long id);
    void add(RecipeEntity recipe);
    void update(RecipeEntity recipe);
    void delete(Long id);
}
