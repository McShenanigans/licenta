package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.RecipeEntity;
import licenta.andreibalinth.backend.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService{
    private final RecipeRepository repository;

    @Override
    public List<RecipeEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<RecipeEntity> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void add(RecipeEntity recipe) {
        repository.save(recipe);
    }

    @Override
    @Transactional
    public void update(RecipeEntity recipe) {
        Optional<RecipeEntity> recipeOpt = repository.findById(recipe.getId());
        if(recipeOpt.isEmpty()) return;
        RecipeEntity savedRecipe = recipeOpt.get();
        savedRecipe.setName(recipe.getName());
        savedRecipe.setDescription(recipe.getDescription());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
