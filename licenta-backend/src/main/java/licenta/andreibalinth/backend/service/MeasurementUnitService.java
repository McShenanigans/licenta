package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.dto.MeasurementUnitEntityDto;

import java.util.List;
import java.util.Optional;

public interface MeasurementUnitService {
    List<MeasurementUnitEntityDto> getAll();
    Optional<MeasurementUnitEntityDto> getById(Long id);
    void update(MeasurementUnitEntityDto measurementUnit);
}
