package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.RecipeEntity;
import licenta.andreibalinth.backend.entities.RecipeTagEntity;
import licenta.andreibalinth.backend.entities.dto.RecipeTagEntityDto;
import licenta.andreibalinth.backend.mappers.RecipeTagMapper;
import licenta.andreibalinth.backend.repository.RecipeRepository;
import licenta.andreibalinth.backend.repository.RecipeTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class RecipeTagServiceImpl implements RecipeTagService{
    private final RecipeTagRepository repository;
    private final RecipeTagMapper mapper;
    private final RecipeRepository recipeRepository;

    @Override
    public List<RecipeTagEntityDto> getAll() {
        return mapper.recipeTagEntityListToRecipeTagEntityDtoList(repository.findAll());
    }

    @Override
    public List<RecipeTagEntityDto> getAllByRecipeId(Long recipeId) {
        AtomicReference<List<RecipeTagEntity>> entities = new AtomicReference<>(new ArrayList<>());
        Optional<RecipeEntity> recipeOpt = recipeRepository.findById(recipeId);
        recipeOpt.ifPresent(recipe -> entities.set(recipe.getTags()));
        return mapper.recipeTagEntityListToRecipeTagEntityDtoList(entities.get());
    }

    @Override
    public Optional<RecipeTagEntityDto> getById(Long id) {
        Optional<RecipeTagEntity> tagOpt = repository.findById(id);
        return tagOpt.map(mapper::recipeTagEntityToRecipeTagEntityDto);
    }

    @Override
    public void add(RecipeTagEntityDto tag) {
        repository.save(mapper.recipeTagDtoToRecipeTagEntity(tag));
    }

    @Override
    @Transactional
    public void update(RecipeTagEntityDto tag) {
        Optional<RecipeTagEntity> tagOpt = repository.findById(tag.getId());
        if(tagOpt.isEmpty()) return;
        RecipeTagEntity savedTag = tagOpt.get();
        savedTag.setName(tag.getName());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
