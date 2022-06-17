package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.RecipeEntity;
import licenta.andreibalinth.backend.entities.dto.RecipeEntityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeEntity recipeEntityDtoToRecipeEntity(RecipeEntityDto dto);
    RecipeEntityDto recipeEntityToRecipeEntityDto(RecipeEntity entity);
    List<RecipeEntityDto> recipeEntityListToRecipeEntityDtoList(List<RecipeEntity> entities);
}
