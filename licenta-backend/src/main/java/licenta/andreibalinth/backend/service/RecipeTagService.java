package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.RecipeTagEntity;

import java.util.List;
import java.util.Optional;

public interface RecipeTagService{
    List<RecipeTagEntity> getAll();
    Optional<RecipeTagEntity> getById(Long id);
    void add(RecipeTagEntity tag);
    void update(RecipeTagEntity tag);
    void delete(Long id);
}
