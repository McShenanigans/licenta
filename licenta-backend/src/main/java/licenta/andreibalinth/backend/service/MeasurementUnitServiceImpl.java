package licenta.andreibalinth.backend.service;

import licenta.andreibalinth.backend.entities.MeasurementUnitEntity;
import licenta.andreibalinth.backend.entities.dto.MeasurementUnitEntityDto;
import licenta.andreibalinth.backend.repository.MeasurementUnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MeasurementUnitServiceImpl implements MeasurementUnitService{
    private final MeasurementUnitRepository repository;

    @Override
    public List<MeasurementUnitEntityDto> getAll() {
        return mapEntitiesToDtos(repository.findAll());
    }

    @Override
    public Optional<MeasurementUnitEntityDto> getById(Long id) {
        Optional<MeasurementUnitEntity> entityOpt = repository.findById(id);
        return entityOpt.map(this::mapEntityToDto);
    }

    @Override
    @Transactional
    public void update(MeasurementUnitEntityDto measurementUnit) {
        Optional<MeasurementUnitEntity> measurementUnitOpt = repository.findById(measurementUnit.getId());
        if(measurementUnitOpt.isEmpty()) return;
        MeasurementUnitEntity savedMeasurementUnit = measurementUnitOpt.get();
        savedMeasurementUnit.setName(measurementUnit.getName());
    }

    private List<MeasurementUnitEntityDto> mapEntitiesToDtos(List<MeasurementUnitEntity> entities){
        return entities.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    private MeasurementUnitEntityDto mapEntityToDto(MeasurementUnitEntity entity){
        MeasurementUnitEntityDto dto = new MeasurementUnitEntityDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
