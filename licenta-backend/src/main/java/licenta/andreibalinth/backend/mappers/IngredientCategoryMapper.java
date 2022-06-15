package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.IngredientCategoryEntity;
import licenta.andreibalinth.backend.entities.dto.IngredientCategoryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientCategoryMapper {
    IngredientCategoryDto ingredientCategoryToIngredientCategoryDto(IngredientCategoryEntity entity);
    IngredientCategoryEntity ingredientCategoryDtoToIngredientCategory(IngredientCategoryDto dto);
    List<IngredientCategoryEntity> ingredientCategoryDtoListToIngredientCategoryList(List<IngredientCategoryDto> dtos);
}
