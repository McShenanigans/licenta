package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.*;
import licenta.andreibalinth.backend.entities.dto.*;
import licenta.andreibalinth.backend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService{
    private final IngredientRepository repository;
    private final MeasurementUnitRepository muRepository;
    private final IngredientCategoryRepository icRepository;
    private final UserRepository userRepository;
    private final UserIngredientQuantityRepository uigRepository;

    @Override
    public List<IngredientEntityDto> getAll() {
        return mapEntityListToDto(repository.findAll());
    }

    @Override
    public Optional<IngredientEntityDto> getById(Long id) {
        Optional<IngredientEntity> entity = repository.findById(id);
        return entity.map(this::mapEntityToDto);
    }

    @Override
    public List<UserIngredientQuantityDto> getAllIngredientsOfUser(Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isEmpty()) return new ArrayList<>();
        return user.get().getUserIngredientQuantities().stream()
                .map(this::convertUIQEntityToUIQDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<IngredientEntityDto> getAllAbsentFromUser(Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) return new ArrayList<>();
        List<IngredientEntity> ingredientsOfUser = userOpt.get().getUserIngredientQuantities().stream()
                .map(UserIngredientQuantity::getIngredient).collect(Collectors.toList());
        List<IngredientEntity> ingredientsAbsentFromUser = repository.findAll().stream()
                .filter(ingredient -> !ingredientsOfUser.contains(ingredient)).collect(Collectors.toList());
        return mapEntityListToDto(ingredientsAbsentFromUser);
    }

    @Override
    public void add(IngredientEntityDto dto) {
        repository.save(mapDtoToEntity(dto));
    }

    @Override
    public void addUserIngredientQuantity(UserIngredientQuantityDto dto, Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) return;

        UserEntity user = userOpt.get();

        UserIngredientQuantity userIngredientQuantity = new UserIngredientQuantity(
                user,
                repository.getById(dto.getIngredient().getId()),
                dto.getQuantity()
        );

        uigRepository.save(userIngredientQuantity);
    }

    @Override
    @Transactional
    public void update(IngredientEntityDto ingredient) {
        Optional<IngredientEntity> ingredientEntityOptional = repository.findById(ingredient.getId());
        if(ingredientEntityOptional.isEmpty()) return;
        IngredientEntity savedIngredient = ingredientEntityOptional.get();
        savedIngredient.setCategories(findCategoryEntitiesForCategoryDtos(ingredient.getCategories()));
        savedIngredient.setName(ingredient.getName());
        savedIngredient.setMeasurementUnit(findMeasurementUnitEntityForMeasurementUnitDto(ingredient.getMeasurementUnit()));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserIngredientQuantityDto getOneByUserIdAndIngredientId(Long userId, Long ingredientId) {
        UserEntity user = userRepository.getById(userId);
        IngredientEntity ingredient = repository.getById(ingredientId);
        Optional<UserIngredientQuantity> userIngredientQuantityOpt = uigRepository.findByUserAndIngredient(user, ingredient);
        if(userIngredientQuantityOpt.isEmpty()) return new UserIngredientQuantityDto();
        else return convertUIQEntityToUIQDto(userIngredientQuantityOpt.get());
    }

    @Override
    @Transactional
    public void updateUserIngredient(UserIngredientQuantityDto dto, Long userId) {
        UserEntity user = userRepository.getById(userId);
        IngredientEntity ingredient = repository.getById(dto.getIngredient().getId());
        Optional<UserIngredientQuantity> userIngredientQuantity = uigRepository.findByUserAndIngredient(user, ingredient);
        if(userIngredientQuantity.isEmpty()) return;

        UserIngredientQuantity uig = userIngredientQuantity.get();
        uig.setQuantity(dto.getQuantity());
    }

    private List<IngredientEntityDto> mapEntityListToDto(List<IngredientEntity> entities){
        return entities.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    private IngredientEntityDto mapEntityToDto(IngredientEntity ingredient){
        IngredientEntityDto dto = new IngredientEntityDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setMeasurementUnit(mapMUEntityToDto(ingredient.getMeasurementUnit()));
        dto.setCategories(mapICEntitiesToDtos(ingredient.getCategories()));
        return dto;
    }

    private IngredientEntity mapDtoToEntity(IngredientEntityDto dto) {
        IngredientEntity entity = new IngredientEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setMeasurementUnit(findMeasurementUnitEntityForMeasurementUnitDto(dto.getMeasurementUnit()));
        entity.setCategories(findCategoryEntitiesForCategoryDtos(dto.getCategories()));
        return entity;
    }

    private MeasurementUnitEntityDto mapMUEntityToDto(MeasurementUnitEntity measurementUnit){
        MeasurementUnitEntityDto dto = new MeasurementUnitEntityDto();
        dto.setId(measurementUnit.getId());
        dto.setName(measurementUnit.getName());
        return dto;
    }

    private List<IngredientCategoryDto> mapICEntitiesToDtos(List<IngredientCategoryEntity> entities){
        return entities.stream().map(this::mapICEntityToDto).collect(Collectors.toList());
    }

    private IngredientCategoryDto mapICEntityToDto(IngredientCategoryEntity entity){
        IngredientCategoryDto dto = new IngredientCategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    private List<IngredientCategoryEntity> findCategoryEntitiesForCategoryDtos(List<IngredientCategoryDto> dtos){
        if(dtos == null) return new ArrayList<>();
        return dtos.stream()
                .map(c -> icRepository.getById(c.getId()))
                .collect(Collectors.toList());
    }

    private MeasurementUnitEntity findMeasurementUnitEntityForMeasurementUnitDto(MeasurementUnitEntityDto dto){
        return muRepository.getById(dto.getId());
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

    private UserIngredientQuantityDto convertUIQEntityToUIQDto(UserIngredientQuantity entity){
        UserIngredientQuantityDto dto = new UserIngredientQuantityDto();
        dto.setIngredient(mapEntityToDto(entity.getIngredient()));
        dto.setUser(convertUserToDto(entity.getUser()));
        dto.setQuantity(entity.getQuantity());
        return dto;
    }
}
