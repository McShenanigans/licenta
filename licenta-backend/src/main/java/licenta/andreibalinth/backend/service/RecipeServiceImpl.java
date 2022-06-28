package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.RecipeEntity;
import licenta.andreibalinth.backend.entities.UserEntity;
import licenta.andreibalinth.backend.entities.UserToRecipeEntity;
import licenta.andreibalinth.backend.entities.dto.RecipeEntityDto;
import licenta.andreibalinth.backend.entities.dto.UserToRecipeDto;
import licenta.andreibalinth.backend.mappers.RecipeMapper;
import licenta.andreibalinth.backend.mappers.UserToRecipeMapper;
import licenta.andreibalinth.backend.repository.RecipeRepository;
import licenta.andreibalinth.backend.repository.UserRepository;
import licenta.andreibalinth.backend.repository.UserToRecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService{
    private final RecipeRepository repository;
    private final RecipeMapper recipeMapper;
    private final UserToRecipeRepository utrRepository;
    private final UserToRecipeMapper utrMapper;
    private final UserRepository userRepository;

    @Override
    public List<RecipeEntityDto> getAll() {
        return recipeMapper.recipeEntityListToRecipeEntityDtoList(repository.findAll());
    }

    @Override
    public List<UserToRecipeDto> getAllForUser(Long userId) {
        return utrMapper.userToRecipeEntityListTouserToRecipeDtoList(utrRepository.findAllByUser_Id(userId));
    }

    @Override
    public Optional<RecipeEntityDto> getById(Long id) {
        return repository.findById(id).map(recipeMapper::recipeEntityToRecipeEntityDto);
    }

    @Override
    public Optional<UserToRecipeDto> getByIdForUser(Long userId, Long recipeId) {
        Optional<UserToRecipeEntity> utrOptional = utrRepository.findByUser_IdAndRecipe_Id(userId, recipeId);
        return utrOptional.map(utrMapper::userToRecipeEntityToUserToRecipeDto);
    }

    @Override
    public void add(RecipeEntityDto recipe) {
        repository.save(recipeMapper.recipeEntityDtoToRecipeEntity(recipe));
    }

    @Override
    public void addForUser(Long userId, UserToRecipeDto dto) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isEmpty()) return;
        RecipeEntity recipe = repository.save(recipeMapper.recipeEntityDtoToRecipeEntity(dto.getRecipe()));
        utrRepository.save(new UserToRecipeEntity(user.get(), recipe, dto.isOwner()));
    }

    @Override
    @Transactional
    public void update(RecipeEntityDto recipe) {
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

    @Override
    public void deleteForUser(Long userId, Long recipeId) {
        Optional<UserToRecipeEntity> utrOpt = utrRepository.findByUser_IdAndRecipe_Id(userId, recipeId);
        if(utrOpt.isEmpty()) return;
        if(utrOpt.get().isOwner()) {
            utrRepository.findAllByRecipe_Id(recipeId).forEach(utrRepository::delete);
            repository.delete(utrOpt.get().getRecipe());
        }
        else utrRepository.delete(utrOpt.get());
    }
}
