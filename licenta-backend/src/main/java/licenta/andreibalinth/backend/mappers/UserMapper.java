package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.UserEntity;
import licenta.andreibalinth.backend.entities.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userEntityToUserDto(UserEntity userEntity);
    UserEntity userDtoToUserEntity(UserDto userDto);
    List<UserDto> userEntityListToUserDtoList(List<UserEntity> userEntities);
}
