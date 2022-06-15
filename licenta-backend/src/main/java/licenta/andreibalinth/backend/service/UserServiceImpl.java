package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.UserEntity;
import licenta.andreibalinth.backend.entities.dto.UserDto;
import licenta.andreibalinth.backend.entities.dto.login.LoginRequestDto;
import licenta.andreibalinth.backend.mappers.UserMapper;
import licenta.andreibalinth.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    @Override
    public List<UserDto> getAll() {
        return mapper.userEntityListToUserDtoList(repository.findAll());
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        Optional<UserEntity> userOpt = repository.findById(id);
        return userOpt.map(mapper::userEntityToUserDto);
    }

    @Override
    public void add(UserDto dto) {
        UserEntity newUser = mapper.userDtoToUserEntity(dto);
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        repository.save(newUser);
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
        return Optional.of(mapper.userEntityToUserDto(user));
    }
}
