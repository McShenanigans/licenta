package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.dto.IngredientEntityDto;
import licenta.andreibalinth.backend.entities.dto.UserIngredientQuantityDto;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    List<IngredientEntityDto> getAll();
    Optional<IngredientEntityDto> getById(Long id);
    List<UserIngredientQuantityDto> getAllIngredientsOfUser(Long userId);
    List<IngredientEntityDto> getAllAbsentFromUser(Long userId);
    void add(IngredientEntityDto ingredient);
    void addUserIngredientQuantity(UserIngredientQuantityDto dto, Long userId);
    void update(IngredientEntityDto ingredient);
    void delete(Long id);

    UserIngredientQuantityDto getOneByUserIdAndIngredientId(Long userId, Long ingredientId);

    void updateUserIngredient(UserIngredientQuantityDto dto, Long userId);
}
