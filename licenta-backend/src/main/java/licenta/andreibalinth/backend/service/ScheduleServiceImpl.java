package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.ScheduleEntryEntity;
import licenta.andreibalinth.backend.entities.UserEntity;
import licenta.andreibalinth.backend.entities.dto.ScheduleEntryDto;
import licenta.andreibalinth.backend.mappers.ScheduleEntryMapper;
import licenta.andreibalinth.backend.repository.ScheduleEntryRepository;
import licenta.andreibalinth.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    ScheduleEntryRepository repository;
    UserRepository userRepository;
    ScheduleEntryMapper mapper;

    @Override
    public List<ScheduleEntryDto> getAllEntriesForUser(Long userId) {
        return mapper.scheduleEntryEntityListToScheduleEntryDtoList(repository.findAllByUser_Id(userId));
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
}
