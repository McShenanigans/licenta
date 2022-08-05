package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.*;
import licenta.andreibalinth.backend.entities.dto.*;
import licenta.andreibalinth.backend.mappers.ComplexScheduleEntryMapper;
import licenta.andreibalinth.backend.mappers.IngredientMapper;
import licenta.andreibalinth.backend.mappers.ScheduleEntryMapper;
import licenta.andreibalinth.backend.repository.ScheduleEntryRepository;
import licenta.andreibalinth.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    ScheduleEntryRepository repository;
    UserRepository userRepository;
    IngredientService ingredientService;
    ScheduleEntryMapper mapper;
    ComplexScheduleEntryMapper complexScheduleEntryMapper;
    IngredientMapper ingredientMapper;

    @Override
    public List<ComplexScheduleEntryDto> getAllEntriesForUser(Long userId) {
        return complexScheduleEntryMapper.mapEntryListToDtoList(getComplexScheduleEntriesForUser(userId));
    }

    @Override
    public List<IngredientQuantityDto> getShoppingListForUser(Long userId){
        Map<IngredientEntity, Double> ingredientQuantityMap = new HashMap<>();
        getComplexScheduleEntriesForUser(userId).stream().map(ComplexScheduleEntry::getMissingIngredients)
                .forEach(missingIngredientsList -> missingIngredientsList.forEach(missingIngredient -> {
                    if (ingredientQuantityMap.containsKey(missingIngredient.getIngredient()))
                        ingredientQuantityMap.put(missingIngredient.getIngredient(), ingredientQuantityMap.get(missingIngredient.getIngredient()) + missingIngredient.getQuantity());
                    else ingredientQuantityMap.put(missingIngredient.getIngredient(), missingIngredient.getQuantity());
                }));

        List<IngredientQuantityDto> shoppingList = new ArrayList<>();
        ingredientQuantityMap.keySet().forEach(ingredient -> shoppingList.add(
                new IngredientQuantityDto(ingredientMapper.ingredientEntityToIngredientEntityDto(ingredient), ingredientQuantityMap.get(ingredient))
        ));
        return shoppingList;
    }

    private List<ComplexScheduleEntry> getComplexScheduleEntriesForUser(Long userId){
        List<ComplexScheduleEntry> complexEntries = new ArrayList<>();
        List<UserIngredientQuantityDto> ingredientQuantitiesOfUser = ingredientService.getAllIngredientsOfUser(userId);

        repository.findAllByUser_Id(userId).forEach(entry -> {
            ComplexScheduleEntry complexEntry = new ComplexScheduleEntry();
            complexEntry.setEntry(entry);
            complexEntry.setMissingIngredients(new ArrayList<>());
            complexEntries.add(complexEntry);

            entry.getRecipe().getQuantities().forEach(ingredientQuantity -> {
                int indexOfCurrentIngredient = findIndexOfCurrentIngredient(ingredientQuantitiesOfUser, ingredientQuantity.getIngredient());

                if(indexOfCurrentIngredient == -1) complexEntry.getMissingIngredients().add(new IngredientQuantityEntity(ingredientQuantity.getIngredient(), ingredientQuantity.getQuantity()));
                else if(ingredientQuantitiesOfUser.get(indexOfCurrentIngredient).getQuantity() < ingredientQuantity.getQuantity())
                    complexEntry.getMissingIngredients().add(new IngredientQuantityEntity(
                            ingredientQuantity.getIngredient(),
                            ingredientQuantity.getQuantity() - ingredientQuantitiesOfUser.get(indexOfCurrentIngredient).getQuantity())
                    );
                else ingredientQuantitiesOfUser.get(indexOfCurrentIngredient).setQuantity(
                            ingredientQuantitiesOfUser.get(indexOfCurrentIngredient).getQuantity() - ingredientQuantity.getQuantity()
                    );
            });

            complexEntry.setAllIngredientsAvailable(complexEntry.getMissingIngredients().size() == 0);
        });

        return complexEntries;
    }

    private int findIndexOfCurrentIngredient(List<UserIngredientQuantityDto> ingredientQuantitiesOfUser, IngredientEntity ingredient) {
        for(int i = 0; i < ingredientQuantitiesOfUser.size(); i++)
            if(ingredientQuantitiesOfUser.get(i).getIngredient().getId().equals(ingredient.getId())) return i;
        return -1;
    }

    @Override
    public void addEntry(ScheduleEntryDto dto, Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) return;
        ScheduleEntryEntity entity = mapper.scheduleEntryDtoToScheduleEntryEntity(dto);
        entity.setUser(userOpt.get());
        repository.save(entity);
    }

    @Override
    @Transactional
    public void updateEntry(ScheduleEntryDto dto) {
        Optional<ScheduleEntryEntity> entryOpt = repository.findById(dto.getId());
        if(entryOpt.isEmpty()) return;
        ScheduleEntryEntity dtoEntry = mapper.scheduleEntryDtoToScheduleEntryEntity(dto);
        ScheduleEntryEntity entry = entryOpt.get();
        entry.setDate(dtoEntry.getDate());
        entry.setRecipe(dtoEntry.getRecipe());
    }

    @Override
    public void deleteEntry(Long entryId) {
        repository.deleteById(entryId);
    }

    @Override
    public void deleteEntryAndRemoveIngredients(Long entryId, Boolean wasCooked) {
        ScheduleEntryEntity entry = repository.getById(entryId);
        RecipeEntity recipe = entry.getRecipe();
        UserEntity user = entry.getUser();

        repository.deleteById(entryId);
        if(!wasCooked) return;

        ingredientService.removeIngredientQuantitiesFromUser(recipe, user);
    }

    @Override
    public void runAutomaticScheduler(AutomaticRecipeSchedulerDto dto) {
        
    }
}
