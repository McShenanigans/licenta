package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.dto.ComplexScheduleEntryDto;
import licenta.andreibalinth.backend.entities.dto.ScheduleEntryDto;

import java.util.List;

public interface ScheduleService {
    List<ComplexScheduleEntryDto> getAllEntriesForUser(Long userId);
    void addEntry(ScheduleEntryDto dto, Long userId);
    void updateEntry(ScheduleEntryDto dto);
    void deleteEntry(Long entryId);
}
