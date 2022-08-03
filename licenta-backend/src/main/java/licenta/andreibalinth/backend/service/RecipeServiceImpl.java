package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.*;
import licenta.andreibalinth.backend.entities.dto.*;
import licenta.andreibalinth.backend.entities.embeddingKeys.RecipeQuantityKey;
import licenta.andreibalinth.backend.mappers.*;
import licenta.andreibalinth.backend.repository.RecipeRepository;
import licenta.andreibalinth.backend.repository.RecipeToIngredientRepository;
import licenta.andreibalinth.backend.repository.UserRepository;
import licenta.andreibalinth.backend.repository.UserToRecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository repository;
    private final RecipeMapper recipeMapper;
    private final UserToRecipeRepository utrRepository;
    private final UserToRecipeMapper utrMapper;
    private final UserRepository userRepository;
    private final RecipeToIngredientRepository rtiRepository;
    private final RecipeIngredientQuantityMapper rtiMapper;
    private final RecipeTagMapper rtMapper;
    private final IngredientMapper ingredientMapper;
    private final RecipeTagMapper recipeTagMapper;

    @Override
    public List<RecipeEntityDto> getAll() {
        return recipeMapper.recipeEntityListToRecipeEntityDtoList(repository.findAll());
    }

    @Override
    public List<UserToRecipeDto> getAllForUser(Long userId) {
        return utrMapper.userToRecipeEntityListTouserToRecipeDtoList(utrRepository.findAllByUser_Id(userId));
    }

    @Override
    public List<RecipeEntityDto> getAllPublicRecipesFromOtherUsers(Long userId, RecipeStoreFilterDto filter) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) return new ArrayList<>();
        List<RecipeEntity> recipesOfUser = utrRepository.findAllByUser_Id(userId).stream().map(UserToRecipeEntity::getRecipe).collect(Collectors.toList());
        List<RecipeEntity> recipes = utrRepository.findAllByUserIsNotAndRecipe_IsPublic(userOpt.get(), true).stream()
                .map(UserToRecipeEntity::getRecipe)
                .filter(recipe -> !recipesOfUser.contains(recipe))
                .collect(Collectors.toList());
        if(!filter.getTags().isEmpty()) {
            List<RecipeTagEntity> tags = recipeTagMapper.recipeTagEntityDtoListToRecipeTagEntityList(filter.getTags());
            recipes = recipes.stream()
                    .filter(recipe -> recipe.getTags().containsAll(tags))
                    .collect(Collectors.toList());
        }
        if(!filter.getIngredients().isEmpty()) {
            List<IngredientEntity> ingredients = ingredientMapper.ingredientEntityDtoListToIngredientEntityList(filter.getIngredients());
            recipes = recipes.stream()
                    .filter(recipe -> recipeHasIngredients(recipe, ingredients))
                    .collect(Collectors.toList());
        }
        return recipeMapper.recipeEntityListToRecipeEntityDtoList(recipes);
    }

    public boolean recipeHasIngredients(RecipeEntity recipe, List<IngredientEntity> ingredients){
        return recipe.getQuantities().stream()
                .map(quantity -> quantity.getIngredient())
                .collect(Collectors.toList())
                .containsAll(ingredients);
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
    @Transactional
    public void addForUser(Long userId, UserToRecipeDto dto) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) return;
        RecipeEntity recipe = recipeMapper.recipeEntityDtoToRecipeEntity(dto.getRecipe());
        recipe.setQuantities(new HashSet<>());
        recipe = repository.save(recipe);
        RecipeEntity finalRecipe = recipe;
        recipe.setQuantities(
                dto.getRecipe().getQuantities().stream().map(quantityDto -> {
                    RecipeIngredientQuantity riq = rtiMapper.recipeIngredientQuantityDtoToRecipeIngredientQuantity(quantityDto);
                    riq.setRecipe(finalRecipe);
                    riq.setId(new RecipeQuantityKey(finalRecipe.getId(), quantityDto.getIngredient().getId()));
                    return rtiRepository.save(riq);
                }).collect(Collectors.toSet())
        );
        utrRepository.save(new UserToRecipeEntity(user.get(), recipe, dto.isOwner()));
    }

    @Override
    @Transactional
    public void update(RecipeEntityDto recipe) {
        Optional<RecipeEntity> recipeOpt = repository.findById(recipe.getId());
        if (recipeOpt.isEmpty()) return;
        RecipeEntity savedRecipe = recipeOpt.get();
        savedRecipe.setName(recipe.getName());
        savedRecipe.setDescription(recipe.getDescription());
        savedRecipe.setTags(rtMapper.recipeTagEntityDtoListToRecipeTagEntityList(recipe.getTags()));
        savedRecipe.setIsPublic(recipe.getIsPublic());

        recipe.getQuantities().forEach(recipeQuantity -> {
            Optional<RecipeIngredientQuantity> rtqOpt = rtiRepository.findAllByRecipe_IdAndIngredient_Id(savedRecipe.getId(), recipeQuantity.getIngredient().getId());
            if(rtqOpt.isEmpty()){
                RecipeIngredientQuantity newRtq = rtiMapper.recipeIngredientQuantityDtoToRecipeIngredientQuantity(recipeQuantity);
                newRtq.setRecipe(savedRecipe);
                newRtq.setId(new RecipeQuantityKey(savedRecipe.getId(), recipeQuantity.getIngredient().getId()));
                rtiRepository.save(newRtq);
                return;
            }
            RecipeIngredientQuantity rtq = rtqOpt.get();
            rtq.setQuantity(recipeQuantity.getQuantity());
        });

        Set<Long> remainingIngredientIds = recipe.getQuantities().stream()
                .map(RecipeIngredientQuantityDto::getIngredient)
                .map(IngredientEntityDto::getId)
                .collect(Collectors.toSet());
        rtiRepository.findAllByRecipe_Id(savedRecipe.getId()).forEach(rtq -> {
            if(remainingIngredientIds.contains(rtq.getIngredient().getId())) return;
            rtiRepository.delete(rtq);
        });
    }

    @Override
    public void createConnectionBetweenUserAndRecipe(Long userId, Long recipeId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        Optional<RecipeEntity> recipeOpt = repository.findById(recipeId);
        if (userOpt.isEmpty() || recipeOpt.isEmpty()) return;

        utrRepository.save(new UserToRecipeEntity(userOpt.get(), recipeOpt.get(), false));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteForUser(Long userId, Long recipeId) {
        Optional<UserToRecipeEntity> utrOpt = utrRepository.findByUser_IdAndRecipe_Id(userId, recipeId);
        if (utrOpt.isEmpty()) return;
        if (utrOpt.get().isOwner()) {
            utrRepository.findAllByRecipe_Id(recipeId).forEach(utrRepository::delete);
            rtiRepository.findAllByRecipe_Id(recipeId).forEach(rtiRepository::delete);
            repository.delete(utrOpt.get().getRecipe());
        } else utrRepository.delete(utrOpt.get());
    }
}
