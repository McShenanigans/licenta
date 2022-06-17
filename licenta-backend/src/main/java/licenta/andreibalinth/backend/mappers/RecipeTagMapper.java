package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.RecipeTagEntity;
import licenta.andreibalinth.backend.entities.dto.RecipeTagEntityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeTagMapper {
    RecipeTagEntity recipeTagDtoToRecipeTagEntity(RecipeTagEntityDto dto);
    RecipeTagEntityDto recipeTagEntityToRecipeTagEntityDto(RecipeTagEntity entity);
    List<RecipeTagEntityDto> recipeTagEntityListToRecipeTagEntityDtoList(List<RecipeTagEntity> entities);
}
