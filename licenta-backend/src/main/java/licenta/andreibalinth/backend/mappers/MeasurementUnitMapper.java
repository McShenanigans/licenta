package licenta.andreibalinth.backend.mappers;

import licenta.andreibalinth.backend.entities.MeasurementUnitEntity;
import licenta.andreibalinth.backend.entities.dto.MeasurementUnitEntityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MeasurementUnitMapper {
    MeasurementUnitEntityDto measurementUnitToMeasurementUnitDto(MeasurementUnitEntity entity);
    MeasurementUnitEntity measurementUnitDtoToMeasurementUnit(MeasurementUnitEntityDto dto);
}
