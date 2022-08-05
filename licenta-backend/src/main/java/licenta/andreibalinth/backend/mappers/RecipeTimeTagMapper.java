package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.RecipeTimeTagEntity;
import licenta.andreibalinth.backend.entities.dto.RecipeTimeTagDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeTimeTagMapper {
    List<RecipeTimeTagDto> toDtos(List<RecipeTimeTagEntity> entities);
    RecipeTimeTagEntity toEntity(RecipeTimeTagDto dto);
}
