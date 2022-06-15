package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.IngredientEntity;
import licenta.andreibalinth.backend.entities.dto.IngredientEntityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    IngredientEntity ingredientEntityDtoToIngredientEntity(IngredientEntityDto dto);
    IngredientEntityDto ingredientEntityToIngredientEntityDto(IngredientEntity entity);
    List<IngredientEntityDto> ingredientEntityListToIngredientEntityDtoList(List<IngredientEntity> ingredients);
}
