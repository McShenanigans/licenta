package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.RecipeIngredientQuantity;
import licenta.andreibalinth.backend.entities.dto.RecipeIngredientQuantityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeIngredientQuantityMapper {
    RecipeIngredientQuantity recipeIngredientQuantityDtoToRecipeIngredientQuantity(RecipeIngredientQuantityDto dto);
    RecipeIngredientQuantityDto recipeIngredientQuantityToRecipeIngredientQuantityDto(RecipeIngredientQuantity entity);
    List<RecipeIngredientQuantityDto> recipeIngredientQuantityListToRecipeIngredientQuantityDtoList(List<RecipeIngredientQuantity> entities);
}
