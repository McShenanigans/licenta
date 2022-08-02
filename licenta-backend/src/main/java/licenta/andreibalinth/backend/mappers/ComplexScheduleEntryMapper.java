package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.ComplexScheduleEntry;
import licenta.andreibalinth.backend.entities.dto.ComplexScheduleEntryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComplexScheduleEntryMapper {
    ComplexScheduleEntryDto mapEntryToDto(ComplexScheduleEntry entry);
    List<ComplexScheduleEntryDto> mapEntryListToDtoList(List<ComplexScheduleEntry> entries);
}
