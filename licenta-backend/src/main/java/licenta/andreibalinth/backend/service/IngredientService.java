package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.dto.IngredientEntityDto;
import licenta.andreibalinth.backend.entities.dto.UserIngredientQuantityDto;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    List<IngredientEntityDto> getAll();
    List<UserIngredientQuantityDto> getAllIngredientsOfUser(Long userId);
    List<IngredientEntityDto> getAllAbsentFromUser(Long userId);
    Optional<IngredientEntityDto> getById(Long id);
    UserIngredientQuantityDto getOneByUserIdAndIngredientId(Long userId, Long ingredientId);
    void add(IngredientEntityDto ingredient);
    void addUserIngredientQuantity(UserIngredientQuantityDto dto, Long userId);
    void update(IngredientEntityDto ingredient);
    void updateUserIngredient(UserIngredientQuantityDto dto, Long userId);
    void delete(Long id);
    void deleteUserIngredient(Long userId, Long ingredientId);
}
