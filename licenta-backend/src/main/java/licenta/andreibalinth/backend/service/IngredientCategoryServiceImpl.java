package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.IngredientCategoryEntity;
import licenta.andreibalinth.backend.entities.dto.IngredientCategoryDto;
import licenta.andreibalinth.backend.repository.IngredientCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IngredientCategoryServiceImpl implements IngredientCategoryService{
    private final IngredientCategoryRepository repository;

    @Override
    public List<IngredientCategoryDto> getAll() {
        return convertEntitiesToDtos(repository.findAll());
    }

    @Override
    public Optional<IngredientCategoryDto> getById(Long id) {
        Optional<IngredientCategoryEntity> entityOpt = repository.findById(id);
        return entityOpt.map(this::convertEntityToDto);
    }

    @Override
    public void add(IngredientCategoryDto category) {
        repository.save(convertDtoToEntity(category));
    }

    @Override
    @Transactional
    public void update(IngredientCategoryDto category) {
        Optional<IngredientCategoryEntity> categoryEntityOptional = repository.findById(category.getId());
        if(categoryEntityOptional.isEmpty()) return;
        IngredientCategoryEntity savedCategory = categoryEntityOptional.get();
        savedCategory.setName(category.getName());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private IngredientCategoryEntity convertDtoToEntity(IngredientCategoryDto dto){
        IngredientCategoryEntity entity = new IngredientCategoryEntity();
        entity.setName(dto.getName());
        entity.setIngredients(new ArrayList<>());
        return entity;
    }

    private List<IngredientCategoryDto> convertEntitiesToDtos(List<IngredientCategoryEntity> entities){
        return entities.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private IngredientCategoryDto convertEntityToDto(IngredientCategoryEntity entity){
        IngredientCategoryDto dto = new IngredientCategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
