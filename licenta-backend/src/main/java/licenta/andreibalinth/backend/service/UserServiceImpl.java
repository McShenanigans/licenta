package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.UserEntity;
import licenta.andreibalinth.backend.entities.dto.UserDto;
import licenta.andreibalinth.backend.entities.dto.login.LoginRequestDto;
import licenta.andreibalinth.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream().map(this::convertUserToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        Optional<UserEntity> userOpt = repository.findById(id);
        return userOpt.map(this::convertUserToDto);
    }

    @Override
    public void add(UserDto dto) {
        repository.save(convertDtoToUser(dto));
    }

    @Override
    @Transactional
    public void update(UserDto user) {
        Optional<UserEntity> savedUserOpt = repository.findById(user.getId());
        if(savedUserOpt.isEmpty()) return;
        UserEntity savedUser = savedUserOpt.get();
        savedUser.setFirstName(user.getFirstName());
        savedUser.setLastName(user.getLastName());
        savedUser.setUsername(user.getUsername());
        savedUser.setPassword(encoder.encode(user.getPassword()));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<UserDto> checkUser(LoginRequestDto requestDto) {
        Optional<UserEntity> userOpt = repository.findByEmail(requestDto.getEmail());
        if(userOpt.isEmpty())
            return Optional.empty();
        UserEntity user = userOpt.get();
        if (!encoder.matches(requestDto.getPassword(), user.getPassword()))
            return Optional.empty();
        return Optional.of(convertUserToDto(user));
    }

    private UserEntity convertDtoToUser(UserDto dto){
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setUserIngredientQuantities(new HashSet<>());
        user.setRecipes(new ArrayList<>());
        return user;
    }

    private UserDto convertUserToDto(UserEntity user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPassword("");
        return dto;
    }
}
