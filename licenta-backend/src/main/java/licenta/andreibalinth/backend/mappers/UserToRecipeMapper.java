package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.UserToRecipeEntity;
import licenta.andreibalinth.backend.entities.dto.UserToRecipeDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserToRecipeMapper {
    UserToRecipeDto userToRecipeEntityToUserToRecipeDto(UserToRecipeEntity entity);
    List<UserToRecipeDto> userToRecipeEntityListTouserToRecipeDtoList(List<UserToRecipeEntity> entities);
}
