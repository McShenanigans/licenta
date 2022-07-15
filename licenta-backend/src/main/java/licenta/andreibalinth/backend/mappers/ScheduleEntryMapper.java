package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.ScheduleEntryEntity;
import licenta.andreibalinth.backend.entities.dto.ScheduleEntryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleEntryMapper {
    ScheduleEntryEntity scheduleEntryDtoToScheduleEntryEntity(ScheduleEntryDto dto);
    ScheduleEntryDto scheduleEntryEntityToScheduleEntryDto(ScheduleEntryEntity entity);
    List<ScheduleEntryDto> scheduleEntryEntityListToScheduleEntryDtoList(List<ScheduleEntryEntity> entities);
    List<ScheduleEntryEntity> scheduleEntryDtoListToScheduleEntryEntityList(List<ScheduleEntryDto> dtos);
}
