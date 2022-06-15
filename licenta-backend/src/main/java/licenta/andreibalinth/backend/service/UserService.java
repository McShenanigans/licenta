package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.dto.UserDto;
import licenta.andreibalinth.backend.entities.dto.login.LoginRequestDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAll();
    Optional<UserDto> getById(Long id);
    void add(UserDto dto);
    void update(UserDto user);
    void delete(Long id);
    Optional<UserDto> checkUser(LoginRequestDto requestDto);
}
