package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.*;
import licenta.andreibalinth.backend.entities.dto.*;
import licenta.andreibalinth.backend.mappers.IngredientCategoryMapper;
import licenta.andreibalinth.backend.mappers.IngredientMapper;
import licenta.andreibalinth.backend.mappers.MeasurementUnitMapper;
import licenta.andreibalinth.backend.mappers.UserIngredientEntityMapper;
import licenta.andreibalinth.backend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService{
    private final IngredientRepository repository;
    private final UserRepository userRepository;
    private final UserIngredientQuantityRepository uigRepository;
    private final IngredientMapper ingredientMapper;
    private final UserIngredientEntityMapper userIngredientMapper;
    private final IngredientCategoryMapper ingredientCategoryMapper;
    private final MeasurementUnitMapper measurementUnitMapper;

    @Override
    public List<IngredientEntityDto> getAll() {
        return ingredientMapper.ingredientEntityListToIngredientEntityDtoList(repository.findAll());
    }

    @Override
    public Optional<IngredientEntityDto> getById(Long id) {
        Optional<IngredientEntity> entity = repository.findById(id);
        return entity.map(ingredientMapper::ingredientEntityToIngredientEntityDto);
    }

    @Override
    public List<UserIngredientQuantityDto> getAllIngredientsOfUser(Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isEmpty()) return new ArrayList<>();
        return userIngredientMapper.userIngredientQuantityListToUserIngredientQuantityDtoList(user.get().getUserIngredientQuantities());
    }

    @Override
    public List<IngredientEntityDto> getAllAbsentFromUser(Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) return new ArrayList<>();
        List<IngredientEntity> ingredientsOfUser = userOpt.get().getUserIngredientQuantities().stream()
                .map(UserIngredientQuantity::getIngredient).collect(Collectors.toList());
        List<IngredientEntity> ingredientsAbsentFromUser = repository.findAll().stream()
                .filter(ingredient -> !ingredientsOfUser.contains(ingredient)).collect(Collectors.toList());
        return ingredientMapper.ingredientEntityListToIngredientEntityDtoList(ingredientsAbsentFromUser);
    }

    @Override
    public void add(IngredientEntityDto dto) {
        repository.save(ingredientMapper.ingredientEntityDtoToIngredientEntity(dto));
    }

    @Override
    public void addUserIngredientQuantity(UserIngredientQuantityDto dto, Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) return;

        UserEntity user = userOpt.get();

        UserIngredientQuantity userIngredientQuantity = new UserIngredientQuantity(
                user,
                repository.getById(dto.getIngredient().getId()),
                dto.getQuantity()
        );

        uigRepository.save(userIngredientQuantity);
    }

    @Override
    @Transactional
    public void update(IngredientEntityDto ingredient) {
        Optional<IngredientEntity> ingredientEntityOptional = repository.findById(ingredient.getId());
        if(ingredientEntityOptional.isEmpty()) return;
        IngredientEntity savedIngredient = ingredientEntityOptional.get();
        savedIngredient.setCategories(ingredientCategoryMapper.ingredientCategoryDtoListToIngredientCategoryList(ingredient.getCategories()));
        savedIngredient.setName(ingredient.getName());
        savedIngredient.setMeasurementUnit(measurementUnitMapper.measurementUnitDtoToMeasurementUnit(ingredient.getMeasurementUnit()));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteUserIngredient(Long userId, Long ingredientId) {
        Optional<UserIngredientQuantity> optionalUiq = uigRepository.findByUserAndIngredient(userRepository.getById(userId), repository.getById(ingredientId));
        if(optionalUiq.isEmpty()) return;
        uigRepository.delete(optionalUiq.get());
    }

    @Override
    @Transactional
    public void removeIngredientQuantitiesFromUser(RecipeEntity recipe, UserEntity user) {
        Map<IngredientEntity, Double> ingredientQuantityMap = new HashMap<>();
        recipe.getQuantities().forEach(ingredientQuantity -> ingredientQuantityMap.put(ingredientQuantity.getIngredient(), ingredientQuantity.getQuantity()));
        user.getUserIngredientQuantities().forEach(uig -> {
            if(!ingredientQuantityMap.containsKey(uig.getIngredient())) return;
            if(uig.getQuantity() < ingredientQuantityMap.get(uig.getIngredient())) uig.setQuantity(0.0);
            else uig.setQuantity(uig.getQuantity() - ingredientQuantityMap.get(uig.getIngredient()));
        });
    }

    @Override
    public UserIngredientQuantityDto getOneByUserIdAndIngredientId(Long userId, Long ingredientId) {
        UserEntity user = userRepository.getById(userId);
        IngredientEntity ingredient = repository.getById(ingredientId);
        Optional<UserIngredientQuantity> userIngredientQuantityOpt = uigRepository.findByUserAndIngredient(user, ingredient);
        if(userIngredientQuantityOpt.isEmpty()) return new UserIngredientQuantityDto();
        else return userIngredientMapper.userIngredientQuantityToUserIngredientQuantityDto(userIngredientQuantityOpt.get());
    }

    @Override
    @Transactional
    public void updateUserIngredient(UserIngredientQuantityDto dto, Long userId) {
        UserEntity user = userRepository.getById(userId);
        IngredientEntity ingredient = repository.getById(dto.getIngredient().getId());
        Optional<UserIngredientQuantity> userIngredientQuantity = uigRepository.findByUserAndIngredient(user, ingredient);
        if (userIngredientQuantity.isEmpty()) return;

        UserIngredientQuantity uig = userIngredientQuantity.get();
        uig.setQuantity(dto.getQuantity());
    }
}
