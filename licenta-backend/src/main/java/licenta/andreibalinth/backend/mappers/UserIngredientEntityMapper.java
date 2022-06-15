package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.UserIngredientQuantity;
import licenta.andreibalinth.backend.entities.dto.UserIngredientQuantityDto;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserIngredientEntityMapper {
    UserIngredientQuantityDto userIngredientQuantityToUserIngredientQuantityDto(UserIngredientQuantity userIngredientQuantity);
    UserIngredientQuantity userIngredientQuantityDtoToUserIngredientQuantity(UserIngredientQuantityDto userIngredientQuantityDto);
    List<UserIngredientQuantityDto> userIngredientQuantityListToUserIngredientQuantityDtoList(Set<UserIngredientQuantity> userIngredientQuantities);
}
